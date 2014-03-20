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
import org.esco.portlets.news.domain.Entity;
import org.esco.portlets.news.services.EntityManager;
import org.esco.portlets.news.services.PermissionManager;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindException;
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
 * 7 juin 2010
 */
public class EntityAttachmentOptionsController extends SimpleFormController implements InitializingBean {

	/** Logger. */
	private static final Log LOG = LogFactory.getLog(EntityAttachmentOptionsController.class);
	/** The Attachement Manager. */
	@Autowired
	private AttachmentManager am;
	/** The Permission Manager. */
	@Autowired
	private PermissionManager pm;
	/** The Entity Manager. */
	@Autowired
	private EntityManager em;

	/**
	 * Constructor.
	 */
	public EntityAttachmentOptionsController() {
		setCommandClass(AttachmentOptionsForm.class);
		setCommandName(Constants.CMD_ATTACHMENT_OPTS);
		setFormView(Constants.ACT_EDIT_E_ATT_OPTIONS);
		setSuccessView(Constants.ACT_VIEW_E_ATT_CONF);
	}

	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() throws Exception {
		if (this.am == null || this.pm == null || this.em == null) {
			throw new IllegalArgumentException(
					"An AttachmentManager, a Permission Manager and an Entity Manager are required");
		}
	}

	/**
	 * @see org.springframework.web.portlet.mvc.SimpleFormController#showForm(javax.portlet.RenderRequest, javax.portlet.RenderResponse, org.springframework.validation.BindException)
	 */
	@Override
	protected ModelAndView showForm(final RenderRequest request, final RenderResponse response,
			final BindException errors)	throws Exception {
		final Long ctxId = Long.valueOf(request.getParameter(Constants.ATT_ENTITY_ID));
		if (!this.pm.isAdminInCtx(ctxId, NewsConstants.CTX_E)) {
			LOG.debug("EntityAttachementOptions view: user has no role admin");
			ModelAndView mav = new ModelAndView(Constants.ACT_VIEW_NOT_AUTH);
			String msg = "you are not authorized for this action";
			mav.addObject(Constants.MSG_ERROR, msg);
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
		long eId = ((AttachmentOptionsForm) command).getEntityId();

		Map<String, Object> model = new HashMap<String, Object>();

		// the user need to be SuperAdmin or Admin.
		if (this.pm.isSuperAdmin()) {
			model.put(Constants.ATT_PM, RolePerm.ROLE_ADMIN.getMask());
		} else if (this.pm.isAdminInCtx(eId, NewsConstants.CTX_E)) {
			model.put(Constants.ATT_PM, RolePerm.ROLE_MANAGER.getMask());
		} else {
			model.put(Constants.ATT_PM, RolePerm.ROLE_USER.getMask());
		}

		model.put(Constants.OBJ_ENTITY, em.getEntityById(eId));

		return model;
	}

	/**
	 * @see org.springframework.web.portlet.mvc.AbstractFormController#formBackingObject(javax.portlet.PortletRequest)
	 */
	@Override
	protected Object formBackingObject(final PortletRequest request) throws Exception {
		AttachmentOptionsForm form = new AttachmentOptionsForm();

		String parameter = request.getParameter(Constants.ATT_ENTITY_ID);
		if (parameter != null) {
			final Long ctxId = Long.valueOf(parameter);
			Entity entity = this.em.getEntityById(ctxId);
			form.setEntityId(entity.getEntityId());

			AttachmentOptions options = am.getEntityAttachmentOptions(entity.getEntityId());
			if (options != null) {
				// Use parameters from the entity
				form.setUseEntityOptions("true");
			} else {
				options = am.getApplicationAttachmentOptions();
				if (options != null) {
					// Use parameters from the application
					form.setUseEntityOptions("false");
				} else {
					form.setUseEntityOptions("true");
				}
			}

			if (options != null) {
				form.setOptionsId(options.getOptionId());
				form.setMaxSize(options.getMaxSize().toString());
				form.setDisplayedMaxSize(options.getMaxSize().toString());
				String authorizedFilesExtensions = options.getAuthorizedFilesExtensions();
				if (StringUtils.isNotEmpty(authorizedFilesExtensions)) {
					String[] exts = authorizedFilesExtensions.split(";");
					List<String> list = new ArrayList<String>();
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
					List<String> list = new ArrayList<String>();
					for (String ext : exts) {
						if (StringUtils.isNotEmpty(ext)) {
							list.add(ext);
						}
					}
					form.setForbiddenList(list);
				}
			}
		}
		return form;
	}

	/**
	 * @see org.springframework.web.portlet.mvc.SimpleFormController#onSubmitAction(javax.portlet.ActionRequest, javax.portlet.ActionResponse, java.lang.Object, org.springframework.validation.BindException)
	 */
	@Override
	protected void onSubmitAction(final ActionRequest request, final ActionResponse response,
			final Object command, final BindException errors) throws Exception {

		AttachmentOptionsForm aof = (AttachmentOptionsForm) command;

		if (!this.pm.isSuperAdmin()) {
			throw new PortletSecurityException(getMessageSourceAccessor().getMessage("exception.notAuthorized.action"));
		}

		long optionsId = aof.getOptionsId();
		long appOptionsId = Long.valueOf(-1);
		AttachmentOptions applicationAttachmentOptions = am.getApplicationAttachmentOptions();
		if (applicationAttachmentOptions != null) {
			appOptionsId = applicationAttachmentOptions.getOptionId();
		}

		if (aof.getUseEntityOptions().equals("false")) {
			// Use parameters from the application
			// If params were defined for this entity, remove them
			if (optionsId > 0) {
				am.deleteAttachmentOptsLinkToEntity(aof.getEntityId());
				if (optionsId != appOptionsId) {
					am.deleteAttachmentOptions(optionsId);
				}
			}
			response.setRenderParameter(Constants.ACT, Constants.ACT_VIEW_E_ATT_CONF);

		} else {
			// Use parameters from the entity, update or save them
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

			options.setGlobalOptions("0");

			boolean isAppOpt;
			if (optionsId == appOptionsId) {
				isAppOpt = true;
			} else {
				isAppOpt = false;
			}

			if (optionsId > 0 && !isAppOpt) {
				options.setOptionId(optionsId);
				am.updateAttachmentOptions(options);
			} else {
				Long id = am.insertAttachmentOptions(options);
				am.linkAttachmentOptionsToEntity(id, aof.getEntityId());
			}

			response.setRenderParameter(Constants.ACT, Constants.ACT_VIEW_E_ATT_CONF);
		}
		response.setRenderParameter(Constants.ATT_ENTITY_ID, String.valueOf(aof.getEntityId()));
	}

	/**
	 * @param request
	 * @param response
	 */
	@Override
	protected ModelAndView renderInvalidSubmit(final RenderRequest request, final RenderResponse response)
			throws Exception {
		if (LOG.isTraceEnabled()) {
			LOG.trace("Entering render Invalid Submit.");
		}
		return null;
	}

	/**
	 * @param request
	 * @param response
	 * @throws Exception
	 * @see org.springframework.web.portlet.mvc.AbstractFormController#
	 *      handleInvalidSubmit(javax.portlet.ActionRequest,
	 *      javax.portlet.ActionResponse)
	 */
	@Override
	protected void handleInvalidSubmit(final ActionRequest request, final ActionResponse response) throws Exception {
		if (LOG.isTraceEnabled()) {
			LOG.trace("Entering handle Invalid Submit.");
		}
		response.setRenderParameter(Constants.ACT, Constants.ACT_VIEW_NEWSSTORE);
	}

}
