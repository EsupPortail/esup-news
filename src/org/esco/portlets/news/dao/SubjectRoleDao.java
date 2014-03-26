/**
 *
 */
package org.esco.portlets.news.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.uhp.portlets.news.domain.Role;
import org.uhp.portlets.news.domain.RolePerm;
import org.uhp.portlets.news.domain.UserRole;

/**
 * @author GIP RECIA - Julien Gribonvald
 * 24 mars 2014
 */
public interface SubjectRoleDao {

	/**
	 * @param principal
	 * @param role
	 * @param ctx
	 * @param ctxId
	 * @param isGroup
	 * @param fromgroup
	 * @throws DataAccessException
	 */
	void addUserRole(String principal, String role, String ctx, Long ctxId,	boolean isGroup, String fromgroup) throws DataAccessException;

	/**
	 * @param target
	 * @param targetCtx
	 * @param role
	 * @return List<UserRole>
	 * @throws DataAccessException
	 */
	List<UserRole> getUsersByRole(Long target, String targetCtx, RolePerm role)	throws DataAccessException;

	/**
	 * @param principal
	 * @param target
	 * @param targetCtx
	 * @param isGroup
	 * @throws DataAccessException
	 */
	void removeUserRoleForCtx(String principal, Long target, String targetCtx, boolean isGroup) throws DataAccessException;

	/**
	 * @param ctxId
	 * @param ctxType
	 * @return List<UserRole>
	 * @throws DataAccessException
	 */
	List<UserRole> loadCtxUsersRolesMap(Long ctxId, String ctxType) throws DataAccessException;

	/**
	 * @param principal
	 * @param ctxId
	 * @param ctxType
	 * @param isGroup
	 * @return String
	 * @throws DataAccessException
	 */
	String getRoleForCtx(String principal, Long ctxId, String ctxType, boolean isGroup) throws DataAccessException;

	/**
	 * @param principal
	 * @param ctxId
	 * @param ctxType
	 * @param isGroup
	 * @return UserRole
	 * @throws DataAccessException
	 */
	UserRole getUserRoleForCtx(String principal, Long ctxId, String ctxType, boolean isGroup) throws DataAccessException;

	/**
	 * @param ctxId
	 * @param ctxType
	 * @param principal
	 * @param isGroup
	 * @return boolean
	 * @throws DataAccessException
	 */
	boolean isUserRoleExistForContext(Long ctxId, String ctxType, String principal, boolean isGroup) throws DataAccessException;

	/**
	 * @return List<Role>
	 * @throws DataAccessException
	 */
	List<Role> getAllRoles() throws DataAccessException;

	/**
	 * @param ctxId
	 * @param ctxType
	 * @throws DataAccessException
	 */
	void removeUsersRoleForCtx(Long ctxId, String ctxType) throws DataAccessException;

	/**
	 * @param ctxId
	 * @param ctxType
	 * @return List<UserRole>
	 * @throws DataAccessException
	 */
	List<UserRole> getUsersRolesForCtx(Long ctxId, String ctxType) throws DataAccessException;

	/**
	 * @param principal
	 * @param isGroup
	 * @param categoryId
	 * @return boolean
	 * @throws DataAccessException
	 */
	boolean userRolesExistInTopicsOfCategory(String principal, boolean isGroup, Long categoryId) throws DataAccessException;

	/**
	 * @param principal
	 * @param isGroup
	 * @param itemId
	 * @return List<String>
	 * @throws DataAccessException
	 */
	List<String> getUserRolesInTopicsByItem(String principal, boolean isGroup, Long itemId) throws DataAccessException;

	/**
	 * @param principal
	 * @param role
	 * @param ctx
	 * @param ctxId
	 * @param isgroup
	 * @param fromGroup
	 * @throws DataAccessException
	 */
	void updateUserRoleForCtx(String principal, String role, String ctx, Long ctxId, boolean isgroup, String fromGroup) throws DataAccessException;

	/**
	 * @param principal
	 * @param isGroup
	 * @param entityId
	 * @return boolean
	 * @throws DataAccessException
	 */
	boolean userRolesExistInCategoriesOfEntity(String principal, boolean isGroup, 	Long entityId) throws DataAccessException;
}