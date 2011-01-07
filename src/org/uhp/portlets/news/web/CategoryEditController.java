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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esco.portlets.news.domain.Type;
import org.esco.portlets.news.services.EntityManager;
import org.esco.portlets.news.services.UserManager;
import org.esco.portlets.news.web.CategoryForm;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.util.Assert;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.ModelAndViewDefiningException;
import org.springframework.web.portlet.bind.PortletRequestDataBinder;
import org.springframework.web.portlet.bind.PortletRequestUtils;
import org.springframework.web.portlet.mvc.SimpleFormController;
import org.uhp.portlets.news.NewsConstants;
import org.uhp.portlets.news.domain.Category;
import org.uhp.portlets.news.domain.RolePerm;
import org.uhp.portlets.news.service.CategoryManager;
import org.uhp.portlets.news.web.support.Constants;

/**
 * modified by GIP RECIA - Gribonvald Julien
 * 15 avr. 2010
 */
public class CategoryEditController extends SimpleFormController implements InitializingBean {

    /** Logger. */
    private static final Log LOG = LogFactory.getLog(CategoryEditController.class);

    /** Manager of a Category . */
    @Autowired 
    private CategoryManager cm;
    /** Manager of an User. */
    @Autowired 
    private UserManager um;
    /** Manager of an Entity. */
    @Autowired
    private EntityManager em;

	/**
	 * Constructor of CategoryEditController.java.
	 */
	public CategoryEditController() {
		setCommandClass(CategoryForm.class); 
		setCommandName(Constants.CMD_CATEGORY);
		setFormView(Constants.ACT_EDIT_CAT);
		setSuccessView(Constants.ACT_VIEW_CAT);
	}
	/**
	 * @param request
	 * @param response
	 * @param command
	 * @param errors
	 * @throws Exception
	 * @see org.springframework.web.portlet.mvc.SimpleFormController#
	 * onSubmitAction(javax.portlet.ActionRequest, javax.portlet.ActionResponse, java.lang.Object, 
	 * org.springframework.validation.BindException)
	 */
	@Override
	protected void onSubmitAction(final ActionRequest request, final ActionResponse response,
			final Object command, final BindException errors) throws Exception {

		CategoryForm catF = (CategoryForm) command;
		if (catF != null && catF.getCategory() != null && catF.getCategory().getCategoryId() > 0 ) {
		    catF.getCategory().setLastUpdateDate(new Date());
			this.cm.saveCategory(catF.getCategory());
			response.setRenderParameter(Constants.ACT, Constants.ACT_VIEW_CAT);
			response.setRenderParameter(Constants.ATT_CAT_ID, 
			        String.valueOf(catF.getCategory().getCategoryId()));
			response.setRenderParameter(Constants.ATT_ENTITY_ID, 
			        String.valueOf(catF.getCategory().getEntityId()));
			
			
			// List of type attached to the category passed in the form
            Set<Long> tIds = new HashSet<Long>();
            if (catF.getTypesIds() != null && catF.getTypesIds().length > 0) {
                for (String str : catF.getTypesIds()) {
                    tIds.add(Long.valueOf(str));
                }
            }
            
            Set<Long> deltypeIds = new HashSet<Long>();
            
            // Ids of authorised types defines in the entity
            Set<Long> autorizedTypesIds = new HashSet<Long>();
            for (Type t :  this.getEm().getAutorizedTypesOfEntity(catF.getCategory().getEntityId())) {
                autorizedTypesIds.add(t.getTypeId());
            }
            // remove from database types that aren't anymore attached to the category
            // and obtains the list of types that aren't already attached to the category
            for (Type t : this.getCm().getTypesOfCategory(catF.getCategory().getCategoryId())) {
                if (autorizedTypesIds.contains(t.getTypeId())) {
                    if (tIds.contains(t.getTypeId())) {
                        tIds.remove(t.getTypeId());
                    } else {
                        deltypeIds.add(t.getTypeId());
                    }
                } else {
                    // remove unauthorised types from the selection
                    deltypeIds.add(t.getTypeId());
                    tIds.remove(t.getTypeId());
                }
            }
            
            // delete link of type that aren't more attached to the category.
            this.getCm().deleteTypeOfCategory(
                    new ArrayList<Long>(deltypeIds), catF.getCategory().getCategoryId());
            
            // insert types that aren't already attached to the category.
            if (!tIds.isEmpty()) {
                this.getCm().addAuthorizedTypeToCategory(new ArrayList<Long>(tIds), catF.getCategory().getCategoryId());
            }
			
			
		} else {
			LOG.error("Category does not exist.");
            throw new IllegalArgumentException("Category does not exist.");
        }
	} 

