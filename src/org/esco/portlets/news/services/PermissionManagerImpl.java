/**
* ESUP-Portail News - Copyright (c) 2009 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esco.portlets.news.services;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esco.portlets.news.dao.EntityRoleDAO;
import org.esco.portlets.news.domain.EntityRole;
import org.esco.portlets.news.utils.PortletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.uhp.portlets.news.dao.UserDao;
import org.uhp.portlets.news.domain.Item;
import org.uhp.portlets.news.domain.Role;
import org.uhp.portlets.news.domain.RolePerm;

/**
 * @author GIP RECIA - Julien Gribonvald
 * 3 mai 2012
 */
@Service("permissionManager")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class PermissionManagerImpl implements PermissionManager {

	/** LOGger.*/
    private static final Log LOG = LogFactory.getLog(UserManagerImpl.class);

	/** Native UserDao. */
	@Autowired private UserDao userDao;
	/** EntityRoleDao. */
	@Autowired private EntityRoleDAO entityRoleDao;
	/** Service access to portlet methods. */
    @Autowired private PortletService portletService;

	/**
	 * @see org.esco.portlets.news.services.PermissionManager#getRoleInCtx(java.lang.Long, java.lang.String)
	 */
	public String getRoleInCtx(final Long ctxId, final String ctxType) {
		final String userId = portletService.getRemoteUser();
		if (this.userDao.isUserSuperAdmin(userId)) {
            return RolePerm.ROLE_ADMIN.getName();
        }
		if (hasRoleInCtx(ctxId, ctxType, RolePerm.ROLE_MANAGER)){
			return RolePerm.ROLE_MANAGER.getName();
		} else if (hasRoleInCtx(ctxId, ctxType, RolePerm.ROLE_CONTRIBUTOR)){
			return RolePerm.ROLE_CONTRIBUTOR.getName();
		} else if (hasRoleInCtx(ctxId, ctxType, RolePerm.ROLE_EDITOR)){
			return RolePerm.ROLE_EDITOR.getName();
		} else if (hasRoleInCtx(ctxId, ctxType, RolePerm.ROLE_USER)){
			return RolePerm.ROLE_USER.getName();
		} else return null;
	}

	/**
	 * Look if a user has a role type in the context.
	 * @param ctxId
	 * @param ctxType
	 * @param role
	 * @return true if the role is associated to the user else false.
	 */
	private boolean hasRoleInCtx(final Long ctxId, final String ctxType, final RolePerm role){
		List<EntityRole> listRoles = this.entityRoleDao.getAllEntityRoleOfRoleInCtx(ctxId, ctxType, role.getName());
		Iterator<EntityRole> it = listRoles.iterator();
		boolean roleFound = false;
		while (!roleFound && it.hasNext()) {
			EntityRole ur = it.next();
			if ((Integer.parseInt(ur.getIsGroup()) == 0 ) && ur.getPrincipal().equals(portletService.getRemoteUser())) {
				roleFound = true;
			} else if ((Integer.parseInt(ur.getIsGroup()) == 1) && portletService.isUserInGroup(ur.getPrincipal())){
				roleFound = true;
			}
		}
		return roleFound;
	}

	/**
	 * Tell if the user has the role in topics where the item is attached.
	 * @param itemId
	 * @param role
	 * @return <code>boolean</code>
	 * @throws DataAccessException
	 */
	private boolean isUserHasRoleInTopicsByItem(final Long itemId, final RolePerm role) {
		boolean hasRole = false;
		List<EntityRole> listRole = this.entityRoleDao.getAllEntityRoleInTopicsByItem(itemId, role.getName());
		Iterator<EntityRole> it = listRole.iterator();
		while (!hasRole && it.hasNext()) {
			EntityRole ur = it.next();
			if ((Integer.parseInt(ur.getIsGroup()) == 0 ) && ur.getPrincipal().equals(portletService.getRemoteUser())) {
				hasRole = true;
			} else if ((Integer.parseInt(ur.getIsGroup()) == 1) && portletService.isUserInGroup(ur.getPrincipal())){
				hasRole = true;
			}
		}
		return hasRole;
	}

	/**
	 * @see org.esco.portlets.news.services.PermissionManager#isAdminInCtx(java.lang.Long, java.lang.String)
	 */
	public boolean isAdminInCtx(final Long ctxId, final String ctxType){
		return this.hasRoleInCtx(ctxId, ctxType, RolePerm.ROLE_MANAGER);
	}

	/**
	 * @see org.esco.portlets.news.services.PermissionManager#isSuperAdmin()
	 */
	public boolean isSuperAdmin() {
		return this.userDao.isUserSuperAdmin(this.portletService.getRemoteUser());
	}

    /**
     * @see org.esco.portlets.news.services.PermissionManager#canValidateItem(org.uhp.portlets.news.domain.Item)
     */
    public boolean canValidateItem(final Item item) {
    	boolean canValidate = false;
    	final String userId = portletService.getRemoteUser();
        try {
            if (this.isSuperAdmin()) {
                return true;
            }
            canValidate = this.isUserHasRoleInTopicsByItem(item.getItemId(),RolePerm.ROLE_MANAGER);
            if (!canValidate){
            	canValidate = this.isUserHasRoleInTopicsByItem(item.getItemId(),RolePerm.ROLE_EDITOR) && item.getPostedBy().equals(userId);
            }
        } catch (DataAccessException e) {
            LOG.warn("canValidateItem::" + e.getLocalizedMessage());
        }
        return canValidate;
    }

    /**
     * @see org.esco.portlets.news.services.PermissionManager#canEditItem(org.uhp.portlets.news.domain.Item)
     */
    public boolean canEditItem(final Item item) {
    	boolean canEdit = false;
    	final String userId = portletService.getRemoteUser();
        try {
            if (this.isSuperAdmin()) {
                return true;
            }

            canEdit = this.isUserHasRoleInTopicsByItem(item.getItemId(),RolePerm.ROLE_MANAGER);
            if (!canEdit){
            	canEdit = (this.isUserHasRoleInTopicsByItem(item.getItemId(),RolePerm.ROLE_CONTRIBUTOR)
            				|| this.isUserHasRoleInTopicsByItem(item.getItemId(),RolePerm.ROLE_EDITOR))
            			&& item.getPostedBy().equals(userId);
            }
        } catch (DataAccessException e) {
            LOG.warn("canEditItem::" + e.getLocalizedMessage());
        }
        return canEdit;
    }

    /**
     * @see org.esco.portlets.news.services.PermissionManager#canEditItem(java.lang.String, org.uhp.portlets.news.domain.Item)
     */
    public boolean canEditItem(final String userId, final Item item) {
    	boolean canEdit = false;
        try {
            if (this.isSuperAdmin()) {
                return true;
            }

            canEdit = this.isUserHasRoleInTopicsByItem(item.getItemId(),RolePerm.ROLE_MANAGER);
            if (!canEdit){
            	canEdit = (this.isUserHasRoleInTopicsByItem(item.getItemId(),RolePerm.ROLE_CONTRIBUTOR)
            				|| this.isUserHasRoleInTopicsByItem(item.getItemId(),RolePerm.ROLE_EDITOR))
            			&& item.getPostedBy().equals(userId);
            }
        } catch (DataAccessException e) {
            LOG.warn("canEditItem::" + e.getLocalizedMessage());
        }
        return canEdit;
    }

	/**
	 * @see org.esco.portlets.news.services.PermissionManager#isUserAccountDisabled()
	 */
	public boolean isUserAccountDisabled() {
		return this.userDao.isUserAccountDisabled(portletService.getRemoteUser());
	}



    /**
     * @param userId
     * @return <code>boolean</code> true if the user is permitted to access to the application.
     * @see org.esco.portlets.news.services.UserManager#isPermitted(java.lang.String)
     */
    /*public boolean isPermitted() {
        return this.userDao.isPermitted(userId);
    }*/

}
