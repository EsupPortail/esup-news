/**
 * ESUP-Portail News - Copyright (c) 2009 ESUP-Portail consortium
 * For any information please refer to http://esup-helpdesk.sourceforge.net
 * You may obtain a copy of the licence at http://www.esup-portail.org/license/
 */
package org.esco.portlets.news.web;

import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esco.portlets.news.domain.Entity;
import org.esco.portlets.news.services.EntityManager;
import org.esco.portlets.news.services.UserManager;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.mvc.AbstractController;
import org.uhp.portlets.news.web.support.Constants;

/**
 *
 * @author GIP RECIA - Gribonvald Julien
 * 9 févr. 2010
 */
public class EntityDeleteController extends AbstractController implements InitializingBean {
    
    /** Logger. */
    private static final Log LOG = LogFactory.getLog(EntityDeleteController.class);
    
    /** The Entity Manager.*/
    @Autowired 
    private EntityManager em;
    /** The User manager. */
    @Autowired 
    private UserManager um;
    
    /** Result of the delete's action. */
    private boolean deleted;
    /** Error message, in messages.properties. */
    private String msgKey;

    /**
     * Constructor of the object TypeDeleteController.java.
     */
    public EntityDeleteController() {
        super();
    }
    
    /**
     * @param request
     * @param response
     * @throws Exception
     * @see org.springframework.web.portlet.mvc.AbstractController#
     * handleActionRequest(javax.portlet.ActionRequest, javax.portlet.ActionResponse)
     */
    @Override
    public void handleActionRequest(final ActionRequest request,
            final ActionResponse response) throws Exception {
        Long entityId = Long.valueOf(request.getParameter(Constants.ATT_ENTITY_ID));
        if (!this.um.isSuperAdmin(request.getRemoteUser())) {
            msgKey = "news.alert.superUserOnly";
        } else if (this.em.deleteEntity(entityId)) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Entity [" + entityId + "] is removed");
            }
            this.deleted = true;
        } else {
            if (LOG.isWarnEnabled()) {
                LOG.warn("Entity [ " + entityId + "] is not empty, delete action is ignored ");
            }
            msgKey = "news.alert.EntityNotDetached";
        }
    }


    /**
     * @param request
     * @param response
     * @return <code>ModelAndView</code>
     * @throws Exception
     * @see org.springframework.web.portlet.mvc.AbstractController#
     * handleRenderRequest(javax.portlet.RenderRequest, javax.portlet.RenderResponse)
     */
    @Override
    public ModelAndView handleRenderRequest(final RenderRequest request,
            final RenderResponse response) throws Exception {
        if (this.deleted) { 
            if (LOG.isDebugEnabled()) {
                LOG.debug("Entity was removed.");
            }
            List<Entity> listEntity = this.em.getEntitiesByUser(request.getRemoteUser());
            ModelAndView mav = new ModelAndView(Constants.ACT_VIEW_HOME);
            mav.addObject(Constants.ATT_E_LIST, listEntity);            
            return mav;
        }   
        return new ModelAndView("Errors", "message", getMessageSourceAccessor().getMessage(msgKey));
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
     * @throws Exception
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(this.getEm(), "The property EntityManager em in class " + getClass().getSimpleName()
                + " must not be null.");
        Assert.notNull(this.getUm(), "The property UserManager um in class " + getClass().getSimpleName()
                + " must not be null.");
    }
}
