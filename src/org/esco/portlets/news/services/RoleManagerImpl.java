/**
 *
 */
package org.esco.portlets.news.services;

import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esco.portlets.news.dao.EntityDAO;
import org.esco.portlets.news.dao.EscoUserDao;
import org.esco.portlets.news.dao.SubjectRoleDao;
import org.esco.portlets.news.domain.EscoUser;
import org.esco.portlets.news.services.group.GroupService;
import org.esupportail.portal.ws.client.PortalGroup;
import org.esupportail.portal.ws.client.exceptions.PortalErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.uhp.portlets.news.NewsConstants;
import org.uhp.portlets.news.dao.CategoryDao;
import org.uhp.portlets.news.dao.TopicDao;
import org.uhp.portlets.news.domain.Category;
import org.uhp.portlets.news.domain.Role;
import org.uhp.portlets.news.domain.RolePerm;
import org.uhp.portlets.news.domain.Topic;
import org.uhp.portlets.news.domain.User;
import org.uhp.portlets.news.domain.UserRole;
import org.uhp.portlets.news.service.exception.ResourceNotFoundException;

/**
 * @author GIP RECIA - Julien Gribonvald
 * 24 mars 2014
 */
@Service("roleManager")
@Transactional(readOnly = true)
public class RoleManagerImpl implements RoleManager {

	/** LOGGER.*/
	static final Log LOG = LogFactory.getLog(RoleManagerImpl.class);

	/** */
	@Autowired
	private CategoryDao categoryDao;
	/** */
	@Autowired
	private EntityDAO entityDao;
	/** */
	@Autowired
	private SubjectRoleDao roleDao;
	/** */
	@Autowired
	private TopicDao topicDao;
	/** */
	@Autowired
	private GroupService groupService;
	/** */
	@Autowired
	private EscoUserDao userDao;


	/**
	 * Contructor of the object RoleManagerImpl.java.
	 */
	public RoleManagerImpl() {
		super();
	}

