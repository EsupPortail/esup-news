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
import java.util.List;
import java.util.Map;

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

public class ItemEditController extends AbstractWizardFormController implements InitializingBean {

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

	/** Manager of an Entity. */
	@Autowired
	private EntityManager em;

	private Long itemId;
	private Long ctxTopicId;

	private String temporaryStoragePath;

	private static final Log log = LogFactory.getLog(ItemEditController.class);

	public ItemEditController() {
		super();
		setCommandClass(ItemForm.class);
		setSessionForm(true);
		setCommandName(Constants.CMD_ITEM);
		setPageAttribute("page");
		setPages(new String[] { Constants.ACT_EDIT_ITEM, Constants.ACT_EDIT_EXTERNAL_ATTACHMENT,
				Constants.ACT_EDIT_INTERNAL_ATTACHMENT, Constants.ACT_EDIT_UPDATE_ATTACHMENT});
	}

	public void afterPropertiesSet() throws Exception {
		if ((this.im == null) || (this.tm == null) || (this.cm == null) || (this.um == null) || (em == null))
			throw new IllegalArgumentException(
					"A ItemManager, a TopicManager, a categoryManager, a entityManager and a userManager are required");
	}

	@Override
	protected void processFinish(ActionRequest request, ActionResponse response, Object command, BindException errors)
	throws Exception {

		ItemForm itemForm = (ItemForm) command;
		Item item = itemForm.getItem();
		if (item != null) {
			final String uid = request.getRemoteUser();
			item.setLastUpdatedBy(uid);
			item.setLastUpdatedDate(new Date());

			// retrieve entity
			Category category = this.cm.getCategoryById(itemForm.getItem().getCategoryId());
			Long entityId = category.getEntityId();

			this.im.updateItem(itemForm, uid);
			this.am.updateItemAttachment(itemForm, item.getItemId(), entityId);

			response.setRenderParameter(Constants.ACT, Constants.ACT_VIEW_ITEM);
			response.setRenderParameter(Constants.ATT_ID, String.valueOf(itemForm.getItem().getItemId()));
			final Long tId = ctxTopicId;
			if (tId != null) {
				response.setRenderParameter(Constants.ATT_TOPIC_ID, String.valueOf(tId));
			}
			// clean temporary directory
			this.am.cleanTempStorageDirectory(this.getPortletContext().getRealPath(temporaryStoragePath));

			ctxTopicId = null;
		} else {
			throw new IllegalArgumentException("Item does not exist.");
		}

	}

