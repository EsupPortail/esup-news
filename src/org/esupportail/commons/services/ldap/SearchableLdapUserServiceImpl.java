/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.services.ldap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import net.sf.ehcache.CacheManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.commons.exceptions.ObjectNotFoundException;
import org.esupportail.commons.exceptions.UserNotFoundException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.ldap.LdapTemplate;
import org.springframework.ldap.support.filter.AndFilter;
import org.springframework.ldap.support.filter.OrFilter;
import org.springframework.ldap.support.filter.WhitespaceWildcardsFilter;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * An implementation of LdapUserService that delegates to a CachingLdapEntityServiceImpl.
 */
public class SearchableLdapUserServiceImpl implements LdapUserService, InitializingBean, Serializable {

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = 2538032574940842579L;

	/**
	 * The default unique attribute.
	 */
	private static final String DEFAULT_ID_ATTRIBUTE = "uid";

	/**
	 * The default object class.
	 */
	private static final String DEFAULT_OBJECT_CLASS = "Person";

	/**
	 * A logger.
	 */
	private static final Log LOG = LogFactory.getLog(SearchableLdapUserServiceImpl.class);
	
	/**
	 * The real LDAP entity service to delegate.
	 */
	private CachingLdapEntityServiceImpl service;
	
	/**
	 * The attribute used by method getLdapUsersFromToken().
	 */
	private String searchAttribute;

	/**
	 * The attributes that will be shown when searching for a user.
	 */
	private List<String> searchDisplayedAttributes;
	
	/**
     * The attributes that will be used to make filter on search request.
     */
	private List<String> filterSearchAttributes;

	/**
	 * Bean constructor.
	 */
	public SearchableLdapUserServiceImpl() {
		super();
		service = new CachingLdapEntityServiceImpl();
		service.setIdAttribute(DEFAULT_ID_ATTRIBUTE);
		service.setObjectClass(DEFAULT_OBJECT_CLASS);
	}

	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() {		
	    if (searchAttribute == null) {
			LOG.info("property searchAttribute is not set, method getLdapUsersFromToken() will fail");
		} else {
			Assert.notEmpty(searchDisplayedAttributes, "property searchDisplayedAttribute is not set");
		}
	    Assert.notEmpty(filterSearchAttributes, "property filterSearchAttributes is not set");
		service.afterPropertiesSet();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getClass().getSimpleName() + "#" + hashCode() + "[" 
		+ "searchDisplayedAttributes=[" + getSearchDisplayedAttributes() + "], " 
		+ "searchAttribute=[" + searchAttribute + "], " 
		+ "service=" + service  
		+ "]";
	}

	/**
	 * @see org.esupportail.commons.services.ldap.LdapUserService#getLdapUser(java.lang.String)
	 */
	public LdapUser getLdapUser(final String id) throws LdapException, UserNotFoundException {
		try {
			return LdapUserImpl.createLdapUser(service.getLdapEntity(id));
		} catch (ObjectNotFoundException e) {
			throw new UserNotFoundException(e);
		}
	}

	/**
	 * @see org.esupportail.commons.services.ldap.LdapUserService#getLdapUsersFromFilter(java.lang.String)
	 */
	public List<LdapUser> getLdapUsersFromFilter(final String filterExpr) throws LdapException {
	    if (LOG.isDebugEnabled()) {
	        LOG.debug("Looking for user with filter : " + filterExpr);
	    }
		return LdapUserImpl.createLdapUsers(service.getLdapEntitiesFromFilter(filterExpr));
	}

	/**
	 * @see org.esupportail.commons.services.ldap.LdapUserService#getLdapUsersFromToken(java.lang.String)
	 */
	public List<LdapUser> getLdapUsersFromToken(final String token) throws LdapException {
		OrFilter filter = new OrFilter();
		filter.or(new WhitespaceWildcardsFilter(searchAttribute, token));
		filter.or(new WhitespaceWildcardsFilter(service.getIdAttribute(), token));
		return getLdapUsersFromFilter(filter.encode());
	}
	
	/**
	 * @param token 
	 * @param filter 
	 * @return <code>List<LdapUser></code>
	 * @throws LdapException 
	 * @see org.esupportail.commons.services.ldap.LdapUserService#
	 * getLdapUsersFromTokenAndFilter(java.lang.String, org.springframework.ldap.support.filter.Filter)
	 */
	public List<LdapUser> getLdapUsersFromTokenAndFilter(final String token, 
	        final org.springframework.ldap.support.filter.Filter filter ) throws LdapException {
	    if (filter != null) {
	        OrFilter orFilter = new OrFilter();
	        orFilter.or(new WhitespaceWildcardsFilter(searchAttribute, token));
	        orFilter.or(new WhitespaceWildcardsFilter(service.getIdAttribute(), token));
	        AndFilter andFilter = new AndFilter();
	        andFilter.and(orFilter);
	        andFilter.and(filter);
	        return getLdapUsersFromFilter(andFilter.encode());
	    } 
	    return getLdapUsersFromToken(token);
	}

	/**
	 * @see org.esupportail.commons.services.ldap.LdapUserService#userMatchesFilter(
	 * java.lang.String, java.lang.String)
	 */
	public boolean userMatchesFilter(final String id, final String filter) throws LdapException {
		return service.entityMatchesFilter(id, filter);
	}

