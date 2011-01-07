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

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cmis.portlets.news.services.AttachmentManager;
import org.esco.portlets.news.domain.Entity;
import org.esco.portlets.news.services.EntityManager;
import org.esco.portlets.news.services.UserManager;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.ModelAndViewDefiningException;
import org.springframework.web.portlet.bind.PortletRequestBindingException;
import org.springframework.web.portlet.bind.PortletRequestDataBinder;
import org.springframework.web.portlet.bind.PortletRequestUtils;
import org.springframework.web.portlet.mvc.AbstractWizardFormController;
import org.uhp.portlets.news.NewsConstants;
import org.uhp.portlets.news.domain.Category;
import org.uhp.portlets.news.domain.Item;
import org.uhp.portlets.news.domain.RolePerm;
import org.uhp.portlets.news.domain.Topic;
import org.uhp.portlets.news.service.CategoryManager;
import org.uhp.portlets.news.service.ItemManager;
import org.uhp.portlets.news.service.TopicManager;
import org.uhp.portlets.news.web.ItemForm.Attachment;
import org.uhp.portlets.news.web.support.Constants;
import org.uhp.portlets.news.web.validator.ItemValidator;

public class ItemAddController extends AbstractWizardFormController implements InitializingBean {
	private static final Log LOGGER = LogFactory.getLog(ItemAddController.class);

	@Autowired
	private ItemManager im;

	@Autowired
	private TopicManager tm;

	@Autowired
	private CategoryManager cm;

	@Autowired
	private UserManager um;

	@Autowired
	private AttachmentManager am;

	private String temporaryStoragePath;
	private Long ctxTopicId;

	/** Manager of an Entity. */
	@Autowired
	private EntityManager em;

	public ItemAddController() {
		super();
		setCommandClass(ItemForm.class);
		setCommandName(Constants.CMD_ITEM);
		setPageAttribute("page");
		setPages(new String[] { Constants.ACT_ADD_ITEM, Constants.ACT_ADD_EXTERNAL_ATTACHMENT,
				Constants.ACT_ADD_INTERNAL_ATTACHMENT, Constants.ACT_ADD_UPDATE_ATTACHMENT });
	}

	public void afterPropertiesSet() throws Exception {
		if ((this.im == null) || (this.cm == null) || (this.tm == null) || (this.um == null) || (this.em == null)
				|| (this.am == null)) {
			throw new IllegalArgumentException("A itemManager , a categoryManager, a topicManager, "
					+ "an AttachmentManager and a userManager are required");
		}
	}

	@Override
	protected void processFinish(ActionRequest request, ActionResponse response, Object command, BindException errors)
	throws Exception {
		final ItemForm itemForm = (ItemForm) command;

		final long pkey = this.im.addItem(itemForm, request.getRemoteUser());

		// retrieve entity
		final Category category = this.cm.getCategoryById(itemForm.getItem().getCategoryId());
		final Long entityId = category.getEntityId();

		// save attachments
		this.am.addAttachmentToItem(itemForm, pkey, entityId);

		response.setRenderParameter(Constants.ACT, Constants.ACT_VIEW_ITEM);
		response.setRenderParameter(Constants.ATT_ID, String.valueOf(itemForm.getItem().getItemId()));
		final Long tId = ctxTopicId;
		if (tId != null) {
			response.setRenderParameter(Constants.ATT_TOPIC_ID, String.valueOf(tId));
		}

		// clean temporary directory
		this.am.cleanTempStorageDirectory(this.getPortletContext().getRealPath(temporaryStoragePath));

		ctxTopicId = null;
	}

	@Override
	protected void processCancel(ActionRequest request, ActionResponse response, Object command, BindException errors)
	throws Exception {
		final int currentPage = getCurrentPage(request);
		if (currentPage == 1 || currentPage == 2 || currentPage == 3) {
			final BindingResult bindingResult = errors.getBindingResult();
			errors = new BindException(bindingResult);

			ItemForm itemForm = (ItemForm) command;
			itemForm.setCategoryId((long) -1);
			itemForm.setTopicId((long) -1);
			itemForm.setItemId((long) -1);

			this.setPageRenderParameter(response, 0);
			request.getPortletSession().setAttribute("_globalCancel", false);

			response.setRenderParameter(Constants.ATT_CAT_ID, request.getParameter(Constants.ATT_CAT_ID));

		} else {
			request.getPortletSession().setAttribute("_globalCancel", true);
			response.setRenderParameter(Constants.ACT, Constants.ACT_VIEW_TOPIC);
		}
	}