	/**
	 * @param principal
	 * @param isGroup
	 * @param role
	 * @param ctx
	 * @param ctxId
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void addSubjectRole(final String principal, final boolean isGroup, final String role, final String ctx, final Long ctxId) {
		// on ajoute le UserRole dans le contexte.
		addSubjectRoleForContext(principal, isGroup, role, ctx, ctxId, null);
		// puis si c'est un groupe on applique la même chose à tous les membres du groupe
		if (isGroup) {
			List<String> users = getGroupService().searchMembersOfPortalGroupById(principal);
			if (users != null) {
				for (String user : users) {
					addSubjectRoleForContext(user, false, role, ctx, ctxId, principal);
				}
			}
		}
	}

	/**
	 * @param principal
	 * @param isGroup
	 * @param role
	 * @param ctx
	 * @param ctxId
	 * @param fromGroup
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	private void addSubjectRoleForContext(final String principal, final boolean isGroup, final String role, final String ctx, final Long ctxId, final String fromGroup) {
		if (!isGroup && this.isSuperAdmin(principal, isGroup)) {
			return;
		} else if (!isGroup) {
			saveUser(principal);
		}
		if (NewsConstants.CTX_T.equalsIgnoreCase(ctx)) {
			this.addUserRoleForTopic(principal, isGroup, fromGroup, role, ctxId, true);
			// we need to add at least the user_role for the user on
			// category and entity if he doesn't have a role on it
			final Long cID = this.topicDao.getTopicById(ctxId).getCategoryId();
			final Long eID = this.categoryDao.getCategoryById(cID).getEntityId();
			this.addUserRoleForCategory(principal, isGroup, fromGroup, RolePerm.ROLE_USER.getName(), cID, false, false);
			this.addUserRoleForEntity(principal, isGroup, fromGroup, RolePerm.ROLE_USER.getName(), eID, false, false);
		} else if (NewsConstants.CTX_C.equals(ctx)) {
			this.addUserRoleForCategory(principal, isGroup, fromGroup, role, ctxId, true, true);
			// we need to add at least the user_role for the user on entity if he doesn't have a role on it
			final Long eID = this.categoryDao.getCategoryById(ctxId).getEntityId();
			this.addUserRoleForEntity(principal, isGroup, fromGroup, RolePerm.ROLE_USER.getName(), eID, false, false);
		} else if (NewsConstants.CTX_E.equals(ctx)) {
			this.addUserRoleForEntity(principal, isGroup, fromGroup, role, ctxId, true, true);
		} else {
			throw new IllegalArgumentException("The context " + ctx + " isn't unknown !");
		}
	}

	/**
	 * @param principal
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	private void saveUser(final String principal) {
		User user = this.userDao.getUserById(principal);
		if (user == null) {
			user = new EscoUser();
			user.setUserId(principal);
		}
		user.setIsSuperAdmin(NewsConstants.S_N);
		user.setEnabled(NewsConstants.S_Y);
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
	 * Add/update user's role in an entity, his categories and his topics.
	 * @param principal
	 * @param isGroup
	 * @param fromGroup
	 * @param newRole
	 * @param ctxId
	 * @param applyOnChild
	 * @param updToLowerRole
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	private void addUserRoleForEntity(final String principal, final boolean isGroup, final String fromGroup,
			final String newRole, final Long ctxId, final boolean applyOnChild, final boolean updToLowerRole) {
		String oldRole = this.roleDao.getRoleForCtx(principal, ctxId, NewsConstants.CTX_E, isGroup);
		if (oldRole == null || oldRole.isEmpty()) {
			// we add the role of the user
			if (LOG.isDebugEnabled()) {
				LOG.debug("Adding user role '" + newRole + "' in entity id[" + ctxId + "].");
			}
			this.roleDao.addUserRole(principal, newRole, NewsConstants.CTX_E, ctxId, isGroup, fromGroup);
			// and we do the same thing on the childs
			if (applyOnChild) {
				List<Category> categories = categoryDao.getAllCategoryOfEntity(ctxId);
				for (Category c : categories) {
					this.addUserRoleForCategory(principal, isGroup, fromGroup, newRole, c.getCategoryId(), true, false);
				}
			}
		} else if (RolePerm.valueOf(oldRole).getMask() < RolePerm.valueOf(newRole).getMask()) {
			// we update the role if the old role is lesser than the newer.
			if (LOG.isDebugEnabled()) {
				LOG.debug("Update of the user role '" + oldRole + "' to a upper role '"
						+ newRole + "' in entity id[" + ctxId + "].");
			}
			this.roleDao.updateUserRoleForCtx(principal, newRole, NewsConstants.CTX_E, ctxId, isGroup, fromGroup);
			// and we do the same thing on the childs
			if (applyOnChild) {
				List<Category> categories = categoryDao.getAllCategoryOfEntity(ctxId);
				for (Category c : categories) {
					this.addUserRoleForCategory(principal, isGroup, fromGroup, newRole, c.getCategoryId(), true, false);
				}
			}
		}  else if (RolePerm.valueOf(oldRole).getMask() > RolePerm.valueOf(newRole).getMask() && updToLowerRole) {
			// we update the role for the entity only to lower role.
			if (LOG.isDebugEnabled()) {
				LOG.debug("Update of the user role '" + oldRole + "' to a lower role '"
						+ newRole + "' in entity id[" + ctxId + "].");
			}
			this.roleDao.updateUserRoleForCtx(principal, newRole, NewsConstants.CTX_E, ctxId, isGroup, fromGroup);
		}
	}

	/**
	 * Add/update user's role in a category and his topics when it's already done for entity.
	 * @param principal
	 * @param isGroup
	 * @param fromGroup
	 * @param newRole
	 * @param ctxId
	 * @param applyOnChild
	 * @param updToLowerRole
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	private void addUserRoleForCategory(final String principal, final boolean isGroup, final String fromGroup,
			final String newRole, final Long ctxId, final boolean applyOnChild, final boolean updToLowerRole) {
		String oldRole = this.roleDao.getRoleForCtx(principal, ctxId, NewsConstants.CTX_C, isGroup);
		if (oldRole == null || oldRole.isEmpty()) {
			// we add the role of the user
			if (LOG.isDebugEnabled()) {
				LOG.debug("Adding user role '" + newRole + "' in category id[" + ctxId + "].");
			}
			this.roleDao.addUserRole(principal, newRole, NewsConstants.CTX_C, ctxId, isGroup, fromGroup);
			// and we do the same thing on the childs
			if (applyOnChild) {
				List<Topic> topics = topicDao.getTopicListByCategory(ctxId);
				for (Topic t : topics) {
					this.addUserRoleForTopic(principal, isGroup, fromGroup, newRole, t.getTopicId(), false);
				}
			}
		} else if (RolePerm.valueOf(oldRole).getMask() < RolePerm.valueOf(newRole).getMask()) {
			// we update the role if the old role is lesser than the newer.
			if (LOG.isDebugEnabled()) {
				LOG.debug("Update of the user role '" + oldRole + "' to a upper role '"
						+ newRole + "' in category id[" + ctxId + "].");
			}
			this.roleDao.updateUserRoleForCtx(principal, newRole, NewsConstants.CTX_C, ctxId, isGroup, fromGroup);
			// and we do the same thing on the childs
			if (applyOnChild) {
				List<Topic> topics = topicDao.getTopicListByCategory(ctxId);
				for (Topic t : topics) {
					this.addUserRoleForTopic(principal, isGroup, fromGroup, newRole, t.getTopicId(), false);
				}
			}
		} else if (RolePerm.valueOf(oldRole).getMask() > RolePerm.valueOf(newRole).getMask() && updToLowerRole) {
			// we update the role to a lower role if the user role in the entity is lesser or equals than the new role.
			final Long eID = this.categoryDao.getCategoryById(ctxId).getEntityId();
			String entityRole = this.roleDao.getRoleForCtx(principal, eID, NewsConstants.CTX_E, isGroup);
			if (RolePerm.valueOf(entityRole).getMask() <= RolePerm.valueOf(newRole).getMask()) {
				if (LOG.isDebugEnabled()) {
					LOG.debug("Update of the user role '" + oldRole + "' to a lower role '"
							+ newRole + "' in category id[" + ctxId + "].");
				}
				this.roleDao.updateUserRoleForCtx(principal, newRole, NewsConstants.CTX_C, ctxId, isGroup, fromGroup);
			}
		}

	}

	/**
	 * Add/update user's role in a topic when it's already done for category and entity.
	 * @param principal
	 * @param isGroup
	 * @param fromGroup
	 * @param newRole
	 * @param ctxId
	 * @param updToLowerRole
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	private void addUserRoleForTopic(final String principal, final boolean isGroup, final String fromGroup,
			final String newRole, final Long ctxId, final boolean updToLowerRole) {
		String oldRole = this.roleDao.getRoleForCtx(principal, ctxId, NewsConstants.CTX_T, isGroup);
		if (oldRole == null || oldRole.isEmpty()) {
			// we add the role of the user
			if (LOG.isDebugEnabled()) {
				LOG.debug("Adding user role '" + newRole + "' in topic id[" + ctxId + "].");
			}
			this.roleDao.addUserRole(principal, newRole, NewsConstants.CTX_T, ctxId, isGroup, fromGroup);
		} else if (RolePerm.valueOf(oldRole).getMask() < RolePerm.valueOf(newRole).getMask()) {
			// we update the role if the old role is lesser than the newer.
			if (LOG.isDebugEnabled()) {
				LOG.debug("Update of the user role '" + oldRole + "' to a upper role '"
						+ newRole + "' in topic id[" + ctxId + "].");
			}
			this.roleDao.updateUserRoleForCtx(principal, newRole, NewsConstants.CTX_T, ctxId, isGroup, fromGroup);
		} else if (RolePerm.valueOf(oldRole).getMask() > RolePerm.valueOf(newRole).getMask() && updToLowerRole) {
			// we update the role to a lower role
			// if the user role in the category is lesser or equals than the new role.
			final Long cID = this.topicDao.getTopicById(ctxId).getCategoryId();
			String catRole = this.roleDao.getRoleForCtx(principal, cID, NewsConstants.CTX_C, isGroup);
			if (RolePerm.valueOf(catRole).getMask() <= RolePerm.valueOf(newRole).getMask()) {
				if (LOG.isDebugEnabled()) {
					LOG.debug("Update of the user role '" + oldRole + "' to a lower role '"
							+ newRole + "' in topci id[" + ctxId + "].");
				}
				this.roleDao.updateUserRoleForCtx(principal, newRole, NewsConstants.CTX_T, ctxId, isGroup, fromGroup);
			}
		}
	}

	/**
	 * @param principal
	 * @param isGroup
	 * @param ctxId
	 * @param ctxType
	 * @see org.esco.portlets.news.services.RoleManager#removeUserRoleForCtx(String, boolean, Long, String)
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void removeUserRoleForCtx(final String principal, final boolean isGroup, final Long ctxId, final String ctxType) {
		if (!isGroup) {
			UserRole ur = roleDao.getUserRoleForCtx(principal, ctxId, ctxType, isGroup);
			if (ur == null || ur.getFromGroup() != null)
				return;
		}
		if (NewsConstants.CTX_E.equals(ctxType)) {
			this.removeUserRoleForEntity(principal, isGroup, ctxId);
		} else if (NewsConstants.CTX_C.equals(ctxType)) {
			this.removeUserRoleForCategory(principal, isGroup, ctxId);
		} else if (NewsConstants.CTX_T.equals(ctxType)) {
			this.removeUserRoleForTopic(principal, isGroup, ctxId);
		} else {
			throw new IllegalArgumentException("The context " + ctxType + " isn't unknown !");
		}
		if (!isGroup && !this.userDao.isSuperAdmin(principal) && !this.userDao.userRoleExist(principal)) {
			this.userDao.deleteUser(principal, false);
		}
	}

	/**
	 * Remove roles from the Entity and on all categories and topics of the entity.
	 * @param principal
	 * @param isGroup
	 * @param target
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	private void removeUserRoleForEntity(final String principal, final boolean isGroup, final Long target) {
		// remove from childs
		List<Category> categories = categoryDao.getAllCategoryOfEntity(target);
		for (Category c : categories) {
			List<Topic> topics = topicDao.getTopicListByCategory(c.getCategoryId());
			for (Topic t : topics) {
				if (LOG.isDebugEnabled()) {
					LOG.debug("Remove user role in topic id[" + t.getTopicId() + "].");
				}
				this.roleDao.removeUserRoleForCtx(principal, t.getTopicId(), NewsConstants.CTX_T, isGroup);
			}
			if (LOG.isDebugEnabled()) {
				LOG.debug("Remove user role in category id[" + c.getCategoryId() + "].");
			}
			this.roleDao.removeUserRoleForCtx(principal, c.getCategoryId(), NewsConstants.CTX_C, isGroup);
		}
		// remove from entity
		if (LOG.isDebugEnabled()) {
			LOG.debug("Remove user role in entity id[" + target + "].");
		}
		this.roleDao.removeUserRoleForCtx(principal, target, NewsConstants.CTX_E, isGroup);
	}

	/**
	 * Remove roles from the Category and on all topics of the category.
	 * @param principal
	 * @param isGroup
	 * @param target
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	private void removeUserRoleForCategory(final String principal, final boolean isGroup, final Long target) {
		Long eID = this.categoryDao.getCategoryById(target).getEntityId();
		String entityRole =  this.roleDao.getRoleForCtx(principal, eID, NewsConstants.CTX_E, isGroup);
		// if it's a simple user_role in entity
		if (RolePerm.ROLE_USER.getName().equalsIgnoreCase(entityRole)) {
			// remove from category
			if (LOG.isDebugEnabled()) {
				LOG.debug("Remove user role in category id[" + target + "].");
			}
			this.roleDao.removeUserRoleForCtx(principal, target, NewsConstants.CTX_C, isGroup);
			// and for childs
			List<Topic> topics = topicDao.getTopicListByCategory(target);
			for (Topic t : topics) {
				// remove from topics
				if (LOG.isDebugEnabled()) {
					LOG.debug("Remove user role in topic id[" + t.getTopicId() + "].");
				}
				this.roleDao.removeUserRoleForCtx(principal, t.getTopicId(), NewsConstants.CTX_T, isGroup);
			}
			if (!roleDao.userRolesExistInCategoriesOfEntity(principal, isGroup, eID)) {
				// remove from entity
				if (LOG.isDebugEnabled()) {
					LOG.debug("The user has no roles in other categories "
							+ "so we remove user role in entity id[" + eID + "].");
				}
				this.roleDao.removeUserRoleForCtx(principal, eID, NewsConstants.CTX_E, isGroup);
			}
		} else {
			// If it's a upper role defined in the entity, we must set the role as same as it's define in the entity
			if (LOG.isDebugEnabled()) {
				LOG.debug("Finaly no remove but update user role in category id[" + target + "] to the category role.");
			}
			this.addUserRoleForCategory(principal, isGroup, null, entityRole, target, false, true);
			if (isGroup) {
				List<String> users = getGroupService().searchMembersOfPortalGroupById(principal);
				if (users != null) {
					for (String user : users) {
						this.addUserRoleForCategory(user, false, principal, entityRole, target, false, true);
					}
				}
			}
			// and for topics of category
			List<Topic> topics = topicDao.getTopicListByCategory(target);
			for (Topic t : topics) {
				// update from category
				if (LOG.isDebugEnabled()) {
					LOG.debug("Finaly no remove but update user role in topic id[" + t.getTopicId()
							+ "] to the entity role.");
				}
				this.addUserRoleForTopic(principal, isGroup, null, entityRole, t.getTopicId(), true);
				if (isGroup) {
					List<String> users = getGroupService().searchMembersOfPortalGroupById(principal);
					if (users != null) {
						for (String user : users) {
							this.addUserRoleForTopic(user, false, principal, entityRole, t.getTopicId(), true);
						}
					}
				}
			}
		}

	}

	/**
	 * Remove roles from the topic and check all other rights in the category/topics/entity.
	 * @param principal
	 * @param isGroup
	 * @param target
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	private void removeUserRoleForTopic(final String principal, final boolean isGroup, final Long target) {
		Long cID = this.topicDao.getTopicById(target).getCategoryId();
		Long eID = this.categoryDao.getCategoryById(cID).getEntityId();
		String catRole = this.roleDao.getRoleForCtx(principal, cID, NewsConstants.CTX_C, isGroup);
		// If it's a USER_ROLE in the category, so the entity too.
		if (RolePerm.ROLE_USER.getName().equalsIgnoreCase(catRole)) {
			// remove from topic
			if (LOG.isDebugEnabled()) {
				LOG.debug("Remove user role in topic id[" + target + "].");
			}
			this.roleDao.removeUserRoleForCtx(principal, target, NewsConstants.CTX_T, isGroup);
			// check if we can remove from category
			if (!this.roleDao.userRolesExistInTopicsOfCategory(principal, isGroup, cID)) {
				if (LOG.isDebugEnabled()) {
					LOG.debug("Remove user role in category id[" + cID + "].");
				}
				this.roleDao.removeUserRoleForCtx(principal, cID, NewsConstants.CTX_C, isGroup);
				// check if we can remove from entity
				if (!roleDao.userRolesExistInCategoriesOfEntity(principal, isGroup, eID)) {
					if (LOG.isDebugEnabled()) {
						LOG.debug("Remove user role in entity id[" + eID + "].");
					}
					this.roleDao.removeUserRoleForCtx(principal, eID, NewsConstants.CTX_E, isGroup);
				}
			}
		} else {
			// If it's a upper role defined in the category, we must set the role as same as it's define in the category
			if (LOG.isDebugEnabled()) {
				LOG.debug("finaly no remove but update user role in topic id[" + target + "] to the category role.");
			}
			this.addUserRoleForTopic(principal, isGroup, null, catRole, target, true);
			if (isGroup) {
				List<String> users = getGroupService().searchMembersOfPortalGroupById(principal);
				if (users != null) {
					for (String user : users) {
						this.addUserRoleForTopic(user, false, principal, catRole, target, true);
					}
				}
			}
		}
	}

	/**
	 * @param target
	 * @param targetCtx
	 * @return <code>Map< String, List< UserRole >></code>
	 * @see org.esco.portlets.news.services.RoleManager#getUserRolesByCtxId(Long, String)
	 */
	public Map<String, List<UserRole>> getUserRolesByCtxId(final Long target, final String targetCtx) {
		Map<String, List<UserRole>> userRoleLists = new HashMap<String, List<UserRole>>();
		Set<RolePerm> set = EnumSet.of(RolePerm.ROLE_MANAGER, RolePerm.ROLE_CONTRIBUTOR, RolePerm.ROLE_EDITOR);
		for (RolePerm r : set) {
			final List<UserRole> list = this.roleDao.getUsersByRole(target, targetCtx, r);
			if (list != null) {
				for (UserRole ur : list) {
					if (ur.getIsGroup().equals("1"))
						getPortalGroupById(ur);
				}
			}
			userRoleLists.put(r.toString(), list);
		}
		return userRoleLists;
	}

