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
 * 4 mai 2012
 */
public class ManagersViewController extends AbstractController implements InitializingBean {

	/** */
	@Autowired private UserManager um;
	/** */
	@Autowired private PermissionManager pm = null;
	/** */
	private int  nbUsersToShow;
	/** */
	private static final int DEFAULT_NB= 10;

	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() throws Exception {
	    Assert.notNull(this.um, "A UserManager is required.");
		if (this.nbUsersToShow <= 0) {
		    this.nbUsersToShow = DEFAULT_NB;
		}
	}

	/**
	 * @see org.springframework.web.portlet.mvc.AbstractController#handleRenderRequestInternal(javax.portlet.RenderRequest, javax.portlet.RenderResponse)
	 */
	@Override
	protected ModelAndView handleRenderRequestInternal(final RenderRequest request,
			final RenderResponse response) throws Exception {
		if (!this.pm.isSuperAdmin()) {
			return new ModelAndView(Constants.ACT_VIEW_NOT_AUTH, Constants.MSG_ERROR, getMessageSourceAccessor().getMessage("news.alert.superUserOnly"));
		}
		ModelAndView mav = new ModelAndView(Constants.ACT_VIEW_M);
		mav.addObject(Constants.ATT_USER_LIST, this.um.getAllUsers());
		mav.addObject(Constants.ATT_NB_ITEM_TO_SHOW, this.nbUsersToShow);
		mav.addObject(Constants.ATT_LDAP_DISPLAY, um.getLdapUserService().getSearchDisplayedAttributes());
		return mav;
	}

	/**
	 * @return int
	 */
	public int getNbUsersToShow() {
		return this.nbUsersToShow;
	}

	/**
	 * @param nbUsersToShow
	 */
	public void setNbUsersToShow(final int nbUsersToShow) {
		this.nbUsersToShow = nbUsersToShow;
	}

}