	/**
	 * @see org.esupportail.commons.services.ldap.BasicLdapService#getStatistics(java.util.Locale)
	 */
	public List<String> getStatistics(final Locale locale) {
		return service.getStatistics(locale);
	}

	/**
	 * @see org.esupportail.commons.services.ldap.BasicLdapService#resetStatistics()
	 */
	public void resetStatistics() {
		service.resetStatistics();
	}

	/**
	 * Set the cache manager.
	 * @param cacheManager
	 */
	public void setCacheManager(final CacheManager cacheManager) {
		service.setCacheManager(cacheManager);
	}

	/**
	 * Set the cache name.
	 * @param cacheName
	 */
	public void setCacheName(final String cacheName) {
		service.setCacheName(cacheName);
	}

	/**
	 * Set the dnSubPath.
	 * @param dnSubPath
	 */
	public void setDnSubPath(final String dnSubPath) {
		service.setDnSubPath(dnSubPath);
	}

	/**
	 * Set the idAttribute.
	 * @param idAttribute
	 */
	public void setIdAttribute(final String idAttribute) {
		service.setIdAttribute(idAttribute);
	}

	/**
	 * Set the attributes.
	 * @param attributes
	 */
	public void setAttributes(final List<String> attributes) {
		service.setAttributes(attributes);
	}

	/**
	 * Set the attributes.
	 * @param attributes
	 */
	public void setAttributesAsString(final String attributes) {
		List<String> list = new ArrayList<String>();
		for (String attribute : attributes.split(",")) {
			if (StringUtils.hasText(attribute)) {
				if (!list.contains(attribute)) {
					list.add(attribute);
				}
			}
		}
		setAttributes(list);
	}

	/**
	 * Set the ldapTemplate.
	 * @param ldapTemplate
	 */
	public void setLdapTemplate(final LdapTemplate ldapTemplate) {
		service.setLdapTemplate(ldapTemplate);
	}

	/**
	 * Set the objectClass.
	 * @param objectClass
	 */
	public void setObjectClass(final String objectClass) {
		service.setObjectClass(objectClass);
	}

	/**
	 * Set the testFilter.
	 * @param testFilter
	 */
	public void setTestFilter(final String testFilter) {
		service.setTestFilter(testFilter);
	}

	/**
	 * @see org.esupportail.commons.services.ldap.BasicLdapService#supportStatistics()
	 */
	public boolean supportStatistics() {
		return service.supportStatistics();
	}

	/**
	 * @see org.esupportail.commons.services.ldap.BasicLdapService#supportsTest()
	 */
	public boolean supportsTest() {
		return service.supportsTest();
	}

	/**
	 * @see org.esupportail.commons.services.ldap.BasicLdapService#test()
	 */
	public void test() {
		service.test();
	}

	/**
	 * @see org.esupportail.commons.services.ldap.BasicLdapService#testLdapFilter(java.lang.String)
	 */
	public String testLdapFilter(final String filterExpr) throws LdapException {
		return service.testLdapFilter(filterExpr);
	}

	/**
	 * @see org.esupportail.commons.services.ldap.LdapUserService#getSearchDisplayedAttributes()
	 */
	public List<String> getSearchDisplayedAttributes() {
		return searchDisplayedAttributes;
	}

	/**
	 * @param searchDisplayedAttributes the searchDisplayedAttributes to set
	 */
	public void setSearchDisplayedAttributes(final List<String> searchDisplayedAttributes) {
		this.searchDisplayedAttributes = searchDisplayedAttributes;
	}

	/**
	 * @param searchDisplayedAttributes the searchDisplayedAttributes to set
	 */
	public void setSearchDisplayedAttributesAsString(final String searchDisplayedAttributes) {
		List<String> list = new ArrayList<String>();
		for (String attribute : searchDisplayedAttributes.split(",")) {
			if (StringUtils.hasText(attribute)) {
				if (!list.contains(attribute)) {
					list.add(attribute.trim());
				}
			}
		}
		setSearchDisplayedAttributes(list);
	}

	/**
	 * @param searchAttribute the searchAttribute to set
	 */
	public void setSearchAttribute(final String searchAttribute) {
		this.searchAttribute = searchAttribute;
	}

	/**
     * @param filterSearchAttributes The values of filterSearchAttributes. 
     */
    public void setFilterSearchAttributes(final List<String> filterSearchAttributes) {
        this.filterSearchAttributes = filterSearchAttributes;
    }
    
    /**
     * @param filterSearchAttrs the filterSearchAttributes to set
     */
    public void setFilterSearchAttributesAsString(final String filterSearchAttrs) {
        List<String> list = new ArrayList<String>();
        for (String attribute : filterSearchAttrs.split(",")) {
            if (StringUtils.hasText(attribute)) {
                if (!list.contains(attribute)) {
                    list.add(attribute.trim());
                }
            }
        }
        setFilterSearchAttributes(list);
    }

    /**
     * @return <code>List<String></code> filterSearchAttributes.
     */
    public List<String> getFilterSearchAttributes() {
        return filterSearchAttributes;
    }

    /**
	 * @return the unique id attribute
	 * @see org.esupportail.commons.services.ldap.SimpleLdapEntityServiceImpl#getIdAttribute()
	 */
	public String getIdAttribute() {
		return service.getIdAttribute();
	}

}
