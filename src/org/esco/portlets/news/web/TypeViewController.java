/**
 * ESUP-Portail News - Copyright (c) 2009 ESUP-Portail consortium
 * For any information please refer to http://esup-helpdesk.sourceforge.net
 * You may obtain a copy of the licence at http://www.esup-portail.org/license/
 */
package org.esco.portlets.news.web;

import java.util.List;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esco.portlets.news.domain.Type;
import org.esco.portlets.news.services.TypeManager;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.mvc.AbstractController;
import org.uhp.portlets.news.util.HostUtils;
import org.uhp.portlets.news.web.support.Constants;

/**
 * Controller.
 * @author GIP RECIA - Gribonvald Julien
 * 9 févr. 2010
 */
@Controller
public class TypeViewController extends AbstractController implements InitializingBean {
    
    /** Logger. */
    private static final Log LOG = LogFactory.getLog(TypeViewController.class);
    
    /** The Type Manager.*/
    @Autowired 
    private TypeManager tm;

    /**
     * Constructeur de l'objet TypeViewController.java.
     */
    public TypeViewController() {
        super();
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
        if (LOG.isDebugEnabled()) {
            LOG.debug("TypeViewController:: entering method handleRenderRequestInternal.");
        }
        final List<Type> types = this.getTm().getAllTypes();
        if (types != null && !types.isEmpty()) {
            ModelAndView mav = new ModelAndView(Constants.ACT_VIEW_TYPE);
            mav.addObject(Constants.ATT_TYPE_LIST, types);
            mav.addObject(Constants.ATT_PORTAL_URL,  HostUtils.getHostUrl(request));
            return mav;
        } 
        throw new ObjectRetrievalFailureException(Type.class, "No object found.");
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
        Assert.notNull(this.getTm(), "The property TypeManager tm in class " + getClass().getSimpleName()
                + " must not be null.");
    }
}
