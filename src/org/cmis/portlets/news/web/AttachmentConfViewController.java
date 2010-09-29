package org.cmis.portlets.news.web;

import java.util.ArrayList;
import java.util.List;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cmis.portlets.news.domain.AttachmentOptions;
import org.cmis.portlets.news.domain.CmisServer;
import org.cmis.portlets.news.services.AttachmentManager;
import org.esco.portlets.news.services.UserManager;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.ModelAndViewDefiningException;
import org.springframework.web.portlet.mvc.AbstractController;
import org.uhp.portlets.news.domain.RolePerm;
import org.uhp.portlets.news.web.support.Constants;

/**
 * View Controller of all attachments properties
 * 
 * @author Anyware Services - Delphine Gavalda. 6 juil. 2010
 */
public class AttachmentConfViewController extends AbstractController implements InitializingBean {

    /** Logger. */
    private static final Log LOG = LogFactory.getLog(AttachmentConfViewController.class);
    /** The Attachment Manager. */
    @Autowired
    private AttachmentManager am;
    /** The User Manager. */
    @Autowired
    private UserManager um;

    /**
     * Constructor of AttachementConfViewController.java.
     */
    public AttachmentConfViewController() {
	super();
    }

    /**
     * @param request
     * @param response
     * @return <code>ModelAndView</code>
     * @throws Exception
     * @see org.springframework.web.portlet.mvc.AbstractController#
     *      handleRenderRequest(javax.portlet.RenderRequest,
     *      javax.portlet.RenderResponse)
     */
    @Override
    public ModelAndView handleRenderRequest(final RenderRequest request, final RenderResponse response)
	    throws Exception {

	if (LOG.isDebugEnabled()) {
	    LOG.debug("AttachementConfViewController:: entering method handleRenderRequestInternal");
	}
	String userUid = request.getRemoteUser();
	if (!this.um.isSuperAdmin(userUid)) {
	    ModelAndView mav = new ModelAndView(Constants.ACT_VIEW_NOT_AUTH);
	    mav.addObject(Constants.MSG_ERROR, getMessageSourceAccessor().getMessage("news.alert.superUserOnly"));
	    throw new ModelAndViewDefiningException(mav);
	}

	ModelAndView mav = new ModelAndView(Constants.ACT_VIEW_ATT_CONF);

	AttachmentOptions options = am.getApplicationAttachmentOptions();
	if (options != null) {
	    mav.addObject(Constants.ATT_MAX_SIZE, options.getMaxSize());
	    String authorizedFilesExtensions = options.getAuthorizedFilesExtensions();
	    if (StringUtils.isNotEmpty(authorizedFilesExtensions)) {
		String[] exts = authorizedFilesExtensions.split(";");
		List list = new ArrayList();
		for (String ext : exts) {
		    if (StringUtils.isNotEmpty(ext)) {
			list.add(ext);
		    }
		}
		mav.addObject(Constants.ATT_AUTH_EXTS, list);
	    }
	    String forbiddenFilesExtensions = options.getForbiddenFilesExtensions();
	    if (StringUtils.isNotEmpty(forbiddenFilesExtensions)) {
		String[] exts = forbiddenFilesExtensions.split(";");
		List list = new ArrayList();
		for (String ext : exts) {
		    if (StringUtils.isNotEmpty(ext)) {
			list.add(ext);
		    }
		}
		mav.addObject(Constants.ATT_FORB_EXTS, list);
	    }
	}

	CmisServer server = am.getApplicationServer();
	if (server != null) {
	    mav.addObject(Constants.ATT_SERV_URL, server.getServerUrl());
	    mav.addObject(Constants.ATT_SERV_LOGIN, server.getServerLogin());
	    mav.addObject(Constants.ATT_SERV_PWD, server.getServerPwd());
	    mav.addObject(Constants.ATT_SERV_REPO_ID, server.getRepositoryId());
	}

	if (this.um.isSuperAdmin(userUid)) {
	    mav.addObject(Constants.ATT_PM, RolePerm.ROLE_ADMIN.getMask());
	} else {
	    mav.addObject(Constants.ATT_PM, RolePerm.ROLE_USER.getMask());
	}
	return mav;
    }

    public void afterPropertiesSet() throws Exception {
	Assert.notNull(this.getAm(), "The property AttachmentManager am in class " + getClass().getSimpleName()
		+ " must not be null.");
	Assert.notNull(this.getUm(), "The property UserManager um in class " + getClass().getSimpleName()
		+ " must not be null.");
    }

    public void setAm(AttachmentManager am) {
	this.am = am;
    }

    public AttachmentManager getAm() {
	return am;
    }

    public UserManager getUm() {
	return um;
    }

    public void setUm(UserManager um) {
	this.um = um;
    }

}
