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
import org.esco.portlets.news.services.PermissionManager;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.portlet.mvc.AbstractController;
import org.uhp.portlets.news.NewsConstants;
import org.uhp.portlets.news.service.SubscribeService;
import org.uhp.portlets.news.web.support.Constants;

/**
 * modified by GIP RECIA - Julien Gribonvald
 * 4 mai 2012
 */
public class SubscriberDeleteController extends AbstractController implements InitializingBean {

	/** */
	private static final Log LOGGER = LogFactory.getLog(SubscriberDeleteController.class);

	/** */
	@Autowired private SubscribeService subService = null;
	/** */
	@Autowired private PermissionManager pm = null;

	/**
	 * Contructor of the object SubscriberDeleteController.java.
	 */
	public SubscriberDeleteController() {
		super();
	}

	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() throws Exception {
		 if ((this.subService == null) || (this.pm == null))
	            throw new IllegalArgumentException("A SubscribeService and a PermissionManager are required");
	}

	/**
	 * @see org.springframework.web.portlet.mvc.AbstractController#handleActionRequestInternal(javax.portlet.ActionRequest, javax.portlet.ActionResponse)
	 */
	@Override
	protected void handleActionRequestInternal(ActionRequest request, ActionResponse response) throws Exception {
        String ctxId = request.getParameter(Constants.ATT_CTX_ID);
        String ctx = request.getParameter(Constants.ATT_CTX);
        Long id = Long.valueOf(request.getParameter(Constants.CMD_SUBSCRIBER));

        if (!this.pm.isAdminInCtx(Long.valueOf(ctxId), ctx)) {
     		LOGGER.warn("SubcribeController:: user " + request.getRemoteUser() + " has no role admin");
 			throw new PortletSecurityException(
 			       getMessageSourceAccessor().getMessage("exception.notAuthorized.action"));
    	 }
        this.subService.removeSubscriberByCtxId(id, Long.valueOf(ctxId), ctx);
        if (NewsConstants.CTX_E.equalsIgnoreCase(ctx)) {
            response.setRenderParameter(Constants.ACT, Constants.ACT_VIEW_AUDIENCE + NewsConstants.CTX_E);
        } else if (NewsConstants.CTX_C.equalsIgnoreCase(ctx)) {
		   response.setRenderParameter(Constants.ACT, Constants.ACT_VIEW_AUDIENCE + NewsConstants.CTX_C);
	    } else if (NewsConstants.CTX_T.equalsIgnoreCase(ctx)) {
	    	response.setRenderParameter(Constants.ACT, Constants.ACT_VIEW_AUDIENCE + NewsConstants.CTX_T);
	    } else {
	        throw new IllegalArgumentException("The context " + ctx  + " isn't unknown !");
	    }
		response.setRenderParameter(Constants.ATT_CTX_ID, ctxId);
	}
}