	/**
	 * @param request
	 * @param response
	 * @param errors
	 * @return <code>ModelAndView</code>
	 * @throws Exception
	 * @see org.springframework.web.portlet.mvc.SimpleFormController#
	 * showForm(javax.portlet.RenderRequest, javax.portlet.RenderResponse, 
	 * org.springframework.validation.BindException)
	 */
	@Override
	protected ModelAndView showForm(final RenderRequest request, final RenderResponse response, 
	        final BindException errors) throws Exception {            
		String uid = request.getRemoteUser();
		Long categoryId = PortletRequestUtils.getLongParameter(request, Constants.ATT_CAT_ID);
		if (!this.um.isUserAdminInCtx(categoryId, NewsConstants.CTX_C, uid)) {
		    ModelAndView mav = new ModelAndView(Constants.ACT_VIEW_NOT_AUTH);
			String msg = "you are not authorized for this action";
			mav.addObject(Constants.MSG_ERROR, msg);
			throw new ModelAndViewDefiningException(mav);
		}
		return super.showForm(request, response, errors);
	}
	/**
	 * @param request
	 * @return <code>CategoryForm</code>
	 * @throws Exception
	 * @see org.springframework.web.portlet.mvc.AbstractFormController#
	 * formBackingObject(javax.portlet.PortletRequest)
	 */
	@Override
	protected Object formBackingObject(final PortletRequest request) throws Exception {
		Long categoryId = PortletRequestUtils.getLongParameter(request, Constants.ATT_CAT_ID);
		CategoryForm catF = new CategoryForm();	
		if (categoryId  != null) {
			catF.setCategory(this.cm.getCategoryById(categoryId));	 
			List<String> tIds = new ArrayList<String>();
            for (Type t : this.getCm().getTypesOfCategory(categoryId)) {
                tIds.add(String.valueOf(t.getTypeId()));
            }
            catF.setTypesIds(tIds.toArray(new String[0]));
            return catF;
		}
		
		throw new ObjectRetrievalFailureException(Category.class, null);
	}
	
	/**
	 * @param request
	 * @param command
	 * @param errors
	 * @return <code>Map<String, Object></code>
	 * @throws Exception
	 * @see org.springframework.web.portlet.mvc.SimpleFormController#
	 * referenceData(javax.portlet.PortletRequest, java.lang.Object, org.springframework.validation.Errors)
	 */
	@Override
	protected Map<String, Object> referenceData(final PortletRequest request, final Object command, 
	        final Errors errors) throws Exception {
	    Category category = ((CategoryForm) command).getCategory();
		Map<String, Object> model = new HashMap<String, Object>();
		model.put(Constants.ATT_TYPE_LIST, this.getEm().getAutorizedTypesOfEntity(category.getEntityId()));
		model.put(Constants.OBJ_ENTITY, this.em.getEntityById(category.getEntityId()));
		model.put(Constants.ATT_PM, RolePerm.valueOf(this.um.getUserRoleInCtx(
		        category.getCategoryId(), NewsConstants.CTX_C, request.getRemoteUser())).getMask());
		return model;
	}
	
	/**
	 * @param request
	 * @param binder
	 * @throws Exception
	 * @see org.springframework.web.portlet.mvc.BaseCommandController#
	 * initBinder(javax.portlet.PortletRequest, org.springframework.web.portlet.bind.PortletRequestDataBinder)
	 */
	@Override
	protected void initBinder(final PortletRequest request, final PortletRequestDataBinder binder)
	throws Exception {
		SimpleDateFormat dateFormat = 
		    new SimpleDateFormat(getMessageSourceAccessor().getMessage(Constants.DATE_FORMAT));
		binder.registerCustomEditor(Date.class, null, new CustomDateEditor(dateFormat, true));
	}
	
	/**
	 * @param request
	 * @param response
	 * @return <code>ModelAndView</code>
	 * @throws Exception
	 * @see org.springframework.web.portlet.mvc.AbstractFormController#
	 * renderInvalidSubmit(javax.portlet.RenderRequest, javax.portlet.RenderResponse)
	 */
	@Override
	protected ModelAndView renderInvalidSubmit(final RenderRequest request, final RenderResponse response)
	throws Exception {
		return null;
	}
	/**
	 * @param request
	 * @param response
	 * @throws Exception
	 * @see org.springframework.web.portlet.mvc.AbstractFormController#
	 * handleInvalidSubmit(javax.portlet.ActionRequest, javax.portlet.ActionResponse)
	 */
	@Override
	protected void handleInvalidSubmit(final ActionRequest request, final ActionResponse response)
	throws Exception {
		response.setRenderParameter(Constants.ACT, Constants.ACT_VIEW_NEWSSTORE);
	}
    /**
     * Getter du membre cm.
     * @return <code>CategoryManager</code> le membre cm.
     */
    public CategoryManager getCm() {
        return cm;
    }
    /**
     * Setter du membre cm.
     * @param cm la nouvelle valeur du membre cm. 
     */
    public void setCm(final CategoryManager cm) {
        this.cm = cm;
    }
    /**
     * Getter du membre um.
     * @return <code>UserManager</code> le membre um.
     */
    public UserManager getUm() {
        return um;
    }
    /**
     * Setter du membre um.
     * @param um la nouvelle valeur du membre um. 
     */
    public void setUm(final UserManager um) {
        this.um = um;
    }
    /**
     * Getter du membre em.
     * @return <code>EntityManager</code> le membre em.
     */
    public EntityManager getEm() {
        return em;
    }
    /**
     * Setter du membre em.
     * @param em la nouvelle valeur du membre em. 
     */
    public void setEm(final EntityManager em) {
        this.em = em;
    }

    /**
     * @throws Exception
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(this.getCm(), "The property CategoryManager cm in class " 
                + getClass().getSimpleName() + " must not be null.");
        Assert.notNull(this.getUm(), "The property UserManager um in class " 
                + getClass().getSimpleName() + " must not be null.");
        Assert.notNull(this.getEm(), "The property EntityManager em in class " 
                + getClass().getSimpleName() + " must not be null.");
    }
}

