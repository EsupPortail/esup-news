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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esco.portlets.news.services.EntityManager;
import org.esco.portlets.news.services.UserManager;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.mvc.AbstractController;
import org.uhp.portlets.news.NewsConstants;
import org.uhp.portlets.news.domain.Category;
import org.uhp.portlets.news.domain.RolePerm;
import org.uhp.portlets.news.service.CategoryManager;
import org.uhp.portlets.news.service.TopicManager;
import org.uhp.portlets.news.web.support.Constants;

@Controller
@RequestMapping(params = "action=viewCategory")
public class CategoryViewController extends AbstractController implements InitializingBean {

	@Autowired private CategoryManager cm=null;

	@Autowired private TopicManager tm=null;

	@Autowired private UserManager um=null;
	/** Manager of an Entity. */
	@Autowired
	private EntityManager em;

	private static final Log LOGGER = LogFactory.getLog(CategoryViewController.class);

	public void afterPropertiesSet() throws Exception {
		if ((this.cm == null) || (this.um==null) || (this.tm==null) || (this.em == null))
			throw new IllegalArgumentException("A CategoryManager, a topicManager, an userManager and an entityManager are required");
	}

	public ModelAndView handleRenderRequestInternal(RenderRequest request, RenderResponse response) throws Exception {
		Long id = Long.valueOf(request.getParameter(Constants.ATT_CAT_ID));
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("CategoryViewController:: entering method handleRenderRequestInternal: catId=" + id);
		}

		Category category = this.cm.getCategoryById(id);
		if (category == null) {
			throw new ObjectRetrievalFailureException(Category.class, id);
		}
		List<String> usersUid = new ArrayList<String>();
		usersUid.add(category.getCreatedBy());
		ModelAndView mav = new ModelAndView(Constants.ACT_VIEW_CAT);
		mav.addObject(Constants.OBJ_CATEGORY, category);
		mav.addObject(Constants.ATT_TYPE_LIST, this.cm.getTypesOfCategory(id));
		mav.addObject(Constants.OBJ_ENTITY, this.em.getEntityById(category.getEntityId()));
		mav.addObject(Constants.ATT_USER_LIST, this.um.getUsersByListUid(usersUid));
		mav.addObject(Constants.ATT_T_LIST, this.tm.getTopicListForCategoryByUser(id, request.getRemoteUser()));
		mav.addObject(Constants.ATT_PM, RolePerm.valueOf(
				this.um.getUserRoleInCtx(id, NewsConstants.CTX_C, request.getRemoteUser())).getMask());
		return mav;
	}

}
