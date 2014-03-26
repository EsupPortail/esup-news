/**
* ESUP-Portail News - Copyright (c) 2009 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/

package org.esco.portlets.news.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esco.portlets.news.domain.EscoUser;
import org.esco.portlets.news.domain.IEscoUser;
import org.esco.portlets.news.services.ldap.LdapUserService;
import org.esupportail.commons.exceptions.UserNotFoundException;
import org.esupportail.commons.services.ldap.LdapException;
import org.esupportail.commons.services.ldap.LdapUser;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.dao.DataAccessException;
import org.springframework.ldap.SearchLimitExceededException;
import org.springframework.ldap.support.filter.EqualsFilter;
import org.springframework.ldap.support.filter.OrFilter;
import org.springframework.util.Assert;
import org.uhp.portlets.news.dao.UserDao;
import org.uhp.portlets.news.domain.Topic;
import org.uhp.portlets.news.domain.User;
//import org.springframework.ldap.support.filter.WhitespaceWildcardsFilter;

/**
 * Dao usefull to get user informations from ldap and from the DB.
 * @author GIP RECIA - Gribonvald Julien
 * 20 juil. 09
 */
public class EscoUserDaoImpl implements EscoUserDao, InitializingBean {

	/** Logger. */
	private static final Log LOG = LogFactory.getLog(EscoUserDaoImpl.class);
	/** Nom d'attribut par defaut du mail utilisateur LDAP. */
	private static final String DEFAULT_ATTR_MAIL = "mail";
	/** Nom d'attribut par defaut du mail utilisateur LDAP. */
	private static final String DEFAULT_ATTR_DISPLAY_NAME = "cn";

	/** Attribut d'affichage du nom de l'utilisateur. */
	private String displayName = DEFAULT_ATTR_DISPLAY_NAME;
	/** Attribut mail de l'utilisateur. */
	private String mail = DEFAULT_ATTR_MAIL;

	/** Dao BD d'un user. */
	private UserDao userDao;
	/** Service Ldap to get users. */
	private LdapUserService ldapUserService;

	/**
	 * Constructeur de l'objet User();Impl.java.
	 */
	public EscoUserDaoImpl() {
		super();
	}

	/**
	 * @param enabled
	 * @param uid
	 * @throws DataAccessException
	 * @see org.esco.portlets.news.dao.EscoUserDao#activateUser(int, java.lang.String)
	 */
	public void activateUser(final int enabled, final String uid)
			throws DataAccessException {
	userDao.activateUser(enabled, uid);
	}

	/**
	 * @param uid
	 * @param rar
	 * @throws DataAccessException
	 * @see org.esco.portlets.news.dao.EscoUserDao#deleteUser(java.lang.String, boolean)
	 */
	public void deleteUser(final String uid, final boolean rar) throws DataAccessException {
		userDao.deleteUser(uid, rar);
	}

	/**
	 * @param user
	 * @throws DataAccessException
	 * @see org.esco.portlets.news.dao.EscoUserDao#deleteUser(org.uhp.portlets.news.domain.User)
	 */
	public void deleteUser(final User user) throws DataAccessException {
	userDao.deleteUser(user);
	}

	/**
	 * @return <code>List< IEscoUser ></code>
	 * @throws DataAccessException
	 * @see org.esco.portlets.news.dao.EscoUserDao#getAllSuperUsers()
	 */
	public List<IEscoUser> getAllSuperUsers() throws DataAccessException, LdapException {
		return this.completeUsersFromLdap(userDao.getAllSuperUsers());
	}

	/**
	 * @return <code>List< IEscoUser ></code>
	 * @throws DataAccessException
	 * @see org.esco.portlets.news.dao.EscoUserDao#getAllUsers()
	 */
	public List<IEscoUser> getAllUsers() throws DataAccessException, LdapException {
		return this.completeUsersFromLdap(userDao.getAllUsers());
	}

	/**
	 * @param categoryId
	 * @return <code>List< IEscoUser ></code>
	 * @throws DataAccessException
	 * @see org.esco.portlets.news.dao.EscoUserDao#getManagersForCategory(java.lang.Long)
	 */
	public List<IEscoUser> getManagersForCategory(final Long categoryId)
			throws DataAccessException, LdapException {
		return this.completeUsersFromLdap(userDao.getManagersForCategory(categoryId));
	}

	/**
	 * @param topicId
	 * @return <code>List< IEscoUser ></code>
	 * @throws DataAccessException
	 * @see org.esco.portlets.news.dao.EscoUserDao#getManagersForTopic(java.lang.Long)
	 */
	public List<IEscoUser> getManagersForTopic(final Long topicId)
			throws DataAccessException, LdapException {
		return this.completeUsersFromLdap(userDao.getManagersForTopic(topicId));
	}

