/**
 *
 */
package org.esco.portlets.news.dao.iBatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.esco.portlets.news.dao.EntityRoleDAO;
import org.esco.portlets.news.domain.EntityRole;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.stereotype.Repository;
import org.uhp.portlets.news.NewsConstants;
import org.uhp.portlets.news.dao.Constants;
import org.uhp.portlets.news.domain.Role;

/**
 * @author GIP RECIA - Julien Gribonvald
 * 9 mai 2012
 */
@Repository("entityRoleDao")
public class EntityRoleDAOImpl extends SqlMapClientDaoSupport implements EntityRoleDAO {

	/**
	 * Contructor of the object EntyRoleDAOImpl.java.
	 */
	public EntityRoleDAOImpl() {
		super();
	}

	/**
	 * @see org.esco.portlets.news.dao.EntityRoleDAO#getAllRoles()
	 */
	@SuppressWarnings("unchecked")
	public List<Role> getAllRoles() throws DataAccessException {
		return  getSqlMapClientTemplate().queryForList("getAllRoles");
	}

	/**
	 * @see org.esco.portlets.news.dao.EntityRoleDAO#addEntityRole(org.esco.portlets.news.domain.EntityRole)
	 */
	public void addEntityRole(final EntityRole entityRole) throws DataAccessException {
		getSqlMapClientTemplate().insert("insertEntityRole", entityRole);
	}

	/**
	 * @see org.esco.portlets.news.dao.EntityRoleDAO#addEntityRole(java.lang.String, boolean, java.lang.String, java.lang.Long, java.lang.String)
	 */
	public void addEntityRole(final String principal, final boolean isGroup, final String role, final Long ctxId, final String ctxType) throws DataAccessException {
		EntityRole ur = new EntityRole();
		ur.setPrincipal(principal);
		ur.setCtxId(ctxId);
		ur.setCtxType(ctxType);
		ur.setIsGroup(isGroup ? "1" : "0");
		ur.setRole(role);
		addEntityRole(ur);
	}

	/**
	 * @see org.esco.portlets.news.dao.EntityRoleDAO#updateEntityRoleInCtx(java.lang.String, boolean, java.lang.String, java.lang.Long, java.lang.String)
	 */
	public void updateEntityRoleInCtx(final String principal, final boolean isGroup, final String role, final Long ctxId, final String ctxType)  throws DataAccessException {
        Map<String, Object> params = new HashMap<String, Object>(5);
        params.put(Constants.CTX_ID, ctxId);
        params.put(Constants.CTX_TYPE, ctxType);
        params.put(Constants.ROLE, role);
        params.put(Constants.PRINCIPAL, principal);
        params.put(Constants.ISGRP, isGroup ? "1" : "0");
        getSqlMapClientTemplate().update("updateEntityRoleInCtx", params);
    }

	/**
	 * @see org.esco.portlets.news.dao.EntityRoleDAO#removeEntityRoleInCtx(java.lang.String, boolean, java.lang.Long, java.lang.String)
	 */
	public void removeEntityRoleInCtx(final String principal, final boolean isGroup, final Long ctxId, String ctxType) throws DataAccessException {
		Map<String, Object> params = new HashMap<String, Object>(4);
		params.put(Constants.CTX_ID, ctxId);
		params.put(Constants.CTX_TYPE, ctxType);
		params.put(Constants.PRINCIPAL, principal);
		params.put(Constants.ISGRP, isGroup ? "1" : "0");
		getSqlMapClientTemplate().delete("removeEntityRoleInCtx", params);
	}

	/**
	 * @see org.esco.portlets.news.dao.EntityRoleDAO#removeAllEntityRoleInCtx(java.lang.Long, java.lang.String)
	 */
	public void removeAllEntityRoleInCtx(final Long ctxId, final String ctxType) throws DataAccessException {
		Map<String, Object> params = new HashMap<String, Object>(2);
		params.put(Constants.CTX_ID, ctxId);
		params.put(Constants.CTX_TYPE, ctxType);
		getSqlMapClientTemplate().delete("removeAllEntityRoleInCtx", params);
	}

