/**
 * ESUP-Portail News - Copyright (c) 2009 ESUP-Portail consortium
 * For any information please refer to http://esup-helpdesk.sourceforge.net
 * You may obtain a copy of the licence at http://www.esup-portail.org/license/
 */
package org.cmis.portlets.news.web;

import java.util.HashMap;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletRequest;
import javax.portlet.PortletSecurityException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cmis.portlets.news.dao.CmisSessionFactory;
import org.cmis.portlets.news.domain.CmisServer;
import org.cmis.portlets.news.services.AttachmentManager;
import org.cmis.portlets.news.utils.SimpleXOREncryption;
import org.esco.portlets.news.services.PermissionManager;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.ModelAndViewDefiningException;
import org.springframework.web.portlet.mvc.SimpleFormController;
import org.uhp.portlets.news.domain.RolePerm;
import org.uhp.portlets.news.web.support.Constants;

/**
 *
 * created by Anyware Services - Delphine Gavalda.
 *
 * 14 juin 2010
 */
public class CmisServerParamsController extends SimpleFormController implements InitializingBean {

	/** Logger. */
	@SuppressWarnings("unused")
	private static final Log LOG = LogFactory.getLog(CmisServerParamsController.class);
	/** AttachmentManager. */
	@Autowired
	private AttachmentManager am;
	/** PermissionManager. */
	@Autowired
	private PermissionManager pm;
	/** CmisSessionFactory. */
	@Autowired
	private CmisSessionFactory sf;



	/**
	 * Constructor.
	 */
	public CmisServerParamsController() {
		setCommandClass(CmisServerParamsForm.class);
		setCommandName(Constants.CMD_CMIS_SERV_PARAMS);
		setFormView(Constants.ACT_EDIT_CMIS_SERV_PARAMS);
		setSuccessView(Constants.ACT_VIEW_ATT_CONF);
	}

	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() throws Exception {
		if (this.am == null || this.pm == null || this.sf == null) {
			throw new IllegalArgumentException("An AttachmentManager, a PermissionManager are required");
		}
	}

	/**
	 * @see org.springframework.web.portlet.mvc.SimpleFormController#showForm(javax.portlet.RenderRequest, javax.portlet.RenderResponse, org.springframework.validation.BindException)
	 */
	@Override
	protected ModelAndView showForm(final RenderRequest request, final RenderResponse response,
			final BindException errors) throws Exception {
		if (!this.pm.isSuperAdmin()) {
			ModelAndView mav = new ModelAndView(Constants.ACT_VIEW_NOT_AUTH);
			mav.addObject(Constants.MSG_ERROR, getMessageSourceAccessor().getMessage("news.alert.superUserOnly"));
			throw new ModelAndViewDefiningException(mav);
		}
		return super.showForm(request, response, errors);
	}

	/**
	 * @see org.springframework.web.portlet.mvc.SimpleFormController#referenceData(javax.portlet.PortletRequest, java.lang.Object, org.springframework.validation.Errors)
	 */
	@Override
	protected Map<String, Object> referenceData(final PortletRequest request, final Object command, final Errors errors)
			throws Exception {

		Map<String, Object> model = new HashMap<String, Object>();

		// the user need to be SuperAdmin
		if (this.pm.isSuperAdmin()) {
			model.put(Constants.ATT_PM, RolePerm.ROLE_ADMIN.getMask());
		} else {
			model.put(Constants.ATT_PM, RolePerm.ROLE_USER.getMask());
		}
		return model;
	}

	/**
	 * @see org.springframework.web.portlet.mvc.AbstractFormController#formBackingObject(javax.portlet.PortletRequest)
	 */
	@Override
	protected Object formBackingObject(final  PortletRequest request) throws Exception {
		CmisServerParamsForm form = new CmisServerParamsForm();

		CmisServer servParams = am.getApplicationServer();
		if (servParams != null) {
			form.setServerId(servParams.getServerId().toString());

			form.setServerUrl(servParams.getServerUrl());
			form.setServerLogin(servParams.getServerLogin());
			form.setServerPwd(servParams.getServerPwd());
			form.setRepositoryId(servParams.getRepositoryId());
		}

		return form;
	}

	/**
	 * @see org.springframework.web.portlet.mvc.SimpleFormController#onSubmitAction(javax.portlet.ActionRequest, javax.portlet.ActionResponse, java.lang.Object, org.springframework.validation.BindException)
	 */
	@Override
	protected void onSubmitAction(final ActionRequest request, final ActionResponse response,
			final Object command, final BindException errors) throws Exception {
		CmisServerParamsForm form = (CmisServerParamsForm) command;

		if (!this.pm.isSuperAdmin()) {
			throw new PortletSecurityException(getMessageSourceAccessor().getMessage("exception.notAuthorized.action"));
		}

		CmisServer servParams = new CmisServer();

		long serverId = 0;
		if (StringUtils.isNotEmpty(form.getServerId())) {
			serverId = Long.valueOf(form.getServerId());
		}

		servParams.setGlobalServer("1");

		String serverUrl = form.getServerUrl();
		if (!serverUrl.startsWith("http://")) {
			serverUrl = "http://" + serverUrl;
		}
		if (!serverUrl.endsWith("/")) {
			serverUrl += "/";
		}
		servParams.setServerUrl(serverUrl.trim());
		servParams.setServerLogin(form.getServerLogin().trim());

		String toEncrypt = form.getServerLogin().trim() + form.getServerPwd().trim();
		String hash = SimpleXOREncryption.encryptDecrypt(toEncrypt);
		servParams.setServerPwd(hash);

		servParams.setRepositoryId(form.getRepositoryId().trim());

		if (serverId > 0) {
			servParams.setServerId(serverId);
			am.updateServerInfos(servParams);
			// delete the cmis session is exists
			sf.removeDefaultSession();

		} else {
			am.insertServerParams(servParams);
		}

		// update displayed fields
		form.setServerId(servParams.getServerId().toString());
		form.setDisplayedServerUrl(servParams.getServerUrl());
		form.setDisplayedServerLogin(servParams.getServerLogin());
		form.setDisplayedServerPwd(servParams.getServerPwd());
		form.setDisplayedRepositoryId(servParams.getRepositoryId());

		response.setRenderParameter(Constants.ACT, Constants.ACT_VIEW_ATT_CONF);
	}

}
