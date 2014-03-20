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

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.cmis.portlets.news.domain.Attachment;
import org.cmis.portlets.news.services.AttachmentManager;
import org.esco.portlets.news.services.EntityManager;
import org.esco.portlets.news.services.PermissionManager;
import org.esco.portlets.news.services.UserManager;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.bind.PortletRequestUtils;
import org.springframework.web.portlet.mvc.AbstractController;
import org.uhp.portlets.news.NewsConstants;
import org.uhp.portlets.news.domain.Category;
import org.uhp.portlets.news.domain.Item;
import org.uhp.portlets.news.domain.RolePerm;
import org.uhp.portlets.news.service.CategoryManager;
import org.uhp.portlets.news.service.ItemManager;
import org.uhp.portlets.news.service.TopicManager;
import org.uhp.portlets.news.web.support.Constants;

/**
 * modified by GIP RECIA - Gribonvald Julien.
 * 20 juil. 2010
 */
public class ItemViewController extends AbstractController implements InitializingBean {

	/** */
	@Autowired private ItemManager im;
	/** */
	@Autowired private TopicManager tm;
	/** */
	@Autowired private CategoryManager cm;
	/** */
	@Autowired private UserManager um;
	/** */
	@Autowired private PermissionManager pm;
	/** */
	@Autowired private AttachmentManager am;
	/** Manager of an Entity. */
	@Autowired private EntityManager em;
	/** */
	private String temporaryStoragePath;


	/**
	 * Constructeur de l'objet ItemViewController.java.
	 */
	public ItemViewController() {
		super();
	}

	/**
	 * @throws Exception
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(this.tm, "The property TypeManager tm in class "
				+ getClass().getSimpleName() + " must not be null.");
		Assert.notNull(this.cm, "The property CategoryManager cm in class "
				+ getClass().getSimpleName() + " must not be null.");
		Assert.notNull(this.um, "The property UserManager um in class "
				+ getClass().getSimpleName() + " must not be null.");
		Assert.notNull(this.em, "The property EntityManager em in class "
				+ getClass().getSimpleName() + " must not be null.");
		Assert.notNull(this.im, "The property ItemManager im in class "
				+ getClass().getSimpleName() + " must not be null.");
		Assert.notNull(this.am, "The property AttachmentManager am in class "
				+ getClass().getSimpleName() + " must not be null.");
		Assert.notNull(this.pm, "The property PermissionManager pm in class "
				+ getClass().getSimpleName() + " must not be null.");
	}

	/**
	 * @param request
	 * @param response
	 * @return ModelAndView
	 * @throws Exception
	 * @see org.springframework.web.portlet.mvc.AbstractController
	 * #handleRenderRequestInternal(javax.portlet.RenderRequest, javax.portlet.RenderResponse)
	 */
	@Override
	protected ModelAndView handleRenderRequestInternal(final RenderRequest request,
			final RenderResponse response) throws Exception {

		// clean attachments temporary directory
		this.am.cleanTempStorageDirectory(this.getPortletContext().getRealPath(temporaryStoragePath));

		final Long id = PortletRequestUtils.getLongParameter(request, Constants.ATT_ITEM_ID);
		final Item item = this.im.getItemById(id);
		final Long tIdFrom = PortletRequestUtils.getLongParameter(request, Constants.ATT_TOPIC_ID);
        final String uid = request.getRemoteUser();

		if (item != null) {
			List<String> usersUid = new ArrayList<String>();
			usersUid.add(uid);
			usersUid.add(item.getPostedBy());
			usersUid.add(item.getLastUpdatedBy());
			ModelAndView mav = new ModelAndView(Constants.ACT_VIEW_ITEM);
			mav.addObject(Constants.OBJ_ITEM, item);
			Category c = this.cm.getCategoryById(item.getCategoryId());
			usersUid.add(c.getCreatedBy());
			mav.addObject(Constants.OBJ_CATEGORY, c);
			mav.addObject(Constants.OBJ_ENTITY, this.em.getEntityById(c.getEntityId()));
			mav.addObject(Constants.ATT_T_LIST, this.im.getTopicListByItem(id));
			int perm = 0;
			if (tIdFrom != null) {
				perm = RolePerm.valueOf(this.pm.getRoleInCtx(tIdFrom, NewsConstants.CTX_T)).getMask();
			} else {
				perm = RolePerm.valueOf(this.pm.getRoleInCtx(item.getCategoryId(), NewsConstants.CTX_C)).getMask();
			}

			List<Attachment> attachments = this.am.getAttachmentsListByItem(id);
			mav.addObject(Constants.ATT_A_LIST, attachments);

			//mav.addObject("r", this.um.canValidate(uid, item) ? Constants.CONST_Y : Constants.CONST_N);
			mav.addObject(Constants.ATT_PM, perm);
			mav.addObject(Constants.ATT_USER_ID, uid);
			mav.addObject(Constants.ATT_USER_LIST, this.um.getUsersByListUid(usersUid));

			if (tIdFrom != null) {
				mav.addObject(Constants.OBJ_TOPIC, this.tm.getTopicById(tIdFrom));
			}

			return mav;
		}
		return new ModelAndView("Errors", "message", "Item with id " + id + " does not exist.");

    }

    /**
     * Getter du membre im.
     * @return <code>ItemManager</code> le membre im.
     */
    public ItemManager getIm() {
        return im;
    }

    /**
     * Setter du membre im.
     * @param im la nouvelle valeur du membre im. 
     */
    public void setIm(final ItemManager im) {
        this.im = im;
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
     * Getter du membre am.
     * @return <code>AttachmentManager</code> le membre am.
     */
    public AttachmentManager getAm() {
        return am;
    }

    /**
     * Setter du membre am.
     * @param am la nouvelle valeur du membre am. 
     */
    public void setAm(final AttachmentManager am) {
        this.am = am;
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
	 * @param temporaryStoragePath
	 *            the temporaryStoragePath to set
	 */
	public void setTemporaryStoragePath(String temporaryStoragePath) {
		this.temporaryStoragePath = temporaryStoragePath;
	}

	/**
	 * @return the temporaryStoragePath
	 */
	public String getTemporaryStoragePath() {
		return temporaryStoragePath;
	}

}
