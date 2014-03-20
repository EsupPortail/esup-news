/**
* ESUP-Portail News - Copyright (c) 2009 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esco.portlets.news.services;

import java.util.List;

import org.uhp.portlets.news.domain.Item;
import org.uhp.portlets.news.domain.Role;

/**
 * Get or check permission of connected user.
 * @author jgribonvald
 * 3 mai 2012
 */
public interface PermissionManager {

    /**
     * Obtain the role of the connected user in the current context
     * @param ctxId
     * @param ctxType
     * @return <code>String</code>
     */
    String getRoleInCtx(final Long ctxId, final String ctxType);

    /**
     * Return if the connected user is a superAdmin.
     * @return <code>boolean</code>
     */
    boolean isSuperAdmin();

    /**
     * Return if the connected user is an Admin in the specified context.
     * @param ctxId
     * @param ctxType
     * @return <code>boolean</code>
     */
    boolean isAdminInCtx(final Long ctxId, final String ctxType);

    /**
     * Return true if the account of the connected user is disabled.
     * @return <code>boolean</code>
     */
    boolean isUserAccountDisabled();

    /**
     * Return if the connected user can access.
     * @return <code>boolean</code>
     */
    //boolean isPermitted();
    //      boolean canViewOnly(String uid, Long itemId);
    /**
     * Return if the connected user can validate the Item
     * @param item
     * @return <code>boolean</code>
     */
    boolean canValidateItem(final Item item);
    /**
     * Return if the connected user can edit the item
     * @param item
     * @return <code>boolean</code>
     */
    boolean canEditItem(final Item item);

    /**
     * Return if the user obtained from the userId can edit the item. Used for servlet context !
     * @param userId
     * @param item
     * @return <code>boolean</code>
     */
    boolean canEditItem(final String userId, final Item item);

}
