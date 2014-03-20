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
import org.esco.portlets.news.services.RoleManager;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.portlet.mvc.AbstractController;
import org.uhp.portlets.news.domain.RolePerm;
import org.uhp.portlets.news.web.support.Constants;

/**
 * modified by GIP RECIA - Gribonvald Julien.
 * 19 juil. 2010
 */
public class UserRoleDeleteController extends AbstractController implements InitializingBean {

    /** */
    private static final Log LOG = LogFactory.getLog(UserRoleDeleteController.class);
	/** */
	@Autowired private RoleManager rm;
	/** */
	@Autowired private PermissionManager pm;
	
	/**
     * Constructeur de l'objet UserRoleDeleteController.java.
     */
    public UserRoleDeleteController() {
        super();
    }

    /**
	 * @throws Exception
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() throws Exception {
		 if (this.rm == null) {
		     throw new IllegalArgumentException("A roleManager is required");
		 }
	}

	/**
	 * @param request
	 * @param response
	 * @throws Exception
	 * @see org.springframework.web.portlet.mvc.AbstractController#
	 * handleActionRequestInternal(javax.portlet.ActionRequest, javax.portlet.ActionResponse)
	 */
	@Override
	protected void handleActionRequestInternal(final ActionRequest request, final ActionResponse response) 
	throws Exception {
        final Long ctxId = Long.valueOf(request.getParameter(Constants.ATT_CTX_ID));
        final String ctx = request.getParameter(Constants.ATT_CTX);
 
        if (!this.pm.isAdminInCtx(ctxId, ctx)) {
     		LOG.warn("UserRoleDeleteController:: user " + request.getRemoteUser() + " has no role admin");
 			throw new PortletSecurityException(
 			        getMessageSourceAccessor().getMessage("exception.notAuthorized.action"));  
    	}
        String role = this.rm.getRoleOfEntityInCtx(request.getParameter(Constants.ATT_USER_ID),
        		"1".equals(request.getParameter(Constants.ATT_IS_GRP)), ctxId, ctx);
        this.rm.removeRoleForEntityInCtx(request.getParameter(Constants.ATT_USER_ID),
        		"1".equals(request.getParameter(Constants.ATT_IS_GRP)), ctxId, ctx);
        String role2 = this.rm.getRoleOfEntityInCtx(request.getParameter(Constants.ATT_USER_ID),
        		"1".equals(request.getParameter(Constants.ATT_IS_GRP)), ctxId, ctx);
		response.setRenderParameter(Constants.ACT, Constants.ACT_VIEW_PERM + ctx);
		response.setRenderParameter(Constants.ATT_CTX_ID, String.valueOf(ctxId));
		if (role2 != null && RolePerm.valueOf(role2).getMask() < RolePerm.valueOf(role).getMask()) {
		    response.setRenderParameter("msg", "news.user.role.notDelButUpdated");
		} else if (role2 != null) {
		    response.setRenderParameter("msg", "news.user.role.notdeleted");
		} else {
		    response.setRenderParameter("msg", "news.user.role.deleted");
		}
	}
	
	
	
}
