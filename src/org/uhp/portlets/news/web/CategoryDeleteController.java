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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esco.portlets.news.services.EntityManager;
import org.esco.portlets.news.services.UserManager;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.mvc.AbstractController;
import org.uhp.portlets.news.NewsConstants;
import org.uhp.portlets.news.domain.RolePerm;
import org.uhp.portlets.news.service.CategoryManager;
import org.uhp.portlets.news.web.support.Constants;


/**
 * Category Delete controller.
 * modified by GIP RECIA - Gribonvald Julien
 * 15 mai 2010
 */
public class CategoryDeleteController extends AbstractController implements InitializingBean {

	/** */
	private static final Log LOG = LogFactory.getLog(CategoryDeleteController.class);
	/** */
	@Autowired private EntityManager em;
	/** */
	@Autowired private CategoryManager cm;
	/** */
	@Autowired private UserManager um;
	/** */
	private boolean deleted;
	/** */
	private String msgKey = "";
	/** */
	private Long entityId;

	/**
	 * Constructeur de l'objet CategoryDeleteController.java.
	 */
	public CategoryDeleteController() {
		super();
	}

	/**
	 * @throws Exception
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() throws Exception {
		if (this.cm == null || this.um == null || this.em == null) {
			throw new IllegalArgumentException("An EntityManager, a CategoryManager and a userManager are required");
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
	public void handleActionRequestInternal(final ActionRequest request, final ActionResponse response)
	throws Exception {
		Long catId = Long.valueOf(request.getParameter(Constants.ATT_CAT_ID));
		entityId = this.cm.getCategoryById(catId).getEntityId();
		this.deleted = false;
		if (!this.um.isUserAdminInCtx(entityId, NewsConstants.CTX_E, request.getRemoteUser())) {
			msgKey = "news.alert.superUserOnly";
		} else if (this.cm.deleteCategory(catId)) {
			// at this state the category is deleted with his types associated.
			if (LOG.isDebugEnabled()) {
				LOG.debug("category [" + catId + "] is removed");
			}
			this.deleted = true;
		} else {
			if (LOG.isWarnEnabled()) {
				LOG.warn("category [ " + catId + "] is not empty, delete action is ignored ");
			}
			msgKey = "news.alert.categoryNotEmpty";
		}
	}

	/**
	 * @param request
	 * @param response
	 * @return <code>ModelAndView</code>
	 * @throws Exception
	 * @see org.springframework.web.portlet.mvc.AbstractController#
	 * handleRenderRequestInternal(javax.portlet.RenderRequest, javax.portlet.RenderResponse)
	 */
	@Override
	public ModelAndView handleRenderRequestInternal(final RenderRequest request, final RenderResponse response)
	throws Exception {
		if (this.deleted) {
			ModelAndView mav = new ModelAndView(Constants.ACT_VIEW_ENTITY);
			mav.addObject(Constants.OBJ_ENTITY, this.em.getEntityById(this.entityId));
			mav.addObject(Constants.ATT_C_LIST,
					this.cm.getListCategoryOfEntityByUser(request.getRemoteUser(), this.entityId));
			// Get rigths of the user in the context
			if (!this.um.isUserAdminInCtx(entityId, NewsConstants.CTX_E, request.getRemoteUser())) {
				mav.addObject(Constants.ATT_PM, RolePerm.valueOf(
					this.um.getUserRoleInCtx(entityId, NewsConstants.CTX_E, request.getRemoteUser()))
					.getMask());
			}
			return mav;
		}
			return new ModelAndView("Errors", "message", getMessageSourceAccessor().getMessage(msgKey));
	}
}
