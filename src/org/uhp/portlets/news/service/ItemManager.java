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
    public List<Item> getItemListByTopic(Long topicId);

    public List<Item> getAllItemListByTopic(Long topicId);

    public List<Item> getValidatedItemListByTopic(Long topicId);

    public List<Item> getArchivedItemListByTopic(Long topicId);

    public List<Item> getPendingItemListByTopic(Long topicId);

    public List<Item> getScheduledItemListByTopic(Long topicId);

    public List<Item> getNItemsByTopic(Long topicId, Integer n);

    public Integer getItemsCountByTopic(Long topicId);

    public Integer getPendingItemsCountByTopic(Long topicId);

    public Integer getPendingItemsCountByCategory(Long categoryId);

    public List<Item> getItemListByCategory(Long categoryId);

    public List<Topic> getTopicListByItem(Long itemId);

    public Item getItemById(Long itemId) throws NoSuchItemException;

    public long saveItem(Item item);

    public long addItem(ItemForm itemForm);

    public long addItem(ItemForm itemForm, String uid);

    public void updateItem(ItemForm itemForm, String uid);

    public void addItemToTopics(Item item, String[] topicIds);

    public void deleteItem(Long itemId);

    public void deleteItemForAllTopics(Long itemId);

    public void deleteItemForTopic(Long itemId, Long topicId);

    public void deleteItemForTopics(Item item, int[] topicIds);

    public void updateItemStatus(Integer status, Long itemId);

    public void updateItemStatus(Long itemId);

    public void updateItemOrderingInTopic(Long itemId, Long topicId, boolean up);

    public void updateItemOrderingToFirstOrLastInTopic(Long itemId, Long topicId, boolean up);

    public int getNbDays();

    public int countItemsWithAttachmentByTopic(Long topicId);

    public List<Item> getItemsWithAttachmentsByTopic(final Long topicId);

}
