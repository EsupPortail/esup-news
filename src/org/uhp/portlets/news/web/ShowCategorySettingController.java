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
package org.uhp.portlets.news.web;

import java.util.ArrayList;
import java.util.List;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.esco.portlets.news.services.EntityManager;
import org.esco.portlets.news.services.UserManager;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.util.Assert;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.mvc.AbstractController;
import org.uhp.portlets.news.NewsConstants;
import org.uhp.portlets.news.domain.Category;
import org.uhp.portlets.news.domain.RolePerm;
import org.uhp.portlets.news.service.CategoryManager;
import org.uhp.portlets.news.web.support.Constants;

/**
 *
 * modification: GIP RECIA - Gribonvald Julien
 * 9 févr. 2010
 */
public class ShowCategorySettingController extends AbstractController implements InitializingBean {
	/** */
	@Autowired
	private CategoryManager cm;
	/** */
	@Autowired
	private UserManager um;
	/** Manager of an Entity. */
	@Autowired
	private EntityManager em;

	/**
	 * Constructeur de l'objet ShowCategorySettingController.java.
	 */
	public ShowCategorySettingController() {
		super();
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
	protected ModelAndView handleRenderRequestInternal(final RenderRequest request, final RenderResponse response)
	throws Exception {
		Category c = this.cm.getCategoryById(Long.valueOf(request.getParameter(Constants.ATT_CAT_ID)));
		if (c == null) {
			throw new ObjectRetrievalFailureException(Category.class, "Category does not exist.");
		}
		List<String> usersUid = new ArrayList<String>();
		usersUid.add(c.getCreatedBy());
		ModelAndView mav = new ModelAndView(Constants.ACT_VIEW_C_SETTING);
		mav.addObject(Constants.OBJ_CATEGORY, c);
		mav.addObject(Constants.ATT_TYPE_LIST, this.cm.getTypesOfCategory(c.getCategoryId()));
		mav.addObject(Constants.OBJ_ENTITY, this.getEm().getEntityById(c.getEntityId()));
		mav.addObject(Constants.ATT_PM, RolePerm.valueOf(this.getUm().getUserRoleInCtx(
				c.getCategoryId(), NewsConstants.CTX_C, request.getRemoteUser())).getMask());
		mav.addObject(Constants.ATT_USER_LIST, this.um.getUsersByListUid(usersUid));
		return mav;
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
	 * @throws Exception
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(this.getCm(), "The property CategoryManager cm in class " + getClass().getSimpleName()
				+ " must not be null.");
		Assert.notNull(this.getUm(), "The property UserManager um in class " + getClass().getSimpleName()
				+ " must not be null.");
		Assert.notNull(this.getUm(), "The property EntityManager em in class " + getClass().getSimpleName()
				+ " must not be null.");
	}


}
