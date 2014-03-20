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
import org.esco.portlets.news.domain.Type;
import org.esco.portlets.news.services.PermissionManager;
import org.esco.portlets.news.services.TypeManager;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.util.Assert;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.mvc.AbstractController;
import org.uhp.portlets.news.web.support.Constants;

/**
 *
 * @author GIP RECIA - Gribonvald Julien
 * 9 févr. 2010
 */
public class TypeDeleteController extends AbstractController implements InitializingBean {
    
    /** Logger. */
    private static final Log LOG = LogFactory.getLog(TypeDeleteController.class);
    
    /** The Type Manager.*/
    @Autowired 
    private TypeManager tm;
 	/** The Permission Manager. */
    @Autowired
    private PermissionManager pm;
    
    /** Permet de savoir si l'action du delete a été effectuée correctement. */
    private boolean deleted;
    /** Message d'erreur à mapper dans le fichier messages.properties. */
    private String msgKey;

    /**
     * Constructeur de l'objet TypeDeleteController.java.
     */
    public TypeDeleteController() {
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
        Long typeId = Long.valueOf(request.getParameter(Constants.ATT_TYPE_ID));
        if (!this.getPm().isSuperAdmin()) {
            msgKey = "news.alert.superUserOnly";
        } else if (this.tm.deleteType(typeId)) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Type [" + typeId + "] is removed");
            }
            this.deleted = true;
        } else {
            if (LOG.isWarnEnabled()) {
                LOG.warn("Type [ " + typeId + "] is not empty, delete action is ignored ");
            }
            msgKey = "news.alert.TypeNotDetached";
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
            final List<Type> types = this.getTm().getAllTypes();
            if (types != null && !types.isEmpty()) {
                ModelAndView mav = new ModelAndView(Constants.ACT_VIEW_TYPE);
                mav.addObject(Constants.ATT_TYPE_LIST, types);            
                return mav;
            } 
            throw new ObjectRetrievalFailureException(Type.class, "No object found.");
        }   
        return new ModelAndView("Errors", "message", getMessageSourceAccessor().getMessage(msgKey));
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
        Assert.notNull(this.getTm(), "The property TypeManager tm in class " + getClass().getSimpleName()
                + " must not be null.");
        Assert.notNull(this.getPm(), "The property PermissionManager pm in class " + getClass().getSimpleName()
                + " must not be null.");
    }
}
