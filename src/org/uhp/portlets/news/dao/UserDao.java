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
import org.uhp.portlets.news.domain.Topic;
import org.uhp.portlets.news.domain.User;

public interface UserDao {

	void insert(User user) throws DataAccessException;

	void update(User user) throws DataAccessException;

	void activateUser(int enabled, String uid) throws DataAccessException;

	void deleteUser(String uid, boolean rar) throws DataAccessException;

	void deleteUser(User user) throws DataAccessException;

	User getUserById(String uid) throws DataAccessException;

	//String getUserNameById(String uid) throws DataAccessException;

	boolean isSuperAdmin(String uid) throws DataAccessException;

	boolean isUserAccountEnabled(String uid) throws DataAccessException;

	List<User> getManagersForCategory(Long categoryId) throws DataAccessException;

	List<User> getManagersForTopic(Long topicId) throws DataAccessException;

	List<User> getManagersForTopics(Integer[] topicIds) throws DataAccessException;

	List<User> getManagersForTopics(Long cId, Integer[] topicIds) throws DataAccessException;

	boolean isTopicManager(Topic topic, User user) throws DataAccessException;

	List<String> getUserRolesInTopicsByTopics(String uid, Integer[] tIds) throws DataAccessException;

	List<User> getAllUsers() throws DataAccessException;

	void updateUserStatus(String uid, String enabled) throws DataAccessException;

	void updateUserLastAccess(String uid) throws DataAccessException;

	boolean isPermitted(String uid) throws DataAccessException;

	List<User> getAllSuperUsers() throws DataAccessException;

	boolean userRoleExist(String uid) throws DataAccessException;

	//List<User> getUserByListUid(List<String> usersUid);

}
