/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.services.ldap;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.ldap.support.filter.Filter;
import org.springframework.util.StringUtils;

/**
 * Class which enable to add a cache to LDAP query,
 * In order to improve execution time.
 * 
 * See /properties/ldap/ldap-example.xml.
 */
public class CachingLdapEntityServiceImpl extends SimpleLdapEntityServiceImpl {

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -2274070901193784799L;

	/**
	 * A constant to calculate statistics.
	 */
	private static final int HUNDRED = 100;
	
	/**
	 * The default name for the cache.
	 */
	private final String defaultCacheName = getClass().getName();
	
	/**
	 * the cache.
	 */
	private Cache cache;
	
	/**
	 * the name of the cache.
	 */
	private String cacheName;
	
	/**
	 * the cacheManager.
	 */
	private CacheManager cacheManager;
	
	/**
	 * A logger.
	 */
	private final Log logger = LogFactory.getLog(getClass());
	
	/**
	 * the number of requests made.
	 */
	private int totalRequests;
	/**
	 * the number of requests answered thanks to the cache.
	 */
	private int cachedRequests;
	/**
	 * the number of operations (i.e. LDAP effective requests).
	 */
	private int successfullOperations;
	/**
	 * the number of connection errors.
	 */
	private int connectionErrors;
	/**
	 * the number of bad filter errors.
	 */
	private int badFilterErrors;
	
	/**
	 * Bean constructor.
	 */
	public CachingLdapEntityServiceImpl() {
		super();
	}
	
	/**
	 * set the default cacheName.
	 */
	protected void setDefaultCacheName() {
		this.cacheName = defaultCacheName;
	}

	/**
	 * @see org.esupportail.commons.services.ldap.SimpleLdapEntityServiceImpl#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		if (cacheManager == null) {
			logger.warn(getClass() + ": property cacheManager is not set, no cache will be used.");
		} else {
			if (!StringUtils.hasText(cacheName)) {
				setDefaultCacheName();
				logger.info(getClass() + ": property cacheName is not set, '" 
						+ cacheName + "' will be used");
			}
			if (!cacheManager.cacheExists(cacheName)) {
				cacheManager.addCache(cacheName);
			}
			cache = cacheManager.getCache(cacheName);
		}
	}

	/**
	 * Count a LdapException and re-throw it immediatly.
	 * @param e 
	 * @return the exception as-is
	 * @throws LdapException 
	 */
	private LdapException countException(final LdapException e) {
		if (supportStatistics()) {
			if (e instanceof LdapBadFilterException) {
				badFilterErrors++;
			} else if (e instanceof LdapConnectionException) {
				connectionErrors++;
			}
		}
		return e;
	}

	/**
	 * @see org.esupportail.commons.services.ldap.SimpleLdapEntityServiceImpl#getLdapEntitiesFromFilter(
	 * org.springframework.ldap.support.filter.Filter)
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected List<LdapEntity> getLdapEntitiesFromFilter(final Filter filter) throws LdapException {
		if (!supportStatistics()) {
			return super.getLdapEntitiesFromFilter(filter);
		}
		String cacheKey = filter.encode();
		totalRequests++;
		Element element = cache.get(cacheKey);
		if (element != null) {
			cachedRequests++;
			if (element.getObjectValue() instanceof LdapBadFilterException) {
				throw (LdapBadFilterException) element.getObjectValue();
			}
			return (List<LdapEntity>) element.getObjectValue();
		}
		try {
			List<LdapEntity> ldapEntities = super.getLdapEntitiesFromFilter(filter);
			cache.put(new Element(cacheKey, ldapEntities));
			successfullOperations++;
			return ldapEntities;
		} catch (LdapBadFilterException e) {
			cache.put(new Element(cacheKey, e));
			throw countException(e);
		} catch (LdapException e) {
			throw countException(e);
		}
	}

	/**
	 * @see org.esupportail.commons.services.ldap.AbstractLdapService#supportStatistics()
	 */
	@Override
	public boolean supportStatistics() {
		return cache != null;
	}
	
	/**
	 * @see org.esupportail.commons.services.ldap.AbstractLdapService#resetStatistics()
	 */
	@Override
	public void resetStatistics() {
		totalRequests = 0;
		cachedRequests = 0;
		successfullOperations = 0;
		connectionErrors = 0;
		badFilterErrors = 0;
	}

	/**
	 * @return the percent of two values.
	 * @param val1
	 * @param val2
	 */
	private int getPercent(final int val1, final int val2) {
		if (val2 == 0) {
			return 0;
		}
		return val1 * HUNDRED / val2;
	}

	/**
	 * @see org.esupportail.commons.services.ldap.AbstractLdapService#getStatistics(java.util.Locale)
	 */
	@Override
	public List<String> getStatistics(@SuppressWarnings("unused")
    final Locale locale) {
		if (!supportStatistics()) {
			throw new UnsupportedOperationException("LDAP statistics are not available");
		}
		List<String> statistics = new LinkedList<String>();
		
		statistics.add("Nombre de requ\u00EAtes : " + totalRequests);
		statistics.add("Requ\u00EAtes cach\u00E9es : " + cachedRequests + "/" + totalRequests 
		        + "(" + getPercent(cachedRequests, totalRequests) + ")"); 
		int operations = totalRequests - cachedRequests;
		statistics.add("Op\u00E9rations LDAP : " + operations + "/" + totalRequests 
		        + "(" + getPercent(operations, totalRequests) + ")");		
		statistics.add("Op\u00E9rations r\u00E9ussies : " + successfullOperations + "/" + operations
		        + "(" +  getPercent(successfullOperations, operations) + ")"); 
		statistics.add("Erreurs de connexion : " + connectionErrors + "/" + operations
		        + "(" +  getPercent(connectionErrors, operations) + ")");
		statistics.add("Erreurs de filtre : " +	badFilterErrors + "/" + operations
				+ "(" +  getPercent(badFilterErrors, operations) + ")");
		int otherErrors = operations - successfullOperations - connectionErrors - badFilterErrors;
		statistics.add("Autres erreurs : " + otherErrors + "/" + operations
				+ "(" +  getPercent(otherErrors, operations) + ")");
		return statistics;
	}

	/**
	 * @param cacheManager the cacheManager to set
	 */
	public void setCacheManager(final CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}

	/**
	 * @param cacheName the cacheName to set
	 */
	public void setCacheName(final String cacheName) {
		this.cacheName = cacheName;
	}

}
