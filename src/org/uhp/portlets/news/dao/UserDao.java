package org.uhp.portlets.news.dao;

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

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.uhp.portlets.news.domain.Role;
import org.uhp.portlets.news.domain.RoleEnum;
import org.uhp.portlets.news.domain.Topic;
import org.uhp.portlets.news.domain.User;
import org.uhp.portlets.news.domain.UserRole;

public interface UserDao {

	public void insert(User user) throws DataAccessException;

	public void update(User user) throws DataAccessException;

	public void activateUser(int enabled, String uid) throws DataAccessException;

	public void deleteUser(String uid, boolean rar) throws DataAccessException;

	public void deleteUser(User user) throws DataAccessException;

	public User getUserById(String uid) throws DataAccessException;

	//public String getUserNameById(String uid) throws DataAccessException;

	public boolean isSuperAdmin(String uid) throws DataAccessException;

	public boolean isUserAccountEnabled(String uid) throws DataAccessException;

	public void addUserRole(UserRole userRole) throws DataAccessException;

	public void addUserRole(User user, String role, String ctx, Long ctxId) throws DataAccessException;

	public void addUserRole(String uid, String role, String ctx, Long ctxId, String isGroup) throws DataAccessException;

	public List<UserRole> getUsersByRole(Long target, String targetCtx, RoleEnum role) throws DataAccessException;

	public void removeUserRoleForCtx(String uid, Long target, String targetCtx) throws DataAccessException;

	public void removeAllUserRolesForTopics(String uid, Long cId) throws DataAccessException;

	public List<UserRole> loadCtxUsersRolesMap(Long ctxId, String ctxType) throws DataAccessException;

	public String getUserRoleForCtx(String uid, Long ctxId, String ctxType) throws DataAccessException;

	public List<User> getManagersForCategory(Long categoryId) throws DataAccessException;

	public List<User> getManagersForTopic(Long topicId) throws DataAccessException;

	public List<User> getManagersForTopics(Integer[] topicIds) throws DataAccessException;

	public List<User> getManagersForTopics(Long cId, Integer[] topicIds) throws DataAccessException;

	public boolean isTopicManager(Topic topic, User user) throws DataAccessException;
	public boolean isUserRoleExistForContext(Long ctxId, String ctxType, String uid) throws DataAccessException;

	public List<User> getAllUsers() throws DataAccessException;

	public List<Role> getAllRoles() throws DataAccessException;

	public void removeUsersRoleForCtx(Long ctxId, String ctxType) throws DataAccessException;

	public void updateUserStatus(String uid, String enabled) throws DataAccessException;

	public void updateUserLastAccess(String uid) throws DataAccessException;

	public List<UserRole> getUsersRolesForCtx(Long ctxId, String ctxType) throws DataAccessException;

	public boolean isPermitted(String uid) throws DataAccessException;

	public List<User> getAllSuperUsers() throws DataAccessException;

	public boolean userRolesExistInTopics(String uid, Long categoryId) throws DataAccessException;

	public List<String> getUserRolesInTopicsByItem(String uid, Long itemId) throws DataAccessException;

	public List<String> getUserRolesInTopicsByTopics(String uid, Integer[] tIds) throws DataAccessException;

	public boolean userRoleExist(String uid) throws DataAccessException;

	//public List<User> getUserByListUid(List<String> usersUid);

	void updateUserRoleForCtx(String uid, String role, String ctx, Long ctxId)  throws DataAccessException;

	public boolean userRolesExistInCategories(String uid, Long entityId) throws DataAccessException;
}
