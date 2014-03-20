/**
 * ESUP-Portail News - Copyright (c) 2009 ESUP-Portail consortium
 * For any information please refer to http://esup-helpdesk.sourceforge.net
 * You may obtain a copy of the licence at http://www.esup-portail.org/license/
 */
package org.esco.portlets.news.services;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esco.portlets.news.dao.EntityDAO;
import org.esco.portlets.news.dao.EntityRoleDAO;
import org.esco.portlets.news.domain.Entity;
import org.esco.portlets.news.domain.EntityRole;
import org.esco.portlets.news.services.group.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.uhp.portlets.news.NewsConstants;
import org.uhp.portlets.news.dao.CategoryDao;
import org.uhp.portlets.news.dao.TopicDao;
import org.uhp.portlets.news.dao.UserDao;
import org.uhp.portlets.news.domain.Category;
import org.uhp.portlets.news.domain.Role;
import org.uhp.portlets.news.domain.RoleEnum;
import org.uhp.portlets.news.domain.RolePerm;
import org.uhp.portlets.news.domain.Topic;
import org.uhp.portlets.news.domain.User;

/**
 * @author GIP RECIA - Julien Gribonvald
 * 9 mai 2012
 */
@Service("roleManager")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class RoleManagerImpl implements RoleManager {

	/** Logger.	 */
	private static final Log LOG = LogFactory.getLog(RoleManagerImpl.class);
	/** Constant. */
	private static final String UPDATE_OF_THE_USER_ROLE = "Update of the user role '";

	/** EntityRoleDao. */
	@Autowired private EntityRoleDAO entityRoleDao;
	/** Native UserDao. */
	@Autowired private UserDao userDao;
	/** */
	@Autowired private CategoryDao categoryDao;
	/** */
	@Autowired private EntityDAO entityDao;
	/** */
	@Autowired private TopicDao topicDao;
	/** Service access to group ws methods. */
	@Autowired private GroupService groupService;

	/**  */
	@Autowired private UserManager um;

	/**
	 * Contructor of the object RoleManagerImpl.java.
	 */
	public RoleManagerImpl() {
		super();
	}

	/**
	 * @see org.esco.portlets.news.services.RoleManager#getEntityRolesInCtx(java.lang.Long, java.lang.String)
	 */
	public List<EntityRole> getEntityRolesInCtx(final Long ctxId, final String ctxType) {
		return this.entityRoleDao.getAllEntityRoleInCtx(ctxId, ctxType);
	}

	/**
	 * @see org.esco.portlets.news.services.RoleManager#getAllRoles()
	 */
	public List<Role> getAllRoles() {
		return this.entityRoleDao.getAllRoles();
	}

	/**
	 * @see org.esco.portlets.news.services.RoleManager#isEntityHasRoleInCtx(java.lang.String, boolean, java.lang.Long, java.lang.String)
	 */
	public boolean isEntityHasRoleInCtx(final String principal, final boolean isGroup,final Long ctxId,String ctxType) {
		List<EntityRole> listRoles = this.entityRoleDao.getAllEntityRoleInCtx(ctxId, ctxType);
		Iterator<EntityRole> it = listRoles.iterator();
		boolean roleFound = false;
		while (!roleFound && it.hasNext()) {
			EntityRole ur = it.next();
			if (!isGroup){
				if ((Integer.parseInt(ur.getIsGroup()) == 0 ) && ur.getPrincipal().equals(principal)) {
					roleFound = true;
				} else if ((Integer.parseInt(ur.getIsGroup()) == 1) && groupService.isMemberOf(principal, ur.getPrincipal())){
					roleFound = true;
				}
			} else {
				if ((Integer.parseInt(ur.getIsGroup()) == 1) && principal.startsWith(ur.getPrincipal())){
					roleFound = true;
				}
			}
		}
		return roleFound;
	}


	/**
	 * @see org.esco.portlets.news.services.RoleManager#getRoleOfEntityInCtx(java.lang.String, boolean, java.lang.Long, java.lang.String)
	 */
	public String getRoleOfEntityInCtx(final String principal, final boolean isGroup, final Long ctxId,final String ctxType) {
		if (!isGroup) {
			if (this.userDao.isUserSuperAdmin(principal)) {
				return RolePerm.ROLE_ADMIN.getName();
			}
		}
		if (isEntityHasThisRoleInCtx(ctxId, ctxType, principal, isGroup, RolePerm.ROLE_MANAGER)){
			return RolePerm.ROLE_MANAGER.getName();
		} else if (isEntityHasThisRoleInCtx(ctxId, ctxType, principal, isGroup, RolePerm.ROLE_CONTRIBUTOR)){
			return RolePerm.ROLE_CONTRIBUTOR.getName();
		} else if (isEntityHasThisRoleInCtx(ctxId, ctxType, principal, isGroup, RolePerm.ROLE_EDITOR)){
			return RolePerm.ROLE_EDITOR.getName();
		} else if (isEntityHasThisRoleInCtx(ctxId, ctxType, principal, isGroup, RolePerm.ROLE_USER)){
			return RolePerm.ROLE_USER.getName();
		} else return null;
	}

	/**
	 * check if an entity has a role in a context
	 * @param ctxId
	 * @param ctxType
	 * @param principal
	 * @param isGroup
	 * @param role
	 * @return <code> boolean</code>
	 */
	private boolean isEntityHasThisRoleInCtx(final Long ctxId, final String ctxType, final String principal,final boolean isGroup, final RolePerm role){
		List<EntityRole> listRoles = this.entityRoleDao.getAllEntityRoleOfRoleInCtx(ctxId, ctxType, role.getName());
		Iterator<EntityRole> it = listRoles.iterator();
		boolean roleFound = false;
		while (!roleFound && it.hasNext()) {
			EntityRole ur = it.next();
			if (!isGroup){
				if ((Integer.parseInt(ur.getIsGroup()) == 0 ) && ur.getPrincipal().equals(principal)) {
					roleFound = true;
				} else if ((Integer.parseInt(ur.getIsGroup()) == 1) && groupService.isMemberOf(principal, ur.getPrincipal())){
					roleFound = true;
				}
			} else {
				if ((Integer.parseInt(ur.getIsGroup()) == 1) && principal.startsWith(ur.getPrincipal())){
					roleFound = true;
				}
			}
		}
		return roleFound;
	}

	/**
	 * @see org.esco.portlets.news.services.RoleManager#addRoleForEntityInCtx(java.lang.String, boolean, java.lang.String, java.lang.Long, java.lang.String)
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void addRoleForEntityInCtx(final String principal, final boolean isGroup, final String role, final Long ctxId, final String ctxType) {
		if (!principal.isEmpty() && (isGroup || !this.userDao.isUserSuperAdmin(principal))) {
			if (!isGroup){
				User user = userDao.getUserById(principal);
				user.setIsSuperAdmin(NewsConstants.S_N);
				user.setEnabled(NewsConstants.S_Y);
				this.um.saveUser(user);
			}
			if (NewsConstants.CTX_T.equalsIgnoreCase(ctxType)) {
				this.addEntityRoleInTopic(principal, isGroup, role, ctxId, true);
				// we need to add at least the user_role for the user on
				// category and entity if he doesn't have a role on it
				final Long cID = this.topicDao.getTopicById(ctxId).getCategoryId();
				final Long eID = this.categoryDao.getCategoryById(cID).getEntityId();
				this.addEntityRoleInCategory(principal, isGroup, RolePerm.ROLE_USER.getName(), cID, false, false);
				this.addEntityRoleInEntity(principal, isGroup, RolePerm.ROLE_USER.getName(), eID, false, false);
			} else if (NewsConstants.CTX_C.equals(ctxType)) {
				this.addEntityRoleInCategory(principal, isGroup, role, ctxId, true, true);
				// we need to add at least the user_role for the user on entity if he doesn't have a role on it
				final Long eID = this.categoryDao.getCategoryById(ctxId).getEntityId();
				this.addEntityRoleInEntity(principal, isGroup, RolePerm.ROLE_USER.getName(), eID, false, false);
			} else if (NewsConstants.CTX_E.equals(ctxType)) {
				this.addEntityRoleInEntity(principal, isGroup, role, ctxId, true, true);
			} else {
				throw new IllegalArgumentException("The context " + ctxType + " isn't unknown !");
			}
		}
	}

	/**
	 * Add/update user's role in an entity, his categories and his topics.
	 * @param principal
	 * @param isGroup
	 * @param newRole
	 * @param ctxId
	 * @param applyOnChild
	 * @param updToLowerRole
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	private void addEntityRoleInEntity(final String principal, final boolean isGroup, final String newRole, final Long ctxId,
			final boolean applyOnChild, final boolean updToLowerRole) {
		String oldRole = this.entityRoleDao.getEntityRoleInCtx(principal, isGroup, ctxId, NewsConstants.CTX_E);
		if (oldRole == null || oldRole.isEmpty()) {
			// we add the role of the user
			if (LOG.isDebugEnabled()) {
				LOG.debug("Adding user role '" + newRole + "' in entity id[" + ctxId + "].");
			}
			this.entityRoleDao.addEntityRole(principal, isGroup, newRole, ctxId, NewsConstants.CTX_E);
			// and we do the same thing on the childs
			if (applyOnChild) {
				List<Category> categories = categoryDao.getAllCategoryOfEntity(ctxId);
				for (Category c : categories) {
					this.addEntityRoleInCategory(principal, isGroup, newRole, c.getCategoryId(), true, false);
				}
			}
		} else if (RolePerm.valueOf(oldRole).getMask() < RolePerm.valueOf(newRole).getMask()) {
			// we update the role if the old role is lesser than the newer.
			if (LOG.isDebugEnabled()) {
				LOG.debug(UPDATE_OF_THE_USER_ROLE + oldRole + "' to a upper role '"
						+ newRole + "' in entity id[" + ctxId + "].");
			}
			this.entityRoleDao.updateEntityRoleInCtx(principal, isGroup, newRole, ctxId, NewsConstants.CTX_E);
			// and we do the same thing on the childs
			if (applyOnChild) {
				List<Category> categories = categoryDao.getAllCategoryOfEntity(ctxId);
				for (Category c : categories) {
					this.addEntityRoleInCategory(principal, isGroup, newRole, c.getCategoryId(), true, false);
				}
			}
		}  else if (RolePerm.valueOf(oldRole).getMask() > RolePerm.valueOf(newRole).getMask() && updToLowerRole) {
			// we update the role for the entity only to lower role.
			if (LOG.isDebugEnabled()) {
				LOG.debug(UPDATE_OF_THE_USER_ROLE + oldRole + "' to a lower role '"
						+ newRole + "' in entity id[" + ctxId + "].");
			}
			this.entityRoleDao.updateEntityRoleInCtx(principal, isGroup, newRole, ctxId, NewsConstants.CTX_E);
		}
	}

	/**
	 * Add/update user's role in a category and his topics when it's already done for entity.
	 * @param principal
	 * @param isGroup
	 * @param newRole
	 * @param ctxId
	 * @param applyOnChild
	 * @param updToLowerRole
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	private void addEntityRoleInCategory(final String principal, final boolean isGroup, final String newRole, final Long ctxId,
			final boolean applyOnChild, final boolean updToLowerRole) {
		String oldRole = this.entityRoleDao.getEntityRoleInCtx(principal, isGroup, ctxId, NewsConstants.CTX_C);
		if (oldRole == null || oldRole.isEmpty()) {
			// we add the role of the user
			if (LOG.isDebugEnabled()) {
				LOG.debug("Adding user role '" + newRole + "' in category id[" + ctxId + "].");
			}
			this.entityRoleDao.addEntityRole(principal, isGroup, newRole, ctxId, NewsConstants.CTX_C);
			// and we do the same thing on the childs
			if (applyOnChild) {
				List<Topic> topics = topicDao.getTopicListByCategory(ctxId);
				for (Topic t : topics) {
					this.addEntityRoleInTopic(principal, isGroup, newRole, t.getTopicId(), false);
				}
			}
		} else if (RolePerm.valueOf(oldRole).getMask() < RolePerm.valueOf(newRole).getMask()) {
			// we update the role if the old role is lesser than the newer.
			if (LOG.isDebugEnabled()) {
				LOG.debug(UPDATE_OF_THE_USER_ROLE + oldRole + "' to a upper role '"
						+ newRole + "' in category id[" + ctxId + "].");
			}
			this.entityRoleDao.updateEntityRoleInCtx(principal, isGroup, newRole, ctxId, NewsConstants.CTX_C);
			// and we do the same thing on the childs
			if (applyOnChild) {
				List<Topic> topics = topicDao.getTopicListByCategory(ctxId);
				for (Topic t : topics) {
					this.addEntityRoleInTopic(principal, isGroup, newRole, t.getTopicId(), false);
				}
			}
		} else if (RolePerm.valueOf(oldRole).getMask() > RolePerm.valueOf(newRole).getMask() && updToLowerRole) {
			// we update the role to a lower role if the user role in the entity is lesser or equals than the new role.
			final Long eID = this.categoryDao.getCategoryById(ctxId).getEntityId();
			String entityRole = this.entityRoleDao.getEntityRoleInCtx(principal, isGroup, eID, NewsConstants.CTX_E);
			if (RolePerm.valueOf(entityRole).getMask() <= RolePerm.valueOf(newRole).getMask()) {
				if (LOG.isDebugEnabled()) {
					LOG.debug(UPDATE_OF_THE_USER_ROLE + oldRole + "' to a lower role '"
							+ newRole + "' in category id[" + ctxId + "].");
				}
				this.entityRoleDao.updateEntityRoleInCtx(principal, isGroup, newRole, ctxId, NewsConstants.CTX_C);
			}
		}

	}

	/**
	 * Add/update user's role in a topic when it's already done for category and entity.
	 * @param principal
	 * @param isGroup
	 * @param newRole
	 * @param ctxId
	 * @param updToLowerRole
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	private void addEntityRoleInTopic(final String principal, final boolean isGroup, final String newRole, final Long ctxId,
			final boolean updToLowerRole) {
		String oldRole = this.entityRoleDao.getEntityRoleInCtx(principal, isGroup, ctxId, NewsConstants.CTX_T);
		if (oldRole == null || oldRole.isEmpty()) {
			// we add the role of the user
			if (LOG.isDebugEnabled()) {
				LOG.debug("Adding user role '" + newRole + "' in topic id[" + ctxId + "].");
			}
			this.entityRoleDao.addEntityRole(principal, isGroup, newRole, ctxId, NewsConstants.CTX_T);
		} else if (RolePerm.valueOf(oldRole).getMask() < RolePerm.valueOf(newRole).getMask()) {
			// we update the role if the old role is lesser than the newer.
			if (LOG.isDebugEnabled()) {
				LOG.debug(UPDATE_OF_THE_USER_ROLE + oldRole + "' to a upper role '"
						+ newRole + "' in topic id[" + ctxId + "].");
			}
			this.entityRoleDao.updateEntityRoleInCtx(principal, isGroup, newRole, ctxId, NewsConstants.CTX_T);
		} else if (RolePerm.valueOf(oldRole).getMask() > RolePerm.valueOf(newRole).getMask() && updToLowerRole) {
			// we update the role to a lower role
			// if the user role in the category is lesser or equals than the new role.
			final Long cID = this.topicDao.getTopicById(ctxId).getCategoryId();
			String catRole = this.entityRoleDao.getEntityRoleInCtx(principal, isGroup, cID, NewsConstants.CTX_C);
			if (RolePerm.valueOf(catRole).getMask() <= RolePerm.valueOf(newRole).getMask()) {
				if (LOG.isDebugEnabled()) {
					LOG.debug(UPDATE_OF_THE_USER_ROLE + oldRole + "' to a lower role '"
							+ newRole + "' in topci id[" + ctxId + "].");
				}
				this.entityRoleDao.updateEntityRoleInCtx(principal, isGroup, newRole, ctxId, NewsConstants.CTX_T);
			}
		}
	}

	/**
	 * @see org.esco.portlets.news.services.RoleManager#removeRoleForEntityInCtx(java.lang.String, boolean, java.lang.Long, java.lang.String)
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void removeRoleForEntityInCtx(final String principal, final boolean isGroup, final Long ctxId, final String ctxType) {
		if (NewsConstants.CTX_E.equals(ctxType)) {
			this.removeEntityRoleInEntity(principal, isGroup, ctxId);
		} else if (NewsConstants.CTX_C.equals(ctxType)) {
			this.removeEntityRoleInCategory(principal, isGroup, ctxId);
		} else if (NewsConstants.CTX_T.equals(ctxType)) {
			this.removeEntityRoleInTopic(principal, isGroup, ctxId);
		} else {
			throw new IllegalArgumentException("The context " + ctxType + " isn't unknown !");
		}
		if (!isGroup && !this.userDao.isUserSuperAdmin(principal) && !this.userDao.isUserHasAnyRole(principal)) {
			this.userDao.deleteUser(principal);
		}
	}

	/**
	 * Remove roles from the Entity and on all categories and topics of the entity.
	 * @param principal
	 * @param isGroup
	 * @param ctxId
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	private void removeEntityRoleInEntity(final String principal, final boolean isGroup, final Long ctxId) {
		// remove from childs
		List<Category> categories = categoryDao.getAllCategoryOfEntity(ctxId);
		for (Category c : categories) {
			List<Topic> topics = topicDao.getTopicListByCategory(c.getCategoryId());
			for (Topic t : topics) {
				if (LOG.isDebugEnabled()) {
					LOG.debug("Remove user role in topic id[" + t.getTopicId() + "].");
				}
				this.entityRoleDao.removeEntityRoleInCtx(principal, isGroup, t.getTopicId(), NewsConstants.CTX_T);
			}
			if (LOG.isDebugEnabled()) {
				LOG.debug("Remove user role in category id[" + c.getCategoryId() + "].");
			}
			this.entityRoleDao.removeEntityRoleInCtx(principal, isGroup, c.getCategoryId(), NewsConstants.CTX_C);
		}
		// remove from entity
		if (LOG.isDebugEnabled()) {
			LOG.debug("Remove user role in entity id[" + ctxId + "].");
		}
		this.entityRoleDao.removeEntityRoleInCtx(principal, isGroup, ctxId, NewsConstants.CTX_E);
	}

	/**
	 * Remove roles from the Category and on all topics of the category.
	 * @param principal
	 * @param isGroup
	 * @param ctxId
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	private void removeEntityRoleInCategory(final String principal, final boolean isGroup, final Long ctxId) {
		Long eID = this.categoryDao.getCategoryById(ctxId).getEntityId();
		String entityRole = this.entityRoleDao.getEntityRoleInCtx(principal, isGroup, eID, NewsConstants.CTX_E);
		// if it's a simple user_role in entity
		if (RolePerm.ROLE_USER.getName().equalsIgnoreCase(entityRole)) {
			// remove from category
			if (LOG.isDebugEnabled()) {
				LOG.debug("Remove user role in category id[" + ctxId + "].");
			}
			this.entityRoleDao.removeEntityRoleInCtx(principal, isGroup, ctxId, NewsConstants.CTX_C);
			// and for childs
			List<Topic> topics = topicDao.getTopicListByCategory(ctxId);
			for (Topic t : topics) {
				// remove from topics
				if (LOG.isDebugEnabled()) {
					LOG.debug("Remove user role in topic id[" + t.getTopicId() + "].");
				}
				this.entityRoleDao.removeEntityRoleInCtx(principal, isGroup, t.getTopicId(), NewsConstants.CTX_T);
			}
			if (!entityRoleDao.entityRolesExistInCategoriesOfEntity(principal, isGroup, eID)) {
				// remove from entity
				if (LOG.isDebugEnabled()) {
					LOG.debug("The user has no roles in other categories "
							+ "so we remove user role in entity id[" + eID + "].");
				}
				this.entityRoleDao.removeEntityRoleInCtx(principal, isGroup, eID, NewsConstants.CTX_E);
			}
		} else {
			// If it's a upper role defined in the entity, we must set the role as same as it's define in the entity
			if (LOG.isDebugEnabled()) {
				LOG.debug("Finaly no remove but update user role in category id[" + ctxId + "] to the category role.");
			}
			this.addEntityRoleInCategory(principal, isGroup, entityRole, ctxId, false, true);
			// and for topics of category
			List<Topic> topics = topicDao.getTopicListByCategory(ctxId);
			for (Topic t : topics) {
				// update from category
				if (LOG.isDebugEnabled()) {
					LOG.debug("Finaly no remove but update user role in topic id[" + t.getTopicId()
							+ "] to the entity role.");
				}
				this.addEntityRoleInTopic(principal, isGroup, entityRole, t.getTopicId(), true);
			}
		}

	}

	/**
	 * Remove roles from the topic and check all other rights in the category/topics/entity.
	 * @param principal
	 * @param isGroup
	 * @param ctxId
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	private void removeEntityRoleInTopic(final String principal, final boolean isGroup, final Long ctxId) {
		Long cID = this.topicDao.getTopicById(ctxId).getCategoryId();
		Long eID = this.categoryDao.getCategoryById(cID).getEntityId();
		String catRole = this.entityRoleDao.getEntityRoleInCtx(principal, isGroup, cID, NewsConstants.CTX_C);
		// If it's a USER_ROLE in the category, so the entity too.
		if (RolePerm.ROLE_USER.getName().equalsIgnoreCase(catRole)) {
			// remove from topic
			if (LOG.isDebugEnabled()) {
				LOG.debug("Remove user role in topic id[" + ctxId + "].");
			}
			this.entityRoleDao.removeEntityRoleInCtx(principal, isGroup, ctxId, NewsConstants.CTX_T);
			// check if we can remove from category
			if (!this.entityRoleDao.entityRolesExistInTopicsOfCategory(principal, isGroup, cID)) {
				if (LOG.isDebugEnabled()) {
					LOG.debug("Remove user role in category id[" + cID + "].");
				}
				this.entityRoleDao.removeEntityRoleInCtx(principal, isGroup, cID, NewsConstants.CTX_C);
				// check if we can remove from entity
				if (!entityRoleDao.entityRolesExistInCategoriesOfEntity(principal, isGroup, eID)) {
					if (LOG.isDebugEnabled()) {
						LOG.debug("Remove user role in entity id[" + eID + "].");
					}
					this.entityRoleDao.removeEntityRoleInCtx(principal, isGroup, eID, NewsConstants.CTX_E);
				}
			}
		} else {
			// If it's a upper role defined in the category, we must set the role as same as it's define in the category
			if (LOG.isDebugEnabled()) {
				LOG.debug("finaly no remove but update user role in topic id[" + ctxId + "] to the category role.");
			}
			this.addEntityRoleInTopic(principal, isGroup, catRole, ctxId, true);
		}
	}

	/**
	 * @see org.esco.portlets.news.services.RoleManager#getEntityRolesInCtxOrderedByRole(java.lang.Long, java.lang.String)
	 */
	public Map<String, List<EntityRole>> getEntityRolesInCtxOrderedByRole(final Long ctxId, final String ctxType) {
		Map<String, List<EntityRole>> userRoleLists = new HashMap<String, List<EntityRole>>();
		for (RoleEnum role : RoleEnum.values()) {
			userRoleLists.put(role.name(), this.entityRoleDao.getAllEntityRoleOfRoleInCtx(ctxId, ctxType, role.name()));
		}
		return userRoleLists;
	}

// TODO revoir ça car ne marche pas avec la prise en compte des groupes !
    /**
     * @see org.esco.portlets.news.services.RoleManager#loadUserEntityRoleMaps(java.lang.String)
     */
    public Map<String, String> loadUserEntityRoleMaps(final String userId) {
        List<Entity> entities = this.entityDao.getEntitiesByUser(userId);
        Map<String, String> map = new HashMap<String, String>();
        for (Entity e : entities) {
            map.put(e.getName(),
                    this.getRoleOfEntityInCtx(userId, false, e.getEntityId(), NewsConstants.CTX_E));
        }
        return map;
    }

    /**
     * @see org.esco.portlets.news.services.RoleManager#loadUserCategoryRoleMaps(java.lang.String)
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
     * @see org.esco.portlets.news.services.RoleManager#loadUserTopicRoleMaps(java.lang.String)
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
    private Map<String, String> getUserRoleMapsForCategoriesInEntity(final String userId, final Long entityId) {
        List<Category> categories = this.categoryDao.getCategoriesOfEntityByUser(userId, entityId);
        Map<String, String> catRoles = new HashMap<String, String>();
        for (Category c : categories) {
            String r = this.getRoleOfEntityInCtx(userId, false, c.getCategoryId(), NewsConstants.CTX_C);
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
    private Map<String, String> getUserRoleMapsForTopicsInCategory(final String userId, final Long categoryId) {
        List<Topic> topics = this.topicDao.getTopicsForCategoryByUser(categoryId, userId);
        Map<String, String> topicRoles = new HashMap<String, String>();
        for (Topic t : topics) {
            String r = this.getRoleOfEntityInCtx(userId, false, t.getTopicId(), NewsConstants.CTX_T);
            if (r != null) {
                topicRoles.put(t.getName(), r);
            }
        }
        return topicRoles;
    }

    /**
	 * @see org.esco.portlets.news.services.RoleManager#isUserSuperAdmin(java.lang.String)
	 */
//	public boolean isUserSuperAdmin(final String userId) {
//		return this.userDao.isUserSuperAdmin(userId);
//	}

	/**
	 * @see org.esco.portlets.news.services.RoleManager#isUserAdminInCtx(java.lang.String, java.lang.Long, java.lang.String)
	 */
//	public boolean isUserAdminInCtx(final String userId, final Long ctxId, final String ctxType){
//		return this.isEntityHasThisRoleInCtx(ctxId, ctxType, userId, false, RolePerm.ROLE_MANAGER);
//	}

	/**
	 * @see org.esco.portlets.news.services.RoleManager#migrateEntityRoleInCtxEntity(java.lang.String, boolean, java.lang.Long)
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void migrateEntityRoleInCtxEntity(final String principal, final boolean isGroup, final Long entityId) {
		if (!userDao.isUserSuperAdmin(principal)
				&& !this.isEntityHasRoleInCtx(principal, isGroup, entityId, NewsConstants.CTX_E)) {
			// we add the role of the user
			this.entityRoleDao.addEntityRole(principal, isGroup, RolePerm.ROLE_USER.getName(), entityId, NewsConstants.CTX_E);
		}
	}
}