	/**
	 * @param topicIds
	 * @return <code>List< IEscoUser ></code>
	 * @throws DataAccessException
	 * @see org.esco.portlets.news.dao.EscoUserDao#getManagersForTopics(java.lang.Integer[])
	 */
	public List<IEscoUser> getManagersForTopics(final Integer[] topicIds)
			throws DataAccessException, LdapException {
		return this.completeUsersFromLdap(userDao.getManagersForTopics(topicIds));
	}

	/**
	 * @param id
	 * @param topicIds
	 * @return <code>List< IEscoUser ></code>
	 * @throws DataAccessException
	 * @see org.esco.portlets.news.dao.EscoUserDao#getManagersForTopics(java.lang.Long, java.lang.Integer[])
	 */
	public List<IEscoUser> getManagersForTopics(final Long id, final Integer[] topicIds)
			throws DataAccessException, LdapException {
		return this.completeUsersFromLdap(userDao.getManagersForTopics(id, topicIds));
	}

	/**
	 * @param uid
	 * @return <code>IEscoUser</code>
	 * @throws DataAccessException
	 * @see org.esco.portlets.news.dao.EscoUserDao#getUserById(java.lang.String)
	 */
	public IEscoUser getUserById(final String uid) throws DataAccessException {
		EscoUser ieu = (EscoUser) userDao.getUserById(uid);
		if (ieu == null) {
			if (LOG.isWarnEnabled()) {
				LOG.warn("EscoUserManager::UserById(): " + " The user wasn't found in the database. ");
			}
			return null;
		}
		try {
			LdapUser ilu = this.ldapUserService.getLdapUser(uid);
			ieu.setFromLdapUser(ilu, displayName, mail);
		} catch (UserNotFoundException e) {
			if (LOG.isWarnEnabled()) {
				LOG.warn("EscoUserManager::UserById(): LdapException : " + e);
			}
		} catch (LdapException e) {
			if (LOG.isWarnEnabled()) {
				LOG.warn("EscoUserManager::UserById(): LdapException : " + e);
			}
			throw e;
		}
		return ieu;
	}

	/**
	 * @param uid
	 * @return <code>String</code>
	 * @throws DataAccessException
	 */
	public boolean isUserInDB(final String uid) throws DataAccessException {
		EscoUser ieu = (EscoUser) userDao.getUserById(uid);
		if (ieu != null) {
			return true;
		}
		return false;
	}

	/**
	 * @param uid
	 * @return <code>String</code>
	 * @throws DataAccessException
	 * @see org.esco.portlets.news.dao.EscoUserDao#getUserNameById(java.lang.String)
	 */
	public String getUserNameById(final String uid) throws LdapException {
		return this.ldapUserService.getLdapUser(uid).getAttribute(displayName);
	}

	/**
	 * @param uid
	 * @param ids
	 * @return <code>List< String ></code>
	 * @throws DataAccessException
	 * @see org.esco.portlets.news.dao.EscoUserDao#getUserRolesInTopicsByTopics(java.lang.String, java.lang.Integer[])
	 */
	public List<String> getUserRolesInTopicsByTopics(final String uid, final Integer[] ids)
			throws DataAccessException {
		return userDao.getUserRolesInTopicsByTopics(uid, ids);
	}

	/**
	 * @param user
	 * @throws DataAccessException
	 * @see org.esco.portlets.news.dao.EscoUserDao#insert(org.uhp.portlets.news.domain.User)
	 */
	public void insert(final User user) throws DataAccessException {
		userDao.insert(user);
	}

	/**
	 * @param uid
	 * @return <code>boolean</code>
	 * @throws DataAccessException
	 * @see org.esco.portlets.news.dao.EscoUserDao#isPermitted(java.lang.String)
	 */
	public boolean isPermitted(final String uid) throws DataAccessException {
		return userDao.isPermitted(uid);
	}

	/**
	 * @param uid
	 * @return <code>boolean</code>
	 * @throws DataAccessException
	 * @see org.esco.portlets.news.dao.EscoUserDao#isSuperAdmin(java.lang.String)
	 */
	public boolean isSuperAdmin(final String uid) throws DataAccessException {
		return userDao.isSuperAdmin(uid);
	}

	/**
	 * @param topic
	 * @param user
	 * @return <code>boolean</code>
	 * @throws DataAccessException
	 * @see org.esco.portlets.news.dao.EscoUserDao
	 * #isTopicManager(org.uhp.portlets.news.domain.Topic, org.uhp.portlets.news.domain.User)
	 */
	public boolean isTopicManager(final Topic topic, final User user)
			throws DataAccessException {
		return userDao.isTopicManager(topic, user);
	}