	/**
	 * @see org.esco.portlets.news.dao.EntityRoleDAO#getAllEntityRoleOfRoleInCtx(java.lang.Long, java.lang.String, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public List<EntityRole> getAllEntityRoleOfRoleInCtx(final Long ctxId, final String ctxType, final String role) throws DataAccessException {
		Map<String, Object> params = new HashMap<String, Object>(3);
		params.put(Constants.CTX_ID, ctxId);
		params.put(Constants.CTX_TYPE, ctxType);
		params.put(Constants.ROLE, role);
		return getSqlMapClientTemplate().queryForList("getAllEntityRoleOfRoleInCtx", params);
	}

	/**
	 * @see org.esco.portlets.news.dao.EntityRoleDAO#getAllEntityRoleInCtx(java.lang.Long, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public List<EntityRole> getAllEntityRoleInCtx(final Long ctxId, final String ctxType) throws DataAccessException {
		Map<String, Object> params = new HashMap<String, Object>(2);
		params.put(Constants.CTX_ID, ctxId);
		params.put(Constants.CTX_TYPE, ctxType);
		return getSqlMapClientTemplate().queryForList("getAllEntityRoleInCtx", params);
	}

	/**
	 * @see org.esco.portlets.news.dao.EntityRoleDAO#getAllEntityRoleInTopicsByItem(java.lang.Long, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public List<EntityRole> getAllEntityRoleInTopicsByItem(final Long itemId, final String role) throws DataAccessException {
		Map<String, Object> params = new HashMap<String, Object>(2);
		params.put(NewsConstants.I_ID, itemId);
		params.put(Constants.ROLE, role);
		return getSqlMapClientTemplate().queryForList("getAllEntityRoleInTopicsByItem", params);
	}

	/**
	 * @see org.esco.portlets.news.dao.EntityRoleDAO#getEntityRoleInCtx(java.lang.String, boolean, java.lang.Long, java.lang.String)
	 */
	public String getEntityRoleInCtx(final String principal, final boolean isGroup, final Long ctxId, final String ctxType) throws DataAccessException {
		Map<String, Object> params = new HashMap<String, Object>(3);
		params.put(Constants.PRINCIPAL, principal);
		params.put(Constants.ISGRP, isGroup ? "1" : "0");
		params.put(Constants.CTX_ID, ctxId);
		if (NewsConstants.CTX_E.equalsIgnoreCase(ctxType)) {
            return (String) getSqlMapClientTemplate().queryForObject("getEntityRoleInEntity", params);
        } else if (NewsConstants.CTX_C.equalsIgnoreCase(ctxType)) {
			return (String) getSqlMapClientTemplate().queryForObject("getEntityRoleInCategory", params);
		} else if (NewsConstants.CTX_T.equalsIgnoreCase(ctxType)) {
			return (String) getSqlMapClientTemplate().queryForObject("getEntityRoleInTopic", params);
		} else {
            throw new IllegalArgumentException("The context " + ctxType  + " isn't unknown !");
        }
	}

	/**
	 * @see org.esco.portlets.news.dao.EntityRoleDAO#entityRolesExistInCategoriesOfEntity(java.lang.String, boolean, java.lang.Long)
	 */
	public boolean entityRolesExistInCategoriesOfEntity(final String principal, final boolean isGroup, final Long entityId) throws DataAccessException {
        Map<String, Object> params = new HashMap<String, Object>(3);
        params.put(Constants.PRINCIPAL, principal);
        params.put(Constants.ISGRP, isGroup ? "1" : "0");
        params.put(NewsConstants.ENTITY_ID, entityId);
        String r = (String) getSqlMapClientTemplate().queryForObject("isEntityRoleExistInCategoriesOfEntity", params);
        return ((r != null) && (Integer.parseInt(r) > 0));
    }

