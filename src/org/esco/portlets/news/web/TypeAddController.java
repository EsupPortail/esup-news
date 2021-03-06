/**
 * ESUP-Portail News - Copyright (c) 2009 ESUP-Portail consortium
 * For any information please refer to http://esup-helpdesk.sourceforge.net
 * You may obtain a copy of the licence at http://www.esup-portail.org/license/
 */
package org.esco.portlets.news.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
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
import org.esco.portlets.news.domain.Type;
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
import org.uhp.portlets.news.domain.RolePerm;
import org.uhp.portlets.news.web.support.Constants;

/**
 * Controller of a Type.
 * @author GIP RECIA - Gribonvald Julien
 * 9 févr. 2010
 */
public class TypeAddController extends SimpleFormController implements InitializingBean {

    /** Logger. */
    private static final Log LOG = LogFactory.getLog(TypeAddController.class);
    
    /** Manager d'une Entity. */
    @Autowired
    private EntityManager em;
    /** Manager d'un type. */
    @Autowired
    private TypeManager tm;
    /** Manager des Users. */
    @Autowired
    private UserManager um;

    /**
     * Constructeur de l'objet TypeAddController.java.
     */
    public TypeAddController() {
        super();
        setCommandClass(TypeForm.class); 
        setCommandName(Constants.CMD_TYPE);
        setFormView(Constants.ACT_ADD_TYPE);
        setSuccessView(Constants.ACT_VIEW_TYPE);
    }

    /**
     * @param request
     * @return Object
     * @throws Exception
     * @see org.springframework.web.portlet.mvc.AbstractFormController#formBackingObject(javax.portlet.PortletRequest)
     */
    @Override
    protected Object formBackingObject(final PortletRequest request) throws Exception {
        TypeForm typeF = new TypeForm();
        typeF.setType(new Type());
        if (LOG.isTraceEnabled()) {
            LOG.trace("Entering FormBackingObject.");
        }
        return typeF;
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
        final TypeForm typeF = (TypeForm) command;
        if (LOG.isTraceEnabled()) {
            LOG.trace("Type given in parameter :" + typeF.getType());
            LOG.trace("List of Entity to associate :" + typeF.getEntitiesIds());
        }
        if (!this.um.isSuperAdmin(request.getRemoteUser())) {            
            throw new PortletSecurityException(
                    getMessageSourceAccessor().getMessage("exception.notAuthorized.action"));  
        }
        Set<Long> eIds = new HashSet<Long>();
        if (typeF.getEntitiesIds() != null && typeF.getEntitiesIds().length > 0) {
            for (String str : typeF.getEntitiesIds()) {
                eIds.add(Long.valueOf(str));
            }
        }
        this.getTm().addType(typeF.getType(), new ArrayList<Long>(eIds));
        response.setRenderParameter(Constants.ACT, Constants.ACT_VIEW_TYPE);                       
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
        ModelAndView mav = new ModelAndView(Constants.ACT_VIEW_NOT_AUTH);
        if (!this.getUm().isSuperAdmin(request.getRemoteUser())) {            
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
        
        model.put(Constants.ATT_E_LIST, this.em.getEntitiesByUser(request.getRemoteUser()));
        if (this.um.isSuperAdmin(request.getRemoteUser())) {
            model.put(Constants.ATT_PM, RolePerm.ROLE_ADMIN.getMask());
        } else {
            model.put(Constants.ATT_PM, "0");
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
        response.setRenderParameter(Constants.ACT, Constants.ACT_VIEW_TYPE);
    }

    /**
     * Getter du membre em.
     * @return <code>EntityManager</code> le membre em.
     */
    public EntityManager getEm() {
        return em;
    }
    
    /**
     * Getter du membre tm.
     * @return <code>TypeManager</code> le membre tm.
     */
    public TypeManager getTm() {
        return tm;
    }
    
    /**
     * Getter du membre um.
     * @return <code>UserManager</code> le membre um.
     */
    public UserManager getUm() {
        return um;
    }
    
    /**
     * Setter du membre em.
     * @param em la nouvelle valeur du membre em. 
     */
    public void setEm(final EntityManager em) {
        this.em = em;
    }

    /**
     * Setter du membre tm.
     * @param tm la nouvelle valeur du membre tm. 
     */
    public void setTm(final TypeManager tm) {
        this.tm = tm;
    }

    /**
     * Setter du membre um.
     * @param um la nouvelle valeur du membre um. 
     */
    public void setUm(final UserManager um) {
        this.um = um;
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
    }
}
