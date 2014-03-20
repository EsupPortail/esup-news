/**
* ESUP-Portail News - Copyright (c) 2009 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.cmis.portlets.news.services;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cmis.portlets.news.dao.AttachmentDao;
import org.cmis.portlets.news.dao.AttachmentOptionsDao;
import org.cmis.portlets.news.dao.CmisAttachmentDao;
import org.cmis.portlets.news.dao.CmisPathFinderHelper;
import org.cmis.portlets.news.dao.CmisServerParamsDao;
import org.cmis.portlets.news.domain.Attachment;
import org.cmis.portlets.news.domain.AttachmentOptions;
import org.cmis.portlets.news.domain.CmisServer;
import org.cmis.portlets.news.services.exceptions.CmisException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.uhp.portlets.news.web.ItemForm;

/**
 *
 * created by Anyware Services - Delphine Gavalda.
 *
 * 10 mai 2010
 */
@Service("attachmentManager")
@Transactional(readOnly = true)
public class AttachmentManagerImpl implements AttachmentManager {

	/** */
	private static final Log LOG = LogFactory.getLog(AttachmentManagerImpl.class);
	/** attachmentdao. */
	@Autowired
	private AttachmentDao attachmentDao;
	/** attachmentOptionsDao. */
	@Autowired
	private AttachmentOptionsDao attachmentOptionsDao;
	/** cmisServerdao. */
	@Autowired
	private CmisServerParamsDao cmisServerDao;
	/** cmisDao. */
	@Autowired
	private CmisAttachmentDao cmisDao;


	/**
	 * Constructor.
	 */
	public AttachmentManagerImpl() {
		super();
	}

	/***************************
	 * Cmis server methods.
	 ***************************/
	/**
	 * @see org.cmis.portlets.news.services.AttachmentManager#getApplicationServer()
	 */
	public CmisServer getApplicationServer() {
		return cmisServerDao.getApplicationServer();
	}

	/**
	 * @see org.cmis.portlets.news.services.AttachmentManager#getEntityServer(java.lang.Long)
	 */
	public CmisServer getEntityServer(final Long entityId) {
		return cmisServerDao.getEntityServer(entityId);
	}

	/**
	 * @see org.cmis.portlets.news.services.AttachmentManager#insertServerParams(org.cmis.portlets.news.domain.CmisServer)
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public Long insertServerParams(final CmisServer serverParams) {
		Long newID = cmisServerDao.insertServerParams(serverParams);
		return newID;
	}

	/**
	 * @see org.cmis.portlets.news.services.AttachmentManager#linkServerToEntity(java.lang.Long, java.lang.Long)
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void linkServerToEntity(final Long serverId, final Long entityId) {
		cmisServerDao.linkServerToEntity(serverId, entityId);
	}

	/**
	 * @see org.cmis.portlets.news.services.AttachmentManager#updateServerInfos(org.cmis.portlets.news.domain.CmisServer)
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void updateServerInfos(final CmisServer serverParams) {
		cmisServerDao.updateServerInfos(serverParams);
	}

	/**
	 * @see org.cmis.portlets.news.services.AttachmentManager#deleteServerLinkToEntity(java.lang.Long)
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteServerLinkToEntity(final Long entityId) {
		cmisServerDao.deleteServerLinkToEntity(entityId);
	}

	/**
	 * @see org.cmis.portlets.news.services.AttachmentManager#deleteServerParams(java.lang.Long)
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteServerParams(final Long serverId) {
		cmisServerDao.deleteServerParams(serverId);
	}

	/********************************************
	 * Attachement parameters management methods.
	 ********************************************/
	/**
	 * @see org.cmis.portlets.news.services.AttachmentManager#getApplicationAttachmentOptions()
	 */
	public AttachmentOptions getApplicationAttachmentOptions() {
		return attachmentOptionsDao.getApplicationOptions();
	}

	/**
	 * @see org.cmis.portlets.news.services.AttachmentManager#getEntityAttachmentOptions(java.lang.Long)
	 */
	public AttachmentOptions getEntityAttachmentOptions(final Long entityId) {
		return attachmentOptionsDao.getEntityOptions(entityId);
	}

