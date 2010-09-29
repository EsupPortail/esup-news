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
import org.esco.portlets.news.services.UserManager;
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
    private static final Log LOG = LogFactory.getLog(CmisServerParamsController.class);

    @Autowired
    private AttachmentManager am;
    @Autowired
    private UserManager um;
    @Autowired
    private CmisSessionFactory sf;

    public void afterPropertiesSet() throws Exception {
	if (this.am == null || this.um == null || this.sf == null) {
	    throw new IllegalArgumentException("An AttachmentManager, a User Manager are required");
	}
    }

    public CmisServerParamsController() {
	setCommandClass(CmisServerParamsForm.class);
	setCommandName(Constants.CMD_CMIS_SERV_PARAMS);
	setFormView(Constants.ACT_EDIT_CMIS_SERV_PARAMS);
	setSuccessView(Constants.ACT_VIEW_ATT_CONF);
    }

    @Override
    protected ModelAndView showForm(RenderRequest request, RenderResponse response, BindException errors)
	    throws Exception {
	if (!this.um.isSuperAdmin(request.getRemoteUser())) {
	    ModelAndView mav = new ModelAndView(Constants.ACT_VIEW_NOT_AUTH);
	    mav.addObject(Constants.MSG_ERROR, getMessageSourceAccessor().getMessage("news.alert.superUserOnly"));
	    throw new ModelAndViewDefiningException(mav);
	}
	return super.showForm(request, response, errors);
    }

    @Override
    protected Map<String, Object> referenceData(final PortletRequest request, final Object command, final Errors errors)
	    throws Exception {
	String uid = request.getRemoteUser();

	Map<String, Object> model = new HashMap<String, Object>();

	// the user need to be SuperAdmin
	if (this.um.isSuperAdmin(uid)) {
	    model.put(Constants.ATT_PM, RolePerm.ROLE_ADMIN.getMask());
	} else {
	    model.put(Constants.ATT_PM, RolePerm.ROLE_USER.getMask());
	}
	return model;
    }

    @Override
    protected Object formBackingObject(PortletRequest request) throws Exception {
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

    @Override
    protected void onSubmitAction(ActionRequest request, ActionResponse response, Object command, BindException errors)
	    throws Exception {
	CmisServerParamsForm form = (CmisServerParamsForm) command;

	if (!this.um.isSuperAdmin(request.getRemoteUser())) {
	    throw new PortletSecurityException("you are not authorized for this action");
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
	servParams.setServerUrl(serverUrl);
	servParams.setServerLogin(form.getServerLogin());

	String toEncrypt = form.getServerLogin() + form.getServerPwd();
	String hash = SimpleXOREncryption.encryptDecrypt(toEncrypt);
	servParams.setServerPwd(hash);

	servParams.setRepositoryId(form.getRepositoryId());

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
