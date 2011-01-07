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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.portlet.mvc.AbstractController;
import org.uhp.portlets.news.NewsConstants;
import org.uhp.portlets.news.web.support.Constants;
import org.uhp.portlets.news.service.ItemManager;
import org.esco.portlets.news.services.UserManager;

public class ItemIncrementController extends AbstractController implements InitializingBean {
	@Autowired private UserManager um=null;
	@Autowired private ItemManager im=null;
	 private static final Log LOGGER = LogFactory.getLog(ItemIncrementController.class);
	   
	    public void afterPropertiesSet() throws Exception {
	        if ((this.im == null) || (this.um == null))
	            throw new IllegalArgumentException("A ItemManager and a userManager are required");
	    }
	 
		@Override
	protected void handleActionRequestInternal(ActionRequest request, ActionResponse response) throws Exception {

	    Long itemId = Long.valueOf(request.getParameter(Constants.ATT_ID));
	    String topicId = request.getParameter(Constants.ATT_TOPIC_ID);
	  
		 if(!this.um.isUserAdminInCtx(Long.valueOf(topicId), NewsConstants.CTX_T, request.getRemoteUser())) {
	     		LOGGER.warn("ItemIncrementController :: user " + request.getRemoteUser() + " has no role admin");
	 			throw new PortletSecurityException(
	 			       getMessageSourceAccessor().getMessage("exception.notAuthorized.action"));  
	    	 }	  
	    int i = Integer.parseInt(request.getParameter(Constants.ATT_INC));
	    if (i == 1) {
	        this.im.updateItemOrderingInTopic(itemId, Long.valueOf(topicId), true);  
	    } else if (i == -1) {
	        this.im.updateItemOrderingInTopic(itemId, Long.valueOf(topicId), false); 
	    } else if (i == 2) {
            this.im.updateItemOrderingToFirstOrLastInTopic(itemId, Long.valueOf(topicId), true); 
        } else {
            this.im.updateItemOrderingToFirstOrLastInTopic(itemId, Long.valueOf(topicId), false); 
        }
		response.setRenderParameter(Constants.ACT, Constants.ACT_VIEW_TOPIC);
		response.setRenderParameter(Constants.ATT_TOPIC_ID, topicId);
		response.setRenderParameter(Constants.ATT_STATUS, Constants.ATT_T);		
	}

}