	/**
	 * @param uid
	 * @return <code>boolean</code>
	 * @throws DataAccessException
	 * @see org.esco.portlets.news.dao.EscoUserDao#isUserAccountEnabled(java.lang.String)
	 */
	public boolean isUserAccountEnabled(final String uid) throws DataAccessException {
		return userDao.isUserAccountEnabled(uid);
	}

	/**
	 * @param user
	 * @throws DataAccessException
	 * @see org.esco.portlets.news.dao.EscoUserDao#update(org.uhp.portlets.news.domain.User)
	 */
	public void update(final User user) throws DataAccessException {
		userDao.update(user);
	}

	/**
	 * @param uid
	 * @throws DataAccessException
	 * @see org.esco.portlets.news.dao.EscoUserDao#updateUserLastAccess(java.lang.String)
	 */
	public void updateUserLastAccess(final String uid) throws DataAccessException {
		userDao.updateUserLastAccess(uid);
	}

	/**
	 * @param uid
	 * @param enabled
	 * @throws DataAccessException
	 * @see org.esco.portlets.news.dao.EscoUserDao#updateUserStatus(java.lang.String, java.lang.String)
	 */
	public void updateUserStatus(final String uid, final String enabled)
			throws DataAccessException {
	userDao.updateUserStatus(uid, enabled);
	}

	/**
	 * @param uid
	 * @return <code>boolean</code>
	 * @throws DataAccessException
	 * @see org.esco.portlets.news.dao.EscoUserDao#userRoleExist(java.lang.String)
	 */
	public boolean userRoleExist(final String uid) throws DataAccessException {
		return userDao.userRoleExist(uid);
	}

	/**
	 * @param token Search value.
	 * @return <code>List< IEscoUser ></code> A list of users corresponding to the search value.
	 * @see org.esco.portlets.news.dao.EscoUserDao#findPersonsByToken(java.lang.String)
	 */
	public List<IEscoUser> findPersonsByToken(final String token) throws LdapException {
		try {
			List<IEscoUser> result = new ArrayList<IEscoUser>();
			List<LdapUser> ldapusers = this.ldapUserService.getLdapUsersFromToken(token);
			for (LdapUser lu : ldapusers) {
				EscoUser eu = new EscoUser();
				eu.setFromLdapUser(lu, this.displayName, this.mail);
				result.add(eu);
			}
			return result;
		} catch (LdapException e) {
			LOG.error("EscoUserDaoImpl::findPersonsByToken: error : " + e.getLocalizedMessage());
			if (e.getCause() instanceof SearchLimitExceededException) {
				throw (SearchLimitExceededException) e.getCause();
			}
			throw e;
		}
	}

	/**
	 * @param token Search value.
	 * @param filter
	 * @return <code>List< IEscoUser ></code> A list of users corresponding to the search value.
	 * @throws LdapException
	 * @see org.esco.portlets.news.dao.EscoUserDao#findPersonsByToken(java.lang.String)
	 */
	public List<IEscoUser> findPersonsByTokenAndFilter(final String token,
			final org.springframework.ldap.support.filter.Filter filter) throws LdapException {
		try {
			List<IEscoUser> result = new ArrayList<IEscoUser>();
			List<LdapUser> ldapusers = this.ldapUserService.getLdapUsersFromTokenAndFilter(token, filter);
			for (LdapUser lu : ldapusers) {
				EscoUser eu = new EscoUser();
				eu.setFromLdapUser(lu, this.displayName, this.mail);
				result.add(eu);
			}
			return result;
		} catch (LdapException e) {
			LOG.error("EscoUserDaoImpl::findPersonsByTokenAndFilter: error : " + e.getLocalizedMessage());
			if (e.getCause() instanceof SearchLimitExceededException) {
				throw (SearchLimitExceededException) e.getCause();
			}
			throw e;
		}
	}

