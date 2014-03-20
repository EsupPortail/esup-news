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

import org.uhp.portlets.news.domain.Item;
import org.uhp.portlets.news.domain.Topic;
import org.uhp.portlets.news.service.exception.NoSuchItemException;
import org.uhp.portlets.news.web.ItemForm;

public interface ItemManager {
    List<Item> getItemListByTopic(Long topicId);

    List<Item> getAllItemListByTopic(Long topicId);

    List<Item> getValidatedItemListByTopic(Long topicId);

    List<Item> getArchivedItemListByTopic(Long topicId);

    List<Item> getPendingItemListByTopic(Long topicId);

    List<Item> getScheduledItemListByTopic(Long topicId);

    List<Item> getNItemsByTopic(Long topicId, Integer n);

    Integer getItemsCountByTopic(Long topicId);

    Integer getPendingItemsCountByTopic(Long topicId);

    Integer getPendingItemsCountByCategory(Long categoryId);

    List<Item> getItemListByCategory(Long categoryId);

    List<Topic> getTopicListByItem(Long itemId);

    Item getItemById(Long itemId) throws NoSuchItemException;

    long saveItem(Item item);

    long addItem(ItemForm itemForm);

    void updateItem(ItemForm itemForm);

    void addItemToTopics(Item item, String[] topicIds);

    void deleteItem(Long itemId);

    void deleteItemForAllTopics(Long itemId);

    void deleteItemForTopic(Long itemId, Long topicId);

    void deleteItemForTopics(Item item, int[] topicIds);

    void updateItemStatus(Integer status, Long itemId);

    void updateItemStatus(Long itemId);

    void updateItemOrderingInTopic(Long itemId, Long topicId, boolean up);

    void updateItemOrderingToFirstOrLastInTopic(Long itemId, Long topicId, boolean up);

    int getNbDays();

    int countItemsWithAttachmentByTopic(Long topicId);

    List<Item> getItemsWithAttachmentsByTopic(final Long topicId);

}