	@Override
	protected void processCancel(ActionRequest request, ActionResponse response, Object command, BindException errors)
	throws Exception {
		int currentPage = getCurrentPage(request);
		if (currentPage == 1 || currentPage == 2 || currentPage == 3) {
			BindingResult bindingResult = errors.getBindingResult();
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
		} else {
			return super.renderFinish(request, response, command, errors);
		}
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
				    String id = StringUtils.isNotEmpty(attachment.getId()) ? attachment.getId() : Integer.toString(aId); 
				    itemForm.setAttachmentToUpdate(id, attachment.getTitle(), attachment.getDesc());
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

						if (attachmentIds != null) {
							for (String id : attachmentIds) {
								org.cmis.portlets.news.domain.Attachment attachment = am.getAttachmentById(Long
										.parseLong(id));
								itemForm.addInternalAttachment(attachment);
							}
						}
						
					} else if (page == 3){
					    	Attachment attachmentUpdated = itemForm.getAttachmentToUpdate();
					    	String id = attachmentUpdated.getId();
					    	boolean found=false;
					    	for (Attachment attachmentToUpdate : itemForm.getAttachments()) {
					    	    if (attachmentToUpdate.getId().equals(id)) {
					    		attachmentToUpdate.setTitle(attachmentUpdated.getTitle());
						    	attachmentToUpdate.setDesc(attachmentUpdated.getDesc());
						    	
						    	attachmentUpdated = null;
						    	found = true;
						    	break;
					    	    }
					    	}
					    	if (!found && StringUtils.isNotEmpty(id)) {
					    	    Attachment attachmentToUpdate = itemForm.getAttachments().get(Integer.parseInt(id));
					    	    attachmentToUpdate.setTitle(attachmentUpdated.getTitle());
					    	    attachmentToUpdate.setDesc(attachmentUpdated.getDesc());
					    	    attachmentUpdated = null;
					    	}
					    	
					}
				}
			}
		} catch (PortletRequestBindingException e) {
			log.warn("An error occurs retrieving a request parameter.");
			log.warn(e, e.fillInStackTrace());
		}
	}

	@Override
	protected int getTargetPage(PortletRequest request, Object command, Errors errors, int currentPage) {
		if (errors.getErrorCount() > 0) {
			return currentPage;
		} else {
			return super.getTargetPage(request, command, errors, currentPage);
		}
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
			if (!this.um.canEditItem(request.getRemoteUser(), this.im.getItemById(itemId))) {
				ModelAndView mav = new ModelAndView("notAuthorized");
				mav.addObject("msgError", getMessageSourceAccessor().getMessage("news.alert.notAuthorizedAction"));
				throw new ModelAndViewDefiningException(mav);
			}
			return super.showForm(request, response, errors);

		} else if (currentPage == 1) {
			// External File
			return super.showForm(request, response, errors);

		} else {
			// Internal File and update
			return super.showForm(request, response, errors);
		}
	}

	@Override
	protected Object formBackingObject(PortletRequest request) throws Exception {
		ItemForm itemForm = new ItemForm();
		itemId = PortletRequestUtils.getLongParameter(request, Constants.ATT_ID);
		Item item = this.im.getItemById(itemId);
		itemForm.setItem(item);
		List<Topic> topics = this.im.getTopicListByItem(itemId);
		String[] topicIds = new String[topics.size()];
		for (int i = 0; i < topics.size(); i++) {
			topicIds[i] = String.valueOf(topics.get(i).getTopicId());
		}
		itemForm.setTopicIds(topicIds);

		List<Attachment> displayedAttachments = itemForm.getAttachments();

		Long iId = itemForm.getItem().getItemId();
		List<org.cmis.portlets.news.domain.Attachment> attachmentsList = am.getAttachmentsListByItem(iId);
		if (attachmentsList != null) {
			for (org.cmis.portlets.news.domain.Attachment att : attachmentsList) {
				Attachment displayedAtt = itemForm.new Attachment();
				displayedAtt.setId(String.valueOf(att.getAttachmentId()));
				displayedAtt.setTitle(att.getTitle());
				displayedAtt.setDesc(att.getDescription());
				displayedAtt.setInsertDate(att.getInsertDate());
				String fileName = att.getFileName();
				displayedAtt.setType(fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length()));

				displayedAttachments.add(displayedAtt);
			}
		}
		itemForm.setAttachments(displayedAttachments);

		return itemForm;
	}

	@Override
	protected void initBinder(PortletRequest request, PortletRequestDataBinder binder) throws Exception {
		SimpleDateFormat dateFormat = new SimpleDateFormat(getMessageSourceAccessor().getMessage(Constants.DATE_FORMAT));
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, null, new CustomDateEditor(dateFormat, true));
	}

	@Override
	protected Map<String, Object> referenceData(PortletRequest request, Object command, Errors errors, int page)
	throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("ItemEditController: entering method referenceData...");
		}
		ItemForm itemForm = (ItemForm) command;
		Map<String, Object> model = new HashMap<String, Object>();
		if (page == 0) {
			final Long cId = itemForm.getItem().getCategoryId();
			final Long tId = PortletRequestUtils.getLongParameter(request, Constants.ATT_TOPIC_ID);
			if (tId != null) {
				ctxTopicId = tId;
			}
			final String uid = request.getRemoteUser();
			int perm = 0;

			if (ctxTopicId != null) {
				model.put(Constants.ATT_TOPIC_ID, ctxTopicId);
				model.put(Constants.OBJ_TOPIC, this.tm.getTopicById(ctxTopicId));
				perm = RolePerm.valueOf(this.um.getUserRoleInCtx(ctxTopicId, NewsConstants.CTX_T, uid)).getMask();
			}
			if (cId != null) {
				final Category category = this.cm.getCategoryById(cId);
				model.put(Constants.OBJ_CATEGORY, category);
				model.put(Constants.OBJ_ENTITY, this.em.getEntityById(category.getEntityId()));
				model.put(Constants.ATT_NB_DAYS, this.im.getNbDays());
				int permTmp = RolePerm.valueOf(this.um.getUserRoleInCtx(cId, NewsConstants.CTX_C, uid)).getMask();
				if (perm < permTmp) {
					perm = permTmp;
				}
				model.put(Constants.ATT_T_LIST, this.tm.getTopicListForCategoryByUser(cId, uid));
			}

			model.put(Constants.ATT_A_LIST, itemForm.getAttachments());

			model.put(Constants.ATT_USER_ID, request.getRemoteUser());
			model.put(Constants.ATT_PM, perm);
			return model;

		} else if (page == 1 || page == 3) {
			// External File or update an attachment
			
			final Long cId = itemForm.getItem().getCategoryId();
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

		} else if (page == 2) {
			// Internal File
			
			final Long cId = itemForm.getItem().getCategoryId();
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
				if (RolePerm.valueOf(this.um.getUserRoleInCtx(topicId, NewsConstants.CTX_T, userUid)).getMask() > 0) {
					Long currentItemId = itemForm.getItem().getItemId();
					for (Item item : items) {
						Long itemId = item.getItemId();
						if (itemId.compareTo(currentItemId) != 0) {
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

				String[] currenttopicIds = itemForm.getTopicIds();

				List<Topic> topicsList = new ArrayList<Topic>();
				for (Topic topic : topics) {
					Long id = topic.getTopicId();
					if (RolePerm.valueOf(this.um.getUserRoleInCtx(id, NewsConstants.CTX_T, userUid)).getMask() > 0) {
						int nb = im.countItemsWithAttachmentByTopic(id);
						for (String currentTopicId : currenttopicIds) {
							if (currentTopicId.equalsIgnoreCase(Long.toString(id))) {
								// remove one item for this topic
								if (nb > 0) {
									nb = nb - 1;
								}
							}
						}
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
					if (RolePerm.valueOf(this.um.getUserRoleInCtx(id, NewsConstants.CTX_C, userUid)).getMask() > 0) {
						categoriesList.add(category);
					}
				}
				model.put("categories", categoriesList);
			}
			return model;
		}
		return model;
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
	public void setTemporaryStoragePath(String temporaryStoragePath) {
		this.temporaryStoragePath = temporaryStoragePath;
	}

	/**
	 * @return the temporaryStoragePath
	 */
	public String getTemporaryStoragePath() {
		return temporaryStoragePath;
	}

}