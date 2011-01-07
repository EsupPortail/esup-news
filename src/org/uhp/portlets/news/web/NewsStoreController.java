package org.uhp.portlets.news.web;

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

import java.util.List;

import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esco.portlets.news.domain.Entity;
import org.esco.portlets.news.services.EntityManager;
import org.esco.portlets.news.services.UserManager;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.mvc.AbstractController;
import org.uhp.portlets.news.NewsConstants;
import org.uhp.portlets.news.domain.RolePerm;
import org.uhp.portlets.news.service.CategoryManager;
import org.uhp.portlets.news.util.HostUtils;
import org.uhp.portlets.news.web.support.Constants;

/**
 * Controller entry point.
 * modified by GIP RECIA - Gribonvald Julien
 * 1 mars 2010
 */
public class NewsStoreController extends AbstractController implements InitializingBean {

    /** Logger. */
    private static final Log LOG = LogFactory.getLog(NewsStoreController.class);  
    /** The EntityManager.*/
    @Autowired
    private EntityManager entityManager;
    /** The Category Manager. */
    @Autowired 
    private CategoryManager categoryManager;
    /** The User Manager.*/
    @Autowired 
    private UserManager userManager;

    /**
     * Constructeur de l'objet NewsStoreController.java.
     */
    public NewsStoreController() {
        super();
    }

    /**
     * @throws Exception
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    public void afterPropertiesSet() throws Exception {
        if ((this.categoryManager == null)  || (this.userManager == null)  || (this.entityManager == null)) {
            throw new IllegalArgumentException("CategoryManager, UserManager and EntityManager are required");
        }
    }

    /**
     * @param request
     * @param response
     * @return <code>ModelAndView</code>
     * @throws Exception
     * @see org.springframework.web.portlet.mvc.AbstractController
     * #handleRenderRequestInternal(javax.portlet.RenderRequest, javax.portlet.RenderResponse)
     */
    @Override
    protected ModelAndView handleRenderRequestInternal(final RenderRequest request, final RenderResponse response)
    throws Exception {
        final String  uid = request.getRemoteUser();
        boolean isSuperAdmin = false;
        PortletSession session;
        ModelAndView mav;
        String msgKey = "";
        boolean denied = false;


        if (!this.userManager.isPermitted(uid)) {
            msgKey = "exception.access.denied";
            denied = true;
        } else if (!this.userManager.isUserAccountEnabled(uid)) {
            msgKey = "exception.account.disabled";
            denied = true;
        }
        if (this.userManager.isSuperAdmin(uid)) {
            isSuperAdmin = true;
        }

        if (denied) {
            mav = new ModelAndView(Constants.ACT_VIEW_NOT_AUTH);
            mav.addObject(Constants.MSG_ERROR, getMessageSourceAccessor().getMessage(msgKey));	
            if (LOG.isDebugEnabled()) {
                LOG.debug("User " + uid + " is rejected : " + getMessageSourceAccessor().getMessage(msgKey));
            }
            return mav;
        }

        if (request.getPortletSession(false) == null) {		      
            this.userManager.updateUserLastAccess(uid);
        }

        session = request.getPortletSession(true);
        session.setAttribute("uid", request.getRemoteUser(), PortletSession.APPLICATION_SCOPE);

       
        List<Entity> listEntity = this.getEntityManager().getEntitiesByUser(uid);
        
        if (listEntity.size() == 1 && !isSuperAdmin) {
            /* Copy of Handler in EntityViewController to redirect automatically 
            the user when there is only one entity to display. */
            Entity entity = listEntity.get(0);
            mav = new ModelAndView(Constants.ACT_VIEW_ENTITY);
            mav.addObject(Constants.OBJ_ENTITY, entity);
            // Get all category for the user.
            mav.addObject(Constants.ATT_C_LIST, 
                    this.getCategoryManager().getListCategoryOfEntityByUser(uid, entity.getEntityId()));
            // Get rigths of the user in the context
            mav.addObject(Constants.ATT_PM, RolePerm.valueOf(
                    this.getUserManager().getUserRoleInCtx(entity.getEntityId(), NewsConstants.CTX_E, uid)).getMask());
            // Usefull for xml and opm links
            mav.addObject(Constants.ATT_PORTAL_URL,  HostUtils.getHostUrl(request));
            
        } else {
            mav = new ModelAndView(Constants.ACT_VIEW_HOME);
            if (isSuperAdmin) {
                mav.addObject(Constants.ATT_PM, RolePerm.ROLE_ADMIN.getMask());
            } 
            mav.addObject(Constants.ATT_E_LIST, listEntity);
        }
        
        
        return mav;
    }

    /**
     * Getter du membre entityManager.
     * @return <code>EntityManager</code> le membre entityManager.
     */
    public EntityManager getEntityManager() {
        return entityManager;
    }

    /**
     * Setter du membre entityManager.
     * @param entityManager la nouvelle valeur du membre entityManager. 
     */
    public void setEntityManager(final EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /**
     * Getter du membre categoryManager.
     * @return <code>CategoryManager</code> le membre categoryManager.
     */
    public CategoryManager getCategoryManager() {
        return categoryManager;
    }

    /**
     * Setter du membre categoryManager.
     * @param categoryManager la nouvelle valeur du membre categoryManager. 
     */
    public void setCategoryManager(final CategoryManager categoryManager) {
        this.categoryManager = categoryManager;
    }

    /**
     * Getter du membre userManager.
     * @return <code>UserManager</code> le membre userManager.
     */
    public UserManager getUserManager() {
        return userManager;
    }

    /**
     * Setter du membre userManager.
     * @param userManager la nouvelle valeur du membre userManager. 
     */
    public void setUserManager(final UserManager userManager) {
        this.userManager = userManager;
    }


}
