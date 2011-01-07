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
import org.esco.portlets.news.domain.Type;
import org.esco.portlets.news.services.EntityManager;
import org.esco.portlets.news.services.TypeManager;
import org.esco.portlets.news.services.UserManager;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.util.Assert;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.ModelAndViewDefiningException;
import org.springframework.web.portlet.bind.PortletRequestUtils;
import org.springframework.web.portlet.mvc.SimpleFormController;
import org.uhp.portlets.news.NewsConstants;
import org.uhp.portlets.news.domain.Category;
import org.uhp.portlets.news.domain.RolePerm;
import org.uhp.portlets.news.domain.UserRole;
import org.uhp.portlets.news.service.CategoryManager;
import org.uhp.portlets.news.web.support.Constants;

/**
 * Controller.
 * @author GIP RECIA - Gribonvald Julien
 * 17 févr. 2010
 */
public class EntityEditController extends SimpleFormController implements InitializingBean {
    /** Logger. */
    private static final Log LOG = LogFactory.getLog(EntityEditController.class);
    
    /** Manager d'une Entity. */
    @Autowired
    private EntityManager em;
    /** Manager of a Category. */
    @Autowired
    private CategoryManager cm;
    /** Manager d'un type. */
    @Autowired
    private TypeManager tm;
    /** Manager des Users. */
    @Autowired
    private UserManager um;
    
