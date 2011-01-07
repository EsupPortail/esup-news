/**
 * ESUP-Portail News - Copyright (c) 2009 ESUP-Portail consortium
 * For any information please refer to http://esup-helpdesk.sourceforge.net
 * You may obtain a copy of the licence at http://www.esup-portail.org/license/
 */
package org.esco.portlets.news.web;

import java.util.HashMap;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletRequest;
import javax.portlet.PortletSecurityException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esco.portlets.news.domain.Filter;
import org.esco.portlets.news.domain.FilterOperator;
import org.esco.portlets.news.domain.FilterType;
import org.esco.portlets.news.services.EntityManager;
import org.esco.portlets.news.services.UserManager;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.ModelAndViewDefiningException;
import org.springframework.web.portlet.bind.PortletRequestUtils;
import org.springframework.web.portlet.mvc.SimpleFormController;
import org.uhp.portlets.news.domain.RolePerm;
import org.uhp.portlets.news.web.support.Constants;

/**
 * Controller for adding a filter.
 * @author GIP RECIA - Gribonvald Julien
 * 19 mai 2010
 */
public class FilterEditController extends SimpleFormController implements InitializingBean {

    /** Logger. */
    private static final Log LOG = LogFactory.getLog(FilterEditController.class);
    
    /** Manager d'une Entity. */
    @Autowired
    private EntityManager em;
    /** Manager des Users. */
    @Autowired
    private UserManager um;

    /**
     * Constructor of FilterAddController.java.
     */
    public FilterEditController() {
        super();
        setCommandClass(Filter.class); 
        setCommandName(Constants.OBJ_FILTER);
        setFormView(Constants.ACT_EDIT_FILTER);
        setSuccessView(Constants.ACT_VIEW_FILTERS);
    }

    /**
     * @param request
     * @return Object
     * @throws Exception
     * @see org.springframework.web.portlet.mvc.AbstractFormController#formBackingObject(javax.portlet.PortletRequest)
     */
    @Override
    protected Object formBackingObject(final PortletRequest request) throws Exception {
        Filter filter;
        if (LOG.isTraceEnabled()) {
            LOG.trace("Entering FormBackingObject.");
        }
        Long filterId = PortletRequestUtils.getLongParameter(request, Constants.ATT_ID); 
        filter = this.getEm().getFilter(filterId);
        return filter;
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
        final Filter filter = (Filter) command;
        if (LOG.isTraceEnabled()) {
            LOG.trace("Filter given in parameter :" + filter);
        }
        if (!this.um.isSuperAdmin(request.getRemoteUser())) {            
            throw new PortletSecurityException(
                    getMessageSourceAccessor().getMessage("exception.notAuthorized.action"));  
        }
        if (filter.getType().equals(FilterType.Group)) {
            filter.setOperator(FilterOperator.EQUAL);
        }
        this.getEm().updateFilterToEntity(filter);
        response.setRenderParameter(Constants.ACT, Constants.ACT_VIEW_FILTERS);
        response.setRenderParameter(Constants.ATT_ENTITY_ID, String.valueOf(filter.getEntityId()));
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
        Filter filter = (Filter) command;
        Map<String, Object> model = new HashMap<String, Object>();        
        model.put(Constants.OBJ_ENTITY, this.em.getEntityById(filter.getEntityId()));
        model.put(Constants.ATT_FILTER_TYPE, FilterType.values());
        model.put(Constants.ATT_FILTER_OPERATOR, FilterOperator.values());
        model.put(Constants.ATT_FILTER_LDAP_ATTRS, this.um.getLdapUserService().getFilterSearchAttributes());
        if (this.um.isSuperAdmin(request.getRemoteUser())) {
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
     * Getter du membre em.
     * @return <code>EntityManager</code> le membre em.
     */
    public EntityManager getEm() {
        return em;
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
        Assert.notNull(this.getUm(), "The property UserManager um in class " 
                + getClass().getSimpleName() + " must not be null.");
        Assert.notNull(this.getEm(), "The property EntityManager em in class " 
                + getClass().getSimpleName() + " must not be null.");
    }
}
