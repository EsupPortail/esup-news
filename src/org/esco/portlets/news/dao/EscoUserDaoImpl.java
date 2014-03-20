/**
 * ESUP-Portail News - Copyright (c) 2009 ESUP-Portail consortium
 * For any information please refer to http://esup-helpdesk.sourceforge.net
 * You may obtain a copy of the licence at http://www.esup-portail.org/license/
 */

package org.esco.portlets.news.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esco.portlets.news.domain.EntityRole;
import org.esco.portlets.news.domain.EscoUser;
import org.esco.portlets.news.domain.IEscoUser;
import org.esco.portlets.news.services.ldap.LdapUserService;
import org.esco.portlets.news.utils.PortletService;
import org.esupportail.commons.exceptions.UserNotFoundException;
import org.esupportail.commons.services.ldap.LdapException;
import org.esupportail.commons.services.ldap.LdapUser;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.dao.DataAccessException;
import org.springframework.ldap.SearchLimitExceededException;
import org.springframework.ldap.support.filter.EqualsFilter;
import org.springframework.ldap.support.filter.OrFilter;
//import org.springframework.ldap.support.filter.WhitespaceWildcardsFilter;
import org.springframework.util.Assert;
import org.uhp.portlets.news.dao.UserDao;
import org.uhp.portlets.news.domain.Role;
import org.uhp.portlets.news.domain.RoleEnum;
import org.uhp.portlets.news.domain.RolePerm;
import org.uhp.portlets.news.domain.Topic;
import org.uhp.portlets.news.domain.User;

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
	/** Service access to portlet methods. */
	//private PortletService portletService;

	/** Service

    /**
	 * Constructeur de l'objet User();Impl.java.
	 */
	public EscoUserDaoImpl() {
		super();
	}

	/**
	 * @param enabled
	 * @param userId
	 * @throws DataAccessException
	 * @see org.esco.portlets.news.dao.EscoUserDao#activateUser(int, java.lang.String)
	 */
	/*public void activateUser(final int enabled, final String userId)
			throws DataAccessException {
		userDao.activateUser(enabled, userId);
	}*/

	/**
	 * @param userRole
	 * @throws DataAccessException
	 * @see org.esco.portlets.news.dao.EscoUserDao#addEntityRole(org.uhp.portlets.news.domain.EntityRole)
	 */
	/*public void addEntityRole(final EntityRole userRole) throws DataAccessException {
		userDao.addEntityRole(userRole);
	}*/

	/**
	 * @param user
	 * @param role
	 * @param ctx
	 * @param ctxId
	 * @throws DataAccessException
	 * @see org.esco.portlets.news.dao.EscoUserDao
	 * #addEntityRole(org.uhp.portlets.news.domain.User, java.lang.String, java.lang.String, java.lang.Long)
	 */
	/*public void addEntityRole(final User user, final String role, final String ctx, final Long ctxId)
			throws DataAccessException {
		userDao.addEntityRole(user, role, ctx, ctxId);
	}*/

	/**
	 * @param userId
	 * @param role
	 * @param ctx
	 * @param ctxId
	 * @param isGroup
	 * @throws DataAccessException
	 * @see org.esco.portlets.news.dao.EscoUserDao
	 * #addEntityRole(java.lang.String, java.lang.String, java.lang.String, java.lang.Long, java.lang.String)
	 */
	/*public void addEntityRole(final String userId, final String role, final String ctx, final Long ctxId,
			final String isGroup) throws DataAccessException {
		userDao.addEntityRole(userId, role, ctx, ctxId, isGroup);
	}*/

	/**
	 * Update the role of the user in the context.
	 * @param userId
	 * @param role
	 * @param ctx
	 * @param ctxId
	 * @throws DataAccessException
	 */
	/*public void updateEntityRoleForCtx(final String userId, final String role, final String ctx, final Long ctxId)
			throws DataAccessException  {
		userDao.updateEntityRoleForCtx(userId, role, ctx, ctxId);
	}*/

	/**
	 * @param userId
	 * @param rar
	 * @throws DataAccessException
	 * @see org.esco.portlets.news.dao.EscoUserDao#deleteUser(java.lang.String, boolean)
	 */
	/*public void deleteUser(final String userId, final boolean rar) throws DataAccessException {
		userDao.deleteUser(userId, rar);
	}*/

	/**
	 * @see org.esco.portlets.news.dao.EscoUserDao#deleteUser(java.lang.String)
	 */
	public void deleteUser(final String userId) throws DataAccessException {
		userDao.deleteUser(userId);
	}

	/**
	 * @return <code>List< Role ></code>
	 * @throws DataAccessException
	 * @see org.esco.portlets.news.dao.EscoUserDao#getAllRoles()
	 */
	/*public List<Role> getAllRoles() throws DataAccessException {
		return userDao.getAllRoles();
	}*/

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
	@Deprecated
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
	/*public List<IEscoUser> getManagersForTopic(final Long topicId)
			throws DataAccessException, LdapException {
		return this.completeUsersFromLdap(userDao.getManagersForTopic(topicId));
	}*/

	/**
	 * @param topicIds
	 * @return <code>List< IEscoUser ></code>
	 * @throws DataAccessException
	 * @see org.esco.portlets.news.dao.EscoUserDao#getManagersForTopics(java.lang.Integer[])
	 */
	/*public List<IEscoUser> getManagersForTopics(final Integer[] topicIds)
			throws DataAccessException, LdapException {
		return this.completeUsersFromLdap(userDao.getManagersForTopics(topicIds));
	}*/

	/**
	 * @param id
	 * @param topicIds
	 * @return <code>List< IEscoUser ></code>
	 * @throws DataAccessException
	 * @see org.esco.portlets.news.dao.EscoUserDao#getManagersForTopics(java.lang.Long, java.lang.Integer[])
	 */
	@Deprecated
	public List<IEscoUser> getManagersForTopics(final Long id, final Integer[] topicIds)
			throws DataAccessException, LdapException {
		return this.completeUsersFromLdap(userDao.getManagersForTopics(id, topicIds));
	}

	/**
	 * @param userId
	 * @return <code>IEscoUser</code>
	 * @throws DataAccessException
	 * @see org.esco.portlets.news.dao.EscoUserDao#getUserById(java.lang.String)
	 */
	public IEscoUser getUserById(final String userId) throws DataAccessException {
		EscoUser ieu = (EscoUser) userDao.getUserById(userId);
		if (ieu == null) {
			if (LOG.isWarnEnabled()) {
				LOG.warn("EscoUserManager::UserById(): " + " The user wasn't found in the database. ");
			}
			return null;
		}
		try {
			LdapUser ilu = this.ldapUserService.getLdapUser(userId);
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
	 * @param userId
	 * @return <code>String</code>
	 * @throws DataAccessException
	 */
	public boolean isUserInDB(final String userId) throws DataAccessException {
		User user = userDao.getUserById(userId);
		if (user != null) {
			return true;
		}
		return false;
	}

	/**
	 * @param userId
	 * @return <code>String</code>
	 * @throws DataAccessException
	 * @see org.esco.portlets.news.dao.EscoUserDao#getUserNameById(java.lang.String)
	 */
	public String getUserNameById(final String userId) throws LdapException {
		return this.ldapUserService.getLdapUser(userId).getAttribute(displayName);
	}

	/**
	 * @see org.esco.portlets.news.dao.EscoUserDao#isUserHasAnyRole(java.lang.String)
	 */
	public boolean isUserHasAnyRole(final String userId) {
		return this.userDao.isUserHasAnyRole(userId);
	}

	/**
	 * @param userId
	 * @param ctxId
	 * @param ctxType
	 * @return <code>String</code>
	 * @throws DataAccessException
	 * @see org.esco.portlets.news.dao.EscoUserDao#getEntityRoleForCtx(java.lang.String, java.lang.Long, java.lang.String)
	 */
	/*public String getEntityRoleForCtx(final String userId, final Long ctxId, final String ctxType)
			throws DataAccessException {
		return userDao.getEntityRoleForCtx(userId, ctxId, ctxType);
	}*/



	/**
	 * @param userId
	 * @param ids
	 * @return <code>List< String ></code>
	 * @throws DataAccessException
	 * @see org.esco.portlets.news.dao.EscoUserDao#getEntityRolesInTopicsByTopics(java.lang.String, java.lang.Integer[])
	 */
	/*public List<String> getEntityRolesInTopicsByTopics(final String userId, final Integer[] ids)
			throws DataAccessException {
		return userDao.getEntityRolesInTopicsByTopics(userId, ids);
	}*/

	/**
	 * @param target
	 * @param targetCtx
	 * @param role
	 * @return <code>List< EntityRole ></code>
	 * @throws DataAccessException
	 * @see org.esco.portlets.news.dao.EscoUserDao
	 * #getUsersByRole(java.lang.Long, java.lang.String, org.uhp.portlets.news.domain.RoleEnum)
	 */
	/*public List<EntityRole> getUsersByRole(final Long target, final String targetCtx,
			final RoleEnum role) throws DataAccessException {
		return userDao.getUsersByRole(target, targetCtx, role);
	}*/

	/**
	 * @param ctxId
	 * @param ctxType
	 * @return <code>List< EntityRole ></code>
	 * @throws DataAccessException
	 * @see org.esco.portlets.news.dao.EscoUserDao#getEntityRolesForCtx(java.lang.Long, java.lang.String)
	 */
	/*public List<EntityRole> getEntityRolesForCtx(final Long ctxId, final String ctxType)
			throws DataAccessException {
		return userDao.getUsersRolesForCtx(ctxId, ctxType);
	}*/

	/**
	 * @param user
	 * @throws DataAccessException
	 * @see org.esco.portlets.news.dao.EscoUserDao#insert(org.uhp.portlets.news.domain.User)
	 */
	public void insert(final User user) throws DataAccessException {
		userDao.insert(user);
	}

	/**
	 * @param userId
	 * @return <code>boolean</code>
	 * @throws DataAccessException
	 * @see org.esco.portlets.news.dao.EscoUserDao#isPermitted(java.lang.String)
	 */
	/*public boolean isPermitted(final String userId) throws DataAccessException {
		return userDao.isPermitted(userId);
	}*/


	/**
	 * @see org.esco.portlets.news.dao.EscoUserDao#isUserSuperAdmin(java.lang.String)
	 */
	public boolean isUserSuperAdmin(final String userId) throws DataAccessException {
		return userDao.isUserSuperAdmin(userId);
	}

	/**
	 * @param topic
	 * @param user
	 * @return <code>boolean</code>
	 * @throws DataAccessException
	 * @see org.esco.portlets.news.dao.EscoUserDao
	 * #isTopicManager(org.uhp.portlets.news.domain.Topic, org.uhp.portlets.news.domain.User)
	 */
	@Deprecated
	public boolean isTopicManager(final Topic topic, final User user)
			throws DataAccessException {
		return userDao.isTopicManager(topic, user);
	}

	/**
	 * @param userId
	 * @return <code>boolean</code>
	 * @throws DataAccessException
	 * @see org.esco.portlets.news.dao.EscoUserDao#isUserAccountEnabled(java.lang.String)
	 */
	/*public boolean isUserAccountEnabled(final String userId) throws DataAccessException {
		return userDao.isUserAccountEnabled(userId);
	}*/

	/**
	 * @param ctxId
	 * @param ctxType
	 * @param userId
	 * @return <code>boolean</code>
	 * @throws DataAccessException
	 * @see org.esco.portlets.news.dao.EscoUserDao
	 * #isEntityRoleExistForContext(java.lang.Long, java.lang.String, java.lang.String)
	 */
	/*public boolean isEntityRoleExistForContext(final Long ctxId, final String ctxType,
			final String userId) throws DataAccessException {
		return userDao.isEntityRoleExistForContext(ctxId, ctxType, userId);
	}*/

	/**
	 * @param ctxId
	 * @param ctxType
	 * @return <code>List< EntityRole ></code>
	 * @throws DataAccessException
	 * @see org.esco.portlets.news.dao.EscoUserDao#loadCtxUsersRolesMap(java.lang.Long, java.lang.String)
	 */
	/*public List<EntityRole> loadCtxUsersRolesMap(final Long ctxId, final String ctxType)
			throws DataAccessException {
		return userDao.loadCtxUsersRolesMap(ctxId, ctxType);
	}*/

	/**
	 * @param userId
	 * @param id
	 * @throws DataAccessException
	 * @see org.esco.portlets.news.dao.EscoUserDao#removeAllEntityRolesForTopics(java.lang.String, java.lang.Long)
	 */
	/*public void removeAllEntityRolesForTopics(final String userId, final Long id)
			throws DataAccessException {
		userDao.removeAllEntityRolesForTopics(userId, id);
	}*/

	/**
	 * @param userId
	 * @param target
	 * @param targetCtx
	 * @throws DataAccessException
	 * @see org.esco.portlets.news.dao.EscoUserDao
	 * #removeEntityRoleForCtx(java.lang.String, java.lang.Long, java.lang.String)
	 */
	/*public void removeEntityRoleForCtx(final String userId, final Long target, final String targetCtx)
			throws DataAccessException {
		userDao.removeEntityRoleForCtx(userId, target, targetCtx);
	}
*/
	/**
	 * @param ctxId
	 * @param ctxType
	 * @throws DataAccessException
	 * @see org.esco.portlets.news.dao.EscoUserDao#removeUsersRoleForCtx(java.lang.Long, java.lang.String)
	 */
//	public void removeUsersRoleForCtx(final Long ctxId, final String ctxType)
//			throws DataAccessException {
//		userDao.removeUsersRoleForCtx(ctxId, ctxType);
//	}

	/**
	 * @param user
	 * @throws DataAccessException
	 * @see org.esco.portlets.news.dao.EscoUserDao#update(org.uhp.portlets.news.domain.User)
	 */
	public void update(final User user) throws DataAccessException {
		userDao.update(user);
	}

	/**
	 * @param userId
	 * @throws DataAccessException
	 * @see org.esco.portlets.news.dao.EscoUserDao#updateLastUserAccess(java.lang.String)
	 */
	public void updateLastUserAccess(final String userId) throws DataAccessException {
		userDao.updateLastUserAccess(userId);
	}

	/**
	 * @param userId
	 * @param enabled
	 * @throws DataAccessException
	 * @see org.esco.portlets.news.dao.EscoUserDao#updateUserStatus(java.lang.String, java.lang.String)
	 */
	public void updateUserStatus(final String userId, final String enabled)
			throws DataAccessException {
		userDao.updateUserStatus(userId, enabled);
	}

	/**
	 * @param userId
	 * @return <code>boolean</code>
	 * @throws DataAccessException
	 * @see org.esco.portlets.news.dao.EscoUserDao#userRoleExist(java.lang.String)
	 */
//	public boolean userRoleExist(final String userId) throws DataAccessException {
//		return userDao.userRoleExist(userId);
//	}

	/**
	 * @param userId
	 * @param categoryId
	 * @return <code>boolean</code>
	 * @throws DataAccessException
	 * @see org.esco.portlets.news.dao.EscoUserDao#userRolesExistInTopicsOfcategory(java.lang.String, java.lang.Long)
	 */
//	public boolean userRolesExistInTopicsOfcategory(final String userId, final Long categoryId)
//			throws DataAccessException {
//		return userDao.userRolesExistInTopics(userId, categoryId);
//	}

	/**
	 * @param userId
	 * @param entityId
	 * @return <code>boolean</code>
	 * @throws DataAccessException
	 * @see org.esco.portlets.news.dao.EscoUserDao#userRolesExistInTopicsOfcategory(java.lang.String, java.lang.Long)
	 */
//	public boolean userRolesExistInCategoriesOfEntity(final String userId, final Long entityId)
//			throws DataAccessException {
//		return userDao.userRolesExistInCategories(userId, entityId);
//	}

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
	 * @param usersUid a list of id/userId to retrieve in the LDAP.
	 * @return <code>Map< String, IEscoUser ></code> A Map of IEscoUser (details of users) with id/userId as key.
	 * @see org.esco.portlets.news.dao.EscoUserDao#findPersonsByListUid(java.util.List)
	 */
	public Map<String, IEscoUser> findPersonsByListUid(final List<String> usersUid) {
		if (usersUid != null && !usersUid.isEmpty()) {
			Set<String> userIds = new HashSet<String>();
			userIds.addAll(usersUid);
			Map<String, IEscoUser> result = new HashMap<String, IEscoUser>();
			OrFilter filter = new OrFilter();
			for (String s : userIds) {
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
		List<String> userIds = new ArrayList<String>();
		if (users != null && !users.isEmpty()) {
			for (User u : users) {
				userIds.add(u.getUserId());
				EscoUser e = (EscoUser) u;
				if (LOG.isDebugEnabled()) {
					LOG.debug(e.toString());
				}
				tmp.put(u.getUserId(), e);
			}
			OrFilter filter = new OrFilter();
			for (String s : userIds) {
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
	 * Getter of member portletService.
	 * @return <code>PortletService</code> the attribute portletService
	 */
//	public PortletService getPortletService() {
//		return portletService;
//	}

	/**
	 * Setter of attribute portletService.
	 * @param portletService the attribute portletService to set
	 */
//	public void setPortletService(final PortletService portletService) {
//		this.portletService = portletService;
//	}

	/**
	 * @throws Exception
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(this.ldapUserService, "The property ldapUserService in class "
				+ this.getClass().getSimpleName() + " must not be null.");
		Assert.notNull(this.userDao, "The property userDao in class "
				+ this.getClass().getSimpleName() + " must not be null.");
//		Assert.notNull(this.portletService, "The property portletService in class "
//				+ this.getClass().getSimpleName() + " must not be null.");
		Assert.hasLength(this.displayName, "The property displayName in class "
				+ this.getClass().getSimpleName() + " must not be null or empty.");
		Assert.hasLength(this.mail, "The property mail in class "
				+ this.getClass().getSimpleName() + " must not be null or empty.");
	}
}
