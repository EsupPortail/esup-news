/**
* ESUP-Portail News - Copyright (c) 2009 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/

package org.esco.portlets.news.services;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esco.portlets.news.dao.EntityDAO;
import org.esco.portlets.news.dao.EscoUserDao;
import org.esco.portlets.news.dao.SubjectRoleDao;
import org.esco.portlets.news.domain.Entity;
import org.esco.portlets.news.domain.IEscoUser;
import org.esco.portlets.news.services.ldap.LdapUserService;
import org.esupportail.commons.services.ldap.LdapException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.uhp.portlets.news.NewsConstants;
import org.uhp.portlets.news.dao.CategoryDao;
import org.uhp.portlets.news.dao.TopicDao;
import org.uhp.portlets.news.domain.Category;
import org.uhp.portlets.news.domain.Item;
import org.uhp.portlets.news.domain.RolePerm;
import org.uhp.portlets.news.domain.Topic;
import org.uhp.portlets.news.domain.User;

/**
 * @author by GIP RECIA - Gribonvald Julien
 * 7 juil. 09
 */
@Service("escoUserManager")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class UserManagerImpl implements UserManager, InitializingBean {

	/** LOGger.*/
	static final Log LOG = LogFactory.getLog(UserManagerImpl.class);

	/** Injection du service LDAP ESUP-commons. */
	private LdapUserService ldapUserService;
	/** */
	@Autowired private CategoryDao categoryDao;
	/** */
	@Autowired private EntityDAO entityDao;
	/** */
	private EscoUserDao userDao;

	/** */
	@Autowired private RoleManager rm;
	/** */
	@Autowired private SubjectRoleDao roleDao;
	/** */
	@Autowired private TopicDao topicDao;

	/**
	 * Constructeur de l'objet UserManagerImpl.java.
	 */
	public UserManagerImpl() {
		super();
	}

	/** Obtains user's details from a list of user's id.
	 * @param usersUid a list of id/uid to retrieve in the LDAP.
	 * @return <code>Map< String, LdapUser ></code> A Map of LdapUser (details of users) with id/uid as key.
	 * @see org.esco.portlets.news.services.UserManager#getUsersByListUid(java.util.List)
	 */
	public Map<String, IEscoUser> getUsersByListUid(final List<String> usersUid) {
		return userDao.findPersonsByListUid(usersUid);
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
	 * Getter du membre userDao.
	 * @return <code>EscoUserDao</code> le membre userDao.
	 */
	public EscoUserDao getUserDao() {
		return userDao;
	}

	/**
	 * Setter du membre userDao.
	 * @param userDao la nouvelle valeur du membre userDao.
	 */
	public void setUserDao(final EscoUserDao userDao) {
		this.userDao = userDao;
	}

	/**
	 * @param userId The id/uid of the user.
	 * @return <code>boolean</code> return true if the user is super admin.
	 * @see org.esco.portlets.news.services.UserManager#isSuperAdmin(java.lang.String)
	 */
	public boolean isSuperAdmin(final String userId) {
		if (userId == null) {
			return false;
		}
		return this.userDao.isSuperAdmin(userId);
	}

	/**
	 * @param userId
	 * @return <code>boolean</code> return true if the user account is enable.
	 * @see org.esco.portlets.news.services.UserManager#isUserAccountEnabled(java.lang.String)
	 */
	public boolean isUserAccountEnabled(final String userId) {
		if (userId == null) {
			return false;
		}
		return this.userDao.isUserAccountEnabled(userId);
	}

	/**
	 * @param user
	 * @see org.esco.portlets.news.services.UserManager#saveUser(org.uhp.portlets.news.domain.User)
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void saveUser(final User user) {
		if (!userDao.isUserInDB(user.getUserId())) {
			user.setRegisterDate(new Date());
			user.setLastAccess(new Date());
			this.userDao.insert(user);
		} else {
			if (LOG.isDebugEnabled()) {
				LOG.debug("saveUser::User exists yet.");
			}
			this.userDao.update(user);
		}
	}


	/**
	 * @param uid
	 * @param enabled
	 * @see org.esco.portlets.news.services.UserManager#updateUserStatus(java.lang.String, java.lang.String)
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void updateUserStatus(final String uid, final String enabled)  {
		try {
			this.userDao.updateUserStatus(uid, enabled);
		} catch (DataAccessException e) {
			LOG.error("updateUserStatus error : " + e.getMessage());
			throw e;
		}
	}

	/**
	 * @param uid
	 * @see org.esco.portlets.news.services.UserManager#updateUserLastAccess(java.lang.String)
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void updateUserLastAccess(final String uid)  {
		try {
			this.userDao.updateUserLastAccess(uid);
		} catch (DataAccessException e) {
			LOG.error("updateUserLastAccess error : " + e.getMessage());
			throw e;
		}
	}

	/**
	 * @param searchBy
	 * @return <code>User</code>
	 * @see org.esco.portlets.news.services.UserManager#findUserByUid(java.lang.String)
	 */
	public IEscoUser findUserByUid(final String searchBy) {
		try {
			return userDao.getUserById(searchBy);
		} catch (DataAccessException e) {
			LOG.error("findUserById:: " + e.getLocalizedMessage());
		}
		return null;
	}

	/**
	 * @param user
	 * @param entityId
	 * @see org.esco.portlets.news.services.UserManager#migrationUserCtxRole(org.uhp.portlets.news.domain.User, java.lang.Long)
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void migrationUserCtxRole(final User user, final Long entityId) {
		if (!rm.isSuperAdmin(user.getUserId(), false)
				&& !rm.isRoleExistForContext(entityId, NewsConstants.CTX_E, user.getUserId(), false)) {
		// we add the role of the user
			this.rm.addSubjectRole(user.getUserId(), false, RolePerm.ROLE_USER.getName(), NewsConstants.CTX_E, entityId);
		}
	}



	/** (non-Javadoc)
	 * @see org.esco.portlets.news.services.UserManager#getUserRoleInCtx(java.lang.Long, java.lang.String, java.lang.String)
	 */
	@Override
	public String getUserRoleInCtx(final Long ctxId, final String ctxType, final String principal) {
		return rm.getUserRoleInCtx(ctxId, ctxType, principal, false);
	}

	/**
	 * @param token Research criteria.
	 * @return <code>List< User ></code> The list of users returned corresponding to the token.
	 * @throws LdapException
	 * @see org.esco.portlets.news.services.UserManager#findPersonsByToken(java.lang.String)
	 */
	public List<IEscoUser> findPersonsByToken(final String token) throws LdapException {
		try {
			return userDao.findPersonsByToken(token);
		} catch (LdapException e) {
			if (LOG.isWarnEnabled()) {
				LOG.warn("UserManager::findPersonsByToken: e=" + e.getLocalizedMessage());
			}
			throw e;
		}
	}

	/**
	 * @param token Research criteria.
	 * @param filter
	 * @return <code>List< User ></code> The list of users returned corresponding to the token.
	 * @throws LdapException
	 * @see org.esco.portlets.news.services.UserManager#findPersonsByToken(java.lang.String)
	 */
	public List<IEscoUser> findPersonsByTokenAndFilter(final String token,
			final org.springframework.ldap.support.filter.Filter filter) throws LdapException {
		try {
			return userDao.findPersonsByTokenAndFilter(token, filter);
		} catch (LdapException e) {
			if (LOG.isWarnEnabled()) {
				LOG.warn("UserManager::findPersonsByTokenAndFilter: e=" + e.getLocalizedMessage());
			}
			throw e;
		}
	}

	/**
	 * @param uid
	 * @see org.esco.portlets.news.services.UserManager#deleteUser(java.lang.String)
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteUser(final String uid) {
		try {
			userDao.deleteUser(uid, true);
		} catch (DataAccessException e) {
			if (LOG.isWarnEnabled()) {
				LOG.warn("deleteUser:: data access error :" + e.getMessage());
			}
			throw e;
		}
	}

	/**
	 * @return <code>List< User ></code> The list of all users.
	 * @see org.esco.portlets.news.services.UserManager#getAllUsers()
	 */
	public List<IEscoUser> getAllUsers() {
		return this.userDao.getAllUsers();
	}

	/**
	 * @return <code>List< User ></code> The list of all super users.
	 * @see org.esco.portlets.news.services.UserManager#getAllSuperUsers()
	 */
	public List<IEscoUser> getAllSuperUsers() {
		return this.userDao.getAllSuperUsers();
	}

	/**
	 * @param uid
	 * @return <code>boolean</code> true if the user has a role.
	 */
	boolean hasAnyRole(final String uid) {
		if (this.userDao.userRoleExist(uid)) {
			return true;
		}
		return false;
	}

	/**
	 * @param ctxId
	 * @param ctxType
	 * @param uid
	 * @return <code>boolean</code> true if the user is admin for the context.
	 * @see org.esco.portlets.news.services.UserManager
	 * #isUserAdminInCtx(java.lang.Long, java.lang.String, java.lang.String)
	 */
	public boolean isUserAdminInCtx(final Long ctxId, final String ctxType, final String uid) {
		if (this.userDao.isSuperAdmin(uid)) {
			return true;
		}
		return this.rm.isInRoleinCtx(ctxId, ctxType, RolePerm.ROLE_MANAGER, uid, false);
	}

	/**
	 * @param userId
	 * @return <code>Map< String, String ></code> The list of roles and associated entities for the user.
	 * @see org.esco.portlets.news.services.UserManager#loadUserEntityRoleMaps(java.lang.String)
	 */
	public Map<String, String> loadUserEntityRoleMaps(final String userId) {
		List<Entity> entities = this.entityDao.getEntitiesByUser(userId);
		Map<String, String> map = new HashMap<String, String>();
		for (Entity e : entities) {
			map.put(e.getName(),
					this.rm.getUserRoleInCtx(e.getEntityId(), NewsConstants.CTX_E, userId, false));
		}
		return map;
	}

	/**
	 * @param userId
	 * @return <code>Map< String,  Map< String, String > ></code>
	 * The list of roles and associated categories for the user.
	 * @see org.esco.portlets.news.services.UserManager#loadUserCategoryRoleMaps(java.lang.String)
	 */
	public Map<String, Map<String, String>> loadUserCategoryRoleMaps(final String userId) {
		List<Entity> entities = this.entityDao.getEntitiesByUser(userId);
		Map<String,  Map<String, String>> map = new HashMap<String,  Map<String, String>>();
		for (Entity e : entities) {
			map.put(e.getName(), this.getUserRoleMapsForCategoriesInEntity(userId, e.getEntityId()));
		}
		return map;
	}

	/**
	 * @param userId
	 * @return <code>Map< String, Map< String, String >></code>
	 * The list of categories associated with the list of roles and associated topics for the user.
	 * @see org.esco.portlets.news.services.UserManager#loadUserTopicRoleMaps(java.lang.String)
	 */
	public Map<String, Map<String, String>> loadUserTopicRoleMaps(final String userId) {
		List<Category> categories = this.categoryDao.getCategoriesByUser(userId);
		Map<String, Map<String, String>> maps = new HashMap<String, Map<String, String>>();
		for (Category c : categories) {
			maps.put(c.getName(), this.getUserRoleMapsForTopicsInCategory(userId, c.getCategoryId()));
		}
		return maps;
	}

	/**
	 * @param userId
	 * @param entityId
	 * @return <code>Map< String, String ></code> The list of roles with associated categories for a user in an entity.
	 */
	private Map<String, String> getUserRoleMapsForCategoriesInEntity(final String userId,
			final Long entityId) {
		List<Category> categories = this.categoryDao.getCategoriesOfEntityByUser(userId, entityId);
		Map<String, String> catRoles = new HashMap<String, String>();
		for (Category c : categories) {
			String r = this.rm.getUserRoleInCtx(c.getCategoryId(), NewsConstants.CTX_C, userId, false);
			if (r != null) {
				catRoles.put(c.getName(), r);
			}
		}
		return catRoles;
	}

	/**
	 * @param userId
	 * @param categoryId
	 * @return <code>Map< String, String ></code> The list of roles with associated topics for a user in a categorie.
	 */
	private Map<String, String> getUserRoleMapsForTopicsInCategory(final String userId,
			final Long categoryId) {
		List<Topic> topics = this.topicDao.getTopicsForCategoryByUser(categoryId, userId);
		Map<String, String> topicRoles = new HashMap<String, String>();
		for (Topic t : topics) {
			String r = this.rm.getUserRoleInCtx(t.getTopicId(), NewsConstants.CTX_T, userId, false);
			if (r != null) {
				topicRoles.put(t.getName(), r);
			}
		}
		return topicRoles;
	}

	/**
	 * @param userId
	 * @return <code>boolean</code> true if the user is permitted to access to the application.
	 * @see org.esco.portlets.news.services.UserManager#isPermitted(java.lang.String)
	 */
	public boolean isPermitted(final String userId) {
		return this.userDao.isPermitted(userId);
	}

	/**
	 * @param uid
	 * @param item
	 * @return <code>boolean</code> true if the user can validate a news.
	 * @see org.esco.portlets.news.services.UserManager
	 * #canValidate(java.lang.String, org.uhp.portlets.news.domain.Item)
	 */
	public boolean canValidate(final String uid, final Item item) {
		try {
			if (this.isAdmin(uid, item.getCategoryId())) {
				return true;
			}
			List<String> roles = this.roleDao.getUserRolesInTopicsByItem(uid, false, item.getItemId());
			if (roles.isEmpty()) {
				return false;
			} else if (roles.contains(RolePerm.ROLE_MANAGER.getName())) {
				return true;
			} else if (roles.contains(RolePerm.ROLE_EDITOR.getName()) && item.getPostedBy().equals(uid)) {
				return true;
			}
		} catch (DataAccessException e) {
			LOG.warn("canValidate::" + e.getLocalizedMessage());
		}
		return false;
	}


	/**
	 * @param uid
	 * @param item
	 * @return <code>boolean</code> true if the user can edit a news.
	 * @see org.esco.portlets.news.services.UserManager
	 * #canEditItem(java.lang.String, org.uhp.portlets.news.domain.Item)
	 */
	public boolean canEditItem(final String uid, final Item item) {
		try {
			if (this.isAdmin(uid, item.getCategoryId())) {
				return true;
			}

			List<String> roles = this.roleDao.getUserRolesInTopicsByItem(uid, false, item.getItemId());
			if (roles.isEmpty()) {
				return false;
			} else if (roles.contains(RolePerm.ROLE_MANAGER.getName())) {
				return true;
			} else if ((roles.contains(RolePerm.ROLE_CONTRIBUTOR.getName())
					|| roles.contains(RolePerm.ROLE_EDITOR.getName()))
					&& (item.getPostedBy().equals(uid))) {
				return true;
			}
		} catch (DataAccessException e) {
			LOG.warn("canEditItem::" + e.getLocalizedMessage());
		}
		return false;
	}

	/**
	 * @param uid
	 * @param cId
	 * @return <code>boolean</code> true if the user is an admin of the categorie.
	 */
	private boolean isAdmin(final String uid, final Long cId) {
		if (this.userDao.isSuperAdmin(uid)
				|| this.rm.isInRoleinCtx(cId, NewsConstants.CTX_C, RolePerm.ROLE_MANAGER, uid, false)) {
			return true;
		}
		return false;
	}

	/**
	 * @param uid
	 * @return obtain the user name from the uid
	 * @see org.esco.portlets.news.services.UserManager#getUserNameByUid(java.lang.String)
	 */
	public String getUserNameByUid(final String uid) {
//TODO a modifier
		try {
			return this.userDao.getUserNameById(uid);
		} catch (org.esupportail.commons.exceptions.UserNotFoundException e) {
			return uid;
		}
	}

	/**
	 * Getter of member rm.
	 * @return <code>RoleManager</code> the attribute rm
	 */
	public RoleManager getRm() {
		return rm;
	}

	/**
	 * Setter of attribute rm.
	 * @param rm the attribute rm to set
	 */
	public void setRm(final RoleManager rm) {
		this.rm = rm;
	}

	/**
	 * Getter of member roleDao.
	 * @return <code>SubjectRoleDao</code> the attribute roleDao
	 */
	public SubjectRoleDao getRoleDao() {
		return roleDao;
	}

	/**
	 * Setter of attribute roleDao.
	 * @param roleDao the attribute roleDao to set
	 */
	public void setRoleDao(SubjectRoleDao roleDao) {
		this.roleDao = roleDao;
	}

	/**
	 * Getter of member categoryDao.
	 * @return <code>CategoryDao</code> the attribute categoryDao
	 */
	public CategoryDao getCategoryDao() {
		return categoryDao;
	}

	/**
	 * Setter of attribute categoryDao.
	 * @param categoryDao the attribute categoryDao to set
	 */
	public void setCategoryDao(CategoryDao categoryDao) {
		this.categoryDao = categoryDao;
	}

	/**
	 * Getter of member entityDao.
	 * @return <code>EntityDAO</code> the attribute entityDao
	 */
	public EntityDAO getEntityDao() {
		return entityDao;
	}

	/**
	 * Setter of attribute entityDao.
	 * @param entityDao the attribute entityDao to set
	 */
	public void setEntityDao(EntityDAO entityDao) {
		this.entityDao = entityDao;
	}

	/**
	 * Getter of member topicDao.
	 * @return <code>TopicDao</code> the attribute topicDao
	 */
	public TopicDao getTopicDao() {
		return topicDao;
	}

	/**
	 * Setter of attribute topicDao.
	 * @param topicDao the attribute topicDao to set
	 */
	public void setTopicDao(TopicDao topicDao) {
		this.topicDao = topicDao;
	}


	/**
	 * @throws Exception
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(this.getLdapUserService(), "The property ldapUserService in class "
				+ getClass().getSimpleName() + " must not be null.");
		Assert.notNull(this.categoryDao, "The property categorieDao in class "
				+ getClass().getSimpleName() + " must not be null.");
		Assert.notNull(this.topicDao, "The property topicDao in class "
				+ getClass().getSimpleName() + " must not be null.");
		Assert.notNull(this.userDao, "The property userDao in class "
				+ getClass().getSimpleName() + " must not be null.");
		Assert.notNull(this.roleDao, "The property roleDao in class "
				+ this.getClass().getSimpleName() + " must not be null.");
	}
}



