/**
 *
 */
package org.esco.portlets.news.dao.iBatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.esco.portlets.news.dao.SubjectRoleDao;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.stereotype.Repository;
import org.uhp.portlets.news.NewsConstants;
import org.uhp.portlets.news.dao.Constants;
import org.uhp.portlets.news.domain.Role;
import org.uhp.portlets.news.domain.RolePerm;
import org.uhp.portlets.news.domain.UserRole;

/**
 * @author GIP RECIA - Julien Gribonvald
 * 24 mars 2014
 */
@Repository("roleDao")
public class SubjectRoleDAOImpl extends SqlMapClientDaoSupport implements SubjectRoleDao {

	/**
	 * Contructor of the object SubjectRoleDaoImpl.java.
	 */
	public SubjectRoleDAOImpl() {
		super();
	}

	/**
	 * @see org.esco.portlets.news.dao.SubjectRoleDao#addUserRole(String, String, String, Long, boolean, String)
	 */
	public void addUserRole(final String principal, final String role, final String ctx,
			final Long ctxId, final boolean isGroup, final String fromGroup) throws DataAccessException {
		UserRole ur = new UserRole(principal, role, isGroup ? "1" : "0", ctxId, ctx, fromGroup);
		getSqlMapClientTemplate().insert("insertUserRole", ur);
	}

	/**
	 * @see org.esco.portlets.news.dao.SubjectRoleDao#getUsersByRole(java.lang.Long, java.lang.String, org.uhp.portlets.news.domain.RolePerm)
	 */
	@SuppressWarnings("unchecked")
	public List<UserRole> getUsersByRole(final Long target, final String targetCtx, final RolePerm role) throws DataAccessException {
		Map<String, Object> params = new HashMap<String, Object>(3);
		params.put(Constants.CTX_ID, target);
		params.put(Constants.CTX_TYPE, targetCtx);
		params.put("roleName", role.name());
		return  getSqlMapClientTemplate().queryForList("getAllUserRolesByCtxIdAndRole", params);
	}

	/**
	 * Update the role of the user in one existing context.
	 * @param principal
	 * @param role
	 * @param ctx
	 * @param ctxId
	 * @param isGroup
	 * @param fromGroup
	 * @throws DataAccessException
	 */
	public void updateUserRoleForCtx(String principal, String role, String ctx, Long ctxId, boolean isGroup, String fromGroup) throws DataAccessException {
		Map<String, Object> params = new HashMap<String, Object>(5);
		params.put(Constants.CTX_ID, ctxId);
		params.put(Constants.CTX_TYPE, ctx);
		params.put("role", role);
		params.put(Constants.PRINCIPAL, principal);
		params.put(Constants.ISGRP, isGroup ? "1" : "0");
		params.put("fromGroup", fromGroup);
		//if (isUserRoleExistForContext(ctxId, ctx, uid)) {
		getSqlMapClientTemplate().update("updateUserRoleForCtx", params);
		//}
	}

	/**
	 * @see org.esco.portlets.news.dao.SubjectRoleDao#removeUserRoleForCtx(java.lang.String, java.lang.Long, java.lang.String, boolean)
	 */
	public void removeUserRoleForCtx(final String principal, final Long target, final String targetCtx, final boolean isGroup) throws DataAccessException {
		Map<String, Object> params = new HashMap<String, Object>(4);
		params.put(Constants.CTX_ID, target);
		params.put(Constants.CTX_TYPE, targetCtx);
		params.put(Constants.PRINCIPAL, principal);
		params.put(Constants.ISGRP, isGroup ? "1" : "0");
		getSqlMapClientTemplate().delete("removeUserRoleForCtx", params);
		if (isGroup) {
			params = new HashMap<String, Object>(3);
			params.put(Constants.CTX_ID, target);
			params.put(Constants.CTX_TYPE, targetCtx);
			params.put(Constants.PRINCIPAL, principal);
			getSqlMapClientTemplate().delete("removeUserRoleFromGroupForCtx", params);
		}
	}

	/**
	 * @see org.esco.portlets.news.dao.SubjectRoleDao#loadCtxUsersRolesMap(java.lang.Long, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public List<UserRole> loadCtxUsersRolesMap(final Long ctxId, final String ctxType) throws DataAccessException {
		Map<String, Object> params = new HashMap<String, Object>(3);
		params.put(Constants.CTX_ID, ctxId);
		params.put(Constants.CTX_TYPE, ctxType);
		params.put(Constants.ISGRP, "0");
		return getSqlMapClientTemplate().queryForList("loadCtxUsersRolesMap", params);
	}

	/**
	 * @see org.esco.portlets.news.dao.SubjectRoleDao#getRoleForCtx(java.lang.String, java.lang.Long, java.lang.String, boolean)
	 */
	public String getRoleForCtx(final String principal, final Long ctxId, final String ctxType, final boolean isGroup) throws DataAccessException {
		UserRole ur = getUserRoleForCtx(principal, ctxId, ctxType, isGroup);
		return (ur != null) ? ur.getRole() : null;
	}

