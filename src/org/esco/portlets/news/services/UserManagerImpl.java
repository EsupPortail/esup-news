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
import org.uhp.portlets.news.domain.Role;
import org.uhp.portlets.news.domain.RoleEnum;
import org.uhp.portlets.news.domain.RolePerm;
import org.uhp.portlets.news.domain.Topic;
import org.uhp.portlets.news.domain.User;
import org.uhp.portlets.news.domain.UserRole;

/**
 * @author by GIP RECIA - Gribonvald Julien
 * 7 juil. 09
 */
@Service("escoUserManager")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true) 
public class UserManagerImpl implements UserManager, InitializingBean {

    /** */
    private static final String UPDATE_OF_THE_USER_ROLE = "Update of the user role '";

    /** LOGger.*/
    private static final Log LOG = LogFactory.getLog(UserManagerImpl.class);

    /** Injection du service LDAP ESUP-commons. */
    private LdapUserService ldapUserService;
    /** */
    @Autowired private CategoryDao categoryDao;
    /** */
    @Autowired 
    private EntityDAO entityDao;
    /** */
    private EscoUserDao userDao;
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
     * @param user
     * @return <code>boolean</code> return true if the user is super admin.
     */
    protected boolean isSuperAdmin(final User user) {
        if (user == null) {
            return false;
        }
        return this.userDao.isSuperAdmin(user.getUserId());
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
     * @see org.esco.portlets.news.services.UserManager
     * #migrationUserCtxRole(org.uhp.portlets.news.domain.User, java.lang.Long)
     */
    @Transactional(propagation = Propagation.REQUIRED)    
    public void migrationUserCtxRole(final User user, final Long entityId) {
        if (!this.isSuperAdmin(user) 
                && !this.userDao.isUserRoleExistForContext(entityId, NewsConstants.CTX_E, user.getUserId())) {
         // we add the role of the user
            this.userDao.addUserRole(user, RolePerm.ROLE_USER.getName(), NewsConstants.CTX_E, entityId);
        }
    }


    /**
     * @param user
     * @param role
     * @param ctx
     * @param ctxId
     * @see org.esco.portlets.news.services.UserManager
     * #addUserCtxRole(org.uhp.portlets.news.domain.User, java.lang.String, java.lang.String, java.lang.Long)
     */
    @Transactional(propagation = Propagation.REQUIRED)    
    public void addUserCtxRole(final User user, final String role, final String ctx, final Long ctxId) {
        if (!this.isSuperAdmin(user)) {
            user.setIsSuperAdmin(NewsConstants.S_N);
            user.setEnabled(NewsConstants.S_Y);
            this.saveUser(user);            
            if (NewsConstants.CTX_T.equalsIgnoreCase(ctx)) {
                this.addUserRoleForTopic(user, role, ctxId, true);
                // we need to add at least the user_role for the user on 
                // category and entity if he doesn't have a role on it
                final Long cID = this.topicDao.getTopicById(ctxId).getCategoryId();
                final Long eID = this.categoryDao.getCategoryById(cID).getEntityId();
                this.addUserRoleForCategory(user, RolePerm.ROLE_USER.getName(), cID, false, false);
                this.addUserRoleForEntity(user, RolePerm.ROLE_USER.getName(), eID, false, false);
            } else if (NewsConstants.CTX_C.equals(ctx)) {
                this.addUserRoleForCategory(user, role, ctxId, true, true);
                // we need to add at least the user_role for the user on entity if he doesn't have a role on it
                final Long eID = this.categoryDao.getCategoryById(ctxId).getEntityId();
                this.addUserRoleForEntity(user, RolePerm.ROLE_USER.getName(), eID, false, false);
            } else if (NewsConstants.CTX_E.equals(ctx)) {
                this.addUserRoleForEntity(user, role, ctxId, true, true);
            } else {
                throw new IllegalArgumentException("The context " + ctx + " isn't unknown !");
            }            
        }
    }
    
    /**
     * Add/update user's role in an entity, his categories and his topics. 
     * @param user
     * @param newRole
     * @param ctxId
     * @param applyOnChild 
     * @param updToLowerRole 
     */
    @Transactional(propagation = Propagation.REQUIRED)    
    private void addUserRoleForEntity(final User user, final String newRole, final Long ctxId, 
            final boolean applyOnChild, final boolean updToLowerRole) {
        String oldRole = this.userDao.getUserRoleForCtx(user.getUserId(), ctxId, NewsConstants.CTX_E);
        if (oldRole == null || oldRole.isEmpty()) {
            // we add the role of the user
            if (LOG.isDebugEnabled()) {
                LOG.debug("Adding user role '" + newRole + "' in entity id[" + ctxId + "].");
            }
            this.userDao.addUserRole(user, newRole, NewsConstants.CTX_E, ctxId);
            // and we do the same thing on the childs
            if (applyOnChild) {
                List<Category> categories = categoryDao.getAllCategoryOfEntity(ctxId);
                for (Category c : categories) {
                    this.addUserRoleForCategory(user, newRole, c.getCategoryId(), true, false);
                }
            }
        } else if (RolePerm.valueOf(oldRole).getMask() < RolePerm.valueOf(newRole).getMask()) {
            // we update the role if the old role is lesser than the newer.
            if (LOG.isDebugEnabled()) {
                LOG.debug(UPDATE_OF_THE_USER_ROLE + oldRole + "' to a upper role '" 
                        + newRole + "' in entity id[" + ctxId + "].");
            }
            this.userDao.updateUserRoleForCtx(user.getUserId(), newRole, NewsConstants.CTX_E, ctxId);
            // and we do the same thing on the childs
            if (applyOnChild) {
                List<Category> categories = categoryDao.getAllCategoryOfEntity(ctxId);
                for (Category c : categories) {
                    this.addUserRoleForCategory(user, newRole, c.getCategoryId(), true, false);
                }
            }
        }  else if (RolePerm.valueOf(oldRole).getMask() > RolePerm.valueOf(newRole).getMask() && updToLowerRole) {
            // we update the role for the entity only to lower role.
            if (LOG.isDebugEnabled()) {
                LOG.debug(UPDATE_OF_THE_USER_ROLE + oldRole + "' to a lower role '" 
                        + newRole + "' in entity id[" + ctxId + "].");
            }
            this.userDao.updateUserRoleForCtx(user.getUserId(), newRole, NewsConstants.CTX_E, ctxId);
        }
    }
    
    /**
     * Add/update user's role in a category and his topics when it's already done for entity. 
     * @param user
     * @param newRole
     * @param ctxId
     * @param applyOnChild 
     * @param updToLowerRole 
     */
    @Transactional(propagation = Propagation.REQUIRED)    
    private void addUserRoleForCategory(final User user, final String newRole, final Long ctxId, 
            final boolean applyOnChild, final boolean updToLowerRole) {
        String oldRole = this.userDao.getUserRoleForCtx(user.getUserId(), ctxId, NewsConstants.CTX_C);
        if (oldRole == null || oldRole.isEmpty()) {
            // we add the role of the user
            if (LOG.isDebugEnabled()) {
                LOG.debug("Adding user role '" + newRole + "' in category id[" + ctxId + "].");
            }
            this.userDao.addUserRole(user, newRole, NewsConstants.CTX_C, ctxId);
            // and we do the same thing on the childs
            if (applyOnChild) {
                List<Topic> topics = topicDao.getTopicListByCategory(ctxId);
                for (Topic t : topics) {
                    this.addUserRoleForTopic(user, newRole, t.getTopicId(), false);
                }
            }
        } else if (RolePerm.valueOf(oldRole).getMask() < RolePerm.valueOf(newRole).getMask()) {
            // we update the role if the old role is lesser than the newer.
            if (LOG.isDebugEnabled()) {
                LOG.debug(UPDATE_OF_THE_USER_ROLE + oldRole + "' to a upper role '" 
                        + newRole + "' in category id[" + ctxId + "].");
            }
            this.userDao.updateUserRoleForCtx(user.getUserId(), newRole, NewsConstants.CTX_C, ctxId);
            // and we do the same thing on the childs
            if (applyOnChild) {
                List<Topic> topics = topicDao.getTopicListByCategory(ctxId);
                for (Topic t : topics) {
                    this.addUserRoleForTopic(user, newRole, t.getTopicId(), false);
                }
            }
        } else if (RolePerm.valueOf(oldRole).getMask() > RolePerm.valueOf(newRole).getMask() && updToLowerRole) {
            // we update the role to a lower role if the user role in the entity is lesser or equals than the new role.
            final Long eID = this.categoryDao.getCategoryById(ctxId).getEntityId();
            String entityRole = this.userDao.getUserRoleForCtx(user.getUserId(), eID, NewsConstants.CTX_E);
            if (RolePerm.valueOf(entityRole).getMask() <= RolePerm.valueOf(newRole).getMask()) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug(UPDATE_OF_THE_USER_ROLE + oldRole + "' to a lower role '" 
                            + newRole + "' in category id[" + ctxId + "].");
                }
                this.userDao.updateUserRoleForCtx(user.getUserId(), newRole, NewsConstants.CTX_C, ctxId);
            }
        }
        
    }
    
    /**
     * Add/update user's role in a topic when it's already done for category and entity. 
     * @param user
     * @param newRole
     * @param ctxId
     * @param updToLowerRole 
     */
    @Transactional(propagation = Propagation.REQUIRED)    
    private void addUserRoleForTopic(final User user, final String newRole, final Long ctxId, 
            final boolean updToLowerRole) {
        String oldRole = this.userDao.getUserRoleForCtx(user.getUserId(), ctxId, NewsConstants.CTX_T);
        if (oldRole == null || oldRole.isEmpty()) {
            // we add the role of the user
            if (LOG.isDebugEnabled()) {
                LOG.debug("Adding user role '" + newRole + "' in topic id[" + ctxId + "].");
            }
            this.userDao.addUserRole(user, newRole, NewsConstants.CTX_T, ctxId);
        } else if (RolePerm.valueOf(oldRole).getMask() < RolePerm.valueOf(newRole).getMask()) {
            // we update the role if the old role is lesser than the newer.
            if (LOG.isDebugEnabled()) {
                LOG.debug(UPDATE_OF_THE_USER_ROLE + oldRole + "' to a upper role '" 
                        + newRole + "' in topic id[" + ctxId + "].");
            }
            this.userDao.updateUserRoleForCtx(user.getUserId(), newRole, NewsConstants.CTX_T, ctxId);
        } else if (RolePerm.valueOf(oldRole).getMask() > RolePerm.valueOf(newRole).getMask() && updToLowerRole) {
            // we update the role to a lower role 
            // if the user role in the category is lesser or equals than the new role.
            final Long cID = this.topicDao.getTopicById(ctxId).getCategoryId();
            String catRole = this.userDao.getUserRoleForCtx(user.getUserId(), cID, NewsConstants.CTX_C);
            if (RolePerm.valueOf(catRole).getMask() <= RolePerm.valueOf(newRole).getMask()) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug(UPDATE_OF_THE_USER_ROLE + oldRole + "' to a lower role '" 
                            + newRole + "' in topci id[" + ctxId + "].");
                }
                this.userDao.updateUserRoleForCtx(user.getUserId(), newRole, NewsConstants.CTX_T, ctxId);
            }
        }
    }

    /**
     * @param uid
     * @param target
     * @param targetCtx
     * @see org.esco.portlets.news.services.UserManager
     * #removeUserRoleForCtx(java.lang.String, java.lang.Long, java.lang.String)
     */
    @Transactional(propagation = Propagation.REQUIRED)    
    public void removeUserRoleForCtx(final String uid, final Long target, final String targetCtx) {        
        if (NewsConstants.CTX_E.equals(targetCtx)) {
            this.removeUserRoleForEntity(uid, target);
        } else if (NewsConstants.CTX_C.equals(targetCtx)) {
            this.removeUserRoleForCategory(uid, target);
        } else if (NewsConstants.CTX_T.equals(targetCtx)) {
            this.removeUserRoleForTopic(uid, target);
        } else {
            throw new IllegalArgumentException("The context " + targetCtx + " isn't unknown !");
        }
        if (!this.userDao.isSuperAdmin(uid) && !this.hasAnyRole(uid)) {
            this.userDao.deleteUser(uid, false);
        }
    }
    
    /**
     * Remove roles from the Entity and on all categories and topics of the entity.
     * @param uid
     * @param target
     */
    @Transactional(propagation = Propagation.REQUIRED)    
    private void removeUserRoleForEntity(final String uid, final Long target) {
        // remove from childs
        List<Category> categories = categoryDao.getAllCategoryOfEntity(target);
        for (Category c : categories) {
            List<Topic> topics = topicDao.getTopicListByCategory(c.getCategoryId());
            for (Topic t : topics) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Remove user role in topic id[" + t.getTopicId() + "].");
                }
                this.userDao.removeUserRoleForCtx(uid, t.getTopicId(), NewsConstants.CTX_T);
            }
            if (LOG.isDebugEnabled()) {
                LOG.debug("Remove user role in category id[" + c.getCategoryId() + "].");
            }
            this.userDao.removeUserRoleForCtx(uid, c.getCategoryId(), NewsConstants.CTX_C);
        }
        // remove from entity
        if (LOG.isDebugEnabled()) {
            LOG.debug("Remove user role in entity id[" + target + "].");
        }
        this.userDao.removeUserRoleForCtx(uid, target, NewsConstants.CTX_E);
    }
    
    /**
     * Remove roles from the Category and on all topics of the category.
     * @param uid
     * @param target
     */
    @Transactional(propagation = Propagation.REQUIRED)    
    private void removeUserRoleForCategory(final String uid, final Long target) {
        Long eID = this.categoryDao.getCategoryById(target).getEntityId();
        String entityRole = this.userDao.getUserRoleForCtx(uid, eID, NewsConstants.CTX_E);
        // if it's a simple user_role in entity 
        if (RolePerm.ROLE_USER.getName().equalsIgnoreCase(entityRole)) {
            // remove from category
            if (LOG.isDebugEnabled()) {
                LOG.debug("Remove user role in category id[" + target + "].");
            }
            this.userDao.removeUserRoleForCtx(uid, target, NewsConstants.CTX_C);
            // and for childs
            List<Topic> topics = topicDao.getTopicListByCategory(target);
            for (Topic t : topics) {
                // remove from topics
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Remove user role in topic id[" + t.getTopicId() + "].");
                }
                this.userDao.removeUserRoleForCtx(uid, t.getTopicId(), NewsConstants.CTX_T);
            }
            if (!userDao.userRolesExistInCategoriesOfEntity(uid, eID)) {
                // remove from entity
                if (LOG.isDebugEnabled()) {
                    LOG.debug("The user has no roles in other categories " 
                            + "so we remove user role in entity id[" + eID + "].");
                }
                this.userDao.removeUserRoleForCtx(uid, eID, NewsConstants.CTX_E);
            }
        } else {
            // If it's a upper role defined in the entity, we must set the role as same as it's define in the entity
            if (LOG.isDebugEnabled()) {
                LOG.debug("Finaly no remove but update user role in category id[" + target + "] to the category role.");
            }
            IEscoUser user = this.userDao.getUserById(uid);
            this.addUserRoleForCategory(user, entityRole, target, false, true);
            // and for topics of category
            List<Topic> topics = topicDao.getTopicListByCategory(target);
            for (Topic t : topics) {
                // update from category
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Finaly no remove but update user role in topic id[" + t.getTopicId() 
                            + "] to the entity role.");
                }
                this.addUserRoleForTopic(user, entityRole, t.getTopicId(), true);
            }
        }
        
    }
    
    /**
     * Remove roles from the topic and check all other rights in the category/topics/entity.
     * @param uid
     * @param target
     */
    @Transactional(propagation = Propagation.REQUIRED)    
    private void removeUserRoleForTopic(final String uid, final Long target) {
        Long cID = this.topicDao.getTopicById(target).getCategoryId();
        Long eID = this.categoryDao.getCategoryById(cID).getEntityId();
        String catRole = this.userDao.getUserRoleForCtx(uid, cID, NewsConstants.CTX_C);
        // If it's a USER_ROLE in the category, so the entity too.
        if (RolePerm.ROLE_USER.getName().equalsIgnoreCase(catRole)) {
            // remove from topic
            if (LOG.isDebugEnabled()) {
                LOG.debug("Remove user role in topic id[" + target + "].");
            }
            this.userDao.removeUserRoleForCtx(uid, target, NewsConstants.CTX_T);
            // check if we can remove from category
            if (!this.userDao.userRolesExistInTopicsOfcategory(uid, cID)) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Remove user role in category id[" + cID + "].");
                }
                this.userDao.removeUserRoleForCtx(uid, cID, NewsConstants.CTX_C);
                // check if we can remove from entity
                if (!userDao.userRolesExistInCategoriesOfEntity(uid, eID)) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Remove user role in entity id[" + eID + "].");
                    }
                    this.userDao.removeUserRoleForCtx(uid, eID, NewsConstants.CTX_E);
                }
            }
        } else {
            // If it's a upper role defined in the category, we must set the role as same as it's define in the category
            if (LOG.isDebugEnabled()) {
                LOG.debug("finaly no remove but update user role in topic id[" + target + "] to the category role.");
            }
            this.addUserRoleForTopic(this.userDao.getUserById(uid), catRole, target, true);
        }
    }

    /**
     * @param user
     * @param ctx
     * @param ctxId
     */
    @Transactional(propagation = Propagation.REQUIRED)    
    public void removeUserRoleForCtx(final User user,  final String ctx, final Long ctxId) {
        this.removeUserRoleForCtx(user.getUserId(), ctxId, ctx);

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
     * @param target
     * @param targetCtx
     * @return <code>Map< String, List< UserRole >></code>
     * @see org.esco.portlets.news.services.UserManager#getUserRolesByCtxId(java.lang.Long, java.lang.String)
     */
    public Map<String, List<UserRole>> getUserRolesByCtxId(final Long target, final String targetCtx) {
        Map<String, List<UserRole>> userRoleLists = new HashMap<String, List<UserRole>>();
        for (RoleEnum r : RoleEnum.values()) {
            userRoleLists.put(r.toString(), this.userDao.getUsersByRole(target, targetCtx, r));
        }
        return userRoleLists;
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
     * @param ctxId
     * @param ctxType
     * @param uid
     * @return <code>String</code> return the role of the user in the context
     * @see org.esco.portlets.news.services.UserManager
     * #getUserRoleInCtx(java.lang.Long, java.lang.String, java.lang.String)
     */
    public String getUserRoleInCtx(final Long ctxId, final String ctxType, final String uid) {
        if (this.userDao.isSuperAdmin(uid)) {
            return RolePerm.ROLE_ADMIN.getName();
        }
        return this.userDao.getUserRoleForCtx(uid, ctxId, ctxType);
    }


    /**
     * @param ctxId
     * @param ctxType
     * @param uid
     * @return <code>boolean</code> true if role and the user exist in the context.
     * @see org.esco.portlets.news.services.UserManager
     * #isUserRoleExistForContext(java.lang.Long, java.lang.String, java.lang.String)
     */
    public boolean isUserRoleExistForContext(final Long ctxId, final String ctxType, final String uid) {
        if (uid != null) {
            if (this.userDao.isSuperAdmin(uid)) {
                return true;
            }
            return this.userDao.isUserRoleExistForContext(ctxId, ctxType, uid);
        } 
        LOG.warn("isUserRoleExistForContext:: uid can't be null");
        throw new IllegalArgumentException("You can't do this action when the user id is null!");

    }

    /**
     * @return <code>List< User ></code> The list of all users.
     * @see org.esco.portlets.news.services.UserManager#getAllUsers()
     */
    public List<IEscoUser> getAllUsers() {
        return this.userDao.getAllUsers();
    }

    /**
     * @return <code>List< Role ></code> The list of all roles.
     * @see org.esco.portlets.news.services.UserManager#getAllRoles()
     */
    public List<Role> getAllRoles() {
        return this.userDao.getAllRoles();
    }


    /**
     * @return <code>List< User ></code> The list of all super users.
     * @see org.esco.portlets.news.services.UserManager#getAllSuperUsers()
     */
    public List<IEscoUser> getAllSuperUsers() {      
        return this.userDao.getAllSuperUsers();
    }

    /**
     * It returns the list of users having a role in the context with there role associated.
     * @param target
     * @param targetCtx
     * @return <code>List< UserRole ></code> The list of userRole for the context.
     * @see org.esco.portlets.news.services.UserManager#getUsersRolesForCtx(java.lang.Long, java.lang.String)
     */
    public List<UserRole> getUsersRolesForCtx(final Long target, final String targetCtx) {
        return this.userDao.getUsersRolesForCtx(target, targetCtx);
    }

    /**
     * @param uid
     * @return <code>boolean</code> true if the user has a role.
     */
    private boolean hasAnyRole(final String uid) {        
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
        return RolePerm.ROLE_MANAGER.getName().equals(this.userDao.getUserRoleForCtx(uid, ctxId, ctxType));
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
                    this.userDao.getUserRoleForCtx(userId, e.getEntityId(), NewsConstants.CTX_E));
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
    private Map<String, String> getUserRoleMapsForCategoriesInEntity(final String userId, final Long entityId) {
        List<Category> categories = this.categoryDao.getCategoriesOfEntityByUser(userId, entityId);
        Map<String, String> catRoles = new HashMap<String, String>();
        for (Category c : categories) {
            String r = this.userDao.getUserRoleForCtx(userId, c.getCategoryId(), NewsConstants.CTX_C);
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
            String r = this.userDao.getUserRoleForCtx(userId, t.getTopicId(), NewsConstants.CTX_T);
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
            List<String> roles = this.userDao.getUserRolesInTopicsByItem(uid, item.getItemId());        
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

            List<String> roles = this.userDao.getUserRolesInTopicsByItem(uid, item.getItemId());        
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
                || this.userDao.getUserRoleForCtx(uid, cId, NewsConstants.CTX_C).equals(
                        RolePerm.ROLE_MANAGER.getName()) ) {
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
    }
}



