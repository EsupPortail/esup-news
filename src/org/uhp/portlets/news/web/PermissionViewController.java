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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esco.portlets.news.domain.IEscoUser;
import org.esco.portlets.news.services.EntityManager;
import org.esco.portlets.news.services.UserManager;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.ModelAndViewDefiningException;
import org.springframework.web.portlet.mvc.AbstractController;
import org.uhp.portlets.news.NewsConstants;
import org.uhp.portlets.news.domain.Category;
import org.uhp.portlets.news.domain.RolePerm;
import org.uhp.portlets.news.domain.Topic;
import org.uhp.portlets.news.domain.User;
import org.uhp.portlets.news.domain.UserRole;
import org.uhp.portlets.news.service.CategoryManager;
import org.uhp.portlets.news.service.TopicManager;
import org.uhp.portlets.news.web.support.Constants;

/**
 * Controller.
 * modified by GIP RECIA - Gribonvald Julien
 * 8 juil. 09
 */
public class PermissionViewController extends AbstractController implements InitializingBean {

    /** Logger. */
	private static final Log LOG = LogFactory.getLog(PermissionViewController.class);

	/** The User Manager.*/
	@Autowired 
	private UserManager um;
	/** The Entity Manager. */
	@Autowired 
	private EntityManager em;
	/** The CategoryManager. */
	@Autowired 
	private CategoryManager cm;
	/** The Topic manager. */
	@Autowired 
	private TopicManager tm;
	/** The context of accesses. */
	private String ctx;

	/** Constructor. */
	public PermissionViewController() {
		super();
	}

	
	/**
	 * @param request
	 * @param response
	 * @return <code>ModelAndView</code>
	 * @throws Exception
	 */
    @Override
	protected ModelAndView handleRenderRequestInternal(final RenderRequest request, final RenderResponse response) 
	   throws Exception {
        List<String> usersUid = new ArrayList<String>();
		final Long ctxId = Long.valueOf(request.getParameter(Constants.ATT_CTX_ID));
		if (!this.getUm().isUserAdminInCtx(ctxId, getCtx(), request.getRemoteUser())) {
			LOG.debug("PermissionView: user has no role admin");
			ModelAndView mav = new ModelAndView(Constants.ACT_VIEW_NOT_AUTH);
			//String msg = "you are not authorized for this action";
			mav.addObject(Constants.MSG_ERROR, 
			        getMessageSourceAccessor().getMessage("exception.notAuthorized.action"));
			throw new ModelAndViewDefiningException(mav);  
		}

		ModelAndView mav = new ModelAndView(Constants.ACT_VIEW_PERM + getCtx());
		mav.addObject(Constants.ATT_CTX_ID, ctxId);
		if (request.getParameter("msg") != null) {
		    mav.addObject("msg", getMessageSourceAccessor().getMessage(request.getParameter("msg")));
		}
		if (getCtx().equalsIgnoreCase(NewsConstants.CTX_E)) {
            mav.addObject(Constants.OBJ_ENTITY, this.getEm().getEntityById(ctxId));
        } else if (getCtx().equalsIgnoreCase(NewsConstants.CTX_C)) {
            Category c = this.getCm().getCategoryById(ctxId);
            mav.addObject(Constants.OBJ_CATEGORY, c);
            mav.addObject(Constants.OBJ_ENTITY, this.getEm().getEntityById(c.getEntityId()));
		} else if (getCtx().equalsIgnoreCase(NewsConstants.CTX_T)) { 
			Topic topic = this.getTm().getTopicById(ctxId);
			mav.addObject(Constants.OBJ_TOPIC, topic);
			Category c = this.getCm().getCategoryById(topic.getCategoryId());
			mav.addObject(Constants.OBJ_CATEGORY, c);
			mav.addObject(Constants.OBJ_ENTITY, this.em.getEntityById(c.getEntityId()));
		}
		// get uid List from users in role
		Map<String, List<UserRole>> usersRoles = this.um.getUserRolesByCtxId(ctxId, this.getCtx());
		for (String r : usersRoles.keySet()) {
		    for (UserRole ur : usersRoles.get(r) ) {
		        if (!usersUid.contains(ur.getPrincipal())) {
		            usersUid.add(ur.getPrincipal());
		        }
		    }
		}
		mav.addObject(Constants.ATT_LIST, usersRoles);
		// add the super admin user's uid to the uid list
		List<IEscoUser> suser = this.um.getAllSuperUsers();
		for (User u : suser) {
		    usersUid.add(u.getUserId());
		}
		mav.addObject(Constants.ATT_SUSER_LIST, this.um.getAllSuperUsers());
		mav.addObject(Constants.ATT_PM, RolePerm.valueOf(this.um.getUserRoleInCtx(ctxId, 
		        this.getCtx(), request.getRemoteUser())).getMask());
		mav.addObject(Constants.ATT_LDAP_DISPLAY, this.um.getLdapUserService().getSearchDisplayedAttributes());
		mav.addObject(Constants.ATT_USER_LIST, this.um.getUsersByListUid(usersUid));
		if (LOG.isTraceEnabled()) {
		    LOG.trace(" ModelAndView : " + mav.toString());
		}
		return mav;	
	}

    /**
     * @throws Exception
     */
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(this.getUm(), "The property UserManager um in class " + getClass().getSimpleName()
                + " must not be null.");
        Assert.hasLength(this.ctx, "ctx property should be defined...");
        Assert.notNull(this.getCm(), "The property CategoryManager cm in class " + getClass().getSimpleName()
                + " must not be null.");
        Assert.notNull(this.getTm(), "The property TopicManager tm in class " + getClass().getSimpleName()
                + " must not be null.");
        Assert.notNull(this.getEm(), "The property EntityManager em in class " + getClass().getSimpleName()
                + " must not be null.");
    }


    /**
     * Getter du membre um.
     * @return <code>EscoUserManager</code> le membre um.
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
     * @return <code>TopicManager</code> le membre tm.
     */
    public TopicManager getTm() {
        return tm;
    }


    /**
     * Setter du membre tm.
     * @param tm la nouvelle valeur du membre tm. 
     */
    public void setTm(final TopicManager tm) {
        this.tm = tm;
    }


    /**
     * Getter du membre ctx.
     * @return <code>String</code> le membre ctx.
     */
    public String getCtx() {
        return ctx;
    }


    /**
     * Setter du membre ctx.
     * @param ctx la nouvelle valeur du membre ctx. 
     */
    public void setCtx(final String ctx) {
        this.ctx = ctx;
    }

}