	/**
	 * @param ctxId
	 * @param ctxType
	 * @param principal
	 * @param isGroup
	 * @return <code>String</code> return the role of the user in the context
	 */
	public String getUserRoleInCtx(final Long ctxId, final String ctxType, final String principal, final boolean isGroup) {
		if (!isGroup && this.userDao.isSuperAdmin(principal)) {
			return RolePerm.ROLE_ADMIN.getName();
		}
		return this.roleDao.getRoleForCtx(principal, ctxId, ctxType, isGroup);
	}

	/**
	 * @param ctxId
	 * @param ctxType
	 * @param principal
	 * @param isGroup
	 * @return <code>boolean</code>
	 * @see org.esco.portlets.news.services.RoleManager#isRoleExistForContext(java.lang.Long, java.lang.String, java.lang.String, boolean)
	 */
	public boolean isRoleExistForContext(final Long ctxId, final String ctxType, final String principal, final boolean isGroup) {
		Assert.notNull(principal,"You can't do this action when the principal is null!");
		return this.roleDao.isUserRoleExistForContext(ctxId, ctxType, principal, isGroup);
	}

	/**
	 * @return <code>List< Role ></code> The list of all roles.
	 * @see org.esco.portlets.news.services.RoleManager#getAllRoles()
	 */
	public List<Role> getAllRoles() {
		return this.roleDao.getAllRoles();
	}

