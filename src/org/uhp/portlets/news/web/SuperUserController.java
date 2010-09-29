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
import org.esco.portlets.news.services.UserManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.mvc.AbstractWizardFormController;
import org.uhp.portlets.news.domain.RolePerm;
import org.uhp.portlets.news.web.support.Constants;

public class SuperUserController extends AbstractWizardFormController {

	private static final Log log = LogFactory.getLog(SuperUserController.class);
	private static final int DEFAULT_NB = 10;

	/** Liste des utilisateur obtenus par recherche. */
	public List<IEscoUser> users;
	@Autowired private UserManager um;
	private int nbItemsToShow;

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
	
	public void afterPropertiesSet() throws Exception {
	    Assert.notNull(this.um, "A UserManager is required.");
		if (this.nbItemsToShow <= 0) {
		    this.nbItemsToShow = DEFAULT_NB;
		}
	}
	@Override
	protected void processFinish(
			ActionRequest request, ActionResponse response,
			Object command, BindException errors)
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
	        log.error(e.getMessage(), e);
	        response.setRenderParameter(Constants.ACT, Constants.ACT_VIEW_M);
	    }
		
	}
	@Override
	protected void processCancel(
			ActionRequest request, ActionResponse response,
			Object command, BindException errors)
	throws Exception {
		response.setRenderParameter(Constants.ACT, Constants.ACT_VIEW_M);
	
	}
	
	@Override
	protected void validatePage(
			Object command, Errors errors, int page, boolean finish) {

		switch (page) {
		case 0: ValidationUtils.rejectIfEmpty(errors, "token", "TOKEN_IS_REQUIRED", "Token is required.");      break;
		case 1: ValidationUtils.rejectIfEmpty(errors, "user.userId", "USERID_REQUIRED", "User should be selected."); break;

		}

	}
	
	@Override
	protected Object formBackingObject(PortletRequest request) throws Exception {	
		PermForm permForm = new PermForm();
		return permForm;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected Map referenceData(PortletRequest request, Object command, Errors errors, int page) throws Exception {
	
	    if (!this.um.isSuperAdmin(request.getRemoteUser())) {
	        log.warn("AddSuperUser:: User " + request.getRemoteUser() 
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
	    	if (log.isDebugEnabled()) {
	    	    log.debug("List of users found : " + users);
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
			if (log.isDebugEnabled()) {
			    log.debug("PF page 2: " + pf);
			}
			return model;
		}
		return null;

	}


	@Override
	protected ModelAndView renderInvalidSubmit(RenderRequest request, RenderResponse response)
	throws Exception {
		return null;
	}
	@Override
	protected void handleInvalidSubmit(ActionRequest request, ActionResponse response)
	throws Exception {
		log.warn("SuperUserController::  handleInvalidSubmit: goto home page");
		response.setRenderParameter(Constants.ACT, Constants.ACT_VIEW_NEWSSTORE);

	}
	@SuppressWarnings("unchecked")
	@Override
	protected boolean isFormSubmission(PortletRequest request) {
	    //try {
	        for (Enumeration params = request.getParameterNames(); params.hasMoreElements();) {
	            String paramName = (String) params.nextElement();
	            log.debug("Attribut Form : " + paramName);
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
