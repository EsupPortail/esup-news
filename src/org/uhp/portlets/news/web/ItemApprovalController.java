package org.uhp.portlets.news.web;

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

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletSecurityException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esco.portlets.news.services.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.portlet.mvc.AbstractController;
import org.uhp.portlets.news.service.ItemManager;
import org.uhp.portlets.news.web.support.Constants;

public class ItemApprovalController extends AbstractController {
	@Autowired private ItemManager im=null;
	@Autowired private UserManager um=null;


	private static final Log LOGGER = LogFactory.getLog(ItemApprovalController.class);

	public void handleActionRequestInternal(ActionRequest request, ActionResponse response) throws Exception {
		final Long itemId = Long.valueOf(request.getParameter(Constants.ATT_ID));
		final String topicId = StringUtils.defaultIfEmpty(request.getParameter(Constants.ATT_TOPIC_ID), null);
		if (!this.um.canValidate(request.getRemoteUser(), this.im.getItemById(itemId))) {
			LOGGER.warn("ItemApprovalController:: user " + request.getRemoteUser() + " has no role admin");
			throw new PortletSecurityException(
			        getMessageSourceAccessor().getMessage("exception.notAuthorized.action"));  
		}
	
		this.im.updateItemStatus(itemId);
		if (topicId != null) {
		    LOGGER.debug("ItemApprovalController::handleActionRequestInternal:topicId=" + topicId 
		            + " itemId=" + itemId + " view=" + Constants.ACT_VIEW_TOPIC);
		    response.setRenderParameter(Constants.ACT, Constants.ACT_VIEW_TOPIC);
		    response.setRenderParameter(Constants.ATT_TOPIC_ID, topicId);
		    response.setRenderParameter(Constants.ATT_STATUS, 
		            this.im.getItemById(itemId).getStatus().endsWith("1")? "0" : "1");
		} else {
			response.setRenderParameter(Constants.ACT, Constants.ACT_VIEW_ITEM);
			response.setRenderParameter(Constants.ATT_ID, String.valueOf(itemId));
		}
	}

}
