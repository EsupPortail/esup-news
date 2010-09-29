package org.uhp.portlets.news.service;

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

import org.uhp.portlets.news.domain.Topic;
import org.uhp.portlets.news.service.exception.NoSuchTopicException;

public interface TopicManager {
	
	public List<Topic> getTopicListByCategory(Long categoryId);
	public List<Topic> getTopicListForCategoryByUser(Long categoryId,	String uid);	

	public Topic getTopicById(Long topicId) throws NoSuchTopicException;

	public void addTopic(Topic topic);
	public void saveTopic(Topic topic);
	
	public boolean deleteTopic(Long topicId);
	
	public void updateTopicOrdering(Long topicId, Long categoryId, boolean up);
	public void updateTopicOrderingToFirstOrLast(Long topicId, Long categoryId, boolean up);
	
	//public List<Topic> getMostRecentTopicByCategory(Integer dayCount, Long categoryId);
}
