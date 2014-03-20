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
import org.esco.portlets.news.services.PermissionManager;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.portlet.mvc.AbstractController;
import org.uhp.portlets.news.NewsConstants;
import org.uhp.portlets.news.service.CategoryManager;
import org.uhp.portlets.news.web.support.Constants;

/**
 * Modified by GIP RECIA - Julien Gribonvald
 * 4 mai 2012
 */
public class CategoryIncrementController extends AbstractController implements InitializingBean {

	/** */
    @Autowired private CategoryManager cm;
    /** */
    @Autowired private PermissionManager pm;
    /** Manager of an Entity. */
    @Autowired
    private EntityManager em;

    /**
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    public void afterPropertiesSet() throws Exception {
        if ((this.cm == null) || (this.pm == null) || (this.em == null))
            throw new IllegalArgumentException("A CategoryManager, an EntityManager and a PermissionManager are required");
    }

    /**
     * @see org.springframework.web.portlet.mvc.AbstractController#handleActionRequestInternal(javax.portlet.ActionRequest, javax.portlet.ActionResponse)
     */
    @Override
    protected void handleActionRequestInternal(final ActionRequest request, final ActionResponse response) throws Exception {

        Long id = Long.valueOf(request.getParameter(Constants.ATT_CAT_ID));
        Long entityId = this.cm.getCategoryById(id).getEntityId();

        if (!this.pm.isAdminInCtx(entityId, NewsConstants.CTX_E)) {
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