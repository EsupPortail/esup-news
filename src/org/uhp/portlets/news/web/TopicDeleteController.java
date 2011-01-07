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
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.mvc.AbstractController;
import org.uhp.portlets.news.NewsConstants;
import org.uhp.portlets.news.domain.Category;
import org.uhp.portlets.news.domain.RolePerm;
import org.uhp.portlets.news.service.CategoryManager;
import org.uhp.portlets.news.service.TopicManager;
import org.esco.portlets.news.domain.Entity;
import org.esco.portlets.news.services.EntityManager;
import org.esco.portlets.news.services.UserManager;
import org.uhp.portlets.news.web.support.Constants;

public class TopicDeleteController extends AbstractController implements InitializingBean {

	@Autowired private TopicManager tm=null;
	@Autowired private CategoryManager cm=null;
	@Autowired private UserManager um=null;
	@Autowired private EntityManager em;

	private static final Log LOGGER = LogFactory.getLog(TopicDeleteController.class);
	private boolean deleted;
	private Long catId;
	
    public void afterPropertiesSet() throws Exception {
        if ((this.tm == null) || (this.cm == null) || (this.um == null) || (this.em == null))
            throw new IllegalArgumentException("A TopicManager, a categoryManager, an EntityManager and a userManager are required");
    }
    
	@Override
	protected void handleActionRequestInternal(ActionRequest request, ActionResponse response) throws Exception {
       Long topicId = Long.valueOf(request.getParameter(Constants.ATT_TOPIC_ID));
         catId = this.tm.getTopicById(topicId).getCategoryId();
         
         if(!this.um.isUserAdminInCtx(catId, NewsConstants.CTX_C, request.getRemoteUser())) {
	     		LOGGER.warn("TopicDeleteController:: user " + request.getRemoteUser() + " has no role admin");
	 			throw new PortletSecurityException(
	 			       getMessageSourceAccessor().getMessage("exception.notAuthorized.action"));  
	    	 }
         this.deleted=false;
		if (!this.tm.deleteTopic(topicId)) {
			if (LOGGER.isWarnEnabled()) {
				LOGGER.warn("Warn : TopicDeleteController:: Not Empty topic : topicid [" + topicId + "] can not be deleted");
			}		
		}
		else { 
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("DeleteTopic:: topicid [" + topicId + "] is removed");
			}
		    this.deleted = true;
		}
								
	}

	@Override
	 protected ModelAndView handleRenderRequestInternal(RenderRequest request,
		      RenderResponse response) throws Exception {
		 if(this.deleted==true) {
	 	
					ModelAndView mav = new ModelAndView(Constants.ACT_VIEW_CAT);
					Category category=this.cm.getCategoryById(this.catId);
					Entity entity = this.em.getEntityById(category.getEntityId());
					mav.addObject(Constants.OBJ_ENTITY, entity);
			        mav.addObject(Constants.OBJ_CATEGORY, category);
			        mav.addObject(Constants.ATT_T_LIST, this.tm.getTopicListForCategoryByUser(this.catId, request.getRemoteUser()));
				    mav.addObject(Constants.ATT_PM, RolePerm.valueOf(this.um.getUserRoleInCtx(category.getCategoryId(), NewsConstants.CTX_C, request.getRemoteUser())).getMask());
				    return mav;
		   
		 } 
		 else {
			 return new ModelAndView("Errors", "message", getMessageSourceAccessor().getMessage("news.alert.topicNotEmpty"));  
		 }
	
		  }
	 
}
