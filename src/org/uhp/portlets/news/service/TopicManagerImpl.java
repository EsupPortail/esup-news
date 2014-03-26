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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esco.portlets.news.dao.EscoUserDao;
import org.esco.portlets.news.dao.SubjectRoleDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.uhp.portlets.news.NewsConstants;
import org.uhp.portlets.news.dao.ItemDao;
import org.uhp.portlets.news.dao.SubscriberDao;
import org.uhp.portlets.news.dao.TopicDao;
import org.uhp.portlets.news.domain.Topic;
import org.uhp.portlets.news.domain.UserRole;
import org.uhp.portlets.news.service.exception.NoSuchTopicException;

@Service("topicManager")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class TopicManagerImpl implements TopicManager {
	private static final Log LOGGER = LogFactory.getLog(TopicManagerImpl.class);

	@Autowired private TopicDao topicDao;
	/** Dao d'un UserRole. */
	@Autowired
	private SubjectRoleDao roleDao;

	public List<Topic> getTopicListByCategory(final Long categoryId) {
		return this.topicDao.getTopicListByCategory(categoryId);

	}

	@Autowired private EscoUserDao userDao;

	public List<Topic> getTopicListForCategoryByUser(final Long categoryId,	final String uid) {
		List<Topic> topics = null;
		List<Topic> ts = new ArrayList<Topic>();
		if (this.userDao.isSuperAdmin(uid)) {
			topics =  this.topicDao.getTopicListByCategory(categoryId);
		} else {
			topics = this.topicDao.getTopicsForCategoryByUser(categoryId, uid);
		}
		for (Topic t : topics) {
			ts.add(this.setTopicCounts(t));
		}
		return topics;
	}

	public Topic getTopicById(final Long topicId) throws NoSuchTopicException {
		try {
			Topic t =  this.topicDao.getTopicById(topicId);
			if(t != null) {
				t = this.setTopicCounts(t);
				return t;
			}

		} catch (DataAccessException e) {
			LOGGER.error(" get Topic error : "+ e.getLocalizedMessage());
		}
		return null;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void saveTopic(Topic topic) {
		if (topic == null) {
			throw new IllegalArgumentException("Cannot save a topic when given a null Topic instance.");
		}

		if (topic.getTopicId() == null) {
			topic.setCreationDate(new Date());
		}
		this.topicDao.save(topic);
	}

	@Autowired private SubscriberDao subDao;
	@Transactional(propagation = Propagation.REQUIRED)
	public boolean deleteTopic(final Long topicId) {
		try {
			if(this.topicDao.delete(topicId)) {
				this.roleDao.removeUsersRoleForCtx(topicId, NewsConstants.CTX_T);
				this.subDao.deleteAllSubscribersByCtxId(topicId, NewsConstants.CTX_T);
				return true;
			}

		} catch (DataAccessException e1) {
			LOGGER.error("delete Topic error:" + e1.getLocalizedMessage());
		}
		return false;
	}

	/**
	 * @param topicId
	 * @param categoryId
	 * @param up
	 * @see org.uhp.portlets.news.service.TopicManager#updateTopicOrdering(java.lang.Long, java.lang.Long, boolean)
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void updateTopicOrdering(final Long topicId, final Long categoryId, final boolean up) {
		Topic topic1 = this.topicDao.getTopicById(topicId);
		List<Topic> topics = this.topicDao.getTopicListByCategory(categoryId);
		int idx = getIdxForTopic(topicId, topics);
		if ((idx == -1) || (!up && (idx == topics.size()-1)) || (up && (idx == 0)) ) {
			LOGGER.debug("updateTopicOrdering:: nothing to do ..." );
		} else {
			if (up) {
				idx--;
			} else {
				idx++;
			}
			this.topicDao.updateTopicOrdering(topic1, topics.get(idx));
		}
	}

	/**
	 * Put to the first or the last position a topic and in this case move other topic.
	 * @param topicId
	 * @param categoryId
	 * @param up Go to the top if true else to the bottom.
	 * @see org.uhp.portlets.news.service.TopicManager#
	 * updateTopicOrderingToFirstOrLast(java.lang.Long, java.lang.Long, boolean)
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void updateTopicOrderingToFirstOrLast(Long topicId, Long categoryId, boolean up) {
		try {
			List<Topic> topics = this.topicDao.getTopicListByCategory(categoryId);
			for (int i=0; i < topics.size(); i++) {
				updateTopicOrdering(topicId, categoryId, up);
			}
		} catch (DataAccessException e) {
			LOGGER.error("Update Topic Ordering To First Or Last error : " + e.getLocalizedMessage());
			throw e;
		}
	}

	/*public List<Topic> getMostRecentTopicByCategory(Integer dayCount, Long categoryId) {

		try {
			return this.topicDao.getMostRecentByCategory(dayCount, categoryId);
		} catch (DataAccessException e) {
			log.error("getMostRecentTopicByCategory error:"+e.getMessage());
		} catch (NumberFormatException nfe) {
			log.error("getMostRecentTopicByCategory error:"+nfe.getMessage());
		}
		return null;
	}*/

	@Transactional(propagation = Propagation.REQUIRED)
	public void addTopic(Topic topic) {

		this.saveTopic(topic);
		List<UserRole> userRoles;
		try {
			userRoles = this.roleDao.getUsersRolesForCtx(topic.getCategoryId(), NewsConstants.CTX_C);
			for(UserRole ur : userRoles) {
				if(!"ROLE_USER".equals(ur.getRole())) {
					this.roleDao.addUserRole(ur.getPrincipal(), ur.getRole(), NewsConstants.CTX_T, topic.getTopicId(), ur.getIsGroup().equals("1"), ur.getFromGroup());
				}
			}
		} catch (DataAccessException e) {
			LOGGER.error("addTopic : " +e.getLocalizedMessage());
		}
	}

	private int getIdxForTopic(final Long id, final List<Topic> topics) {
		int n=-1;
		for(Topic t : topics) {
			n++;
			if(t.getTopicId().compareTo(id)==0) {
				break;
			}
		}
		return n;
	}

	@Autowired private ItemDao itemDao;
	private Topic setTopicCounts(final Topic topic) {
		Topic t = topic;
		Long tId = t.getTopicId();
		t.setPendingCount(this.itemDao.getPendingItemsCountByTopic(tId).intValue());
		t.setCount(this.itemDao.getItemsCountByTopic(tId).intValue());
		t.setScheduleCount(this.itemDao.getScheduledItemsCountByTopic(tId).intValue());
		t.setTotalCount(this.itemDao.getTotalItemsCountByTopic(tId).intValue());
		t.setArchivedCount(t.getTotalCount() - t.getCount() - t.getPendingCount() - t.getScheduleCount());
		return t;
	}
}
