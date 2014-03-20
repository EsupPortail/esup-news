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

import java.util.ArrayList;
import java.util.List;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.mvc.AbstractController;
import org.uhp.portlets.news.NewsConstants;
import org.uhp.portlets.news.web.support.Constants;
import org.uhp.portlets.news.domain.Category;
import org.uhp.portlets.news.domain.RolePerm;
import org.uhp.portlets.news.domain.Topic;
import org.uhp.portlets.news.service.CategoryManager;
import org.uhp.portlets.news.service.TopicManager;
import org.esco.portlets.news.services.EntityManager;
import org.esco.portlets.news.services.PermissionManager;
import org.esco.portlets.news.services.UserManager;

/**
 * modified by  GIP RECIA - Julien Gribonvald
 * 4 mai 2012
 */
public class ShowTopicSettingController extends AbstractController implements InitializingBean {	
	/** */
    @Autowired private TopicManager tm;
    /** */
    @Autowired private CategoryManager cm;
    /** */
    @Autowired private PermissionManager pm;
    /** */
    @Autowired private UserManager um;
    /** Manager of an Entity. */
    @Autowired
    private EntityManager em;

    /**
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    public void afterPropertiesSet() throws Exception {
        if ((this.tm == null) || (this.cm == null) || (this.um == null) || (this.pm == null) || (this.em == null))
            throw new IllegalArgumentException("A TopicManager, a categoryManager, a userManager and a PermissionManager are required");
    }

    /**
     * @see org.springframework.web.portlet.mvc.AbstractController#handleRenderRequestInternal(javax.portlet.RenderRequest, javax.portlet.RenderResponse)
     */
    @Override
    protected ModelAndView handleRenderRequestInternal(RenderRequest request, RenderResponse response) throws Exception {	
        Topic topic=this.tm.getTopicById(Long.valueOf(request.getParameter(Constants.ATT_TOPIC_ID)));
        if(topic == null) {
            throw new IllegalArgumentException("Topic does not exist.");
        }
        List<String> usersUid = new ArrayList<String>();
        usersUid.add(topic.getCreatedBy());        
        ModelAndView mav = new ModelAndView(Constants.ACT_VIEW_T_SETTING);
        mav.addObject(Constants.OBJ_TOPIC, topic);
        Category c = this.cm.getCategoryById(topic.getCategoryId());
        usersUid.add(c.getCreatedBy());
        mav.addObject(Constants.OBJ_CATEGORY, c);
        mav.addObject(Constants.OBJ_ENTITY, this.em.getEntityById(c.getEntityId()));
        mav.addObject(Constants.ATT_PM, RolePerm.valueOf(this.pm.getRoleInCtx(topic.getTopicId(), NewsConstants.CTX_T)).getMask());
        mav.addObject(Constants.ATT_USER_LIST, this.um.getUsersByListUid(usersUid));
        return mav;			
    }

}
