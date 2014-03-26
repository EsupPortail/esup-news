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

import org.esco.portlets.news.services.EntityManager;
import org.esco.portlets.news.services.UserManager;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.mvc.AbstractController;
import org.uhp.portlets.news.NewsConstants;
import org.uhp.portlets.news.domain.Category;
import org.uhp.portlets.news.domain.RolePerm;
import org.uhp.portlets.news.service.CategoryManager;
import org.uhp.portlets.news.service.TopicManager;
import org.uhp.portlets.news.util.FeedInfoUtil;
import org.uhp.portlets.news.web.support.Constants;



public class TopicFeedsViewController extends AbstractController implements InitializingBean {
	@Autowired private CategoryManager cm=null;
	@Autowired private TopicManager tm=null;
	@Autowired private UserManager um=null;
	/** Manager of an Entity. */
	@Autowired
	private EntityManager em;
	private static final String FEED = "feed";
	private String feedType;

	public void afterPropertiesSet() throws Exception {
		if ((this.cm == null) || (this.um == null) || (this.tm == null) || (this.em == null))
			throw new IllegalArgumentException("A CategoryManager, a UserManager, a topicManager and a entityManager are required");
	}

	@Override
	protected ModelAndView handleRenderRequestInternal(final RenderRequest request, final RenderResponse response) throws Exception {

		final Long id = Long.valueOf(request.getParameter(Constants.ATT_CAT_ID));
		final Category c = this.cm.getCategoryById(id);
		if (c == null) {
			throw new ObjectRetrievalFailureException(Category.class, id);
		}
		List<String> usersUid = new ArrayList<String>();
		usersUid.add(c.getCreatedBy());

		final String uid = request.getRemoteUser();
		ModelAndView mav = new ModelAndView(Constants.ACT_VIEW_XMLTOPICS);
		mav.addObject(FEED, FeedInfoUtil.getFeedInfo(request, this.feedType));
		mav.addObject(Constants.OBJ_CATEGORY, c);
		mav.addObject(Constants.OBJ_ENTITY, this.em.getEntityById(c.getEntityId()));
		mav.addObject(Constants.ATT_T_LIST, this.tm.getTopicListForCategoryByUser(id, uid));
		mav.addObject(Constants.ATT_PM, RolePerm.valueOf(
				this.um.getUserRoleInCtx(id, NewsConstants.CTX_C, uid)).getMask());
		mav.addObject(Constants.ATT_USER_LIST, this.um.getUsersByListUid(usersUid));
		return mav;
	}

	public String getFeedType() {
		return feedType;
	}

	public void setFeedType(final String feedType) {
		this.feedType = feedType;
	}

}
