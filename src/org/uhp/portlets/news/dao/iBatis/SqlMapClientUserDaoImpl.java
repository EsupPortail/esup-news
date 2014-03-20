/**
 * @Project NewsPortlet : http://sourcesup.cru.fr/newsportlet/
 * Copyright (C) 2007-2008 University Nancy 1
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation version 2 of the License.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */
package org.uhp.portlets.news.dao.iBatis;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esco.portlets.news.domain.EntityRole;

import org.springframework.dao.DataAccessException;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.stereotype.Repository;
import org.uhp.portlets.news.NewsConstants;
import org.uhp.portlets.news.dao.Constants;
import org.uhp.portlets.news.dao.UserDao;
import org.uhp.portlets.news.domain.Role;
import org.uhp.portlets.news.domain.RoleEnum;
import org.uhp.portlets.news.domain.Topic;
import org.uhp.portlets.news.domain.User;
import org.uhp.portlets.news.service.exception.UserNotFoundException;

/**
 * @author GIP RECIA - Julien Gribonvald
 * 3 mai 2012
 */
@Repository("sqlUserDao")
public class SqlMapClientUserDaoImpl extends SqlMapClientDaoSupport implements UserDao {

	/** Logger. */
	private  static final  Log LOGGER = LogFactory.getLog(SqlMapClientUserDaoImpl.class);

	/*
	 * *************************************
	 * CHECKED
	 **************************************/
	/**
	 * @see org.uhp.portlets.news.dao.UserDao#insert(org.uhp.portlets.news.domain.User)
	 */
	public void insert(final User user) throws DataAccessException {
		getSqlMapClientTemplate().insert("insertUser", user);
	}
	/**
	 * @see org.uhp.portlets.news.dao.UserDao#update(org.uhp.portlets.news.domain.User)
	 */
	public void update(final User user) throws DataAccessException {
		getSqlMapClientTemplate().update("updateUser", user);
	}
	/**
	 * @see org.uhp.portlets.news.dao.UserDao#updateLastUserAccess(java.lang.String)
	 */
	public void updateLastUserAccess(final String userId)  throws DataAccessException {
		Map<String, Object> params = new HashMap<String, Object>(2);
		Calendar c= Calendar.getInstance();
		params.put("lastAccess", c.getTime());
		params.put(Constants.USER_ID, userId);
		getSqlMapClientTemplate().update("updateUserLastAccess", params);
	}
	/**
	 * @see org.uhp.portlets.news.dao.UserDao#updateUserStatus(java.lang.String, java.lang.String)
	 */
	public void updateUserStatus(String userId, String enabled) throws DataAccessException {
		Map<String, Object> params = new HashMap<String, Object>(2);
		params.put(Constants.ENABLED, enabled);
		params.put(Constants.USER_ID,userId);
		getSqlMapClientTemplate().update("updateUserStatus", params);

	}
	/**
	 * @see org.uhp.portlets.news.dao.UserDao#deleteUser(java.lang.String)
	 */
	public void deleteUser(final String userId) throws DataAccessException {
		deleteUser(userId, true);
	}
	/**
	 * Delete all roles of a user and delete him after.
	 * @param userId
	 * @param rar
	 * @throws DataAccessException
	 */
	private void deleteUser(final String userId, boolean rar) throws DataAccessException {
		if(rar) {
			removeAllRolesForUser(userId);
		}
		getSqlMapClientTemplate().delete("deleteUser", userId) ;
		if(LOGGER.isDebugEnabled()) {
			LOGGER.debug("user "+ userId + " is deleted");
		}
	}
	/**
	 * Delete all roles of a user.
	 * @param userId
	 * @throws DataAccessException
	 */
	private void removeAllRolesForUser(final String userId) throws DataAccessException {
		getSqlMapClientTemplate().delete("removeAllRolesForUser", userId);
	}
	/**
	 * @see org.uhp.portlets.news.dao.UserDao#getUserById(java.lang.String)
	 */
	public User getUserById(final String userId) throws UserNotFoundException, DataAccessException {
		return (User) getSqlMapClientTemplate().queryForObject("getUserById", userId);
	}
	/**
	 * @see org.uhp.portlets.news.dao.UserDao#isUserSuperAdmin(java.lang.String)
	 */
	public boolean isUserSuperAdmin(final String userId) throws DataAccessException {
		String r = (String) getSqlMapClientTemplate().queryForObject("getUserSuperAdmin", userId);
		return ((r != null) && (Integer.parseInt(r) > 0));
	}
	/**
	 * @see org.uhp.portlets.news.dao.UserDao#isUserAccountDisabled(java.lang.String)
	 */
	public boolean isUserAccountDisabled(final String userId) throws DataAccessException {
		User user = (User) getSqlMapClientTemplate().queryForObject("getUserById", userId);
		if (user == null  || user.getEnabled() == null) return false;
		return ((Integer.parseInt(user.getEnabled()) == 0));
	}
	/**
	 * @see org.uhp.portlets.news.dao.UserDao#getAllUsers()
	 */
	@SuppressWarnings("unchecked")
	public List<User> getAllUsers() throws DataAccessException {
		return getSqlMapClientTemplate().queryForList("getAllUsers");
	}
	/**
	 * @see org.uhp.portlets.news.dao.UserDao#getAllSuperUsers()
	 */
	@SuppressWarnings("unchecked")
	public List<User> getAllSuperUsers() throws DataAccessException {
		return getSqlMapClientTemplate().queryForList("getAllSuperUsers");
	}

