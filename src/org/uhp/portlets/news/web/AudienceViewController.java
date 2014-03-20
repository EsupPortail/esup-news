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
import org.esco.portlets.news.services.EntityManager;
import org.esco.portlets.news.services.PermissionManager;
import org.esco.portlets.news.services.UserManager;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.mvc.AbstractController;
import org.uhp.portlets.news.NewsConstants;
import org.uhp.portlets.news.domain.Category;
import org.uhp.portlets.news.domain.RolePerm;
import org.uhp.portlets.news.domain.Subscriber;
import org.uhp.portlets.news.domain.Topic;
import org.uhp.portlets.news.service.CategoryManager;
import org.uhp.portlets.news.service.SubscribeService;
import org.uhp.portlets.news.service.TopicManager;
import org.uhp.portlets.news.web.support.Constants;

/**
 * modified by GIP RECIA - Gribonvald Julien.
 * 14 avr. 2010
 */
public class AudienceViewController extends AbstractController implements InitializingBean {


    /** Logger. */
    private static final Log LOG = LogFactory.getLog(AudienceViewController.class);

    /** */
    private String ctx;

    /** */
    @Autowired private SubscribeService subService;
    /** */
    @Autowired private CategoryManager cm;
    /** */
    @Autowired private TopicManager tm;
    /** */
    @Autowired private UserManager um;
    /** */
    @Autowired private PermissionManager pm;
    /** The Entity Manager. */
    @Autowired
    private EntityManager em;

    /**
     * Constructeur de l'objet AudienceViewController.java.
     */
    public AudienceViewController() {
        super();
    }
    /**
     * @throws Exception
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(this.getSubService(), "A SubscribeService is required.");
        Assert.notNull(this.getCm(), "A CategoryManager is required.");
        Assert.notNull(this.getTm(), "A TopicManager is required.");
		Assert.notNull(this.getPm(), "A PermissionManager is required.");
        Assert.notNull(this.getUm(), "A UserManager is required.");
        Assert.notNull(this.getEm(), "A EntityManager is required.");
        Assert.hasLength(this.getCtx(), "ctx property should be defined...");
    }

    /**
     * @param request
     * @param response
     * @return <code>ModelAndView</code>
     * @throws Exception
     * @see org.springframework.web.portlet.mvc.AbstractController#
     * handleRenderRequestInternal(javax.portlet.RenderRequest, javax.portlet.RenderResponse)
     */
    @Override
    protected ModelAndView handleRenderRequestInternal(final RenderRequest request,
            final RenderResponse response) throws Exception {
        final Long ctxId = Long.valueOf(request.getParameter(Constants.ATT_CTX_ID));

        final ModelAndView mav = new ModelAndView(Constants.ACT_VIEW_AUDIENCE + this.ctx);
        List<String> usersUid = new ArrayList<String>();

        mav.addObject(Constants.ATT_CTX_ID, ctxId);
        if (this.getCtx().equalsIgnoreCase(NewsConstants.CTX_E)) {
            mav.addObject(Constants.OBJ_ENTITY, this.getEm().getEntityById(ctxId));
        } else if (this.getCtx().equalsIgnoreCase(NewsConstants.CTX_C)) {
            Category c = this.getCm().getCategoryById(ctxId);
            mav.addObject(Constants.OBJ_CATEGORY, c);
            mav.addObject(Constants.OBJ_ENTITY, this.getEm().getEntityById(c.getEntityId()));
        } else if (this.getCtx().equalsIgnoreCase(NewsConstants.CTX_T)) {
            final Topic topic = this.getTm().getTopicById(ctxId);
            Category c = this.getCm().getCategoryById(topic.getCategoryId());
            mav.addObject(Constants.OBJ_TOPIC, topic);
            mav.addObject(Constants.OBJ_CATEGORY, c);
            mav.addObject(Constants.OBJ_ENTITY, this.getEm().getEntityById(c.getEntityId()));
        }

        // get uid List from subscribers
        Map<String, List<Subscriber>> subcribers = this.getSubService().getSubscribersByCtxId(ctxId, this.getCtx());
        for (Map.Entry<String, List<Subscriber>> ls : subcribers.entrySet()) {
			for (Subscriber s : ls.getValue()) {
				if (s.getIsGroup() == 0 && !usersUid.contains(s.getPrincipal())) {
					usersUid.add(s.getPrincipal());
				}
			}
		}

        mav.addObject(Constants.ATT_LIST, subcribers);
        mav.addObject(Constants.ATT_PM, RolePerm.valueOf(
                this.getPm().getRoleInCtx(ctxId, this.getCtx())).getMask());
        mav.addObject(Constants.ATT_LDAP_DISPLAY, this.getUm().getLdapUserService().getSearchDisplayedAttributes());
        mav.addObject(Constants.ATT_USER_LIST, this.getUm().getUsersByListUid(usersUid));
        if (LOG.isTraceEnabled()) {
            LOG.trace(" ModelAndView : " + mav.toString());
        }
        return mav;

    }

    /**
     * Getter du membre subService.
     * @return <code>SubscribeService</code> le membre subService.
     */
    public SubscribeService getSubService() {
        return subService;
    }

    /**
     * Setter du membre subService.
     * @param subService la nouvelle valeur du membre subService. 
     */
    public void setSubService(final SubscribeService subService) {
        this.subService = subService;
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

