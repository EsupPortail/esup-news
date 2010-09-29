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

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esco.portlets.news.dao.EscoUserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.uhp.portlets.news.NewsConstants;
import org.uhp.portlets.news.dao.ItemDao;
import org.uhp.portlets.news.dao.TopicDao;
import org.uhp.portlets.news.domain.Item;
import org.uhp.portlets.news.domain.RolePerm;
import org.uhp.portlets.news.domain.Topic;
import org.uhp.portlets.news.service.exception.NoSuchItemException;
import org.uhp.portlets.news.web.ItemForm;

@Service("itemManager")
@Transactional(readOnly = true)
public class ItemManagerImpl implements ItemManager {
    private static final int DEFAULT_NB_DAY_EXP = 30;
    private int nbDaysAfterForExp = DEFAULT_NB_DAY_EXP;
    private static final String STATUS_VALIDATE = "1";
    private static final String STATUS_NOT_VALIDATE = "0";

    @Autowired
    private ItemDao itemDao;

    private static final Log log = LogFactory.getLog(ItemManagerImpl.class);

    public List<Item> getItemListByTopic(final Long topicId) {
	return this.itemDao.getItemListByTopic(topicId);
    }

    public List<Item> getValidatedItemListByTopic(final Long topicId) {
	return this.itemDao.getValidatedItemListByTopic(topicId);
    }

    public List<Item> getScheduledItemListByTopic(final Long topicId) {
	return this.itemDao.getScheduledItemListByTopic(topicId);
    }

    public List<Item> getNItemsByTopic(final Long topicId, final Integer n) {
	return itemDao.getNItemsByTopic(topicId, n);
    }

    public List<Item> getItemListByCategory(final Long categoryId) {
	return this.itemDao.getItemListByCategory(categoryId);
    }