	/**
	 * It returns the list of users having a role in the context with there role associated.
	 * @param target
	 * @param targetCtx
	 * @return <code>List< UserRole ></code> The list of userRole for the context.
	 * @see org.esco.portlets.news.services.RoleManager#getUsersRolesForCtx(java.lang.Long, java.lang.String)
	 */
	public List<UserRole> getUsersRolesForCtx(final Long target, final String targetCtx) {
		return this.roleDao.getUsersRolesForCtx(target, targetCtx);
	}

	/**
	 * @param principal The id/uid of the user.
	 * @return <code>boolean</code> return true if the user is super admin.
	 * @see org.esco.portlets.news.services.RoleManager#isSuperAdmin(String, boolean)
	 */
	public boolean isSuperAdmin(final String principal, final boolean isGroup) {
		if (principal == null || isGroup) {
			return false;
		}
		return this.userDao.isSuperAdmin(principal);
	}



	/**
	 * @see org.esco.portlets.news.services.RoleManager#isInRoleinCtx(java.lang.Long, java.lang.String, org.uhp.portlets.news.domain.RolePerm, java.lang.String, boolean)
	 */
	public boolean isInRoleinCtx(final Long ctxId, final String ctxType, final RolePerm role, final String principal, final boolean isGroup) {
		if (!isGroup && principal != null && isSuperAdmin(principal, isGroup) )
			return true;
		String r = getUserRoleInCtx(ctxId, ctxType, principal, isGroup);
		if (r == null || r.isEmpty())
			return false;
		return role.compare(role, RolePerm.valueOf(r)) >= 0;
	}

