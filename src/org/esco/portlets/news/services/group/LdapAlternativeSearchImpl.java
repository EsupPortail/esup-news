/**
 *
 */
package org.esco.portlets.news.services.group;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esco.portlets.news.services.ldap.LdapUserService;
import org.esupportail.commons.services.ldap.LdapException;
import org.esupportail.commons.services.ldap.LdapUser;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.ldap.support.filter.EqualsFilter;
import org.springframework.ldap.support.filter.Filter;
import org.springframework.util.Assert;

/**
 * @author GIP RECIA - Julien Gribonvald
 * 26 mars 2014
 */
public class LdapAlternativeSearchImpl implements AlternativeSearchMembersService, InitializingBean {

	/** */
	private static final Log LOG = LogFactory.getLog(LdapAlternativeSearchImpl.class);

	/** Service ldap to make search. */
	private LdapUserService ldapService;

	/** */
	private String groupIdPrefixe = "smartldap.";

	/**
	 * Contructor of the object LdapAlternativeSearchImpl.java.
	 */
	public LdapAlternativeSearchImpl() {
		super();
	}

	/**
	 * @see org.esco.portlets.news.services.group.AlternativeSearchMembersService#AlternativeSearchMembersOfGroupById(java.lang.String)
	 */
	public List<String> AlternativeSearchMembersOfGroupById(final String groupId) throws LdapException {
		List<String> uids = new ArrayList<String>();
		List<LdapUser> users = ldapService.getLdapUsersFromFilter(ldapService.getSearchAttribute() + "=" + groupId.replace(this.groupIdPrefixe, ""));
		if (users != null && ! users.isEmpty()){
			for (LdapUser user : users){
				uids.add(user.getId());
			}
			if (LOG.isDebugEnabled())
				LOG.debug("Members of group '" + groupId + "' :" + uids.toString());
			return uids;
		}
		return null;
	}

	/**
	 * @param groupId
	 * @return <code>Filter</code>
	 */
	private Filter makeFilter(final String groupId) {
		Filter filter = new EqualsFilter(ldapService.getSearchAttribute(), groupId);
		return filter;
	}

	/**
	 * Getter of member ldapService.
	 * @return <code>LdapUserService</code> the attribute ldapService
	 */
	public LdapUserService getLdapService() {
		return ldapService;
	}

	/**
	 * Setter of attribute ldapService.
	 * @param ldapService the attribute ldapService to set
	 */
	public void setLdapService(LdapUserService ldapService) {
		this.ldapService = ldapService;
	}

	/**
	 * Getter of member groupIdPrefixe.
	 * @return <code>String</code> the attribute groupIdPrefixe
	 */
	public String getGroupIdPrefixe() {
		return groupIdPrefixe;
	}

	/**
	 * Setter of attribute groupIdPrefixe.
	 * @param groupIdPrefixe the attribute groupIdPrefixe to set
	 */
	public void setGroupIdPrefixe(final String groupIdPrefixe) {
		this.groupIdPrefixe = groupIdPrefixe;
	}

	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(this.ldapService, "The property ldapService in class "
				+ this.getClass().getSimpleName() + " must not be null.");
	}


}
