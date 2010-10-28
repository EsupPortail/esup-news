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

	public interface TopicDao {
			
			public List<Topic> getTopicsForCategoryByUser(Long categoryId, String uid) throws DataAccessException;
			
			public List<Topic> getTopicListByCategory(Long categoryId) throws DataAccessException;

			public Topic getTopicById(Long topicId) throws DataAccessException;

			public void save(Topic topic) throws DataAccessException;

			public boolean delete(Long topicId) throws DataAccessException;

			public void updateTopicOrdering(Topic topic, Topic neighbor) throws DataAccessException;

			//public List<Topic> getMostRecentByCategory(Integer dayCount, Long categoryId) throws DataAccessException;

			public List<Topic> getPendingTopicsForCategory(final Long categoryId) throws DataAccessException;

			public boolean isTopicNameExistInCat(String name, Long id) throws DataAccessException;
			
			public boolean isSameTopicNameExistInCat(String name, Long topicId, Long catId) throws DataAccessException;
	        
	}