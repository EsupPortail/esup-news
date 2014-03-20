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

import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletRequest;
import javax.portlet.PortletSecurityException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.esco.portlets.news.domain.EscoUser;
import org.esco.portlets.news.domain.IEscoUser;
import org.esco.portlets.news.services.PermissionManager;
import org.esco.portlets.news.services.UserManager;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.mvc.AbstractWizardFormController;
import org.uhp.portlets.news.domain.RolePerm;
import org.uhp.portlets.news.web.support.Constants;

/**
 * modified by GIP RECIA - Julien Gribonvald
 * 4 mai 2012
 */
public class SuperUserController extends AbstractWizardFormController implements InitializingBean {

	/** */
	private static final Log LOGGER = LogFactory.getLog(SuperUserController.class);
	/** */
	private static final int DEFAULT_NB = 10;

	/** */
	@Autowired private UserManager um;
	/** */
	@Autowired private PermissionManager pm;
	/** */
	private int nbItemsToShow;

	/** Liste des utilisateur obtenus par recherche. */
	public List<IEscoUser> users;

	/**
	 * Contructor of the object SuperUserController.java.
	 */
	public  SuperUserController() {
		setCommandClass(PermForm.class);
		setCommandName(Constants.CMD_PERM);
		setAllowDirtyBack(true);
		setAllowDirtyForward(false);
		setSessionForm(true);
		setPageAttribute(Constants.ATT_PAGE);
		setPages(new String[] {Constants.ACT_ADD_SUPERADMIN, Constants.ACT_ADD_SUPERADMIN, Constants.ACT_ADD_SUPERADMIN } );
	}

