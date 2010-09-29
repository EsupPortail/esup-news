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
import org.cmis.portlets.news.services.AttachmentManager;
import org.esco.portlets.news.services.UserManager;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.portlet.bind.PortletRequestUtils;
import org.springframework.web.portlet.mvc.AbstractController;
import org.uhp.portlets.news.domain.Category;
import org.uhp.portlets.news.domain.Item;
import org.uhp.portlets.news.domain.Topic;
import org.uhp.portlets.news.service.CategoryManager;
import org.uhp.portlets.news.service.ItemManager;
import org.uhp.portlets.news.web.support.Constants;

public class ItemDeleteController extends AbstractController implements InitializingBean {

    @Autowired
    private ItemManager im = null;
    @Autowired
    private UserManager um = null;
    @Autowired
    private CategoryManager cm;
    @Autowired
    private AttachmentManager am;

    private static final Log log = LogFactory.getLog(ItemDeleteController.class);

    public void afterPropertiesSet() throws Exception {
        if ((this.im == null) || (this.um == null))
            throw new IllegalArgumentException("A ItemManager and a userManager are required");
    }

    @Override
    public void handleActionRequestInternal(final ActionRequest request, final ActionResponse response) throws Exception {
        final Long itemId = PortletRequestUtils.getLongParameter(request, Constants.ATT_ID);
        //final Long topicId = Long.valueOf(request.getParameter(Constants.ATT_TOPIC_ID));
        final String topicId = StringUtils.defaultIfEmpty(request.getParameter(Constants.ATT_TOPIC_ID), null);
        final String fromStatus = request.getParameter(Constants.ATT_STATUS);

        if (!this.um.canEditItem(request.getRemoteUser(), this.im.getItemById(itemId))) {
            log.warn("ItemDelete: user " + request.getRemoteUser() + " has no permission to delete this item [" + itemId + "]");
            throw new PortletSecurityException(
                    getMessageSourceAccessor().getMessage("exception.notAuthorized.action"));
        }
        final boolean deleteAll = (Constants.ATT_T.equals(request.getParameter("all"))) ? true : false;

        // retrieve entity
        Item item = im.getItemById(itemId);
        Category category = cm.getCategoryById(item.getCategoryId());
        Long entityId = category.getEntityId();

        this.am.deleteItemAttachments(itemId, entityId);

        if (deleteAll) {
            if (log.isDebugEnabled()) {
                log.debug("ItemDelete: delete item (itemid=" + itemId + ") for all topics");
            }
            this.im.deleteItemForAllTopics(itemId);
            this.im.deleteItem(itemId);
        } else {
            if (log.isDebugEnabled()) {
                log.debug("ItemDelete:: delete item (itemid=" + itemId + ") for topic (topicId=" + topicId + ")");
            }
            if (topicId != null) {
            	this.im.deleteItemForTopic(itemId, Long.valueOf(topicId));
            }
        }
        
        if (topicId != null) {
        	response.setRenderParameter(Constants.ACT, Constants.ACT_VIEW_TOPIC);
        	response.setRenderParameter(Constants.ATT_TOPIC_ID, String.valueOf(topicId));
        	response.setRenderParameter(Constants.ATT_STATUS, fromStatus);
        } else {
        	response.setRenderParameter(Constants.ACT, Constants.ACT_VIEW_CAT);
    		response.setRenderParameter(Constants.ATT_CAT_ID, String.valueOf(item.getCategoryId()));
        }

    }

}
