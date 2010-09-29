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

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.portlet.mvc.AbstractController;
import org.uhp.portlets.news.NewsConstants;
import org.uhp.portlets.news.web.support.Constants;
import org.uhp.portlets.news.domain.Topic;
import org.uhp.portlets.news.service.TopicManager;
import org.esco.portlets.news.services.UserManager;

public class TopicIncrementController extends AbstractController implements InitializingBean {

	@Autowired private TopicManager tm=null;
	@Autowired private UserManager um=null;
    
    public void afterPropertiesSet() throws Exception {
        if ((this.tm == null) || (this.um == null))
            throw new IllegalArgumentException("A topicManager and a userManager are required");
    }

	@Override
	protected void handleActionRequestInternal(final ActionRequest request, final ActionResponse response) throws Exception {
	  
	    Long id = Long.valueOf(request.getParameter(Constants.ATT_TOPIC_ID));
		Long categoryId = tm.getTopicById(id).getCategoryId();

		if (!this.um.isUserAdminInCtx(categoryId, NewsConstants.CTX_C, request.getRemoteUser())) {     		
		    throw new PortletSecurityException(
		            getMessageSourceAccessor().getMessage("exception.notAuthorized.action"));  
		}
		
		int i = Integer.parseInt(request.getParameter(Constants.ATT_INC));
        if (i == 1) {
            this.tm.updateTopicOrdering(id, categoryId, true);  
        } else if (i == -1) {
            this.tm.updateTopicOrdering(id, categoryId, false); 
        } else if (i == 2) {
            this.tm.updateTopicOrderingToFirstOrLast(id, categoryId, true); 
        } else {
            this.tm.updateTopicOrderingToFirstOrLast(id, categoryId, false); 
        }

		response.setRenderParameter(Constants.ACT, Constants.ACT_VIEW_CAT);
		response.setRenderParameter(Constants.ATT_CAT_ID, String.valueOf(categoryId));
	}                                         

}
