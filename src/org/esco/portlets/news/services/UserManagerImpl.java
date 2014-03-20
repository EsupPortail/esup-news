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
import org.esco.portlets.news.dao.EntityRoleDAO;
import org.esco.portlets.news.dao.EscoUserDao;
import org.esco.portlets.news.domain.Entity;
import org.esco.portlets.news.domain.EntityRole;
import org.esco.portlets.news.domain.IEscoUser;
import org.esco.portlets.news.services.ldap.LdapUserService;
import org.esco.portlets.news.utils.PortletService;
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

/**
 * @author by GIP RECIA - Gribonvald Julien
 * 7 juil. 09
 */
@Service("escoUserManager")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class UserManagerImpl implements UserManager, InitializingBean {

    /** LOGger.*/
    private static final Log LOG = LogFactory.getLog(UserManagerImpl.class);

    /** Injection du service LDAP ESUP-commons. */
    private LdapUserService ldapUserService;
    /** */
    @Autowired private CategoryDao categoryDao;
    /** */
    private EscoUserDao userDao;
    /** */
    @Autowired private TopicDao topicDao;
    /** Service access to portlet methods. */
    @Autowired private PortletService portletService;

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
     * @return <code>boolean</code> return true if the user is super admin.
     * @see org.esco.portlets.news.services.UserManager#isSuperAdmin()
     */
    /*public boolean isSuperAdmin() {
        return this.userDao.isSuperAdmin();
    }*/

    /**
     * @param userId
     * @return <code>boolean</code> return true if the user account is enable.
     * @see org.esco.portlets.news.services.UserManager#isUserAccountEnabled(java.lang.String)
     */
    /*public boolean isUserAccountEnabled(final String userId) {
        if (userId == null) {
            return false;
        }
        return this.userDao.isUserAccountEnabled(userId);
    }*/

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
     * @see org.esco.portlets.news.services.UserManager#updateUserStatus(java.lang.String, java.lang.String)
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateUserStatus(final String userId, final String enabled)  {
        try {
            this.userDao.updateUserStatus(userId, enabled);
        } catch (DataAccessException e) {
            LOG.error("updateUserStatus error : " + e.getMessage());
            throw e;
        }
    }

    /**
     * @see org.esco.portlets.news.services.UserManager#updateLastUserAccess()
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateLastUserAccess()  {
        try {
            this.userDao.updateLastUserAccess(portletService.getRemoteUser());
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
     * @see org.esco.portlets.news.services.UserManager#deleteUser(java.lang.String)
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteUser(final String uid) {
        try {
            userDao.deleteUser(uid);
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
     * @return <code>String</code> return the role of the user in the context
     * @see org.esco.portlets.news.services.UserManager
     * #getUserRoleInCtx(java.lang.Long, java.lang.String)
     */
//    public String getRoleForUserInCtx(final Long ctxId, final String ctxType) {
//        if (this.userDao.isSuperAdmin()) {
//            return RolePerm.ROLE_ADMIN.getName();
//        }
//        return this.userDao.getRoleOfUserInCtx(ctxId, ctxType);
//    }


    /**
     * @param ctxId
     * @param ctxType
     * @param uid
     * @return <code>boolean</code> true if role and the user exist in the context.
     * @see org.esco.portlets.news.services.UserManager
     * #isUserRoleExistForContext(java.lang.Long, java.lang.String, java.lang.String)
     */
//    public boolean isUserRoleExistForContext(final Long ctxId, final String ctxType, final String uid) {
//        if (uid != null) {
//            if (this.userDao.isSuperAdmin(uid)) {
//                return true;
//            }
//            return this.userDao.isUserRoleExistForContext(ctxId, ctxType, uid);
//        }
//        LOG.warn("isUserRoleExistForContext:: uid can't be null");
//        throw new IllegalArgumentException("You can't do this action when the user id is null!");
//
//    }

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
     * It returns the list of users/groups having a role in the context with there role associated.
     * @param target
     * @param targetCtx
     * @return <code>List< UserRole ></code> The list of userRole for the context.
     * @see org.esco.portlets.news.services.UserManager#getUserRolesInCtx(java.lang.Long, java.lang.String)
     */
//    public List<EntityRole> getUserRolesInCtx(final Long target, final String targetCtx) {
//        return this.userDao.getUserRolesInCtx(target, targetCtx);
//    }

    /**
     * @param uid
     * @return <code>boolean</code> true if the user has a role.
     */
//    private boolean hasAnyRole(final String uid) {
//        if (this.userDao.userRoleExist(uid)) {
//            return true;
//        }
//        return false;
//    }

    /**
     * @param ctxId
     * @param ctxType
     * @param uid
     * @return <code>boolean</code> true if the user is admin for the context.
     * @see org.esco.portlets.news.services.UserManager
     * #isUserAdminInCtx(java.lang.Long, java.lang.String, java.lang.String)
     */
//    public boolean isUserAdminInCtx(final Long ctxId, final String ctxType, final String uid) {
//        if (this.userDao.isSuperAdmin(uid)) {
//            return true;
//        }
//        return RolePerm.ROLE_MANAGER.getName().equals(this.userDao.getUserRoleForCtx(uid, ctxId, ctxType));
//    }



    /**
     * @param cId
     * @return <code>boolean</code> true if the user is an admin of the categorie.
     */
//    private boolean isAdmin(final Long cId) {
//    	final String uid = portletService.getRemoteUser();
//        if (this.userDao.isSuperAdmin()
//                || this.userDao.getUserRoleForCtx(cId, NewsConstants.CTX_C).equals(
//                        RolePerm.ROLE_MANAGER.getName()) ) {
//            return true;
//        }
//        return false;
//    }


    /**
     * @param userId
     * @return obtain the user name from the uid
     * @see org.esco.portlets.news.services.UserManager#getUserNameById(java.lang.String)
     */
    public String getUserNameById(final String userId) {
        try {
			return this.userDao.getUserNameById(userId);
		} catch (org.esupportail.commons.exceptions.UserNotFoundException e) {
			return userId;
		}
    }

    /**
	 * @see org.esco.portlets.news.services.UserManager#getAttrDisplayName()
	 */
	public String getAttrDisplayName() {
		return userDao.getDisplayName();
	}

	/**
	 * @see org.esco.portlets.news.services.UserManager#getAttrMail()
	 */
	public String getAttrMail() {
		return userDao.getMail();
	}

	/**
     * @throws Exception
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(this.getLdapUserService(), "The property ldapUserService in class "
                + getClass().getSimpleName() + " must not be null.");
        Assert.notNull(this.portletService, "The property portletService in class "
                + getClass().getSimpleName() + " must not be null.");
        Assert.notNull(this.categoryDao, "The property categorieDao in class "
                + getClass().getSimpleName() + " must not be null.");
        Assert.notNull(this.topicDao, "The property topicDao in class "
                + getClass().getSimpleName() + " must not be null.");
        Assert.notNull(this.userDao, "The property userDao in class "
                + getClass().getSimpleName() + " must not be null.");
    }
}