	/**
	 * Get user's details from a list of user's id.
	 * @param usersUid a list of id/uid to retrieve in the LDAP.
	 * @return <code>Map< String, IEscoUser ></code> A Map of IEscoUser (details of users) with id/uid as key.
	 * @see org.esco.portlets.news.dao.EscoUserDao#findPersonsByListUid(java.util.List)
	 */
	public Map<String, IEscoUser> findPersonsByListUid(final List<String> usersUid) {
		if (usersUid != null && !usersUid.isEmpty()) {
			Set<String> uids = new HashSet<String>();
			uids.addAll(usersUid);
			Map<String, IEscoUser> result = new HashMap<String, IEscoUser>();
			OrFilter filter = new OrFilter();
			for (String s : uids) {
				filter.or(new EqualsFilter(ldapUserService.getIdAttribute(), s));
			}
			if (LOG.isTraceEnabled()) {
				LOG.trace("Search filter : " + filter.encode());
				LOG.trace("Attribute's list :" + ldapUserService.getSearchDisplayedAttributes().toString());
			}
			try {
				List<LdapUser> ldapusers = this.ldapUserService.getLdapUsersFromFilter(filter.encode());
				for (LdapUser lu : ldapusers) {
					EscoUser eu = new EscoUser();
					eu.setFromLdapUser(lu, this.displayName, this.mail);
					result.put(eu.getId(), eu);
				}
			}  catch (LdapException e) {
				LOG.error("EscoUserdaoImpl::findPersonsByListUid: error : " + e.getLocalizedMessage());
				throw e;
			}
			if (LOG.isTraceEnabled()) {
				LOG.trace("EscoUserdaoImpl::findPersonsByListUid: " + result);
			}
			return result;
		}
		return null;
	}

	/**
	 * From a User list obtained from the database load the users attributes.
	 * @param users the list of users to find in Ldap.
	 * @return <code>List< IEscoUser ></code>
	 * @throws LdapException
	 */
	private List<IEscoUser> completeUsersFromLdap(final List<User> users) throws LdapException {
		List<IEscoUser> iEscoUser = new ArrayList<IEscoUser>();
		Map<String, EscoUser> tmp = new HashMap<String, EscoUser>();
		List<String> uids = new ArrayList<String>();
		if (users != null && !users.isEmpty()) {
			for (User u : users) {
				uids.add(u.getUserId());
				EscoUser e = (EscoUser) u;
				if (LOG.isDebugEnabled()) {
					LOG.debug(e.toString());
				}
				tmp.put(u.getUserId(), e);
			}
			OrFilter filter = new OrFilter();
			for (String s : uids) {
				filter.or(new EqualsFilter(ldapUserService.getIdAttribute(), s));
			}
			if (LOG.isTraceEnabled()) {
				LOG.trace("Search filter : " + filter.encode());
			}
			try {
				for (LdapUser u : ldapUserService.getLdapUsersFromFilter(filter.encode())) {
					EscoUser eu = tmp.get(u.getId());
					eu.setFromLdapUser(u, this.displayName, this.mail);
					tmp.put(u.getId(), eu);
				}
			} catch (LdapException e) {
				if (LOG.isWarnEnabled()) {
					LOG.warn("EscoUserManager::getAllSuperUsers(): LdapException : " + e);
				}
				throw e;
			}
			iEscoUser.addAll(tmp.values());
			if (LOG.isTraceEnabled()) {
				LOG.trace("List of Esco Users : " + tmp.values());
			}
		}
		return iEscoUser;
	}

	/**
	 * Getter du membre displayName.
	 * @return <code>String</code> le membre displayName.
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * Setter du membre displayName.
	 * @param displayName la nouvelle valeur du membre displayName.
	 */
	public void setDisplayName(final String displayName) {
		this.displayName = displayName;
	}

	/**
	 * Getter du membre mail.
	 * @return <code>String</code> le membre mail.
	 */
	public String getMail() {
		return mail;
	}

	/**
	 * Setter du membre mail.
	 * @param mail la nouvelle valeur du membre mail.
	 */
	public void setMail(final String mail) {
		this.mail = mail;
	}

	/**
	 * Getter du membre userDao.
	 * @return <code>UserDao</code> le membre userDao.
	 */
	public UserDao getUserDao() {
		return userDao;
	}

	/**
	 * Setter du membre userDao.
	 * @param userDao la nouvelle valeur du membre userDao.
	 */
	public void setUserDao(final UserDao userDao) {
		this.userDao = userDao;
	}

	/**
	 * Getter du membre ldapUserService.
	 * @return <code>LdapUserService</code> le membre ldapUserService.
	 */
	public LdapUserService getLdapUserService() {
		return ldapUserService;
	}

	/**
	 * Setter du membre ldapUserService.
	 * @param ldapUserService la nouvelle valeur du membre ldapUserService.
	 */
	public void setLdapUserService(final LdapUserService ldapUserService) {
		this.ldapUserService = ldapUserService;
	}


	/**
	 * @throws Exception
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() throws Exception {
	Assert.notNull(this.ldapUserService, "The property ldapUserService in class "
			+ this.getClass().getSimpleName() + " must not be null.");
	Assert.notNull(this.userDao, "The property userDao in class "
			+ this.getClass().getSimpleName() + " must not be null.");
	Assert.hasLength(this.displayName, "The property displayName in class "
			+ this.getClass().getSimpleName() + " must not be null or empty.");
	Assert.hasLength(this.mail, "The property mail in class "
			+ this.getClass().getSimpleName() + " must not be null or empty.");
	}
}
