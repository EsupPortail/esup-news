package org.uhp.portlets.news.dao.iBatis;

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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.stereotype.Repository;
import org.uhp.portlets.news.NewsConstants;
import org.uhp.portlets.news.dao.Constants;
import org.uhp.portlets.news.dao.ItemDao;
import org.uhp.portlets.news.dao.SequenceDao;
import org.uhp.portlets.news.domain.Item;
import org.uhp.portlets.news.domain.Topic;
import org.uhp.portlets.news.service.exception.NoSuchItemException;

@Repository("itemDao")
public class SqlMapClientItemDaoImpl extends SqlMapClientDaoSupport implements ItemDao {
    private static final Log LOGGER = LogFactory.getLog(SqlMapClientItemDaoImpl.class);
    @Autowired
    private SequenceDao sequenceDao;

    public void setSequenceDao(SequenceDao sequenceDao) {
	this.sequenceDao = sequenceDao;
    }

    @SuppressWarnings("unchecked")
    public List<Item> getItemListByTopic(final Long topicId) throws DataAccessException {
	return getSqlMapClientTemplate().queryForList("getItemListByTopic", topicId);

    }

    @SuppressWarnings("unchecked")
    public List<Item> getValidatedItemListByTopic(final Long topicId) throws DataAccessException {
	return getSqlMapClientTemplate().queryForList("getValidatedItemListByTopic", topicId);
    }

    @SuppressWarnings("unchecked")
    public List<Item> getNItemsByTopic(final Long topicId, final Integer n) throws DataAccessException {
	Map<String, Object> params = new HashMap<String, Object>(2);
	params.put(NewsConstants.T_ID, topicId);
	params.put("n", n);
	return getSqlMapClientTemplate().queryForList("getNItemsByTopic", params);

    }

    @SuppressWarnings("unchecked")
    public List<Item> getItemListByCategory(final Long categoryId) {
	return getSqlMapClientTemplate().queryForList("getItemListByCategory", categoryId);
    }

    private Integer insert(Item item) throws DataAccessException {
	item.setItemId(this.sequenceDao.getNextId(Constants.SEQ_ITEM));
	return (Integer) getSqlMapClientTemplate().insert("insertItem", item);

    }

    private void update(final Item item) throws DataAccessException {
	getSqlMapClientTemplate().update("updateItem", item);
    }

    public long save(final Item item) throws DataAccessException {
	long pkey = -1;
	if (item.getItemId() == null)
	{
	    insert(item);
	    pkey = item.getItemId().longValue();

	} else
	{
	    update(item);
	}
	return pkey;
    }

    public void insertItemToTopics(final Item item, final String[] topicIds) throws DataAccessException {
	Map<String, Object> params = new HashMap<String, Object>(3);
	for (int i = 0; i < topicIds.length; i++)
	{
	    params.put(NewsConstants.I_ID, item.getItemId());
	    int ord = getMaxDisplayOrder(Long.valueOf(topicIds[i])).intValue() + 1;
	    params.put(NewsConstants.T_ID, Long.valueOf(topicIds[i]));
	    params.put(Constants.DISPLAY_ORDER, Integer.valueOf(ord));
	    getSqlMapClientTemplate().insert("insertItemToTopic", params);
	    params.clear();
	}
    }

    public void updateItemStatus(final String status, final Long itemId) throws DataAccessException {
	Map<String, Object> params = new HashMap<String, Object>(2);
	params.put(Constants.STATUS, status);
	params.put(NewsConstants.I_ID, itemId);
	getSqlMapClientTemplate().update("updateItemStatus", params);
    }

    public void delete(final Long itemId) throws DataAccessException {
	getSqlMapClientTemplate().delete("deleteItem", itemId);
	if (LOGGER.isDebugEnabled())
	{
	    LOGGER.debug("ItemDao:: item [itemId=" + itemId + "] has been removed");
	}
    }

    public void deleteItemForTopic(final Long itemId, final Long topicId) throws DataAccessException {
	Map<String, Object> params = new HashMap<String, Object>(2);
	params.put(NewsConstants.I_ID, itemId);
	params.put(NewsConstants.T_ID, topicId);
	getSqlMapClientTemplate().delete("deleteItemForTopic", params);
    }

    public void deleteItemForAllTopics(final Long itemId) throws DataAccessException {
	getSqlMapClientTemplate().delete("deleteItemForAllTopic", itemId);
    }

    public void deleteItemForTopics(final Item item, final int[] topicIds) throws DataAccessException {
	for (int i = 0; i < topicIds.length; i++)
	{
	    deleteItemForTopic(item.getItemId(), Long.valueOf(topicIds[i]));
	}
    }

    public Item getItemById(final Long itemId) {
	Item item = (Item) getSqlMapClientTemplate().queryForObject("getItemById", itemId);
	if (item == null)
	{
	    if (LOGGER.isWarnEnabled())
	    {
		LOGGER.warn("item [" + itemId + "] not found...");
	    }
	    throw new NoSuchItemException("Item not found error");
	}
	return item;
    }

    public Integer getMaxDisplayOrder(final Long topicId) throws DataAccessException {
	Integer maxOrd = (Integer) getSqlMapClientTemplate().queryForObject("getMaxItemOrderInTopic", topicId);
	if (maxOrd == null)
	    maxOrd = 0;
	return maxOrd;
    }

    public Integer getItemsCountByTopic(final Long topicId) throws DataAccessException {
	return (Integer) getSqlMapClientTemplate().queryForObject("getItemsCountByTopic", topicId);
    }

