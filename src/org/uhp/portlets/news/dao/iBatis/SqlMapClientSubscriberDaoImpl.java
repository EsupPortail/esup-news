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

import org.uhp.portlets.news.dao.Constants;
import org.uhp.portlets.news.dao.SequenceDao;
import org.uhp.portlets.news.dao.SubscriberDao;
import org.uhp.portlets.news.domain.Subscriber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.stereotype.Repository;

@Repository("subscriberDao")
public class SqlMapClientSubscriberDaoImpl extends SqlMapClientDaoSupport
		implements SubscriberDao {

	@Autowired private SequenceDao sequenceDao;

	public void setSequenceDao(SequenceDao sequenceDao) {
		this.sequenceDao = sequenceDao;
	}

	public void insertSubscriber(final Subscriber subscriber) {
	    subscriber.setId(this.sequenceDao.getNextId(Constants.SEQ_SUBSCRIBER));			
		getSqlMapClientTemplate().insert("addSubscriber", subscriber);
	}

	@SuppressWarnings("unchecked")
	public List<Subscriber> getSubscribers(final Long target, final String targetCtx, final String subType, final Boolean isGrp) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(Constants.CTX_ID, target);
		params.put(Constants.CTX_TYPE, targetCtx);
		params.put(Constants.SUB_TYPE, subType);
		if (isGrp) {
			return  getSqlMapClientTemplate().queryForList("getSubscriberGroupsByCtxIdSubType", params);
			
		}
		else {
			return getSqlMapClientTemplate().queryForList("getSubscribersByCtxIdSubType", params);
			
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public List<Subscriber> getSubscribers(final Long target, final String targetCtx, final String subType) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put(Constants.CTX_ID, target);
			params.put(Constants.CTX_TYPE, targetCtx);
			params.put(Constants.SUB_TYPE, subType);					 
			return getSqlMapClientTemplate().queryForList("getAllSubscribersByCtxIdSubType", params);	
	}
	
	public void deleteAllSubscribersByCtxId(Long target, String targetCtx) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(Constants.CTX_ID, target);
		params.put(Constants.CTX_TYPE, targetCtx);
		getSqlMapClientTemplate().delete("removeAllSubscribersByCtxId", params);

	}

	public void deleteAllSubscribersByCtxIdForSubType(Long target, String targetCtx, String subType) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(Constants.CTX_ID, target);
		params.put(Constants.CTX_TYPE, targetCtx);
		params.put(Constants.SUB_TYPE, subType);
		getSqlMapClientTemplate().delete("removeAllSubscribersByCtxIdForSubType", params);

	}

	public void deleteSubscriberByCtxId(Long id, Long target, String targetCtx) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put(Constants.CTX_ID, target);
			params.put(Constants.CTX_TYPE, targetCtx);
			params.put(Constants.ID, id);
			getSqlMapClientTemplate().delete("removeSubscriberByCtxId", params);		
	}

	@SuppressWarnings("unchecked")
	public List<Subscriber> getSubscribersByCtxId(Long target, String targetCtx, Boolean isGrp) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put(Constants.CTX_ID, target);
			params.put(Constants.CTX_TYPE, targetCtx);
			if (isGrp) {
				return  getSqlMapClientTemplate().queryForList("getSubscriberGroupsByCtxId", params);
			}
			else {
				return getSqlMapClientTemplate().queryForList("getSubscribersByCtxId", params);
			}	
	}

	public boolean isSubscriberExistInCtx(String principal, Subscriber subscriber) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(Constants.CTX_ID, subscriber.getCtxId());
		params.put(Constants.CTX_TYPE, subscriber.getCtxType());
		params.put(Constants.PRINCIPAL, principal);
		params.put(Constants.ISGRP, Integer.toString(subscriber.getIsGroup()));
		Integer  rt = (Integer)getSqlMapClientTemplate().queryForObject("isSubscriberExistInCtx", params);
		if (rt == null) return false;
		return  (rt.intValue() > 0);
	}

	public boolean hasSubscribers(Long target, String targetCtx) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(Constants.CTX_ID, target);
		params.put(Constants.CTX_TYPE, targetCtx);
		Integer  rt = (Integer)getSqlMapClientTemplate().queryForObject("hasSubscribersInCtx", params);
		if (rt == null) { return false;	 }
		return  (rt.intValue() > 0);
	}

}