	/**
	 * @see org.cmis.portlets.news.services.AttachmentManager#updateAttachmentOptions(org.cmis.portlets.news.domain.AttachmentOptions)
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void updateAttachmentOptions(final AttachmentOptions options) {
		attachmentOptionsDao.updateAttachmentOptions(options);
	}

	/**
	 * @see org.cmis.portlets.news.services.AttachmentManager#insertAttachmentOptions(org.cmis.portlets.news.domain.AttachmentOptions)
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public Long insertAttachmentOptions(final AttachmentOptions options) {
		Long newID = attachmentOptionsDao.insertAttachmentOptions(options);
		return newID;
	}

	/**
	 * @see org.cmis.portlets.news.services.AttachmentManager#linkAttachmentOptionsToEntity(java.lang.Long, java.lang.Long)
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void linkAttachmentOptionsToEntity(final Long optionsId, final Long entityId) {
		attachmentOptionsDao.linkAttachmentOptionsToEntity(optionsId, entityId);
	}

	/**
	 * @see org.cmis.portlets.news.services.AttachmentManager#deleteAttachmentOptsLinkToEntity(java.lang.Long)
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteAttachmentOptsLinkToEntity(final Long entityId) {
		attachmentOptionsDao.deleteAttachmentOptsLinkToEntity(entityId);
	}

	/**
	 * @see org.cmis.portlets.news.services.AttachmentManager#deleteAttachmentOptions(java.lang.Long)
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteAttachmentOptions(final Long optionsId) {
		attachmentOptionsDao.deleteAttachmentOptions(optionsId);
	}

	/***************************************
	 * Attachement management methods.
	 *
	 * @throws CmisException
	 ***************************************/

