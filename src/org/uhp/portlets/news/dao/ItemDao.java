package org.uhp.portlets.news.dao;

/**
 * @Project NewsPortlet : http://sourcesup.cru.fr/newsportlet/ 
 * Copyright (C)  2007-2008 University Nancy 1
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
import org.uhp.portlets.news.domain.Item;
import org.uhp.portlets.news.domain.Topic;

public interface ItemDao {

    public List<Item> getItemListByTopic(Long topicId) throws DataAccessException;

    public List<Item> getPendingItemListByTopic(Long topicId) throws DataAccessException;

    public List<Item> getExpiredItemListByTopic(Long topicId) throws DataAccessException;

    public List<Item> getAllItemListByTopic(Long topicId) throws DataAccessException;

    public List<Item> getValidatedItemListByTopic(Long topicId) throws DataAccessException;

    public List<Item> getScheduledItemListByTopic(Long topicId) throws DataAccessException;

    public List<Item> getNItemsByTopic(Long topicId, Integer n) throws DataAccessException;

    public Integer getItemsCountByTopic(Long topicId) throws DataAccessException;

    public Integer getPendingItemsCountByTopic(Long topicId) throws DataAccessException;

    public Integer getScheduledItemsCountByTopic(Long topicId) throws DataAccessException;

    public Integer getTotalItemsCountByTopic(Long topicId) throws DataAccessException;

    public Integer getPendingItemsCountByTopics(String[] topicIds) throws DataAccessException;

    public Integer getItemsCountByCategory(Long categoryId) throws DataAccessException;

    public Integer getPendingItemsCountByCategory(Long categoryId) throws DataAccessException;

    public List<Item> getItemListByCategory(Long categoryId) throws DataAccessException;

    public List<Item> getMostRecentItemsByCategory(Long categoryId, Integer dayCount) throws DataAccessException;

    public List<Topic> getTopicListByItem(Long itemId) throws DataAccessException;

    public boolean hasProtectedTopics(Long itemId) throws DataAccessException;

    public boolean isOrphan(Long itemId) throws DataAccessException;

    public long save(Item item) throws DataAccessException;

    public Item getItemById(Long itemId) throws DataAccessException;

    public void insertItemToTopics(Item item, String[] topicIds) throws DataAccessException;

    public void delete(Long itemId) throws DataAccessException;

    public void deleteItemForTopic(Long itemId, Long topicId) throws DataAccessException;

    public void deleteItemForTopics(Item item, int[] topicIds) throws DataAccessException;

    public void deleteItemForAllTopics(Long itemId) throws DataAccessException;

    public void updateItemStatus(String status, Long itemId) throws DataAccessException;

    public void updateItemOrderingInTopic(Item item, Item neighbor, Long topicId) throws DataAccessException;

    public int countItemsWithAttachmentByTopic(final Long topicId) throws DataAccessException;

    public List<Item> getItemsWithAttachmentsByTopic(final Long topicId) throws DataAccessException;
}
