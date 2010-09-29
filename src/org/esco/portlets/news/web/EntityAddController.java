/**
 * ESUP-Portail News - Copyright (c) 2009 ESUP-Portail consortium
 * For any information please refer to http://esup-helpdesk.sourceforge.net
 * You may obtain a copy of the licence at http://www.esup-portail.org/license/
 */
package org.esco.portlets.news.web;

import java.util.ArrayList;
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
import org.esco.portlets.news.domain.Entity;
import org.esco.portlets.news.services.EntityManager;
import org.esco.portlets.news.services.TypeManager;
import org.esco.portlets.news.services.UserManager;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.ModelAndViewDefiningException;
import org.springframework.web.portlet.mvc.SimpleFormController;
import org.uhp.portlets.news.NewsConstants;
import org.uhp.portlets.news.domain.Category;
import org.uhp.portlets.news.domain.RolePerm;
import org.uhp.portlets.news.domain.UserRole;
import org.uhp.portlets.news.service.CategoryManager;
import org.uhp.portlets.news.web.support.Constants;

/**
 * Controller of an Entity.
 * @author GIP RECIA - Gribonvald Julien
 * 17 mars 2010
 */
public class EntityAddController extends SimpleFormController implements InitializingBean {

    /** Logger. */
    private static final Log LOG = LogFactory.getLog(EntityAddController.class);
    
    /** Manager of an Entity. */
    @Autowired
    private EntityManager em;
    /** Manager of a Category. */
    @Autowired
    private CategoryManager cm;
    /** Manager of a Type. */
    @Autowired
    private TypeManager tm;
    /** Manager of Users. */
    @Autowired
    private UserManager um;

    /**
     * Constructor of the object TypeAddController.java.
     */
    public EntityAddController() {
        super();
        setCommandClass(EntityForm.class); 
        setCommandName(Constants.CMD_ENTITY);
        setFormView(Constants.ACT_ADD_ENTITY);
        setSuccessView(Constants.ACT_VIEW_HOME);
    }

    /**
     * @param request
     * @return Object
     * @throws Exception
     * @see org.springframework.web.portlet.mvc.AbstractFormController#formBackingObject(javax.portlet.PortletRequest)
     */
    @Override
    protected Object formBackingObject(final PortletRequest request) throws Exception {
        EntityForm entF = new EntityForm();
        entF.setEntity(new Entity());
        entF.getEntity().setCreatedBy(request.getRemoteUser());
        if (LOG.isTraceEnabled()) {
            LOG.trace("Entering FormBackingObject.");
        }
        return entF;
    }

    /**
     * @param request
     * @param response
     * @param command
     * @param errors
     * @throws Exception
     * @see org.springframework.web.portlet.mvc.SimpleFormController#
     * onSubmitAction(javax.portlet.ActionRequest, javax.portlet.ActionResponse, 
     * java.lang.Object, org.springframework.validation.BindException)
     */
    @Override
    protected void onSubmitAction(final ActionRequest request, final ActionResponse response, 
            final Object command, final BindException errors) throws Exception {
        final EntityForm entF = (EntityForm) command;
        if (LOG.isTraceEnabled()) {
            LOG.trace("Entity given in parameters :" + entF.getEntity());
            LOG.trace("List of Type to associate with the Entity :" + entF.getTypesIds());
            LOG.trace("List of categories associated :" + entF.getCategoriesIds());
        }
        if (!this.um.isSuperAdmin(request.getRemoteUser())) {            
            throw new PortletSecurityException(
                    getMessageSourceAccessor().getMessage("exception.notAuthorized.action"));  
        }
        Set<Long> tIds = new HashSet<Long>();
        if (entF.getTypesIds() != null && entF.getTypesIds().length > 0) {
            for (String str : entF.getTypesIds()) {
                tIds.add(Long.valueOf(str));
            }
        }
        // Save the new Entity.
        this.getEm().saveEntity(entF.getEntity());
        // Save the association between Types and Entity.
        this.getEm().addAutorizedTypesOfEntity(new ArrayList<Long>(tIds), entF.getEntity().getEntityId());
        // Associate a category without Entity to an Entity.
        if (entF.getCategoriesIds() != null && entF.getCategoriesIds().length > 0) {
            int displayOrder = 1;
            for (String str : entF.getCategoriesIds()) {
                Category cat = this.getCm().getCategoryById(Long.valueOf(str));
                cat.setEntityId(entF.getEntity().getEntityId());
                cat.setDisplayOrder(displayOrder);
                this.getCm().saveCategory(cat);
                this.getCm().addAuthorizedTypeToCategory(new ArrayList<Long>(tIds), cat.getCategoryId());
                displayOrder++;
                
                List<UserRole> users = this.getUm().getUsersRolesForCtx(cat.getCategoryId(), NewsConstants.CTX_C);
                for (UserRole u : users) {
                    this.um.migrationUserCtxRole(this.um.findUserByUid(u.getPrincipal()), cat.getEntityId());
                }
            }
        }
        response.setRenderParameter(Constants.ACT, Constants.ACT_VIEW_NEWSSTORE);
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
        if (LOG.isTraceEnabled()) {
            LOG.trace("Entering show form.");
        }
        if (!this.getUm().isSuperAdmin(request.getRemoteUser())) {            
            ModelAndView mav = new ModelAndView(Constants.ACT_VIEW_NOT_AUTH);
            mav.addObject(Constants.MSG_ERROR, getMessageSourceAccessor().getMessage("news.alert.superUserOnly"));
            throw new ModelAndViewDefiningException(mav);
        }
        return super.showForm(request, response, errors);
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
        Map<String, Object> model = new HashMap<String, Object>();
        if (LOG.isTraceEnabled()) {
            LOG.trace("Entering reference data.");
        }
        if (this.um.isSuperAdmin(request.getRemoteUser())) {
            model.put(Constants.ATT_TYPE_LIST, this.getTm().getAllTypes());
            model.put(Constants.ATT_C_LIST, this.getCm().getAloneCategory());
            model.put(Constants.ATT_PM, RolePerm.ROLE_ADMIN.getMask());
        } else {
            model.put(Constants.ATT_PM, RolePerm.ROLE_USER.getMask());
        }

        return model;
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
     * Getter of member em.
     * @return <code>EntityManager</code> the member em.
     */
    public EntityManager getEm() {
        return em;
    }
    
    /**
     * Getter of member tm.
     * @return <code>TypeManager</code> the member tm.
     */
    public TypeManager getTm() {
        return tm;
    }
    
    /**
     * Getter of member um.
     * @return <code>UserManager</code> the member um.
     */
    public UserManager getUm() {
        return um;
    }
    
    /**
     * Setter of member em.
     * @param em the new value of the member em. 
     */
    public void setEm(final EntityManager em) {
        this.em = em;
    }

    /**
     * Setter of member tm.
     * @param tm the new value of the member tm. 
     */
    public void setTm(final TypeManager tm) {
        this.tm = tm;
    }

    /**
     * Setter of member um.
     * @param um the new value of the member um. 
     */
    public void setUm(final UserManager um) {
        this.um = um;
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
     * @throws Exception
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(this.getTm(), "The property TypeManager tm in class " 
                + getClass().getSimpleName() + " must not be null.");
        Assert.notNull(this.getCm(), "The property CategoryManager cm in class " 
                + getClass().getSimpleName() + " must not be null.");
        Assert.notNull(this.getUm(), "The property UserManager um in class " 
                + getClass().getSimpleName() + " must not be null.");
        Assert.notNull(this.getEm(), "The property EntityManager em in class " 
                + getClass().getSimpleName() + " must not be null.");
    }
}
