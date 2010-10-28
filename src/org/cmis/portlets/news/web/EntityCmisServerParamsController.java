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
import org.esco.portlets.news.domain.Entity;
import org.esco.portlets.news.services.EntityManager;
import org.esco.portlets.news.services.UserManager;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.ModelAndViewDefiningException;
import org.springframework.web.portlet.mvc.SimpleFormController;
import org.uhp.portlets.news.NewsConstants;
import org.uhp.portlets.news.domain.RolePerm;
import org.uhp.portlets.news.web.support.Constants;

/**
 * 
 * created by Anyware Services - Delphine Gavalda.
 * 
 * 14 juin 2010
 */
public class EntityCmisServerParamsController extends SimpleFormController implements InitializingBean {

    /** Logger. */
    private static final Log LOG = LogFactory.getLog(EntityCmisServerParamsController.class);

    @Autowired
    private AttachmentManager am;
    /** The User Manager. */
    @Autowired
    private UserManager um;
    /** The Entity Manager. */
    @Autowired
    private EntityManager em;
    @Autowired
    private CmisSessionFactory sf;


    /**
     * Constructor.
     */
    public EntityCmisServerParamsController() {
	setCommandClass(CmisServerParamsForm.class);
	setCommandName(Constants.CMD_CMIS_SERV_PARAMS);
	setFormView(Constants.ACT_EDIT_E_CMIS_SERV_PARAMS);
	setSuccessView(Constants.ACT_VIEW_E_ATT_CONF);
    }
    
    public void afterPropertiesSet() throws Exception {
	if (this.am == null || this.um == null || this.em == null || this.sf == null) {
	    throw new IllegalArgumentException(
		    "An AttachmentManager, a User Manager and an Entity Manager are required");
	}
    }

    @Override
    protected ModelAndView showForm(final RenderRequest request, final RenderResponse response, 
	    				final BindException errors)
	    throws Exception {
	BindException newError = null;
	String eid = request.getParameter(Constants.ATT_ENTITY_ID);
	String cancel = request.getParameter("cancel");
	if (eid != null) {
	    final Long ctxId = Long.valueOf(eid);
	    if (!this.um.isUserAdminInCtx(ctxId, NewsConstants.CTX_E, request.getRemoteUser())) {
		LOG.debug("EntityCmisServerParams view: user has no role admin");
		ModelAndView mav = new ModelAndView(Constants.ACT_VIEW_NOT_AUTH);
		String msg = "you are not authorized for this action";
		mav.addObject(Constants.MSG_ERROR, msg);
		throw new ModelAndViewDefiningException(mav);
	    }
	}
	if (cancel != null) {
	    BindingResult bindingResult = errors.getBindingResult();
	    newError = new BindException(bindingResult);
	}
	if (newError != null) {
	    return super.showForm(request, response, newError);
	} else {  
	    return super.showForm(request, response, errors);
	}
    }

    @Override
    protected Map<String, Object> referenceData(final PortletRequest request, final Object command, final Errors errors)
	    throws Exception {
	long eId = ((CmisServerParamsForm) command).getEntityId();
	String uid = request.getRemoteUser();

	Map<String, Object> model = new HashMap<String, Object>();

	// the user need to be SuperAdmin or Admin.
	if (this.um.isSuperAdmin(uid)) {
	    model.put(Constants.ATT_PM, RolePerm.ROLE_ADMIN.getMask());
	} else if (this.um.isUserAdminInCtx(eId, NewsConstants.CTX_E, uid)) {
	    model.put(Constants.ATT_PM, RolePerm.ROLE_MANAGER.getMask());
	} else {
	    model.put(Constants.ATT_PM, RolePerm.ROLE_USER.getMask());
	}

	model.put(Constants.OBJ_ENTITY, em.getEntityById(eId));

	return model;
    }

    @Override
    protected Object formBackingObject(final PortletRequest request) throws Exception {
	CmisServerParamsForm form = new CmisServerParamsForm();

	String parameter = request.getParameter(Constants.ATT_ENTITY_ID);
	if (parameter != null) {
	    final Long ctxId = Long.valueOf(parameter);
	    Entity entity = this.em.getEntityById(ctxId);
	    form.setEntityId(entity.getEntityId());

	    CmisServer servParams = am.getEntityServer(entity.getEntityId());
	    if (servParams != null) {
		form.setUseEntityServer("true");

	    } else {
		servParams = am.getApplicationServer();
		if (servParams != null) {
		    // Use parameters from the application
		    form.setUseEntityServer("false");
		} else {
		    form.setUseEntityServer("true");
		}
	    }
	    if (servParams != null) {
		form.setServerId(servParams.getServerId().toString());

		form.setServerUrl(servParams.getServerUrl());
		form.setServerLogin(servParams.getServerLogin());
		form.setServerPwd(servParams.getServerPwd());
		form.setRepositoryId(servParams.getRepositoryId());
	    }

	} else {
	    form.setUseEntityServer("false");
	}

	return form;
    }

    @Override
    protected void onSubmitAction(final ActionRequest request, final ActionResponse response, 
	    				final Object command, final BindException errors)
	    throws Exception {
	CmisServerParamsForm form = (CmisServerParamsForm) command;

	if (!this.um.isSuperAdmin(request.getRemoteUser())) {
	    throw new PortletSecurityException("you are not authorized for this action");
	}

	long serverId = 0;
	long appServerId = -1;
	if (StringUtils.isNotEmpty(form.getServerId())) {
	    serverId = Long.valueOf(form.getServerId());
	}
	CmisServer applicationServer = am.getApplicationServer();
	if (applicationServer != null) {
	    appServerId = applicationServer.getServerId();
	}

	if (form.getUseEntityServer().equals("false")) {
	    // Use parameters from the application
	    // If params were defined for this entity, remove them
	    if (serverId > 0) {
		am.deleteServerLinkToEntity(form.getEntityId());
		if (serverId != appServerId) {
		    am.deleteServerParams(serverId);
		}
	    }
	    response.setRenderParameter(Constants.ACT, Constants.ACT_VIEW_E_ATT_CONF);

	} else {
	    // Use parameters from the entity, update or save them
	    CmisServer servParams = new CmisServer();
	    servParams.setGlobalServer("0");

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

	    boolean isAppServer;
	    if (serverId == appServerId) {
		isAppServer = true;
	    } else {
		isAppServer = false;
	    }
	    
	    if (serverId > 0 && !isAppServer) {
		servParams.setServerId(serverId);
		am.updateServerInfos(servParams);
		// delete the cmis session is exists
		sf.removeSession(form.getEntityId());

	    } else {
		Long id = am.insertServerParams(servParams);
		am.linkServerToEntity(id, form.getEntityId());
	    }

	    // update displayed fields
	    form.setUseEntityServer("true");
	    form.setServerId(servParams.getServerId().toString());

	    form.setDisplayedServerUrl(servParams.getServerUrl());
	    form.setDisplayedServerLogin(servParams.getServerLogin());
	    form.setDisplayedServerPwd(servParams.getServerPwd());
	    form.setDisplayedRepositoryId(servParams.getRepositoryId());

	    response.setRenderParameter(Constants.ACT, Constants.ACT_VIEW_E_ATT_CONF);
	}
	response.setRenderParameter(Constants.ATT_ENTITY_ID, String.valueOf(form.getEntityId()));
    }

}
