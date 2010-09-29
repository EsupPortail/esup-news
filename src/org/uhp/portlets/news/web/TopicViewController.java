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
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.mvc.AbstractController;

import org.uhp.portlets.news.NewsConstants;
import org.uhp.portlets.news.util.HostUtils;
import org.uhp.portlets.news.web.support.Constants;
import org.uhp.portlets.news.domain.Category;
import org.uhp.portlets.news.domain.Item;
import org.uhp.portlets.news.domain.RolePerm;
import org.uhp.portlets.news.domain.Topic;
import org.uhp.portlets.news.service.CategoryManager;
import org.uhp.portlets.news.service.ItemManager;
import org.uhp.portlets.news.service.TopicManager;
import org.esco.portlets.news.services.EntityManager;
import org.esco.portlets.news.services.UserManager;

public class TopicViewController extends AbstractController
implements InitializingBean {
    @Autowired private TopicManager tm = null;
    @Autowired private ItemManager im = null;
    @Autowired private CategoryManager cm = null;
    @Autowired private UserManager um = null;
    /** Manager of an Entity. */
    @Autowired
    private EntityManager em;
    private String  nbItemsToShow = null;
    private static final int DEFAULT_NB = 10;

    public void afterPropertiesSet() throws Exception {
        if ((this.tm == null) || (this.im == null) || (this.cm == null)  || (this.um == null) || (this.em == null))
            throw new IllegalArgumentException(
            "A TopicManager , ItemManager, a userManager, a categoryManager and a entityManager are required");
        if(nbItemsToShow == null) nbItemsToShow = Integer.toString(DEFAULT_NB);
    }

    @Override
    protected ModelAndView handleRenderRequestInternal(final RenderRequest request, final RenderResponse response) throws Exception {
        final Long id = Long.valueOf(request.getParameter(Constants.ATT_TOPIC_ID));
        final String status = request.getParameter(Constants.ATT_STATUS);	
        List<Item> itemList = null;
        Topic topic = this.tm.getTopicById(id);
        if (topic != null) {

            switch (Integer.parseInt(status)) {
            case 0 :
                itemList = this.im.getPendingItemListByTopic(id);

                break;
            case 1 :
                itemList = this.im.getValidatedItemListByTopic(id);
                break;
            case 2 :
                itemList = this.im.getArchivedItemListByTopic(id);
                break;
            case 3 :
                itemList = this.im.getScheduledItemListByTopic(id);
                break;
            default :
                itemList = this.im.getAllItemListByTopic(id);
            }

            List<String> usersUid = new ArrayList<String>();
            for (Item i : itemList) {
                usersUid.add(i.getLastUpdatedBy());
                usersUid.add(i.getPostedBy());
            }
            
            Category category = this.cm.getCategoryById(topic.getCategoryId());

            ModelAndView mav = new ModelAndView(Constants.ACT_VIEW_TOPIC);
            mav.addObject(Constants.OBJ_TOPIC, topic);
            mav.addObject("s", status);
            mav.addObject(Constants.ATT_I_LIST, itemList); 
            mav.addObject(Constants.OBJ_CATEGORY, category);
            mav.addObject(Constants.OBJ_ENTITY, this.em.getEntityById(category.getEntityId()));
            mav.addObject(Constants.ATT_PORTAL_URL, HostUtils.getHostUrl(request));
            mav.addObject(Constants.ATT_USER_ID, request.getRemoteUser());
            mav.addObject(Constants.ATT_NB_ITEM_TO_SHOW, this.nbItemsToShow);       
            mav.addObject(Constants.ATT_PM, RolePerm.valueOf(
                    this.um.getUserRoleInCtx(topic.getTopicId(), NewsConstants.CTX_T, request.getRemoteUser())).getMask());
            mav.addObject(Constants.ATT_USER_LIST, this.um.getUsersByListUid(usersUid));
            return mav;
        } else {
            throw new ObjectRetrievalFailureException(Topic.class, id);
        }

    }

    public String getNbItemsToShow() {
        return nbItemsToShow;
    }

    public void setNbItemsToShow(final String nbItemsToShow) {
        this.nbItemsToShow = nbItemsToShow;
    }

}