    /**
     * Constructeur de l'objet EntityEditController.java.
     */
    public EntityEditController() {
        setCommandClass(EntityForm.class); 
        setCommandName(Constants.CMD_ENTITY);
        setFormView(Constants.ACT_EDIT_ENTITY);
        setSuccessView(Constants.ACT_VIEW_HOME);
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
        if (LOG.isTraceEnabled()) {
            LOG.trace("Entering onSubmitAction of " + this.getClass().getName());
        }
        final EntityForm entityF = (EntityForm) command;
        
        if (!this.um.isSuperAdmin(request.getRemoteUser())) {            
            throw new PortletSecurityException(
                    getMessageSourceAccessor().getMessage("exception.notAuthorized.action"));  
        }
        
        if (entityF != null && entityF.getEntity() != null && entityF.getEntity().getEntityId() != null) {
            if (LOG.isTraceEnabled()) {
                LOG.trace("Entity in parameter :" + entityF.getEntity());
                LOG.trace("List of types associated :" + entityF.getTypesIds());
                LOG.trace("List of categories associated :" + entityF.getCategoriesIds());
            }
            this.getEm().saveEntity(entityF.getEntity());
            
            // List of type attached to the entity passed in the form
            Set<Long> tIds = new HashSet<Long>();
            if (entityF.getTypesIds() != null && entityF.getTypesIds().length > 0) {
                for (String str : entityF.getTypesIds()) {
                tIds.add(Long.valueOf(str));
                }
            }
            
            // Obtains the list of type that are not anymore attached to the entity
            // and obtains the list of type that aren't already attached.
            Set<Long> deltypeIds = new HashSet<Long>();
            for (Type t :  this.getEm().getAutorizedTypesOfEntity(entityF.getEntity().getEntityId())) {
                if (tIds.contains(t.getTypeId())) {
                    tIds.remove(t.getTypeId());
                } else {
                    deltypeIds.add(t.getTypeId());
                }
            }
            // delete de link of type that aren't more attached to the entity.
            this.getEm().deleteAutorizedTypesOfEntity(
                    new ArrayList<Long>(deltypeIds), entityF.getEntity().getEntityId());
            
            // insert types that aren't already attached to the entity.
            if (!tIds.isEmpty()) {
                this.getEm().addAutorizedTypesOfEntity(new ArrayList<Long>(tIds), entityF.getEntity().getEntityId());
            }
            
            // Associate a category without Entity to an Entity.
            if (entityF.getCategoriesIds() != null && entityF.getCategoriesIds().length > 0) {
                List<Category> cList = this.getCm().getListCategoryOfEntityByUser(
                        request.getRemoteUser(), entityF.getEntity().getEntityId());
                int displayOrder = 0;
                if (cList != null && !cList.isEmpty()) {
                    displayOrder = cList.get(0).getDisplayOrder();
                }
                for (String str : entityF.getCategoriesIds()) {
                    Category cat = this.getCm().getCategoryById(Long.valueOf(str));
                    cat.setEntityId(entityF.getEntity().getEntityId());
                    displayOrder++;
                    cat.setDisplayOrder(displayOrder);
                    this.getCm().saveCategory(cat);
                    
                    List<UserRole> users = this.getUm().getUsersRolesForCtx(cat.getCategoryId(), NewsConstants.CTX_C);
                    for (UserRole u : users) {
                        this.um.migrationUserCtxRole(this.um.findUserByUid(u.getPrincipal()), cat.getEntityId());
                    }
                }
            }
                        
            response.setRenderParameter(Constants.ACT, Constants.ACT_VIEW_HOME);
        } else {
            LOG.error("Entity does not exist.");
            throw new IllegalArgumentException("Entity does not exist.");
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
     * org.springframework.validation.BindException, java.util.Map)
     */
    @Override
    protected ModelAndView showForm(final RenderRequest request, final RenderResponse response, 
            final BindException errors) throws Exception {
        if (LOG.isTraceEnabled()) {
            LOG.trace("Entering show form of " + this.getClass().getName());
        }
        ModelAndView mav = new ModelAndView(Constants.ACT_VIEW_NOT_AUTH);
        if (!this.getUm().isSuperAdmin(request.getRemoteUser())) {            
            mav.addObject(Constants.MSG_ERROR, getMessageSourceAccessor().getMessage("news.alert.superUserOnly"));
            throw new ModelAndViewDefiningException(mav);
        }
        return super.showForm(request, response, errors);
    }

    /**
     * @param request
     * @return Object
     * @throws Exception
     * @see org.springframework.web.portlet.mvc.AbstractFormController#formBackingObject(javax.portlet.PortletRequest)
     */
    @Override
    protected Object formBackingObject(final PortletRequest request) throws Exception {
        if (LOG.isTraceEnabled()) {
            LOG.trace("Entering FormBackingObject of " + this.getClass().getName());
        }
        Long eid = PortletRequestUtils.getLongParameter(request, Constants.ATT_ENTITY_ID);
        EntityForm entityF = new EntityForm();
        if (eid != null) {
            entityF.setEntity(this.getEm().getEntityById(eid));
            List<String> tIds = new ArrayList<String>();
            for (Type t : this.getEm().getAutorizedTypesOfEntity(eid)) {
                tIds.add(String.valueOf(t.getTypeId()));
            }
            entityF.setTypesIds(tIds.toArray(new String[0]));
            return entityF;
        }
        throw new ObjectRetrievalFailureException(Entity.class, null);
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
        if (LOG.isTraceEnabled()) {
            LOG.trace("Entering referenceData of " + this.getClass().getName());
        }
        Map<String, Object> model = new HashMap<String, Object>();

        model.put(Constants.ATT_TYPE_LIST, this.getTm().getAllTypes());
        model.put(Constants.ATT_C_LIST, this.getCm().getAloneCategory());
        model.put(Constants.ATT_PM, RolePerm.ROLE_ADMIN.getMask());

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
        response.setRenderParameter(Constants.ACT, Constants.ACT_VIEW_HOME);
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
     * Getter du membre tm.
     * @return <code>TypeManager</code> le membre tm.
     */
    public TypeManager getTm() {
        return tm;
    }


    /**
     * Setter du membre tm.
     * @param tm la nouvelle valeur du membre tm. 
     */
    public void setTm(final TypeManager tm) {
        this.tm = tm;
    }


    /**
     * @throws Exception
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(this.getTm(), "The property TypeManager tm in class " 
                + getClass().getSimpleName() + " must not be null.");
        Assert.notNull(this.getUm(), "The property UserManager um in class " 
                + getClass().getSimpleName() + " must not be null.");
        Assert.notNull(this.getEm(), "The property EntityManager em in class " 
                + getClass().getSimpleName() + " must not be null.");
        Assert.notNull(this.getCm(), "The property CategoryManager cm in class " 
                + getClass().getSimpleName() + " must not be null.");
    }
}
