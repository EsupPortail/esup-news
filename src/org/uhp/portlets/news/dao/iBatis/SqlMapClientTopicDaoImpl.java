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

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.stereotype.Repository;
import org.uhp.portlets.news.NewsConstants;
import org.uhp.portlets.news.dao.Constants;
import org.uhp.portlets.news.dao.SequenceDao;
import org.uhp.portlets.news.dao.TopicDao;
import org.uhp.portlets.news.domain.Topic;
@Repository("topicDao")
public class SqlMapClientTopicDaoImpl extends SqlMapClientDaoSupport implements
		TopicDao {
	private static final Log LOGGER = LogFactory.getLog(SqlMapClientTopicDaoImpl.class);
	@Autowired private SequenceDao sequenceDao;

	public void setSequenceDao(final SequenceDao sequenceDao) {
		this.sequenceDao = sequenceDao;
	}

	@SuppressWarnings("unchecked")
	public List<Topic> getTopicListByCategory(final Long categoryId) throws DataAccessException {
		return  getSqlMapClientTemplate().queryForList("getTopicListByCategory", categoryId);
	}

	@SuppressWarnings("unchecked")
	public List<Topic> getTopicsForCategoryByUser(final Long categoryId, final String uid) throws DataAccessException {
		Map<String, Object> params = new HashMap<String, Object>();		
		params.put(NewsConstants.C_ID, categoryId);
		params.put(Constants.PRINCIPAL, uid);
		return  getSqlMapClientTemplate().queryForList("getAvailableTopicsByUser", params);
	}

	public Topic getTopicById(final Long topicId) throws DataAccessException {	
	    Topic topic = null;
        try {
            topic = (Topic) getSqlMapClientTemplate().queryForObject("getTopicById", topicId);
            if(topic == null) {
                LOGGER.error("getTopic:: topic null");
                throw new ObjectRetrievalFailureException(Topic.class, topicId);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }   
		return topic;
	}

	private Integer insert(Topic topic) throws DataAccessException {
		Integer maxOrder = getMaxDisplayOrder(topic.getCategoryId());
		int newOrder = (maxOrder  != null) ? (maxOrder.intValue() + 1) : 1;
		
		topic.setCreationDate(new Date());
		topic.setLastUpdateDate(new Date());
		topic.setDisplayOrder(newOrder);
		Long id=this.sequenceDao.getNextId(Constants.SEQ_TOPIC);
	    topic.setTopicId(id);
		return (Integer) getSqlMapClientTemplate().insert("insertTopic", topic);

	}
	
	private void update(Topic topic) throws DataAccessException {
		topic.setLastUpdateDate(new Date());
		getSqlMapClientTemplate().update("updateTopic", topic);

	}
	
	public void save(Topic topic) throws DataAccessException {
		if(topic.getTopicId() == null)  {
			insert(topic);
		}
		else {
			update(topic);
		}

	}

	 public boolean canDeleteTopic(final Long topicId) throws DataAccessException {
		 Integer t = (Integer) getSqlMapClientTemplate().queryForObject("canDeleteTopic", topicId);
		 return (t == null || t.intValue() < 1);
    }
	 
	public boolean delete(final Long topicId) throws DataAccessException {
		boolean success = false;
		if(canDeleteTopic(topicId)) {
		    getSqlMapClientTemplate().delete("deleteTopic", topicId);
		    success = true;
		}
		return success;
	}

	public Integer getMaxDisplayOrder(final Long categoryId) throws DataAccessException {
		return (Integer) getSqlMapClientTemplate().queryForObject("getMaxTopicOrderInCategory",categoryId);
	}                   
	
	@SuppressWarnings("unchecked")
	/*public List<Topic> getMostRecentByCategory(final Integer dayCount, final Long categoryId) throws DataAccessException {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(Constants.DAY_COUNT, dayCount);
		params.put(NewsConstants.C_ID, categoryId);		
		return getSqlMapClientTemplate().queryForList("getMostRecentByCategory", params);
	}*/
	
	private void updateTopicOrder(Topic topic, int order) throws DataAccessException  {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(NewsConstants.T_ID, topic.getTopicId());	
		params.put(Constants.DISPLAY_ORDER, Integer.valueOf(order));
		try {
			getSqlMapClientTemplate().update("updateTopicOrderById", params);
		} catch (DataAccessException e) {
		    LOGGER.warn("SqlMapClientTopicDaoImpl:: updateTopicOrder : Error : "+ e.getMessage());		
		}
	}

	public void updateTopicOrdering(Topic topic, Topic neighbor) throws DataAccessException {
		Integer order1 = topic.getDisplayOrder();
		this.updateTopicOrder(topic, neighbor.getDisplayOrder());
		this.updateTopicOrder(neighbor,  order1); 
    }
	
	@SuppressWarnings("unchecked")
	public List<Topic> getPendingTopicsForCategory(final Long categoryId) throws DataAccessException {
		return getSqlMapClientTemplate().queryForList("getPendingTopicsForCategory", categoryId);	
	}

	public boolean isTopicNameExistInCat(String name, Long id) throws DataAccessException {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(Constants.NAME, name);	
		params.put(Constants.ID, id);		
		return (((Integer) getSqlMapClientTemplate().queryForObject("isTopicNameExistInCat", params)).intValue() > 0);
	}

	public boolean isSameTopicNameExistInCat(String name, Long topicId, Long catId) throws DataAccessException {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(Constants.NAME, name);
		params.put(NewsConstants.T_ID, topicId);
		params.put(Constants.CAT_ID, catId);
		return (((Integer) getSqlMapClientTemplate().queryForObject("isSameTopicNameExistInCat", params)).intValue() > 0);		
	}

}