	@Override
	protected ModelAndView renderCancel(RenderRequest request, RenderResponse response, Object command,
			BindException errors) throws Exception {
		boolean isGlobalCancel = ((Boolean) request.getPortletSession().getAttribute("_globalCancel")).booleanValue();

		request.getPortletSession().removeAttribute("_globalCancel");
		if (!isGlobalCancel) {
			return showForm(request, response, errors);
		} 
		return super.renderFinish(request, response, command, errors);
	}

	@Override
	protected void validatePage(Object command, Errors errors, int page, boolean finish) {
		ItemForm itemForm = (ItemForm) command;
		ItemValidator itemValidator = (ItemValidator) getValidator();
		if (finish) {
			itemValidator.validate1stPart(command, errors);
			return;
		}
		switch (page) {
		case 0:
			itemValidator.validate1stPart(command, errors);
			break;
		case 1:
			Category category = this.cm.getCategoryById(itemForm.getItem().getCategoryId());
			Entity entity = this.em.getEntityById(category.getEntityId());

			itemValidator.validate2ndPart(entity.getEntityId().toString(), command, errors);
			break;
		case 2:
			itemValidator.validate3rdPart(command, errors);
			break;
		case 3:
			itemValidator.validate4rdPart(command, errors);
			break;
		default:
			break;
		}
	}

	/**
	 * @param request
	 * @param command
	 * @param errors
	 * @param page
	 * @see org.springframework.web.portlet.mvc.AbstractWizardFormController#
	 *      onBindAndValidate(javax.portlet.PortletRequest, java.lang.Object,
	 *      org.springframework.validation.BindException, int)
	 */
	@Override
	protected void postProcessPage(javax.portlet.ActionRequest request, Object command, Errors errors, int page) {
		ItemForm itemForm = (ItemForm) command;

		String toRemove = "";
		String toUpdate = "";
		try {
			toRemove = PortletRequestUtils.getStringParameter(request, "removeAttachment");
			toUpdate = PortletRequestUtils.getStringParameter(request, "updateAttachment");

			if (StringUtils.isNotEmpty(toRemove)) {
				int i = Integer.parseInt(toRemove);
				if (i >= 0 && i < itemForm.getAttachments().size()) {
					Attachment attachment = itemForm.getAttachments().get(i);
					File tempDiskStoredFile = attachment.getTempDiskStoredFile();
					if (tempDiskStoredFile != null) {
						tempDiskStoredFile.delete();
					}
					itemForm.getAttachments().remove(i);
				}

			} else if (StringUtils.isNotEmpty(toUpdate)) {
				int aId = Integer.parseInt(toUpdate);
				// retrieve the attachement in the list
				if (aId >= 0 && aId < itemForm.getAttachments().size()) {
					Attachment attachment = itemForm.getAttachments().get(aId);
					// update the form
					// set the position in the list as ID to retrive the attachment after the update
					itemForm.setAttachmentToUpdate(Integer.toString(aId), attachment.getTitle(), attachment.getDesc());
				}   

			} else {
				if (errors.getErrorCount() == 0) {
					if (page == 1) {

						itemForm.addExternalAttachment(this.getPortletContext().getRealPath(temporaryStoragePath));

					} else if (page == 2) {
						Long categoryId = PortletRequestUtils.getLongParameter(request, "categoryId");
						Long topicId = PortletRequestUtils.getLongParameter(request, "topicId");
						Long selectedTopicId = PortletRequestUtils.getLongParameter(request, "selectedTopic");
						Long selectedItemId = PortletRequestUtils.getLongParameter(request, "itemId");
						String[] attachmentIds = request.getParameterValues("attachmentIds");

						itemForm.setCategoryId(categoryId);
						itemForm.setItemId(selectedItemId);
						if (selectedItemId != null) {
							itemForm.setTopicId(selectedTopicId);
						} else {
							itemForm.setTopicId(topicId);
						}
						
						if (attachmentIds != null && attachmentIds.length > 0) {
							for (String id : attachmentIds) {
								boolean attached = false;
								for (Attachment att: itemForm.getAttachments()) {
									if (att.getId() != null && att.getId().length() > 0 && Long.parseLong(att.getId()) == Long.parseLong(id)) {
										attached=true;
										break;
									}
								}
								if (!attached) {
									org.cmis.portlets.news.domain.Attachment attachment = am.getAttachmentById(Long.valueOf(id));
									itemForm.addInternalAttachment(attachment);
								}
							}
						}

					} else if (page == 3){
						Attachment attachmentUpdated = itemForm.getAttachmentToUpdate();
						String id = attachmentUpdated.getId();
						// this ID is the position in the list
						Attachment attachmentToUpdate = itemForm.getAttachments().get(Integer.parseInt(id));
						attachmentToUpdate.setTitle(attachmentUpdated.getTitle());
						attachmentToUpdate.setDesc(attachmentUpdated.getDesc());

						attachmentUpdated = null;	   
					}
				}
			}
		} catch (PortletRequestBindingException e) {
			LOGGER.warn("An error occurs retrieving a request parameter.");
			LOGGER.warn(e, e.fillInStackTrace());
		}
	}

