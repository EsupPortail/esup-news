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
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.portlet.mvc.AbstractController;
import org.esco.portlets.news.services.UserManager;
import org.uhp.portlets.news.NewsConstants;
import org.uhp.portlets.news.web.support.Constants;

public class UserActivateController extends AbstractController implements InitializingBean {      
	@Autowired private UserManager um=null;

	private static final Log LOGGER = LogFactory.getLog(UserActivateController.class);

	public void afterPropertiesSet() throws Exception {
		if (this.um == null)
			throw new IllegalArgumentException("A UserManager is required");

	}
	
	@Override
	protected void handleActionRequestInternal(final ActionRequest request, final ActionResponse response) throws Exception {

		if (!this.um.isSuperAdmin(request.getRemoteUser())) {
			LOGGER.warn("UserActivateController:: user " + request.getRemoteUser() + " has no superAdmin role");
 			throw new PortletSecurityException(
 			       getMessageSourceAccessor().getMessage("exception.notAuthorized.action"));  
    	}
		final String userId = StringUtils.defaultIfEmpty(request.getParameter(Constants.ATT_USER_ID), null);

		String status  = (Constants.ATT_T.equals(this.um.findUserByUid(userId).getEnabled())) ? Constants.ATT_F: Constants.ATT_T;
		this.um.updateUserStatus(userId, status);					
		response.setRenderParameter(Constants.ACT,Constants.ACT_VIEW_M);		
	}

}
