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
import org.springframework.dao.DataAccessException;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.stereotype.Repository;
import org.uhp.portlets.news.NewsConstants;
import org.uhp.portlets.news.dao.Constants;
import org.uhp.portlets.news.dao.UserDao;
import org.uhp.portlets.news.domain.Topic;
import org.uhp.portlets.news.domain.User;
import org.uhp.portlets.news.service.exception.UserNotFoundException;

@Repository("sqlUserDao")
public class SqlMapClientUserDaoImpl extends SqlMapClientDaoSupport implements UserDao {
	private  static final  Log LOGGER = LogFactory.getLog(SqlMapClientUserDaoImpl.class);

	public void insert(final User user) throws DataAccessException {
		getSqlMapClientTemplate().insert("insertUser", user);
	}

	public void update(final User user) throws DataAccessException {
		getSqlMapClientTemplate().update("updateUser", user);
	}

	public void updateUserLastAccess(final String uid)  throws DataAccessException {
		Map<String, Object> params = new HashMap<String, Object>(2);
		Calendar c= Calendar.getInstance();
		params.put("lastAccess", c.getTime());
		params.put(Constants.USER_ID, uid);
		getSqlMapClientTemplate().update("updateUserLastAccess", params);
	}

	public void activateUser(final int enabled, final String uid) throws DataAccessException {
		Map<String, Object> params = new HashMap<String, Object>(2);
		params.put(Constants.ENABLED, Integer.valueOf(enabled));
		params.put(Constants.USER_ID, uid);
		getSqlMapClientTemplate().update("updateEnabled", params);
	}

	public void deleteUser(final String uid, boolean rar) throws DataAccessException {
		if(rar) {
			removeAllRolesForUser(uid);
		}
		getSqlMapClientTemplate().delete("deleteUser", uid) ;
		if(LOGGER.isDebugEnabled()) {
			LOGGER.debug("user "+ uid + " is deleted");
		}
	}

	public void deleteUser(final User user) throws DataAccessException {
		deleteUser(user.getUserId(), true);

	}

	public User getUserById(final String uid) throws UserNotFoundException, DataAccessException {
		return (User) getSqlMapClientTemplate().queryForObject("getUserById", uid);
	}

	public boolean isSuperAdmin(final String uid) throws DataAccessException {
		String r = (String) getSqlMapClientTemplate().queryForObject("isUserSuperAdmin", uid);
		return ((r != null) && (Integer.parseInt(r) > 0));
	}

	@SuppressWarnings("unchecked")
	public List<User> getManagersForCategory(Long categoryId) throws DataAccessException {
		return getSqlMapClientTemplate().queryForList("getManagersForCategory", categoryId);
	}

	@SuppressWarnings("unchecked")
	public List<User> getManagersForTopic(Long topicId) throws DataAccessException {
		return getSqlMapClientTemplate().queryForList("getManagersForTopic",topicId);
	}

	@SuppressWarnings("unchecked")
	public List<User> getManagersForTopics(Integer[] topicIds) throws DataAccessException {
		Map<String, Object> params = new HashMap<String, Object>(4);
		params.put(NewsConstants.T_IDS, topicIds);
		params.put("role", "ROLE_MANAGER");
		params.put(Constants.CTX_TYPE, NewsConstants.CTX_T);
		params.put(Constants.ENABLED, "1");
		return getSqlMapClientTemplate().queryForList("getManagersForTopics",params);
	}

	@SuppressWarnings("unchecked")
	public List<User> getManagersForTopics(Long cId, Integer[] topicIds) throws DataAccessException {
		Map<String, Object> params = new HashMap<String, Object>(5);
		params.put("cId", cId);
		params.put(NewsConstants.T_IDS, topicIds);
		params.put("role", "ROLE_MANAGER");
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

	@SuppressWarnings("unchecked")
	public List<User> getAllUsers() throws DataAccessException {
		return getSqlMapClientTemplate().queryForList("getAllUsers");
	}

	@SuppressWarnings("unchecked")
	public List<String> getUserRolesInTopicsByTopics(String uid, Integer[] tIds) throws DataAccessException {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(NewsConstants.T_IDS, tIds);
		params.put(Constants.PRINCIPAL, uid);
		params.put(Constants.CTX_TYPE, NewsConstants.CTX_T);
		return getSqlMapClientTemplate().queryForList("getUserRolesInTopicsByTopics", params);
	}

	public void updateUserStatus(String uid, String enabled) throws DataAccessException {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(Constants.ENABLED, enabled);
		params.put(Constants.USER_ID,uid);
		getSqlMapClientTemplate().update("updateUserStatus", params);

	}

	public boolean isPermitted(String uid) throws DataAccessException {
		String r = (String) getSqlMapClientTemplate().queryForObject("isPermitted", uid);
		return ((r != null) && (Integer.parseInt(r) > 0));
	}

	public boolean isUserAccountEnabled(String uid) throws DataAccessException {
		String r = (String) getSqlMapClientTemplate().queryForObject("isUserAccountEnabled", uid);
		return ((r != null) && (Integer.parseInt(r) > 0));
	}

	@SuppressWarnings("unchecked")
	public List<User> getAllSuperUsers() throws DataAccessException {
		return getSqlMapClientTemplate().queryForList("getAllSuperUsers");
	}

	public boolean userRoleExist(String uid) throws DataAccessException {
		String r = (String) getSqlMapClientTemplate().queryForObject("userHasAnyRole", uid);
		return ((r != null) && (Integer.parseInt(r) > 0));
	}

	/**
	 * @param uid
	 * @throws DataAccessException
	 */
	public void removeAllRolesForUser(final String uid) throws DataAccessException {
		getSqlMapClientTemplate().delete("removeAllRolesForUser", uid);
	}

	/*public String getUserNameById(final String uid) throws UserNotFoundException, DataAccessException {
		return (String) getSqlMapClientTemplate().queryForObject("getUserNameById", uid);
	}*/

	/*public List<User> getUserByListUid(List<String> usersUid) {
		return getSqlMapClientTemplate().queryForList("getUserByListUid", usersUid);
	}*/

}
