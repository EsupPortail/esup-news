package org.cmis.portlets.news.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import org.cmis.portlets.news.domain.AttachmentOptions;
import org.cmis.portlets.news.services.AttachmentManager;
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
 * 7 juin 2010
 */
public class AttachmentOptionsController extends SimpleFormController implements InitializingBean {

    /** Logger. */
    @SuppressWarnings("unused")
    private static final Log LOG = LogFactory.getLog(AttachmentOptionsController.class);
    /** The Attachment Manager. */
    @Autowired
    private AttachmentManager am;
    /** The User Manager. */
    @Autowired
    private UserManager um;

    /**
     * Constructor.
     */
    public AttachmentOptionsController() {
	setCommandClass(AttachmentOptionsForm.class);
	setCommandName(Constants.CMD_ATTACHMENT_OPTS);
	setFormView(Constants.ACT_EDIT_ATT_OPTIONS);
	setSuccessView(Constants.ACT_VIEW_ATT_CONF);
    }

    public void afterPropertiesSet() throws Exception {
	if (this.am == null || this.um == null) {
	    throw new IllegalArgumentException("An AttachmentManager, a User Manager are required");
	}
    }

    @Override
    protected ModelAndView showForm(final RenderRequest request, final RenderResponse response, 
	    				final BindException errors)
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

    @SuppressWarnings("unchecked")
    @Override
    protected Object formBackingObject(final PortletRequest request) throws Exception {
	AttachmentOptionsForm form = new AttachmentOptionsForm();

	AttachmentOptions options = am.getApplicationAttachmentOptions();
	if (options != null) {
	    form.setOptionsId(options.getOptionId());
	    form.setMaxSize(options.getMaxSize().toString());
	    // form.setDisplayedMaxSize(options.getMaxSize().toString());
	    String authorizedFilesExtensions = options.getAuthorizedFilesExtensions();
	    if (StringUtils.isNotEmpty(authorizedFilesExtensions)) {
		String[] exts = authorizedFilesExtensions.split(";");
		List list = new ArrayList();
		for (String ext : exts) {
		    if (StringUtils.isNotEmpty(ext)) {
			list.add(ext);
		    }
		}
		form.setAuthorizedList(list);
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
		form.setForbiddenList(list);
	    }
	}
	return form;
    }

    @Override
    protected void onSubmitAction(final ActionRequest request, final ActionResponse response, 
	    				final Object command, final BindException errors)
	    throws Exception {

	AttachmentOptionsForm aof = (AttachmentOptionsForm) command;

	if (!this.um.isSuperAdmin(request.getRemoteUser())) {
	    throw new PortletSecurityException("you are not authorized for this action");
	}

	long optionsId = aof.getOptionsId();

	AttachmentOptions options = new AttachmentOptions();
	long maxSize = 0;
	if (StringUtils.isNotEmpty(aof.getMaxSize())) {
	    maxSize = Long.parseLong(aof.getMaxSize());
	}
	options.setMaxSize(maxSize);

	String[] authorizedList = aof.getAuthorizedExts().split(";");
	StringBuilder listOfAuthExts = new StringBuilder("");
	boolean notempty = false;
	if (authorizedList != null) {
	    for (String ext : authorizedList) {
		if (StringUtils.isNotEmpty(ext)) {
		    listOfAuthExts.append(ext);
		    listOfAuthExts.append(";");
		    notempty = true;
		}
	    }
	    if (notempty) {
		options.setAuthorizedFilesExtensions(listOfAuthExts.toString());
	    }
	}
	String[] forbiddenList = aof.getForbiddenExts().split(";");
	StringBuilder listOfForbidenExts = new StringBuilder("");
	notempty = false;
	if (forbiddenList != null) {
	    for (String ext : forbiddenList) {
		if (StringUtils.isNotEmpty(ext)) {
		    listOfForbidenExts.append(ext);
		    listOfForbidenExts.append(";");
		    notempty = true;
		}
	    }
	    if (notempty) {
		options.setForbiddenFilesExtensions(listOfForbidenExts.toString());
	    }
	}

	options.setGlobalOptions("1");

	if (optionsId > 0) {
	    options.setOptionId(optionsId);
	    am.updateAttachmentOptions(options);
	} else {
	    am.insertAttachmentOptions(options);
	}

	response.setRenderParameter(Constants.ACT, Constants.ACT_VIEW_ATT_CONF);
    }

}
