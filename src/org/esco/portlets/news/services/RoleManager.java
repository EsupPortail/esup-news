/**
 * ESUP-Portail News - Copyright (c) 2009 ESUP-Portail consortium
 * For any information please refer to http://esup-helpdesk.sourceforge.net
 * You may obtain a copy of the licence at http://www.esup-portail.org/license/
 */
package org.esco.portlets.news.services;

import java.util.List;
import java.util.Map;

import org.esco.portlets.news.domain.EntityRole;
import org.uhp.portlets.news.domain.Role;

/**
 * Manager of roles for groups or users.
 * @author GIP RECIA - Julien Gribonvald
 * 9 mai 2012
 */
public interface RoleManager {

	/**
	 * Get the complete list of Role with their I18N reference
	 * @return <code>List< Role ></code>
	 */
	List<Role> getAllRoles();

	/**
	 * Give the list of users or groups having a role in the context with there role associated.
	 * @param ctxId
	 * @param ctxType
	 * @return <code>List< EntityRole ></code>
	 */
	List<EntityRole> getEntityRolesInCtx(final Long ctxId, final String ctxType);

	/**
	 * Tell if an entity has a role in a context.
	 * @param principal
	 * @param isGroup
	 * @param ctxId
	 * @param ctxType
	 * @return <code>boolean</code>
	 */
	boolean isEntityHasRoleInCtx(final String principal, final boolean isGroup, final Long ctxId, final String ctxType);

	/**
	 * Get the role of an Entity in a context, null if not role is define.
	 * @param principal
	 * @param isGroup
	 * @param ctxId
	 * @param ctxType
	 * @return <code>String</code>
	 */
	String getRoleOfEntityInCtx(final String principal, final boolean isGroup, final Long ctxId,final String ctxType);

	/**
	 * Add a role to an entity in a ctx.
	 * @param principal
	 * @param isGroup
	 * @param role
	 * @param ctxId
	 * @param ctxType
	 */
	void addRoleForEntityInCtx(final String principal, final boolean isGroup, final String role, final Long ctxId, final String ctxType);

	/**
	 * Remove a role of an entity in a ctx.
	 * @param principal
	 * @param isGroup
	 * @param ctxId
	 * @param ctxType
	 */
	void removeRoleForEntityInCtx(final String principal, final boolean isGroup, final Long ctxId, final String ctxType);


	/**
	 * @param ctxId
	 * @param ctxType
	 * @param groupId
	 * @return <code>String</code>
	 */
	//	String getGroupRoleInCtx(Long ctxId, String ctxType, String groupId);

	/**
	 * Get all EntityRole in a context in a map where the key is a role.
	 * @param ctxId
	 * @param ctxType
	 * @return <code>Map< String, List< EntityRole >></code>
	 */
	Map<String, List<EntityRole>> getEntityRolesInCtxOrderedByRole(final Long ctxId, final String ctxType);

	/**
	 * Use this function with caution,
	 * it's usefull to make the migration of user/group role with the introduction of the entity context.
	 * @param principal
	 * @param isGroup
	 * @param entityId
	 */
	void migrateEntityRoleInCtxEntity(final String principal, final boolean isGroup, final Long entityId);

	/**
	 * Return a Map with entity name as key and the role of the user as value for the user.
	 * @param userId
	 * @return <code>Map<String, String></code>
	 */
	Map<String, String> loadUserEntityRoleMaps(final String userId);

	/**
     * Return a Map with key entity name and values are a Map of roles and associated categories for the user.
	 * @param userId
	 * @return <code>Map< String, Map< String, String >></code>
	 */
	Map<String, Map<String, String>> loadUserCategoryRoleMaps(final String userId);

	/**
	 * Return a Map with key the category name and values are a Map of role and associated topics for the user.
	 * @param userId
	 * @return <code>Map< String, Map< String, String >></code>
	 */
    Map<String, Map<String, String>> loadUserTopicRoleMaps(final String userId);


	/**
	 * Tell if a user is a superAdmin.
	 * @param principal
	 * @return <code>boolean</code>
	 */
	// boolean isUserSuperAdmin(final String principal);

	/**
	 * Tell if a user is a manager in a context.
	 * @param userId
	 * @param ctxId
	 * @param ctxType
	 * @return <code>boolean</code>
	 */
	//  boolean isUserAdminInCtx(final String userId, final Long ctxId, final String ctxType);
}
