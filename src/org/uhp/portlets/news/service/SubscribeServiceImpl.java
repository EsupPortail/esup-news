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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esco.portlets.news.services.group.GroupService;
import org.esupportail.portal.ws.client.PortalGroup;
import org.esupportail.portal.ws.client.exceptions.PortalErrorException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.uhp.portlets.news.dao.SubscriberDao;
import org.uhp.portlets.news.domain.SubscribeType;
import org.uhp.portlets.news.domain.Subscriber;
import org.uhp.portlets.news.service.exception.ResourceNotFoundException;

@Service("subscribeService")
public class SubscribeServiceImpl implements SubscribeService, InitializingBean {

	private static final Log LOG = LogFactory.getLog(SubscribeServiceImpl.class);
	@Autowired private SubscriberDao subscriberDao;
	@Autowired
	private GroupService groupService;


	@Transactional(propagation = Propagation.REQUIRED)
	public void addSubscriber(final Subscriber subscriber) {
		this.subscriberDao.insertSubscriber(subscriber);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void addSubscribers(final String[] subKey, final Subscriber subscriber) {
		for(String sk : subKey) {
			if(this.subscriberDao.isSubscriberExistInCtx(sk, subscriber)) {
				if(LOG.isWarnEnabled()) {
					LOG.warn("This principal exists in the context, subscriber insertion is ignored...");
				}
			}
			else {
				subscriber.setPrincipal(sk);
				addSubscriber(subscriber);
			}
		}
	}

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	private List<Subscriber> getSubscribersByCtxIdSubType(final Long target, final String targetCtx, final String subType) {
		return this.subscriberDao.getSubscribers(target, targetCtx, subType);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void removeAllSubscribersByCtxId(final Long target, final String targetCtx) {
		this.subscriberDao.deleteAllSubscribersByCtxId(target, targetCtx);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void removeAllSubscribersByCtxIdForSubType(final Long target, final String targetCtx, final String subType) {
		this.subscriberDao.deleteAllSubscribersByCtxIdForSubType(target, targetCtx, subType);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void removeSubscriberByCtxId(final Long id, final Long target, final String targetCtx) {
		this.subscriberDao.deleteSubscriberByCtxId(id, target,  targetCtx);
	}

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public Map<String, List<Subscriber>> getSubscribersByCtxId(final Long target, final String targetCtx) {
		Map<String, List<Subscriber>> subLists = new HashMap<String, List<Subscriber>>();
		final SubscribeType[] subTypes = SubscribeType.values();
		for (SubscribeType s : subTypes ) {
			final List<Subscriber> list = getSubscribersByCtxIdSubType(target, targetCtx, s.toString());
			if (list != null) {
				for (Subscriber sb : list) {
					getPortalGroupById(sb);
				}
			}
			subLists.put(s.toString(), list);
		}
		return subLists;
	}

	public boolean isSubscriberExistInCtx(final String sk, final Subscriber subscriber) {
		return this.subscriberDao.isSubscriberExistInCtx(sk, subscriber);
	}


	private void getPortalGroupById(final Subscriber sb) throws ResourceNotFoundException {
		try {
			PortalGroup pg = this.getGroupService().getPortalGroupById(sb.getPrincipal());
			if (pg != null) {
				sb.setDisplayName(pg.getName());
			} else {
				sb.setDisplayName(null);
			}
		} catch (PortalErrorException e) {
			LOG.warn("SubscribeService::getSubscribersByCtxId():: principal=" + sb.getPrincipal()
					+ " PortalErrorException " + e.getMessage());
			throw new ResourceNotFoundException("Is web service for uportal groups correctly installed? "
					+ e.getMessage());
		}
	}

	/**
	 * Getter du membre groupService.
	 * @return <code>GroupService</code> le membre groupService.
	 */
	public GroupService getGroupService() {
		return groupService;
	}

	/**
	 * Setter du membre groupService.
	 * @param groupService la nouvelle valeur du membre groupService.
	 */
	public void setGroupService(final GroupService groupService) {
		this.groupService = groupService;
	}

	/**
	 * @throws Exception
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() throws Exception {
	   Assert.notNull(this.groupService, "The property groupService in class "
			   + this.getClass().getSimpleName() + " must not be null.");
	   Assert.notNull(this.subscriberDao, "The property subscriberDao in class "
			   + this.getClass().getSimpleName() + " must not be null.");
	}
}
