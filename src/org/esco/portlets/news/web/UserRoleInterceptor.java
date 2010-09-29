/**
 * ESUP-Portail News - Copyright (c) 2009 ESUP-Portail consortium
 * For any information please refer to http://esup-helpdesk.sourceforge.net
 * You may obtain a copy of the licence at http://www.esup-portail.org/license/
 */
package org.esco.portlets.news.web;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esco.portlets.news.services.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.handler.HandlerInterceptorAdapter;
import org.uhp.portlets.news.domain.RolePerm;
import org.uhp.portlets.news.web.support.Constants;

/**
 *
 * @author GIP RECIA - Gribonvald Julien
 * 1 mars 2010
 */
public class UserRoleInterceptor extends HandlerInterceptorAdapter {
    /** Logger. */
    private static final Log LOG = LogFactory.getLog(UserRoleInterceptor.class);
    
    /** Manager des Users. */
    @Autowired
    private UserManager um;

    /**
     * Constructeur de l'objet MenuInterceptor.java.
     */
    public UserRoleInterceptor() {
        super();
    }
    
    

    /**
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     * @see org.springframework.web.portlet.handler.HandlerInterceptorAdapter
     * #postHandleRender(javax.portlet.RenderRequest, javax.portlet.RenderResponse, java.lang.Object, 
     * org.springframework.web.portlet.ModelAndView)
     */
    @Override
    public void postHandleRender(final RenderRequest request, final RenderResponse response, 
                final Object handler, final ModelAndView modelAndView) throws Exception {
        
        if (!modelAndView.getModelMap().containsKey(Constants.ATT_PM)) {
            if (this.um.isSuperAdmin(request.getRemoteUser())) {            
                modelAndView.addObject(Constants.ATT_PM, RolePerm.ROLE_ADMIN.getMask());
                if (LOG.isDebugEnabled()) {
                    LOG.debug("UserRoleInterceptor the user has the role :" + RolePerm.ROLE_ADMIN.getName());
                }
            }
        } else if (LOG.isDebugEnabled()) {
            String name = null;
            for (RolePerm r : RolePerm.values()) {
                if (r.getMask() == Integer.valueOf(modelAndView.getModelMap().get(Constants.ATT_PM).toString())) {
                    name = r.getName();
                    break;
                }
            }
            LOG.debug("UserRoleInterceptor the user has the role :" 
                    + name);
          }
        
    }
    
}