    public Item getItemById(final Long itemId) throws NoSuchItemException {
	Item item = null;
	try
	{
	    item = this.itemDao.getItemById(itemId);
	} catch (NumberFormatException ex)
	{
	    throw new NoSuchItemException("No such item [" + itemId + "] :" + ex);
	}
	return item;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public long saveItem(Item item) {
	if (item == null)
	{
	    throw new IllegalArgumentException("Cannot save a item when given a null Item instance.");
	}
	Calendar c = Calendar.getInstance();
	item.setLastUpdatedDate(c.getTime());
	long pkey = this.itemDao.save(this.setItemStartEndDates(item));
	return pkey;
    }

    private Date getDefaultExpirationDate(final Date sd) {
	Calendar c = Calendar.getInstance();
	if (sd != null)
	{
	    c.setTime(sd);
	}
	c.add(Calendar.DATE, nbDaysAfterForExp);
	return c.getTime();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void addItemToTopics(final Item item, final String[] topicIds) {
	this.itemDao.insertItemToTopics(item, topicIds);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteItem(final Long itemId) {
	this.itemDao.delete(itemId);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteItemForTopics(final Item item, final int[] topicIds) {
	this.itemDao.deleteItemForTopics(item, topicIds);
	this.removeOrphan(item.getItemId());
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteItemForAllTopics(final Long itemId) {
	this.itemDao.deleteItemForAllTopics(itemId);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void updateItemStatus(Integer status, final Long itemId) {
	this.itemDao.updateItemStatus(status.toString(), itemId);
    }

    public Integer getItemsCountByTopic(final Long topicId) {
	return this.itemDao.getItemsCountByTopic(topicId);
    }

    public List<Topic> getTopicListByItem(final Long itemId) {
	return this.itemDao.getTopicListByItem(itemId);
    }

    public List<Item> getAllItemListByTopic(final Long topicId) {
	return this.itemDao.getAllItemListByTopic(topicId);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteItemForTopic(final Long itemId, final Long topicId) {
	this.itemDao.deleteItemForTopic(itemId, topicId);
	// added
	this.removeOrphan(itemId);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void updateItemOrderingInTopic(final Long itemId, final Long topicId, boolean up) {
	final Item item = this.itemDao.getItemById(itemId);
	final List<Item> items = this.itemDao.getValidatedItemListByTopic(topicId);
	int idx = -1;
	for (Item it : items)
	{
	    idx++;
	    if (it.getItemId().compareTo(itemId) == 0)
	    {
		break;
	    }
	}
	if ((idx == -1) || (!up && (idx == items.size() - 1)) || (up && (idx == 0)))
	{
	    log.debug("updateItemOrderingInTopic:: nothing to do ");
	    return;
	}
	this.itemDao.updateItemOrderingInTopic(item, items.get((up) ? idx - 1 : idx + 1), topicId);
    }

    /**
     * Put to the first or the last position an item and in this case move other
     * items.
     * 
     * @param itemId
     * @param topicId
     * @param up
     *            Go to the top if true else to the bottom.
     * @see org.uhp.portlets.news.service.ItemManager#updateItemOrderingToFirstOrLastInTopic(java.lang.Long,
     *      java.lang.Long, boolean)
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateItemOrderingToFirstOrLastInTopic(Long itemId, Long topicId, boolean up) {
	final List<Item> items = this.itemDao.getValidatedItemListByTopic(topicId);
	for (int i = 0; i < items.size(); i++)
	{
	    updateItemOrderingInTopic(itemId, topicId, up);
	}
    }

    public List<Item> getPendingItemListByTopic(final Long topicId) {
	return this.itemDao.getPendingItemListByTopic(topicId);
    }

    public Integer getPendingItemsCountByTopic(final Long topicId) {
	return this.itemDao.getPendingItemsCountByTopic(topicId);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public long addItem(final ItemForm itemForm) {
	if (itemForm == null)
	{
	    throw new IllegalArgumentException("Cannot add a item when given a null ItemForm instance.");
	}
	long pkey = saveItem(itemForm.getItem());
	this.itemDao.insertItemToTopics(itemForm.getItem(), itemForm.getTopicIds());

	// return the primary key of the newly inserted row
	return pkey;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public long addItem(ItemForm itemForm, String uid) {
	boolean isSuperU = setItemStatus(itemForm, uid);
	long pkey = addItem(itemForm);
	// return the primary key of the newly inserted row
	return pkey;
    }

    @Autowired
    private EscoUserDao userDao;
    @Autowired
    private TopicDao topicDao;

    @Transactional(propagation = Propagation.REQUIRED)
    public void updateItem(ItemForm itemForm, String uid) {
	boolean isSuperU = false;
	if (itemForm == null)
	{
	    throw new IllegalArgumentException("Cannot update a item when given a null ItemForm instance.");
	}
	isSuperU = setItemStatus(itemForm, uid);
	Item item = this.setItemStartEndDates(itemForm.getItem());
	if (isSuperU || this.userDao.getUserRoleForCtx(uid, item.getCategoryId(), NewsConstants.CTX_C).equals(RolePerm.ROLE_MANAGER.getName()))
	{
	    this.itemDao.deleteItemForAllTopics(item.getItemId());
	} else
	{
	    List<Topic> topics = this.topicDao.getTopicsForCategoryByUser(item.getCategoryId(), uid);
	    for (Topic t : topics)
	    {
		this.itemDao.deleteItemForTopic(item.getItemId(), t.getTopicId());
	    }
	}
	this.itemDao.save(itemForm.getItem());
	this.itemDao.insertItemToTopics(itemForm.getItem(), itemForm.getTopicIds());
    }

    private boolean setItemStatus(ItemForm itemForm, String uid) {
	boolean isSuperU = false;
	if (this.userDao.isSuperAdmin(uid))
	{
	    itemForm.getItem().setStatus(STATUS_VALIDATE);
	    isSuperU = true;
	} else
	{

	    List<String> roles = this.userDao.getUserRolesInTopicsByTopics(uid, toInteger(itemForm.getTopicIds()));

	    if (roles.contains(RolePerm.ROLE_MANAGER.getName()))
	    {
		itemForm.getItem().setStatus(STATUS_VALIDATE);
	    } else if (roles.contains(RolePerm.ROLE_CONTRIBUTOR.getName()))
	    {
		itemForm.getItem().setStatus(STATUS_NOT_VALIDATE);
	    } else
	    {
		itemForm.getItem().setStatus(STATUS_VALIDATE);
	    }
	}
	return isSuperU;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void updateItemStatus(final Long itemId) {
	String curStatus = this.itemDao.getItemById(itemId).getStatus();
	this.itemDao.updateItemStatus("1".equals(curStatus) ? "0" : "1", itemId);

    }

    public int getNbDaysAfterForExp() {
	return this.nbDaysAfterForExp;
    }

    public void setNbDaysAfterForExp(int nbDaysAfterForExp) {
	this.nbDaysAfterForExp = nbDaysAfterForExp;
    }

    public List<Item> getArchivedItemListByTopic(Long topicId) {
	try
	{
	    return this.itemDao.getExpiredItemListByTopic(topicId);

	} catch (DataAccessException e)
	{
	    if (log.isWarnEnabled())
	    {
		log.warn("Data access error : " + e.getMessage());
	    }
	}
	return null;
    }

    public Integer getPendingItemsCountByCategory(Long categoryId) {
	try
	{
	    return this.itemDao.getPendingItemsCountByCategory(categoryId);
	} catch (DataAccessException e)
	{
	    if (log.isWarnEnabled())
	    {
		log.warn("data access error : " + e.getMessage());
	    }
	}
	return null;
    }

    public int getNbDays() {
	return this.nbDaysAfterForExp;
    }

    private Integer[] toInteger(final String[] arr) {
	if (arr == null)
	{
	    return null;
	}

	final Integer[] res = new Integer[arr.length];
	for (int i = 0; i < arr.length; i++)
	{
	    res[i] = Integer.valueOf(arr[i]);
	}
	return res;
    }

    private Item setItemStartEndDates(Item item) {
	Calendar c = Calendar.getInstance();
	if (item.getStartDate() == null)
	{
	    item.setStartDate(c.getTime());
	    if (log.isDebugEnabled())
	    {
		log.debug("setItemStartDate: item.startDate not set, default startDate to today : " + item.getStartDate());
	    }
	}
	if (item.getEndDate() == null)
	{
	    item.setEndDate(this.getDefaultExpirationDate(item.getStartDate()));
	    if (log.isDebugEnabled())
	    {
		log.debug("setItemEndDate: item.endDate  not set, default endDate to  startDate + " + nbDaysAfterForExp + " : " + item.getEndDate());
	    }
	}
	return item;
    }

    private void removeOrphan(final Long itemId) {
	if (this.itemDao.isOrphan(itemId))
	{
	    this.itemDao.delete(itemId);
	}
    }

    public int countItemsWithAttachmentByTopic(Long topicId) {
	try
	{
	    return this.itemDao.countItemsWithAttachmentByTopic(topicId);
	} catch (DataAccessException e)
	{
	    if (log.isWarnEnabled())
	    {
		log.warn("data access error : " + e.getMessage());
	    }
	}
	return -1;
    }

    public List<Item> getItemsWithAttachmentsByTopic(final Long topicId) {
	return this.itemDao.getItemsWithAttachmentsByTopic(topicId);
    }

}