	/**
	 * @see org.esco.portlets.news.dao.EntityRoleDAO#entityRolesExistInTopicsOfCategory(java.lang.String, boolean, java.lang.Long)
	 */
	public boolean entityRolesExistInTopicsOfCategory(final String principal, final boolean isGroup, final Long categoryId) throws DataAccessException {
		Map<String, Object> params = new HashMap<String, Object>(3);
		params.put(Constants.PRINCIPAL, principal);
        params.put(Constants.ISGRP, isGroup ? "1" : "0");
		params.put(NewsConstants.C_ID, categoryId);
		String r = (String) getSqlMapClientTemplate().queryForObject("isEntityRoleExistInTopicsOfCategory", params);
		return ((r != null) && (Integer.parseInt(r) > 0));

	}



	/* NOT USED ?
	 * public void addEntityRole(User user, String role, String ctx, Long ctxId) throws DataAccessException {
	EntityRole ur = new EntityRole();
	ur.setPrincipal(user.getUserId());
	ur.setCtxId(ctxId);
	ur.setCtxType(ctx);
	ur.setIsGroup("0");
	ur.setRole(role);
	addEntityRole(ur);
}*/





	/*@SuppressWarnings("unchecked")
	public List<EntityRole> getEntityRolesByRole(Long target, String targetCtx, RoleEnum role) throws DataAccessException {

		Map<String, Object> params = new HashMap<String, Object>(3);
		params.put(Constants.CTX_ID, target);
		params.put(Constants.CTX_TYPE, targetCtx);
		params.put("roleName", role.name());
		return  getSqlMapClientTemplate().queryForList("getAllEntityRolesByCtxIdAndRole", params);
	}



	@SuppressWarnings("unchecked")
	public List<EntityRole> loadCtxUsersRolesMap(Long ctxId, String ctxType) throws DataAccessException {
		Map<String, Object> params = new HashMap<String, Object>(3);
		params.put(Constants.CTX_ID, ctxId);
		params.put(Constants.CTX_TYPE, ctxType);
		params.put(Constants.ISGRP, "0");
		return getSqlMapClientTemplate().queryForList("loadCtxUsersRolesMap", params);
	}

	public boolean isEntityRoleExistForContext(Long ctxId, String ctxType, String userId) throws DataAccessException {
		String query;

		if (NewsConstants.CTX_E.equals(ctxType)) {
            query = "isEntityRoleExistForEntity";
        } else if (NewsConstants.CTX_C.equals(ctxType)) {
			query = "isEntityRoleExistForCategory";
		} else if (NewsConstants.CTX_T.equals(ctxType)) {
			query = "isEntityRoleExistForTopic";
		} else {
		    throw new IllegalArgumentException("The context " + ctxType  + " isn't unknown !");
		}
		Map<String, Object> params = new HashMap<String, Object>(2);
		params.put(Constants.CTX_ID, ctxId);
		params.put(Constants.PRINCIPAL, userId);
		String r = (String) getSqlMapClientTemplate().queryForObject(query, params);
		return ((r != null) && (Integer.parseInt(r) > 0));
	}





	public void removeAllEntityRolesForTopics(String userId, Long cId) throws DataAccessException {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cId", cId);
		params.put(Constants.PRINCIPAL, userId);
		getSqlMapClientTemplate().delete("removeAllEntityRolesForTopics", params);
	}

	@SuppressWarnings("unchecked")
	public List<EntityRole> getEntityRolesInTopicsByTopics(Integer[] tIds) throws DataAccessException {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(NewsConstants.T_IDS, tIds);
		params.put(Constants.CTX_TYPE, NewsConstants.CTX_T);
		return getSqlMapClientTemplate().queryForList("getEntityRolesInTopicsByTopics", params);
	}

	public boolean entityRoleExist(String userId) throws DataAccessException {
		String r = (String) getSqlMapClientTemplate().queryForObject("userHasAnyRole", userId);
		return ((r != null) && (Integer.parseInt(r) > 0));
	}*/



}