	/**
	 * @param request
	 * @param command
	 * @param errors
	 * @param currentPage
	 * @return
	 * @see org.springframework.web.portlet.mvc.AbstractWizardFormController#
	 *      getTargetPage(javax.portlet.PortletRequest, java.lang.Object,
	 *      org.springframework.validation.Errors, int)
	 */
	@Override
	protected int getTargetPage(final PortletRequest request, final Object command, final Errors errors,
			final int currentPage) {
		if (errors.getErrorCount() > 0) {
			return currentPage;
		}
		return super.getTargetPage(request, command, errors, currentPage);
	}

	@Override
	protected ModelAndView showForm(RenderRequest request, RenderResponse response, BindException errors)
	throws Exception {
		int currentPage = 0;

		try {
			currentPage = getCurrentPage(request);
		} catch (IllegalStateException ise) {
			// do nothing, currentpage is set to 0
		}

		if (currentPage == 0) {
			final Long cId = PortletRequestUtils.getLongParameter(request, Constants.ATT_CAT_ID);
			final Long tId = PortletRequestUtils.getLongParameter(request, Constants.ATT_TOPIC_ID);
			int perm = 0;
			if (cId != null) {
				perm = RolePerm.valueOf(this.um.getUserRoleInCtx(cId, NewsConstants.CTX_C, request.getRemoteUser()))
				.getMask();
			}
			if (tId != null) {
				perm += RolePerm.valueOf(this.um.getUserRoleInCtx(tId, NewsConstants.CTX_T, request.getRemoteUser()))
				.getMask();
			}

			ModelAndView mav = new ModelAndView(Constants.ACT_VIEW_NOT_AUTH);
			if (perm <= 0) {
				mav.addObject(Constants.MSG_ERROR, getMessageSourceAccessor().getMessage(
						"news.alert.notAuthorizedAction"));
				throw new ModelAndViewDefiningException(mav);
			}
			return super.showForm(request, response, errors);

		} else if (currentPage == 1) {
			// External File
			return super.showForm(request, response, errors);

		} else {
			// Internal File
			return super.showForm(request, response, errors);
		}
	}

	@Override
	protected Object formBackingObject(final PortletRequest request) throws Exception {
		ItemForm itemForm = new ItemForm();

		final Long cId = PortletRequestUtils.getLongParameter(request, Constants.ATT_CAT_ID);
		Item item = new Item();
		if (cId != null) {
			item.setCategoryId(cId);

		}
		final String uid = request.getRemoteUser();
		item.setPostedBy(uid);
		final Date postDate = new Date();
		item.setPostDate(postDate);
		item.setLastUpdatedDate(postDate);
		item.setLastUpdatedBy(request.getRemoteUser());
		itemForm.setItem(item);

		List<Attachment> displayedAttachments = itemForm.getAttachments();
		itemForm.setAttachments(displayedAttachments);

		return itemForm;
	}

	@Override
	protected void initBinder(PortletRequest request, PortletRequestDataBinder binder) throws Exception {
		SimpleDateFormat dateFormat = new SimpleDateFormat(getMessageSourceAccessor().getMessage(Constants.DATE_FORMAT));
		binder.registerCustomEditor(Date.class, null, new CustomDateEditor(dateFormat, true));
		// binder.registerCustomEditor(String[].class, new StringArrayEditor());
	}

