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


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esco.portlets.news.services.EntityManager;
import org.esco.portlets.news.services.UserManager;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.ModelAndViewDefiningException;
import org.springframework.web.portlet.bind.PortletRequestUtils;
import org.springframework.web.portlet.mvc.SimpleFormController;
import org.uhp.portlets.news.NewsConstants;
import org.uhp.portlets.news.domain.Category;
import org.uhp.portlets.news.domain.RolePerm;
import org.uhp.portlets.news.domain.Topic;
import org.uhp.portlets.news.domain.UserRole;
import org.uhp.portlets.news.service.CategoryManager;
import org.uhp.portlets.news.service.TopicManager;
import org.uhp.portlets.news.web.support.Constants;

public class TopicAddController extends SimpleFormController implements InitializingBean {
	@Autowired private CategoryManager cm = null;
	@Autowired private TopicManager tm = null;
	@Autowired private UserManager um = null;
	/** Manager of an Entity. */
	@Autowired
	private EntityManager em;

	private static final Log LOGGER = LogFactory.getLog(TopicAddController.class);

	public TopicAddController() {
		super();
		setCommandClass(Topic.class);
		setCommandName(Constants.OBJ_TOPIC);
		setFormView(Constants.ACT_ADD_TOPIC);
		setSuccessView(Constants.ACT_VIEW_CAT);
	}

	public void afterPropertiesSet() throws Exception {
		if ((this.cm == null) || (this.tm == null) || (this.um == null) || (this.em == null))
			throw new IllegalArgumentException("A CategoryManager, a topicManager, a entityManager and a userManager are required");
	}

	@Override
	protected void onSubmitAction(ActionRequest request, ActionResponse response,
			Object command,	BindException errors) throws Exception {
		Topic topic = (Topic) command;
		securityCheck(request.getRemoteUser(), NewsConstants.CTX_C, topic.getCategoryId());
		this.tm.addTopic(topic);

		// Duplicate user rights defined on the category to the topic
		List<UserRole> lur = this.um.getUsersRolesForCtx(topic.getCategoryId(), NewsConstants.CTX_C);
		for (UserRole ur : lur) {
			if (!RolePerm.ROLE_USER.getName().equalsIgnoreCase(ur.getRole())) {
				this.um.addUserCtxRole(this.um.findUserByUid(ur.getPrincipal()), ur.getRole(), NewsConstants.CTX_T,
						topic.getTopicId());
			}
		}

		response.setRenderParameter(Constants.ACT, Constants.ACT_VIEW_CAT);
		response.setRenderParameter(Constants.ATT_CAT_ID, String.valueOf(((Topic) command).getCategoryId()));
	}

	private void securityCheck(final String userId, final String CtxType, final Long CtxId) throws Exception {
		ModelAndView mav = new ModelAndView(Constants.ACT_VIEW_NOT_AUTH);
		if(!this.um.isUserAdminInCtx(CtxId, CtxType, userId)) {
			if(LOGGER.isWarnEnabled()) {
				LOGGER.warn("AddTopic:: user " + userId +" has no permission for this action");
			}
			mav.addObject(Constants.MSG_ERROR, getMessageSourceAccessor().getMessage("news.alert.notAuthorizedAction"));
			throw new ModelAndViewDefiningException(mav);
		}
	}

	@Override
	protected Object formBackingObject(PortletRequest request)
	throws Exception {
		securityCheck(request.getRemoteUser(), NewsConstants.CTX_C, PortletRequestUtils.getLongParameter(request, Constants.ATT_CAT_ID));
		Topic topic = new Topic();
		topic.setCreatedBy(request.getRemoteUser());
		Long id = PortletRequestUtils.getLongParameter(request, Constants.ATT_CAT_ID);
		topic.setCategoryId(id);
		return topic;
	}


	@Override
	protected Map<String,Object> referenceData(PortletRequest request, Object command, Errors errors) throws Exception
	{
		Long id = ((Topic) command).getCategoryId();
		securityCheck(request.getRemoteUser(), NewsConstants.CTX_C, id);
		Map<String,Object> model = new HashMap<String,Object>();
		Category category = this.cm.getCategoryById(id);
		model.put(Constants.OBJ_CATEGORY, category);
		model.put(Constants.OBJ_ENTITY, this.em.getEntityById(category.getEntityId()));
		model.put(Constants.ATT_PM, RolePerm.valueOf(this.um.getUserRoleInCtx(id, NewsConstants.CTX_C, request.getRemoteUser())).getMask());
		return model;
	}
	@Override
	protected ModelAndView renderInvalidSubmit(RenderRequest request, RenderResponse response)
	throws Exception {
		return null;
	}
	@Override
	protected void handleInvalidSubmit(ActionRequest request, ActionResponse response)
	throws Exception {
		response.setRenderParameter(Constants.ACT,Constants.ACT_VIEW_NEWSSTORE);
	}

}

