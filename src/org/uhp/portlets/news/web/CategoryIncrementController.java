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

import org.esco.portlets.news.services.EntityManager;
import org.esco.portlets.news.services.UserManager;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.portlet.mvc.AbstractController;
import org.uhp.portlets.news.NewsConstants;
import org.uhp.portlets.news.service.CategoryManager;
import org.uhp.portlets.news.web.support.Constants;

public class CategoryIncrementController extends AbstractController implements InitializingBean {

    @Autowired private CategoryManager cm=null;
    @Autowired private UserManager um=null;
    /** Manager of an Entity. */
    @Autowired
    private EntityManager em;

    public void afterPropertiesSet() throws Exception {
        if ((this.cm == null) || (this.um == null) || (this.em == null))
            throw new IllegalArgumentException("A categoryManager and a userManager are required");
    }

    @Override
    protected void handleActionRequestInternal(final ActionRequest request, final ActionResponse response) throws Exception {

        Long id = Long.valueOf(request.getParameter(Constants.ATT_CAT_ID));
        Long entityId = this.cm.getCategoryById(id).getEntityId();

        if (!this.um.isUserAdminInCtx(entityId, NewsConstants.CTX_E, request.getRemoteUser())) {     		
            throw new PortletSecurityException(
                    getMessageSourceAccessor().getMessage("exception.notAuthorized.action"));  
        }

        int i = Integer.parseInt(request.getParameter(Constants.ATT_INC));
        if (i == 1) {
            this.cm.updateCategoryOrdering(id, entityId, true);  
        } else if (i == -1) {
            this.cm.updateCategoryOrdering(id, entityId, false); 
        } else if (i == 2) {
            this.cm.updateCategoryOrderingToFirstOrLast(id, entityId, true); 
        } else {
            this.cm.updateCategoryOrderingToFirstOrLast(id, entityId, false); 
        }
        response.setRenderParameter(Constants.ATT_ENTITY_ID, String.valueOf(entityId));
        response.setRenderParameter(Constants.ACT, Constants.ACT_VIEW_ENTITY);

    }
    
    

}