	/**
	 * @see org.cmis.portlets.news.services.AttachmentManager#addAttachmentToItem(org.uhp.portlets.news.web.ItemForm, java.lang.Long, java.lang.Long, java.lang.String)
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void addAttachmentToItem(final ItemForm itemForm, final Long itemId, final Long entityId, final String userID)
	throws DataAccessException, CmisException {
		List<org.uhp.portlets.news.web.ItemForm.Attachment> attachments = itemForm.getAttachments();
		for (org.uhp.portlets.news.web.ItemForm.Attachment att : attachments) {
			if (StringUtils.isNotEmpty(att.getId())) {
				attachmentDao.addAttachmentToItem(Long.valueOf(att.getId()), itemId);
			} else {
				// save the new file and get the sqlMap object
				Long categoryId = itemForm.getCategoryId();
				String[] topicIds = itemForm.getTopicIds();
				Date insertDate = att.getInsertDate();
				String originalFilename = att.getTempDiskStoredFile().getName();

				String userPrefix = "user_" + userID + "_";
				if (originalFilename.startsWith(userPrefix)) {
				    // remove the user id prefix
				    originalFilename = originalFilename.substring(userPrefix.length());
				}
				// remove the postfix
				originalFilename = originalFilename.substring(0, originalFilename.lastIndexOf("."));

				Map<String, Object> prop = new HashMap<String, Object>();
				prop.put(CmisPathFinderHelper.FILE_NAME, originalFilename);
				prop.put(CmisPathFinderHelper.INSERT_DATE, insertDate);
				prop.put(CmisPathFinderHelper.CATEGORY_ID, categoryId);
				prop.put(CmisPathFinderHelper.TOPICS_ID, topicIds);
				prop.put(CmisPathFinderHelper.ENTITY_ID, entityId);

				Attachment sqlAtt = cmisDao.insertAttachment(att, entityId, prop);

				if(sqlAtt != null) {
        				try {
        					// link this file to the news
        					long sqlId = attachmentDao.insertAttachment(sqlAtt);
        					attachmentDao.addAttachmentToItem(sqlId, itemId);
        				} catch (Exception e) {
        					LOG.error(e, e.fillInStackTrace());
        				}
				}
			}
		}
	}

	/**
	 * @see org.cmis.portlets.news.services.AttachmentManager#updateItemAttachment(org.uhp.portlets.news.web.ItemForm, java.lang.Long, java.lang.Long)
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void updateItemAttachment(final ItemForm itemForm, final Long itemId,
			final Long entityId) throws DataAccessException,
			CmisException {
		// Current list of attachments
		List<org.uhp.portlets.news.web.ItemForm.Attachment> attachments = itemForm.getAttachments();
		// Old stored list of attachments
		List<Attachment> storedAttachmentsList = getAttachmentsListByItem(itemId);

		// Find those to delete
		for (Attachment oldAtt : storedAttachmentsList) {
			Long oldAttId = oldAtt.getAttachmentId();
			boolean exists = false;
			for (org.uhp.portlets.news.web.ItemForm.Attachment formAtt : attachments) {
				if (StringUtils.isNotEmpty(formAtt.getId())) {
					if (formAtt.getId().equalsIgnoreCase(String.valueOf(oldAttId))) {
						// this attchment is already binded to the news item
						exists = true;

						// update the attachment
						Map<String, Object> params = new HashMap<String, Object>();
						params.put("attachmentId", Long.valueOf(formAtt.getId()));
						params.put("title", formAtt.getTitle());
						params.put("description", formAtt.getDesc());
						attachmentDao.updateAttachment(params);

						// remove from list
						attachments.remove(formAtt);
						break;
					}
				}
			}
			if (!exists) {
				// delete attachement for this item
				deleteAttachment(oldAtt, itemId, entityId);
			}
		}

		// Save all that remains
		for (org.uhp.portlets.news.web.ItemForm.Attachment formAtt : attachments) {
			if (StringUtils.isNotEmpty(formAtt.getId())) {
				attachmentDao.addAttachmentToItem(Long.valueOf(formAtt.getId()), itemId);
				// update the title and the descr, if needed
				Attachment oldAtt = getAttachmentById(Long.valueOf(formAtt.getId()));
				if (!oldAtt.getTitle().equalsIgnoreCase(formAtt.getTitle())
					|| !oldAtt.getDescription().equalsIgnoreCase(formAtt.getDesc())) {
				    	// update the attachment
					Map<String, Object> params = new HashMap<String, Object>();
					params.put("attachmentId", Long.valueOf(formAtt.getId()));
					params.put("title", formAtt.getTitle());
					params.put("description", formAtt.getDesc());
					attachmentDao.updateAttachment(params);
				}

			} else {
				// save the new file and get the sqlMap object
				Long categoryId = itemForm.getCategoryId();
				String[] topicIds = itemForm.getTopicIds();
				Date insertDate = formAtt.getInsertDate();
				String originalFilename = formAtt.getTempDiskStoredFile().getName();

				String prefix = String.valueOf(itemId) + "_";
				if (originalFilename.startsWith(prefix)) {
				    // remove the item id prefix
				    originalFilename = originalFilename.substring(prefix.length());
				}
				// remove the postfix
				originalFilename = originalFilename.substring(0, originalFilename.lastIndexOf("."));

				Map<String, Object> prop = new HashMap<String, Object>();
				prop.put(CmisPathFinderHelper.FILE_NAME, originalFilename);
				prop.put(CmisPathFinderHelper.INSERT_DATE, insertDate);
				prop.put(CmisPathFinderHelper.CATEGORY_ID, categoryId);
				prop.put(CmisPathFinderHelper.TOPICS_ID, topicIds);
				prop.put(CmisPathFinderHelper.ENTITY_ID, entityId);

				Attachment sqlAtt = cmisDao.insertAttachment(formAtt, entityId, prop);

				if (sqlAtt != null) {
					try {
						// link this file to the news
						long sqlId = attachmentDao.insertAttachment(sqlAtt);
						attachmentDao.addAttachmentToItem(sqlId, itemId);
					} catch (Exception e) {
						LOG.error(e, e.fillInStackTrace());
					}
				}
			}
		}

	}

	/**
	 * @see org.cmis.portlets.news.services.AttachmentManager#getAttachmentsListByItem(java.lang.Long)
	 */
	public List<Attachment> getAttachmentsListByItem(final Long itemId) throws DataAccessException {
		return this.attachmentDao.getAttachmentsListByItem(itemId);
	}