	/**
	 * @see org.uhp.portlets.news.dao.UserDao#isUserHasAnyRole(java.lang.String)
	 */
	public boolean isUserHasAnyRole(String userId) throws DataAccessException {
		String r = (String) getSqlMapClientTemplate().queryForObject("userHasAnyRole", userId);
		return ((r != null) && (Integer.parseInt(r) > 0));
	}


	/**
	 * @see org.uhp.portlets.news.dao.UserDao#getEntityRolesForCtx(java.lang.Long, java.lang.String)
	 * Not Used
	 */
	/*@SuppressWarnings("unchecked")
	public List<EntityRole> getEntityRolesForCtx(Long ctxId, String ctxType)
			throws DataAccessException {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(Constants.CTX_ID, ctxId);
		params.put(Constants.CTX_TYPE, ctxType);
		return getSqlMapClientTemplate().queryForList("getEntityRolesForCtx", params);
	}*/



	/*
	 * *******************************************
	 *  NOT CHECKED
	 *********************************************/

	/* NOT USED ?
	 * public void activateUser(final int enabled, final String userId) throws DataAccessException {
		Map<String, Object> params = new HashMap<String, Object>(2);
		params.put(Constants.ENABLED, Integer.valueOf(enabled));
		params.put(Constants.USER_ID, userId);
		getSqlMapClientTemplate().update("updateEnabled", params);
	}*/

	/*public boolean isPermitted(String userId) throws DataAccessException {
		String r = (String) getSqlMapClientTemplate().queryForObject("isPermitted", userId);
		return ((r != null) && (Integer.parseInt(r) > 0));
	}*/
	/*public String getUserNameById(final String userId) throws UserNotFoundException, DataAccessException {
		return (String) getSqlMapClientTemplate().queryForObject("getUserNameById", userId);
	}*/

    /*public List<User> getUserByListUid(List<String> usersUid) {
        return getSqlMapClientTemplate().queryForList("getUserByListUid", usersUid);
    }*/

	@SuppressWarnings("unchecked")
	@Deprecated
	public List<User> getManagersForCategory(Long categoryId) throws DataAccessException {
		return getSqlMapClientTemplate().queryForList("getManagersForCategory", categoryId);
	}

//	@SuppressWarnings("unchecked")
//	public List<User> getManagersForTopic(Long topicId) throws DataAccessException {
//		return getSqlMapClientTemplate().queryForList("getManagersForTopic",topicId);
//	}

//	@SuppressWarnings("unchecked")
//	public List<User> getManagersForTopics(Integer[] topicIds) throws DataAccessException {
//		Map<String, Object> params = new HashMap<String, Object>(4);
//		params.put(NewsConstants.T_IDS, topicIds);
//		params.put(Constants.ROLE, "ROLE_MANAGER");
//		params.put(Constants.CTX_TYPE, NewsConstants.CTX_T);
//		params.put(Constants.ENABLED, "1");
//		return getSqlMapClientTemplate().queryForList("getManagersForTopics",params);
//	}

	@Deprecated
	@SuppressWarnings("unchecked")
	public List<User> getManagersForTopics(Long cId, Integer[] topicIds) throws DataAccessException {
		Map<String, Object> params = new HashMap<String, Object>(5);
		params.put("cId", cId);
		params.put(NewsConstants.T_IDS, topicIds);
		params.put(Constants.ROLE, "ROLE_MANAGER");
		params.put(Constants.CTX_TYPE, NewsConstants.CTX_T);
		params.put(Constants.ENABLED, "1");
		return getSqlMapClientTemplate().queryForList("getManagersForTopics",params);
	}

	public boolean isTopicManager(Topic topic, User user) throws DataAccessException {
		Map<String, Object> params = new HashMap<String, Object>(2);
		params.put(NewsConstants.T_ID, topic.getTopicId());
		params.put(NewsConstants.UID, user.getUserId());
		Integer r = (Integer) getSqlMapClientTemplate().queryForObject("isTopicManager", params);
		return ((r != null) && (r.intValue() > 0));
	}

}