	/**
	 * @param ur
	 */
	private void getPortalGroupById(final UserRole ur){
		try {
			PortalGroup pg = this.groupService.getPortalGroupById(ur.getPrincipal());
			if (pg != null) {
				ur.setDisplayName(pg.getName());
			} else {
				ur.setDisplayName(null);
			}
		} catch (PortalErrorException e) {
			LOG.warn("UserManagerService::getUserRolesByCtxId():: principal=" + ur.getPrincipal()
					+ " PortalErrorException " + e.getMessage());
			throw new ResourceNotFoundException("Is web service for uportal groups correctly installed? "
					+ e.getMessage());
		}
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
	public void setCategoryDao(final CategoryDao categoryDao) {
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
	public void setEntityDao(final EntityDAO entityDao) {
		this.entityDao = entityDao;
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
	public void setRoleDao(final SubjectRoleDao roleDao) {
		this.roleDao = roleDao;
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
	public void setTopicDao(final TopicDao topicDao) {
		this.topicDao = topicDao;
	}

	/**
	 * Getter of member groupService.
	 * @return <code>GroupService</code> the attribute groupService
	 */
	public GroupService getGroupService() {
		return groupService;
	}

	/**
	 * Setter of attribute groupService.
	 * @param groupService the attribute groupService to set
	 */
	public void setGroupService(final GroupService groupService) {
		this.groupService = groupService;
	}

	/**
	 * Getter of member userDao.
	 * @return <code>EscoUserDao</code> the attribute userDao
	 */
	public EscoUserDao getUserDao() {
		return userDao;
	}

	/**
	 * Setter of attribute userDao.
	 * @param userDao the attribute userDao to set
	 */
	public void setUserDao(final EscoUserDao userDao) {
		this.userDao = userDao;
	}

	/**
	 * @throws Exception
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(this.categoryDao, "The property categorieDao in class "
				+ getClass().getSimpleName() + " must not be null.");
		Assert.notNull(this.topicDao, "The property topicDao in class "
				+ getClass().getSimpleName() + " must not be null.");
		Assert.notNull(this.groupService, "The property groupService in class "
				+ this.getClass().getSimpleName() + " must not be null.");
		Assert.notNull(this.userDao, "The property userDao in class"
				+ getClass().getSimpleName() + " must not be null.");
		Assert.notNull(this.roleDao, "The property roleDao in class"
				+ getClass().getSimpleName() + " must not be null.");
	}

}