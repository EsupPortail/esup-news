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
package org.uhp.portlets.news.web;

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
import javax.portlet.PortletSecurityException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esco.portlets.news.services.EntityManager;
import org.esco.portlets.news.services.UserManager;
import org.esco.portlets.news.web.CategoryForm;
import org.esco.portlets.news.web.EntityAddController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
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
import org.uhp.portlets.news.domain.UserRole;
import org.uhp.portlets.news.service.CategoryManager;
import org.uhp.portlets.news.web.support.Constants;


/**
 * modified by GIP RECIA - Gribonvald Julien.
 * 15 avr. 2010
 */
public class CategoryAddController extends SimpleFormController {
    
    /** Logger. */
    private static final Log LOG = LogFactory.getLog(EntityAddController.class);

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
     * Constructor of CategoryAddController.java.
     */
    public CategoryAddController() {
        setCommandClass(CategoryForm.class); 
        setCommandName(Constants.CMD_CATEGORY);
        setFormView(Constants.ACT_ADD_CAT);
        setSuccessView(Constants.ACT_VIEW_ENTITY);	

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
        CategoryForm catForm = (CategoryForm) command;
        if (LOG.isTraceEnabled()) {
            LOG.trace("Category given in parameters :" + catForm.getCategory());
            LOG.trace("List of Type to associate with the Entity :" + catForm.getTypesIds());
        }
        // if the user is superAdmin he is automatically admin.
        if (!this.getUm().isUserAdminInCtx(catForm.getCategory().getEntityId(), 
                        NewsConstants.CTX_E, request.getRemoteUser())) {            
            throw new PortletSecurityException(
                    getMessageSourceAccessor().getMessage("exception.notAuthorized.action"));  
        }
        //to avoid duplicates entry
        Set<Long> tIds = new HashSet<Long>();
        if (catForm.getTypesIds() != null && catForm.getTypesIds().length > 0) {
            for (String str : catForm.getTypesIds()) {
                tIds.add(Long.valueOf(str));
            }
        }
        // save the category
        this.getCm().saveCategory(catForm.getCategory());
        
        // Save the association between Types and Category.
        this.getCm().addAuthorizedTypeToCategory(new ArrayList<Long>(tIds), catForm.getCategory().getCategoryId());
        
        // Duplicate user rights defined on the entity to the category
        List<UserRole> lur = this.getUm().getUsersRolesForCtx(catForm.getCategory().getEntityId(), NewsConstants.CTX_E);
        for (UserRole ur : lur) {
            if (!RolePerm.ROLE_USER.getName().equalsIgnoreCase(ur.getRole())) {
                this.um.addUserCtxRole(this.um.findUserByUid(ur.getPrincipal()), ur.getRole(), NewsConstants.CTX_C, 
                        catForm.getCategory().getCategoryId());
            }
        }
        
        
        response.setRenderParameter(Constants.ACT, Constants.ACT_VIEW_CAT);
        response.setRenderParameter(Constants.ATT_CAT_ID, String.valueOf(catForm.getCategory().getCategoryId()));
    }

    /**
     * @param request
     * @param response
     * @param errors
     * @return <code>ModelAndView</code>
     * @throws Exception
     * @see org.springframework.web.portlet.mvc.SimpleFormController#
     * showForm(javax.portlet.RenderRequest, javax.portlet.RenderResponse, org.springframework.validation.BindException)
     */
    @Override
    protected ModelAndView showForm(final RenderRequest request, final RenderResponse response, 
            final BindException errors) throws Exception {

        if (!this.getUm().isUserAdminInCtx(
                        PortletRequestUtils.getLongParameter(request, Constants.ATT_ENTITY_ID), 
                        NewsConstants.CTX_E, request.getRemoteUser())) {        	
            ModelAndView mav = new ModelAndView(Constants.ACT_VIEW_NOT_AUTH);
            mav.addObject(Constants.MSG_ERROR, getMessageSourceAccessor().getMessage("news.alert.superUserOnly"));
            throw new ModelAndViewDefiningException(mav);
        }
        return super.showForm(request, response, errors);
    }
    /**
     * @param request
     * @return <code>CategoryForm</code>
     * @throws Exception
     * @see org.springframework.web.portlet.mvc.AbstractFormController#formBackingObject(javax.portlet.PortletRequest)
     */
    @Override
    protected Object formBackingObject(final PortletRequest request) throws Exception {
        CategoryForm catF =  new CategoryForm();
        Category category = new Category();
        category.setCreatedBy(request.getRemoteUser());	
        Long id = PortletRequestUtils.getLongParameter(request, Constants.ATT_ENTITY_ID); 
        category.setEntityId(id);
        catF.setCategory(category);
        return catF;
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
        Category cat = ((CategoryForm) command).getCategory();
        String uid = request.getRemoteUser();
        Map<String, Object> model = new HashMap<String, Object>();        
        model.put(Constants.OBJ_ENTITY, this.em.getEntityById(cat.getEntityId()));
        // the user need to be SuperAdmin or Admin.
        if (this.getUm().isSuperAdmin(uid)) {                
            model.put(Constants.ATT_TYPE_LIST, this.getEm().getAutorizedTypesOfEntity(cat.getEntityId()));
            model.put(Constants.ATT_PM, RolePerm.ROLE_ADMIN.getMask());
        } else if (this.getUm().isUserAdminInCtx(cat.getEntityId(), NewsConstants.CTX_E, uid)) { 
            model.put(Constants.ATT_TYPE_LIST, this.getEm().getAutorizedTypesOfEntity(cat.getEntityId()));
            model.put(Constants.ATT_PM, RolePerm.ROLE_MANAGER.getMask());
        } else {
            model.put(Constants.ATT_PM, RolePerm.ROLE_USER.getMask());
        }
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
        if (LOG.isTraceEnabled()) {
            LOG.trace("Entering init Binder.");
        }
        SimpleDateFormat dateFormat = 
            new SimpleDateFormat(getMessageSourceAccessor().getMessage(Constants.DATE_FORMAT));
        binder.registerCustomEditor(Date.class, null, new	CustomDateEditor(dateFormat, true));
        binder.setAllowedFields(
                new String[] {"category.name", "category.desc", "category.langue", "category.rssAllowed", 
                        "category.refreshPeriod",  "category.refreshFrequency", "category.publicView", "typesIds", });
    }
    /**
     * @param request  
     * @param response 
     */
    @Override
    protected ModelAndView renderInvalidSubmit(final RenderRequest request, final RenderResponse response) 
    throws Exception {
        if (LOG.isTraceEnabled()) {
            LOG.trace("Entering render Invalid Submit.");
        }
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
        if (LOG.isTraceEnabled()) {
            LOG.trace("Entering handle Invalid Submit.");
        }
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

