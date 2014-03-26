/**
 *
 */
package org.esco.portlets.news.services;

import java.util.List;
import java.util.Map;

import org.uhp.portlets.news.domain.Role;
import org.uhp.portlets.news.domain.RolePerm;
import org.uhp.portlets.news.domain.UserRole;

/**
 * @author GIP RECIA - Julien Gribonvald
 * 24 mars 2014
 */
public interface RoleManager {

	/**
	 * @param principal
	 * @param isGroup TODO
	 * @param role
	 * @param ctx
	 * @param ctxId
	 */
	void addSubjectRole(String principal, boolean isGroup, String role, String ctx, Long ctxId);

	/**
	 * @param principal
	 * @param isGroup
	 * @param target
	 * @param targetCtx
	 */
	void removeUserRoleForCtx(String principal, boolean isGroup, Long target, String targetCtx);

	/**
	 * @param target
	 * @param targetCtx
	 * @return <code>Map< String, List< UserRole >></code>
	 */
	Map<String, List<UserRole>> getUserRolesByCtxId(Long target, String targetCtx);

	/**
	 * @param ctxId
	 * @param ctxType
	 * @param principal
	 * @param isGroup
	 * @return <code>boolean</code>
	 */
	boolean isRoleExistForContext(Long ctxId, String ctxType, String principal, boolean isGroup);

	/**
	 * @return <code>List< Role ></code>
	 */
	List<Role> getAllRoles();

	/**
	 * Give the list of users having a role in the context with there role associated.
	 * @param target
	 * @param targetCtx
	 * @return <code>List< UserRole ></code>
	 */
	List<UserRole> getUsersRolesForCtx(Long target, String targetCtx);

	//  move to securityService
	/**
	 * @param ctxId
	 * @param ctxType
	 * @param principal
	 * @param isGroup
	 * @return <code>String</code>
	 */
	String getUserRoleInCtx(Long ctxId, String ctxType, String principal, boolean isGroup);

	/**
	 * @param principal
	 * @param isGroup
	 * @return <code>boolean</code>
	 */
	boolean isSuperAdmin(final String principal, final boolean isGroup);

	/**
	 * Tell if a subject is in the role (or upper role) in the specified context.
	 * @param ctxId
	 * @param ctxType
	 * @param role
	 * @param principal
	 * @param isGroup
	 * @return <code>boolean</code>
	 */
	boolean isInRoleinCtx(Long ctxId, String ctxType, RolePerm role, String principal, boolean isGroup);

}