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
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.esco.portlets.news.services.PermissionManager;
import org.esco.portlets.news.services.UserManager;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.mvc.AbstractController;
import org.uhp.portlets.news.web.support.Constants;

/**
 * modified by GIP RECIA - Julien Gribonvald
 * 10 mai 2012
 */
public class UserDeleteController extends AbstractController implements InitializingBean {

	/** */
    private static final int DEFAULT_NB = 10;
    /** */
    private static final String MSG_KEY = "news.user.deleted";
    /** */
    @Autowired private UserManager um;
    /** */
    @Autowired
    private PermissionManager pm;
    /** */
    private String msgKey = "";
    /** */
    private  String userId;
    /** */
    private boolean deleted;
    /** */
    private int  nbUsersToShow;

    /**
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(this.um, "A UserManager is required.");
        if (this.nbUsersToShow <= 0) {
            this.nbUsersToShow = DEFAULT_NB;
        }
    }

    /**
     * @see org.springframework.web.portlet.mvc.AbstractController#handleActionRequestInternal(javax.portlet.ActionRequest, javax.portlet.ActionResponse)
     */
    @Override
    protected void handleActionRequestInternal(final ActionRequest request, final ActionResponse response)
    throws Exception {
        userId = request.getParameter(Constants.ATT_USER_ID);
        this.deleted = false;
        if (!this.pm.isSuperAdmin()) {
            msgKey = "news.alert.superUserOnly";
        } else  {
            this.um.deleteUser(userId);
            this.deleted = true;
        }
    }

    /**
     * @see org.springframework.web.portlet.mvc.AbstractController#handleRenderRequestInternal(javax.portlet.RenderRequest, javax.portlet.RenderResponse)
     */
    @Override
    protected ModelAndView handleRenderRequestInternal(final RenderRequest request, final RenderResponse response)
    throws Exception {
        if (this.deleted) {
            ModelAndView mav = new ModelAndView(Constants.ACT_VIEW_M);
            mav.addObject(Constants.ATT_USER_LIST, this.um.getAllUsers());
            mav.addObject(Constants.ATT_NB_ITEM_TO_SHOW, this.nbUsersToShow);
            mav.addObject("msg", getMessageSourceAccessor().getMessage(MSG_KEY));
            return mav;

        }
        return new ModelAndView("Errors", "message", getMessageSourceAccessor().getMessage(msgKey));
    }

}