	/**
	 * @see org.esco.portlets.news.dao.SubjectRoleDao#getUserRoleForCtx(String, Long, String, boolean)
	 */
	public UserRole getUserRoleForCtx(final String principal, final Long ctxId, final String ctxType, final boolean isGroup) throws DataAccessException {
		Map<String, Object> params = new HashMap<String, Object>(3);
		params.put(Constants.PRINCIPAL, principal);
		params.put(Constants.CTX_ID, ctxId);
		params.put(Constants.ISGRP, isGroup ? "1" : "0");
		if (NewsConstants.CTX_E.equalsIgnoreCase(ctxType)) {
			return (UserRole) getSqlMapClientTemplate().queryForObject("getUserRoleForEntity", params);
		} else if (NewsConstants.CTX_C.equalsIgnoreCase(ctxType)) {
			return (UserRole) getSqlMapClientTemplate().queryForObject("getUserRoleForCategory", params);
		} else if (NewsConstants.CTX_T.equalsIgnoreCase(ctxType)) {
			return (UserRole) getSqlMapClientTemplate().queryForObject("getUserRoleForTopic", params);
		} else {
			throw new IllegalArgumentException("The context " + ctxType  + " isn't unknown !");
		}
	}

	/**
	 * @see org.esco.portlets.news.dao.SubjectRoleDao#isUserRoleExistForContext(java.lang.Long, java.lang.String, java.lang.String, boolean)
	 */
	public boolean isUserRoleExistForContext(final Long ctxId, final String ctxType, final String principal, final boolean isGroup) throws DataAccessException {
		String query;

		if (NewsConstants.CTX_E.equals(ctxType)) {
			query = "isUserRoleExistForEntity";
		} else if (NewsConstants.CTX_C.equals(ctxType)) {
			query = "isUserRoleExistForCategory";
		} else if (NewsConstants.CTX_T.equals(ctxType)) {
			query = "isUserRoleExistForTopic";
		} else {
			throw new IllegalArgumentException("The context " + ctxType  + " isn't unknown !");
		}
		Map<String, Object> params = new HashMap<String, Object>(3);
		params.put(Constants.CTX_ID, ctxId);
		params.put(Constants.PRINCIPAL, principal);
		params.put(Constants.ISGRP, isGroup ? "1" : "0");
		String r = (String) getSqlMapClientTemplate().queryForObject(query, params);
		return ((r != null) && (Integer.parseInt(r) > 0));
	}

	/**
	 * @see org.esco.portlets.news.dao.SubjectRoleDao#getAllRoles()
	 */
	@SuppressWarnings("unchecked")
	public List<Role> getAllRoles() throws DataAccessException {
		return  getSqlMapClientTemplate().queryForList("getAllRoles");
	}

	/**
	 * @see org.esco.portlets.news.dao.SubjectRoleDao#removeUsersRoleForCtx(java.lang.Long, java.lang.String)
	 */
	public void removeUsersRoleForCtx(final Long ctxId, final String ctxType) throws DataAccessException {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(Constants.CTX_ID, ctxId);
		params.put(Constants.CTX_TYPE, ctxType);
		getSqlMapClientTemplate().delete("removeUsersRoleForCtx", params);
	}

	/**
	 * @see org.esco.portlets.news.dao.SubjectRoleDao#getUsersRolesForCtx(java.lang.Long, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public List<UserRole> getUsersRolesForCtx(final Long ctxId, final String ctxType) throws DataAccessException {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(Constants.CTX_ID, ctxId);
		params.put(Constants.CTX_TYPE, ctxType);
		return getSqlMapClientTemplate().queryForList("getUsersRolesForCtx", params);
	}

	/**
	 * @see org.esco.portlets.news.dao.SubjectRoleDao#userRolesExistInTopicsOfCategory(java.lang.String, boolean, java.lang.Long)
	 */
	public boolean userRolesExistInTopicsOfCategory(final String principal, final boolean isGroup, final Long categoryId) throws DataAccessException {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(Constants.PRINCIPAL, principal);
		params.put(NewsConstants.C_ID, categoryId);
		params.put(Constants.ISGRP, isGroup ? "1" : "0");
		String r = (String) getSqlMapClientTemplate().queryForObject("isUserRoleExistInTopics", params);
		return ((r != null) && (Integer.parseInt(r) > 0));

	}

	/**
	 * @see org.esco.portlets.news.dao.SubjectRoleDao#getUserRolesInTopicsByItem(java.lang.String, boolean, java.lang.Long)
	 */
	@SuppressWarnings("unchecked")
	public List<String> getUserRolesInTopicsByItem(final String principal, final boolean isGroup, final Long itemId) throws DataAccessException {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(NewsConstants.I_ID, itemId);
		params.put(Constants.PRINCIPAL, principal);
		params.put(Constants.ISGRP, isGroup ? "1" : "0");
		return getSqlMapClientTemplate().queryForList("getUserRolesInTopicsByItem", params);
	}

	/**
	 * @see org.esco.portlets.news.dao.SubjectRoleDao#userRolesExistInCategoriesOfEntity(java.lang.String, boolean, java.lang.Long)
	 */
	public boolean userRolesExistInCategoriesOfEntity(final String principal, final boolean isGroup, final Long entityId) throws DataAccessException {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(Constants.PRINCIPAL, principal);
		params.put(NewsConstants.ENTITY_ID, entityId);
		params.put(Constants.ISGRP, isGroup ? "1" : "0");
		String r = (String) getSqlMapClientTemplate().queryForObject("isUserRoleExistInCategories", params);
		return ((r != null) && (Integer.parseInt(r) > 0));
	}

}