	/**
	 * Setter du membre nbItemsToShow.
	 * @param nbItemsToShow la nouvelle valeur du membre nbItemsToShow.
	 */
	public void setNbItemsToShow(final int nbItemsToShow) {
		this.nbItemsToShow = nbItemsToShow;
	}

	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(this.um, "A UserManager is required.");
		Assert.notNull(this.pm, "A PermissionManager is required.");
		if (this.nbItemsToShow <= 0) {
			this.nbItemsToShow = DEFAULT_NB;
		}
	}
	/**
	 * @see org.springframework.web.portlet.mvc.AbstractWizardFormController#processFinish(javax.portlet.ActionRequest, javax.portlet.ActionResponse, java.lang.Object, org.springframework.validation.BindException)
	 */
	@Override
	protected void processFinish(ActionRequest request, ActionResponse response, Object command, BindException errors)
			throws Exception {
	    try {
			PermForm permForm = (PermForm) command;
			IEscoUser u = null;

			for (IEscoUser user : users) {
				if(user.getUserId().equalsIgnoreCase(permForm.getUser().getUserId())) {
					u = user;
					u.setIsSuperAdmin(Constants.ATT_T);
					u.setEnabled(Constants.ATT_T);
				}
			}
			if (u != null) {
				this.um.saveUser(u);
			}
			response.setRenderParameter(Constants.ACT, Constants.ACT_VIEW_M);
	    } catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			response.setRenderParameter(Constants.ACT, Constants.ACT_VIEW_M);
	    }

	}
	/**
	 * @see org.springframework.web.portlet.mvc.AbstractWizardFormController#processCancel(javax.portlet.ActionRequest, javax.portlet.ActionResponse, java.lang.Object, org.springframework.validation.BindException)
	 */
	@Override
	protected void processCancel(ActionRequest request, ActionResponse response, Object command, BindException errors)
			throws Exception {
		response.setRenderParameter(Constants.ACT, Constants.ACT_VIEW_M);

	}

	/**
	 * @see org.springframework.web.portlet.mvc.AbstractWizardFormController#validatePage(java.lang.Object, org.springframework.validation.Errors, int, boolean)
	 */
	@Override
	protected void validatePage(Object command, Errors errors, int page, boolean finish) {

		switch (page) {
		case 0: ValidationUtils.rejectIfEmpty(errors, "token", "TOKEN_IS_REQUIRED", "Token is required.");      break;
		case 1: ValidationUtils.rejectIfEmpty(errors, "user.userId", "USERID_REQUIRED", "User should be selected."); break;

		}
	}

	/**
	 * @see org.springframework.web.portlet.mvc.AbstractFormController#formBackingObject(javax.portlet.PortletRequest)
	 */
	@Override
	protected Object formBackingObject(PortletRequest request) throws Exception {
		PermForm permForm = new PermForm();
		return permForm;
	}

	/**
	 * @see org.springframework.web.portlet.mvc.AbstractWizardFormController#referenceData(javax.portlet.PortletRequest, java.lang.Object, org.springframework.validation.Errors, int)
	 */
	@Override
	protected Map<String, Object> referenceData(PortletRequest request, Object command, Errors errors, int page) throws Exception {

		if (!this.pm.isSuperAdmin()) {
			LOGGER.warn("AddSuperUser:: User " + request.getRemoteUser()
					+ " has no SuperUser role : not authorized for this action");
			throw new PortletSecurityException(
					getMessageSourceAccessor().getMessage("exception.notAuthorized.action"));
		}
		Map<String, Object> model = new HashMap<String, Object>();

		if (page == 0) {
			model.put(Constants.ATT_ROLE,  RolePerm.ROLE_ADMIN.getName());
			return model;
		} else if (page == 1) {
			PermForm permForm = (PermForm) command;
			model.put(Constants.ATT_IS_GRP, permForm.getIsGroup());
			users = this.um.findPersonsByToken(permForm.getToken());
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("List of users found : " + users);
			}
			model.put(Constants.ATT_USER_LIST, users);
			model.put(Constants.ATT_LDAP_DISPLAY, um.getLdapUserService().getSearchDisplayedAttributes());
			model.put(Constants.ATT_NB_ITEM_TO_SHOW, this.nbItemsToShow);
			model.put(Constants.ERRORS, errors);
			return model;
		} else if (page == 2) {
			PermForm pf = (PermForm) command;
			model.put(Constants.ATT_ROLE, pf.getRole());
			model.put(Constants.ATT_IS_GRP, pf.getIsGroup());
			EscoUser u = null;
			for (IEscoUser user : users) {
				if (user.getUserId().equalsIgnoreCase(pf.getUser().getUserId())) {
					u = (EscoUser) user;
				}
			}
			model.put(Constants.OBJ_USER, u);
			model.put(Constants.ATT_LDAP_DISPLAY, um.getLdapUserService().getSearchDisplayedAttributes());
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("PF page 2: " + pf);
			}
			return model;
		}
		return null;

	}


	/**
	 * @see org.springframework.web.portlet.mvc.AbstractWizardFormController#renderInvalidSubmit(javax.portlet.RenderRequest, javax.portlet.RenderResponse)
	 */
	@Override
	protected ModelAndView renderInvalidSubmit(RenderRequest request, RenderResponse response)
			throws Exception {
		return null;
	}
	/**
	 * @see org.springframework.web.portlet.mvc.AbstractWizardFormController#handleInvalidSubmit(javax.portlet.ActionRequest, javax.portlet.ActionResponse)
	 */
	@Override
	protected void handleInvalidSubmit(ActionRequest request, ActionResponse response)
			throws Exception {
		LOGGER.warn("SuperUserController::  handleInvalidSubmit: goto home page");
		response.setRenderParameter(Constants.ACT, Constants.ACT_VIEW_NEWSSTORE);

	}
	/**
	 * @see org.springframework.web.portlet.mvc.AbstractWizardFormController#isFormSubmission(javax.portlet.PortletRequest)
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected boolean isFormSubmission(PortletRequest request) {
		//try {
		for (Enumeration<String> params = request.getParameterNames(); params.hasMoreElements();) {
			String paramName = params.nextElement();
			LOGGER.debug("Attribut Form : " + paramName);
			if (paramName.startsWith(PARAM_TARGET)
					|| paramName.equals(PARAM_FINISH)
					|| paramName.equals(PARAM_FINISH))   {
				return true;
			}
		}
		return super.isFormSubmission(request);
		/*} catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }*/
	}


}
