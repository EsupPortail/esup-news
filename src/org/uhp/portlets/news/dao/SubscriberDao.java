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
package org.uhp.portlets.news.dao;

import java.util.List;

import org.uhp.portlets.news.domain.Subscriber;


public interface SubscriberDao {
	public void insertSubscriber(Subscriber subscriber);
	
	public List<Subscriber> getSubscribersByCtxId(Long target,  String targetCtx,  Boolean isGrp);
	
	public List<Subscriber> getSubscribers(Long target, String targetCtx, String subType, Boolean isGrp);
	
	public List<Subscriber> getSubscribers(Long target, String targetCtx, String subType);

	public boolean isSubscriberExistInCtx(String principal, Subscriber subscriber);
	
	public void deleteSubscriberByCtxId(Long id, Long target, String targetCtx);
	
	public void deleteAllSubscribersByCtxId(Long target, String targetCtx);
	
	public void deleteAllSubscribersByCtxIdForSubType(Long target, String targetCtx, String subType);
	
	public boolean hasSubscribers(Long target, String targetCtx);
}
