/**
 * ESUP-Portail News - Copyright (c) 2009 ESUP-Portail consortium
 * For any information please refer to http://esup-helpdesk.sourceforge.net
 * You may obtain a copy of the licence at http://www.esup-portail.org/license/
 */
package org.esco.portlets.news.web;

import java.util.List;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esco.portlets.news.domain.Filter;
import org.esco.portlets.news.domain.FilterType;
import org.esco.portlets.news.services.EntityManager;
import org.esco.portlets.news.services.UserManager;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.util.Assert;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.mvc.AbstractController;
import org.uhp.portlets.news.NewsConstants;
import org.uhp.portlets.news.domain.RolePerm;
import org.uhp.portlets.news.web.support.Constants;

/**
 *
 * @author GIP RECIA - Gribonvald Julien
 * 21 mai 2010
 */
public class FilterDeleteController extends AbstractController implements InitializingBean {
    
    /** Logger. */
    private static final Log LOG = LogFactory.getLog(FilterDeleteController.class);
    
    /** The Entity Manager.*/
    @Autowired 
    private EntityManager em;
    /** The User manager. */
    @Autowired 
    private UserManager um;
    
    /** Permet de savoir si l'action du delete a été effectuée correctement. */
    private boolean deleted;
    /** Message d'erreur à mapper dans le fichier messages.properties. */
    private String msgKey;
    /** */
    private Long entityId;
    
    /**
     * Constructor of FilterDeleteController.java.
     */
    public FilterDeleteController() {
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
        Long filterId = Long.valueOf(request.getParameter(Constants.ATT_FILTER_ID));
        entityId = Long.valueOf(request.getParameter(Constants.ATT_ENTITY_ID));
        if (!this.um.isSuperAdmin(request.getRemoteUser())) {
            msgKey = "news.alert.superUserOnly";
        } else if (this.em.deleteFilterOfEntity(filterId, entityId)) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Filter [ filterId=" + filterId + ", entityId=" + entityId + " ] is removed.");
            }
            this.deleted = true;
        } else {
            if (LOG.isWarnEnabled()) {
                LOG.warn("Filter [ filterId=" + filterId + ", entityId=" + entityId + " ] wasn't found.");
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
            if (LOG.isDebugEnabled()) {
                LOG.debug("FilterViewController:: entering method handleRenderRequestInternal - entityId=" + entityId);
            }
            final Map<FilterType, List<Filter>> filters = this.getEm().getFiltersByTypeOfEntity(entityId);
            if (filters != null && !filters.isEmpty()) {
                ModelAndView mav = new ModelAndView(Constants.ACT_VIEW_FILTERS);
                mav.addObject(Constants.ATT_FILTER_MAP, filters);
                mav.addObject(Constants.OBJ_ENTITY, this.em.getEntityById(entityId));
                mav.addObject(Constants.ATT_PM, RolePerm.valueOf(
                        this.um.getUserRoleInCtx(entityId, NewsConstants.CTX_E, request.getRemoteUser())).getMask());
                return mav;
            } 
            throw new ObjectRetrievalFailureException(Filter.class, "entityId=" + entityId);
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