	/**
	 * @param request
	 * @param command
	 * @param errors
	 * @return
	 * @see org.springframework.web.portlet.mvc.SimpleFormController#
	 *      referenceData(javax.portlet.PortletRequest, java.lang.Object,
	 *      org.springframework.validation.Errors)
	 */
	@Override
	protected Map<String, Object> referenceData(final PortletRequest request, final Object command,
			final Errors errors, final int page) throws Exception {
		ItemForm itemForm = (ItemForm) command;

		if (page == 0) {
			Long cId = itemForm.getItem().getCategoryId();
			if (cId == null) {
				cId = PortletRequestUtils.getLongParameter(request, Constants.ATT_CAT_ID);
			}
			Map<String, Object> model = new HashMap<String, Object>();
			final String uid = request.getRemoteUser();
			final Long tId = PortletRequestUtils.getLongParameter(request, Constants.ATT_TOPIC_ID);
			if (tId != null) {
				ctxTopicId = tId;
			}
			int perm = 0;

			if (ctxTopicId != null) {
				model.put(Constants.OBJ_TOPIC, this.tm.getTopicById(ctxTopicId));
				perm = RolePerm.valueOf(this.um.getUserRoleInCtx(ctxTopicId, NewsConstants.CTX_T, uid)).getMask();
			}
			if (cId != null) {
				final Category category = this.cm.getCategoryById(cId);
				int nbDays = this.im.getNbDays();
				model.put(Constants.OBJ_CATEGORY, category);
				model.put(Constants.OBJ_ENTITY, this.em.getEntityById(category.getEntityId()));
				model.put(Constants.ATT_NB_DAYS, nbDays);
				model.put(Constants.ATT_T_LIST, this.tm.getTopicListForCategoryByUser(cId, uid));
				int permTmp = RolePerm.valueOf(this.um.getUserRoleInCtx(cId, NewsConstants.CTX_C, uid)).getMask();
				if (perm < permTmp) {
					perm = permTmp;
				}
			}

			if (itemForm != null) {
				model.put(Constants.ATT_A_LIST, itemForm.getAttachments());
			}

			model.put(Constants.ATT_USER_ID, request.getRemoteUser());
			model.put(Constants.ATT_PM, perm);

			return model;

		} else if (page == 1 || page == 3) {
			// External File or update an attachment
			Map<String, Object> model = new HashMap<String, Object>();

			Long cId = itemForm.getItem().getCategoryId();
			if (cId == null) {
				cId = PortletRequestUtils.getLongParameter(request, Constants.ATT_CAT_ID);
			}
			final Long tId = ctxTopicId;
			final String userUid = request.getRemoteUser();
			int perm = 0;

			if (cId != null && cId != -1) {
				Category category = this.cm.getCategoryById(cId);
				model.put(Constants.OBJ_CATEGORY, category);
				perm = RolePerm.valueOf(this.um.getUserRoleInCtx(cId, NewsConstants.CTX_C, userUid)).getMask();
			}
			if (tId != null && tId != -1) {
				model.put(Constants.OBJ_TOPIC, this.tm.getTopicById(tId));
				int permTmp = RolePerm.valueOf(this.um.getUserRoleInCtx(tId, NewsConstants.CTX_T, userUid)).getMask();
				if (perm < permTmp) {
					perm = permTmp;
				}
			}
			model.put(Constants.ATT_USER_ID, userUid);
			model.put(Constants.ATT_PM, perm);
			return model;

		} else {
			// Internal File
			Map<String, Object> model = new HashMap<String, Object>();

			Long cId = itemForm.getItem().getCategoryId();
			if (cId == null) {
				cId = PortletRequestUtils.getLongParameter(request, Constants.ATT_CAT_ID);
			}
			final Long tId = ctxTopicId;
			final String userUid = request.getRemoteUser();

			Long entityId = (long) -1;
			int perm = 0;

			if (cId != null && cId != -1) {
				Category category = this.cm.getCategoryById(cId);
				entityId = category.getEntityId();
				model.put(Constants.OBJ_CATEGORY, category);
				perm = RolePerm.valueOf(this.um.getUserRoleInCtx(cId, NewsConstants.CTX_C, userUid)).getMask();
			}
			if (tId != null && tId != -1) {
				model.put(Constants.OBJ_TOPIC, this.tm.getTopicById(tId));
				int permTmp = RolePerm.valueOf(this.um.getUserRoleInCtx(tId, NewsConstants.CTX_T, userUid)).getMask();
				if (perm < permTmp) {
					perm = permTmp;
				}
			}

			model.put(Constants.ATT_USER_ID, userUid);
			model.put(Constants.ATT_PM, perm);

			Long categoryId = itemForm.getCategoryId();
			Long topicId = itemForm.getTopicId();
			Long selectedItemId = itemForm.getItemId();

			if (selectedItemId != null && selectedItemId != -1) {
				// Attachments list
				Item selectedItem = im.getItemById(selectedItemId);
				Topic selectedTopic = tm.getTopicById(topicId);
				Category selectedCat = cm.getCategoryById(selectedTopic.getCategoryId());

				List<org.cmis.portlets.news.domain.Attachment> attachmentsListByItem = am
				.getAttachmentsListByItem(selectedItem.getItemId());
				model.put("attachments", attachmentsListByItem);

				// For breadcrumb
				model.put("selectedCategory", selectedCat);
				model.put("selectedTopic", selectedTopic);
				model.put("selectedItem", selectedItem);

			} else if (topicId != null && topicId != -1) {
				// Items list
				List<Item> items = im.getItemsWithAttachmentsByTopic(topicId);
				Topic selectedTopic = tm.getTopicById(topicId);
				Category selectedCat = cm.getCategoryById(selectedTopic.getCategoryId());
				List<Item> itemsList = new ArrayList<Item>();
				if (RolePerm.valueOf(this.um.getUserRoleInCtx(topicId, NewsConstants.CTX_T, request.getRemoteUser()))
						.getMask() > 0) {
					Long currentItemId = itemForm.getItem().getItemId();
					for (Item item : items) {
						if (item.getItemId() != currentItemId) {
							itemsList.add(item);
						}
					}
				}
				model.put("items", itemsList);
				// For breadcrumb
				model.put("selectedCategory", selectedCat);
				model.put("selectedTopic", selectedTopic);

			} else if (categoryId != null && categoryId != -1) {
				// Retrieve the topics list for the selected category
				List<Topic> topics = tm.getTopicListByCategory(categoryId);
				Category selectedCat = cm.getCategoryById(categoryId);

				List<Topic> topicsList = new ArrayList<Topic>();
				for (Topic topic : topics) {
					Long id = topic.getTopicId();
					if (RolePerm.valueOf(this.um.getUserRoleInCtx(id, NewsConstants.CTX_T, request.getRemoteUser()))
							.getMask() > 0) {
						int nb = im.countItemsWithAttachmentByTopic(id);
						topic.setCount(nb);
						topicsList.add(topic);
					}
				}
				model.put("topics", topicsList);
				// For breadcrumb
				model.put("selectedCategory", selectedCat);

			} else {
				// Category list
				List<Category> categories = cm.getListCategoryOfEntityByUser(userUid, entityId);
				List<Category> categoriesList = new ArrayList<Category>();
				for (Category category : categories) {
					Long id = category.getCategoryId();
					if (RolePerm.valueOf(this.um.getUserRoleInCtx(id, NewsConstants.CTX_C, request.getRemoteUser()))
							.getMask() > 0) {
						categoriesList.add(category);
					}
				}
				model.put("categories", categoriesList);
			}
			return model;
		}
	}

	@Override
	protected ModelAndView renderInvalidSubmit(RenderRequest request, RenderResponse response) throws Exception {
		return null;
	}

	@Override
	protected void handleInvalidSubmit(ActionRequest request, ActionResponse response) throws Exception {
		response.setRenderParameter(Constants.ACT, Constants.ACT_VIEW_NEWSSTORE);
	}

	/**
	 * @param temporaryStoragePath
	 *            the temporaryStoragePath to set
	 */
	public void setTemporaryStoragePath(final String temporaryStoragePath) {
		this.temporaryStoragePath = temporaryStoragePath;
	}

	/**
	 * @return the temporaryStoragePath
	 */
	public String getTemporaryStoragePath() {
		return temporaryStoragePath;
	}

}