    @SuppressWarnings("unchecked")
    public List<Topic> getTopicListByItem(final Long itemId) throws DataAccessException {

	return getSqlMapClientTemplate().queryForList("getTopicListByItem", itemId);
    }

    @SuppressWarnings("unchecked")
    public List<Item> getMostRecentItemsByCategory(Long categoryId, Integer dayCount) throws DataAccessException {
	Map<String, Object> params = new HashMap<String, Object>(2);
	params.put(NewsConstants.C_ID, categoryId);
	params.put(Constants.DAY_COUNT, dayCount);
	return getSqlMapClientTemplate().queryForList("getMostRecentItemsByCategory", params);
    }

    @SuppressWarnings("unchecked")
    public List<Item> getAllItemListByTopic(final Long topicId) throws DataAccessException {

	return getSqlMapClientTemplate().queryForList("getAllItemListByTopic", topicId);
    }

    @SuppressWarnings("unchecked")
    public List<Item> getScheduledItemListByTopic(Long topicId) throws DataAccessException {
	return getSqlMapClientTemplate().queryForList("getScheduledItemListByTopic", topicId);
    }

    private Integer getItemOrderInTopic(final Long itemId, final Long topicId) throws DataAccessException {
	Map<String, Object> params = new HashMap<String, Object>(2);
	params.put(NewsConstants.I_ID, itemId);
	params.put(NewsConstants.T_ID, topicId);
	return (Integer) getSqlMapClientTemplate().queryForObject("getItemOrderInTopic", params);
    }

    private void updateItemOrder(Item item, Long topicId, Integer order) {
	Map<String, Object> params = new HashMap<String, Object>(3);
	params.put(NewsConstants.I_ID, item.getItemId());
	params.put(NewsConstants.T_ID, topicId);
	params.put(Constants.DISPLAY_ORDER, order);
	try
	{
	    getSqlMapClientTemplate().update("updateItemOrderInTopic", params);
	} catch (DataAccessException e)
	{
	    LOGGER.error("SqlMapClientItemDaoImpl:: updateItemOrder : Error : " + e.getMessage());

	}
    }

    public void updateItemOrderingInTopic(Item item, Item neighbor, Long topicId) throws DataAccessException {
	Integer order1 = getItemOrderInTopic(item.getItemId(), topicId);
	this.updateItemOrder(item, topicId, getItemOrderInTopic(neighbor.getItemId(), topicId));
	this.updateItemOrder(neighbor, topicId, order1);
    }

    @SuppressWarnings("unchecked")
    public List<Item> getPendingItemListByTopic(final Long topicId) throws DataAccessException {
	return getSqlMapClientTemplate().queryForList("getPendingItemListByTopic", topicId);
    }

    public Integer getPendingItemsCountByTopic(final Long topicId) throws DataAccessException {
	return (Integer) getSqlMapClientTemplate().queryForObject("getPendingItemsCountByTopic", topicId);
    }

    public Integer getScheduledItemsCountByTopic(Long topicId) throws DataAccessException {
	return (Integer) getSqlMapClientTemplate().queryForObject("getScheduledItemsCountByTopic", topicId);
    }

    public Integer getTotalItemsCountByTopic(Long topicId) throws DataAccessException {
	return (Integer) getSqlMapClientTemplate().queryForObject("getTotalItemsCountByTopic", topicId);
    }

    @SuppressWarnings("unchecked")
    public List<Item> getExpiredItemListByTopic(final Long topicId) throws DataAccessException {
	return getSqlMapClientTemplate().queryForList("getExpiredItemListByTopic", topicId);
    }

    public Integer getPendingItemsCountByCategory(final Long categoryId) throws DataAccessException {

	return (Integer) getSqlMapClientTemplate().queryForObject("getPendingItemsCountByCategory", categoryId);
    }

    public Integer getItemsCountByCategory(final Long categoryId) throws DataAccessException {
	return (Integer) getSqlMapClientTemplate().queryForObject("getItemsCountByCategory", categoryId);
    }

    public Integer getPendingItemsCountByTopics(final String[] topicIds) throws DataAccessException {
	Map<String, Object> params = new HashMap<String, Object>();
	params.put(NewsConstants.T_IDS, topicIds);
	return (Integer) getSqlMapClientTemplate().queryForObject("getPendingItemsCountForTopics", params);
    }

    public boolean hasProtectedTopics(Long itemId) throws DataAccessException {
	Integer rt = (Integer) getSqlMapClientTemplate().queryForObject("hasProtectedTopic", itemId);
	if (rt == null)
	    return false;
	return (rt.intValue() > 0);
    }

    public boolean isOrphan(Long itemId) throws DataAccessException {
	Integer rt = (Integer) getSqlMapClientTemplate().queryForObject("isOrphan", itemId);
	if (rt == null)
	    return true;
	return (rt.intValue() < 1);
    }

    public int countItemsWithAttachmentByTopic(final Long topicId) throws DataAccessException {
	return ((Integer) getSqlMapClientTemplate().queryForObject("countItemsWithAttachmentByTopic", topicId)).intValue();
    }

    @SuppressWarnings("unchecked")
    public List<Item> getItemsWithAttachmentsByTopic(final Long topicId) throws DataAccessException {
	return getSqlMapClientTemplate().queryForList("getItemsWithAttachmentsByTopic", topicId);
    }
}
