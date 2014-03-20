/**
 * ESUP-Portail News - Copyright (c) 2009 ESUP-Portail consortium
 * For any information please refer to http://esup-helpdesk.sourceforge.net
 * You may obtain a copy of the licence at http://www.esup-portail.org/license/
 */
package org.esco.portlets.news.dao;

import java.util.List;

import org.esco.portlets.news.domain.EntityRole;
import org.springframework.dao.DataAccessException;
import org.uhp.portlets.news.domain.Role;

/**
 * @author GIP RECIA - Julien Gribonvald
 * 9 mai 2012
 */
public interface EntityRoleDAO {

	/**
	 * Return the list of all available roles.
	 * @return <code>List<Role></code>
	 * @throws DataAccessException
	 */
	List<Role> getAllRoles() throws DataAccessException;

	/**
	 * Add a role associated to an entity (user, group, ...).
	 * @param entityRole
	 * @throws DataAccessException
	 */
	void addEntityRole(final EntityRole entityRole) throws DataAccessException;

	/**
	 * Add a role associated to an entity (user, group, ...).
	 * @param principal
	 * @param isGroup
	 * @param role
	 * @param ctxId
	 * @param ctxType
	 * @throws DataAccessException
	 */
	void addEntityRole(final String principal, final boolean isGroup, final String role, final Long ctxId, final String ctxType) throws DataAccessException;

	/**
	 * Update a role associated to an entity (user, group, ...).
	 * @param principal
	 * @param isGroup
	 * @param role
	 * @param ctxId
	 * @param ctxType
	 * @throws DataAccessException
	 */
	void updateEntityRoleInCtx(final String principal, final boolean isGroup, final String role, final Long ctxId, final String ctxType)  throws DataAccessException;

	/**
	 * Delete a role associated to an entity (user, group, ...).
	 * @param principal
	 * @param isGroup
	 * @param ctxId
	 * @param ctxType
	 * @throws DataAccessException
	 */
	void removeEntityRoleInCtx(final String principal, final boolean isGroup, final Long ctxId, String ctxType) throws DataAccessException;

	/**
	 * Delete all role defined in a context
	 * @param ctxId
	 * @param ctxType
	 * @throws DataAccessException
	 */
	void removeAllEntityRoleInCtx(final Long ctxId, final String ctxType) throws DataAccessException;

	/**
	 * Return the list of EntityRole of a given role in a context.
	 * @param ctxId
	 * @param ctxType
	 * @param role
	 * @return List<EntityRole> the list of EntityRole of a given role in a context.
	 * @throws DataAccessException
	 */
	List<EntityRole> getAllEntityRoleOfRoleInCtx(final Long ctxId, final String ctxType, String role) throws DataAccessException;

	/**
	 * Return all EntityRole (role associated to a user, a group, etc) in a context.
	 * @param ctxId
	 * @param ctxType
	 * @return <code>List<EntityRole></code>
	 * @throws DataAccessException
	 */
	List<EntityRole> getAllEntityRoleInCtx(final Long ctxId, final String ctxType) throws DataAccessException;

	/**
	 * Return the list of EntityRole with the given role in a topic from an item id.
	 * @param itemId
	 * @param role
	 * @return <code>List<EntityRole></code>
	 * @throws DataAccessException
	 */
	List<EntityRole> getAllEntityRoleInTopicsByItem(final Long itemId, final String role) throws DataAccessException;

	/**
	 * Get the role of an EntityRole in a context.
	 * @param principal
	 * @param isGroup
	 * @param ctxId
	 * @param ctxType
	 * @return <code>String</code>
	 * @throws DataAccessException
	 */
	String getEntityRoleInCtx(final String principal, final boolean isGroup, final Long ctxId, final String ctxType) throws DataAccessException;

	/**
	 * Tell if an entityRole as a role in categories of an entity.
	 * @param principal
	 * @param isGroup
	 * @param entityId
	 * @return <code>boolean</code>
	 * @throws DataAccessException
	 */
	boolean entityRolesExistInCategoriesOfEntity(final String principal, final boolean isGroup, final Long entityId) throws DataAccessException;

	/**
	 * Tell if an entityRole as a role in topics of a category.
	 * @param principal
	 * @param isGroup
	 * @param categoryId
	 * @return <code>boolean</code>
	 * @throws DataAccessException
	 */
	boolean entityRolesExistInTopicsOfCategory(final String principal, final boolean isGroup, final Long categoryId) throws DataAccessException;

	/**
	 * Give all roles from topic list
	 * @param tIds
	 * @return <code> List<EntityRole> </code>
	 * @throws DataAccessException
	 */
	//List<EntityRole> getEntityRolesInTopicsByTopics(Integer[] tIds) throws DataAccessException;

	/**
	 * Tell if a role is already define in a context.
	 * @param ctxId
	 * @param ctxType
	 * @param userid
	 * @return <code>boolean</code>
	 * @throws DataAccessException
	 */
	//boolean isEntityRoleExistInContext(final Long ctxId, final String ctxType, final String userid) throws DataAccessException;

	// NOT USED ? void addEntityRole(User user, String role, String ctx, Long ctxId) throws DataAccessException;
	// NOT USED ? void removeAllEntityRolesForTopics(String uid, Long cId) throws DataAccessException;



	//List<EntityRole> getEntityRolesByRole(Long target, String targetCtx, RoleEnum role) throws DataAccessException;

	//List<EntityRole> loadCtxUsersRolesMap(Long ctxId, String ctxType) throws DataAccessException;


	// CHECKED : List<EntityRole> getEntityRolesForCtx(Long ctxId, String ctxType) throws DataAccessException;



	//boolean entityRoleExist(String uid) throws DataAccessException;
}