	/**
	 * @see org.cmis.portlets.news.services.AttachmentManager#getAttachmentById(java.lang.Long)
	 */
	public Attachment getAttachmentById(final Long id) throws DataAccessException {
		return this.attachmentDao.getAttachmentById(id);
	}

	/**
	 * @see org.cmis.portlets.news.services.AttachmentManager#deleteAttachment(org.cmis.portlets.news.domain.Attachment, java.lang.Long, java.lang.Long)
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteAttachment(final Attachment attachment, final Long itemId,
			final Long entityId) throws DataAccessException,
			CmisException {
		// remove the link between this item and the attachment

		Long attachmentId = attachment.getAttachmentId();
		this.attachmentDao.deleteAttachmentForItem(attachment.getAttachmentId(), itemId);

		// Is this attachment still linked to items ?
		List<Long> itemsLinkedToAttachment = this.attachmentDao.getItemsLinkedToAttachment(attachmentId);
		if (itemsLinkedToAttachment != null) {
			if (itemsLinkedToAttachment.size() == 0) {
				// delete totally this attachement
				this.attachmentDao.deleteAttachment(attachmentId);
				cmisDao.deleteAttachment(attachment.getCmisUid(), entityId);
			}
		} else {
			// delete totally this attachement
			this.attachmentDao.deleteAttachment(attachmentId);
			cmisDao.deleteAttachment(attachment.getCmisUid(), entityId);
		}
	}

	/**
	 * @see org.cmis.portlets.news.services.AttachmentManager#deleteItemAttachments(java.lang.Long, java.lang.Long)
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteItemAttachments(final Long itemId, final Long entityId)
	throws DataAccessException, CmisException {
		List<Attachment> attachments = getAttachmentsListByItem(itemId);
		if (attachments != null) {
			for (Attachment att : attachments) {
				// remove the link between this item and the attachment
				this.attachmentDao.deleteAttachmentForItem(att.getAttachmentId(), itemId);

				// Is this attachment still linked to items ?
				List<Long> itemsLinkedToAttachment = this.attachmentDao.getItemsLinkedToAttachment(att
						.getAttachmentId());
				if (itemsLinkedToAttachment != null) {
					if (itemsLinkedToAttachment.size() == 0) {
						// delete totally this attachement
						this.attachmentDao.deleteAttachment(att.getAttachmentId());
						this.cmisDao.deleteAttachment(att.getCmisUid(), entityId);
					}
				} else {
					// delete totally this attachement
					this.attachmentDao.deleteAttachment(att.getAttachmentId());
					this.cmisDao.deleteAttachment(att.getCmisUid(), entityId);
				}
			}
		}
	}

	/**
	 * @see org.cmis.portlets.news.services.AttachmentManager#cleanTempStorageDirectory(java.lang.String)
	 */
	public void cleanTempStorageDirectory(final String path) {
	    cleanTempStorageDirectory(path, null);
	}

	/**
	 * @see org.cmis.portlets.news.services.AttachmentManager#cleanTempStorageDirectory(java.lang.String, java.lang.String)
	 */
	public void cleanTempStorageDirectory(final String path, final String prefix) {
		try {
		    	if(prefix != null){
		    	    File dir = new File(path);
		    	    if (dir.exists() && dir.isDirectory()) {
		    		File[] listFiles = dir.listFiles();
				for (File file : listFiles) {
				    if(file.getName().startsWith(prefix)) {
					file.delete();
				    }
				}
		    	    }
		    	} else {
        			long now = Calendar.getInstance().getTimeInMillis();
        			File dir = new File(path);
        			if (dir.exists() && dir.isDirectory()) {
        				File[] listFiles = dir.listFiles();
        				for (File file : listFiles) {
        					long lastModified = file.lastModified();
        					// Test if this file has been added more than 30 min ago
        					if (((now - lastModified) / 1000) > 1800) {
        						file.delete();
        					}
        				}
        			}
		    	}
		} catch (Exception e) {
			LOG.error("Unable to delete temporary files from " + path, e);
		}
	}

}
