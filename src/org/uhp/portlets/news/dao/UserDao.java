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

import org.esco.portlets.news.domain.EntityRole;
import org.springframework.dao.DataAccessException;
import org.uhp.portlets.news.domain.Role;
import org.uhp.portlets.news.domain.RoleEnum;
import org.uhp.portlets.news.domain.Topic;
import org.uhp.portlets.news.domain.User;

public interface UserDao {

	void insert(final User user) throws DataAccessException;

	void update(final User user) throws DataAccessException;

	void updateUserStatus(final String userId, final String enabled) throws DataAccessException;

	void updateLastUserAccess(final String userId) throws DataAccessException;

	void deleteUser(final String userId) throws DataAccessException;

	User getUserById(final String userId) throws DataAccessException;

	boolean isUserSuperAdmin(final String userId) throws DataAccessException;

	boolean isUserAccountDisabled(final String userId) throws DataAccessException;

	List<User> getAllUsers() throws DataAccessException;

	List<User> getAllSuperUsers() throws DataAccessException;

	boolean isUserHasAnyRole(final String userId);


	// WARNING Can delete user without ROLES : public void deleteUser(String uid, boolean rar) throws DataAccessException;
	// NOT USED ? public void activateUser(int enabled, String uid) throws DataAccessException;

	//public List<User> getUserByListUid(List<String> usersUid);

	//public boolean isPermitted(String uid) throws DataAccessException;


	@Deprecated
	List<User> getManagersForCategory(Long categoryId) throws DataAccessException;

			//List<User> getManagersForTopic(Long topicId) throws DataAccessException;

			//List<User> getManagersForTopics(Integer[] topicIds) throws DataAccessException;

	@Deprecated
	List<User> getManagersForTopics(Long cId, Integer[] topicIds) throws DataAccessException;

	@Deprecated
	boolean isTopicManager(Topic topic, User user) throws DataAccessException;

}
