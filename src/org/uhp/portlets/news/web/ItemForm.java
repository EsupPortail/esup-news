package org.uhp.portlets.news.web;

/**
 * @Project NewsPortlet : http://sourcesup.cru.fr/newsportlet/ Copyright (C)
 *          2007-2008 University Nancy 1
 *
 *          This program is free software; you can redistribute it and/or modify
 *          it under the terms of the GNU General Public License as published by
 *          the Free Software Foundation version 2 of the License.
 *
 *          This program is distributed in the hope that it will be useful, but
 *          WITHOUT ANY WARRANTY; without even the implied warranty of
 *          MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *          General Public License for more details.
 *
 *          You should have received a copy of the GNU General Public License
 *          along with this program; if not, write to the Free Software
 *          Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 *          02110-1301, USA.
 */

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.multipart.MultipartFile;
import org.uhp.portlets.news.domain.Item;

/**
 * @author GIP RECIA - Julien Gribonvald
 * 4 mai 2012
 */
public class ItemForm implements Serializable {
	/** */
	private static final Log LOGGER = LogFactory.getLog(ItemForm.class);

	/** */
	private static final long serialVersionUID = 1L;
	/** */
	private Item item = new Item();
	/** */
	private String[] topicIds;
	/** */
	private List<Attachment> attachments;

	/** Values for external attachment. */
	private Attachment external = new Attachment();
	/** Values for attachment to update. */
	private Attachment attachmentToUpdate;

	/** Values for internal attachment. */
	private Long categoryId;
	/** */
	private Long topicId;
	/** */
	private Long itemId;

	/**
	 * @return <code> Item</code>
	 */
	public Item getItem() {
		return this.item;
	}
	/**
	 * @param item
	 */
	public void setItem(final Item item) {
		this.item = item;
	}
	/**
	 * @return String[]
	 */
	public String[] getTopicIds() {
		return this.topicIds;
	}
	/**
	 * @param topicIds
	 */
	public void setTopicIds(final String[] topicIds) {
		this.topicIds = new String[topicIds.length];
		System.arraycopy(topicIds, 0, this.topicIds, 0, topicIds.length);
	}
	/**
	 * @return Attachment
	 */
	public Attachment getExternal() {
		return external;
	}
	/**
	 * @param external
	 */
	public void setExternal(final Attachment external) {
		this.external = external;
	}
	/**
	 * @return Long
	 */
	public Long getCategoryId() {
		return categoryId;
	}
	/**
	 * @param categoryId
	 */
	public void setCategoryId(final Long categoryId) {
		this.categoryId = categoryId;
	}
	/**
	 * @return Long
	 */
	public Long getTopicId() {
		return topicId;
	}
	/**
	 * @param topicId
	 */
	public void setTopicId(final Long topicId) {
		this.topicId = topicId;
	}
	/**
	 * @return Long
	 */
	public Long getItemId() {
		return itemId;
	}
	/**
	 * @param itemId
	 */
	public void setItemId(final Long itemId) {
		this.itemId = itemId;
	}
	/**
	 * @return List<Attachment>
	 */
	public List<Attachment> getAttachments() {
		if (attachments == null) {
			attachments = new ArrayList<Attachment>();
		}
		return attachments;
	}
	/**
	 * @param externalAttachments
	 */
	public void setAttachments(final List<Attachment> externalAttachments) {
		this.attachments = externalAttachments;
	}
	/**
	 * @param attachment
	 */
	public void addInternalAttachment(final org.cmis.portlets.news.domain.Attachment attachment) {
		Attachment att = new Attachment();
		att.setId(attachment.getAttachmentId().toString());
		att.setTitle(attachment.getTitle());
		att.setDesc(attachment.getDescription());
		att.setInsertDate(attachment.getInsertDate());

		String filename = attachment.getFileName();
		att.setType(filename.substring(filename.lastIndexOf(".") + 1, filename.length()));

		getAttachments().add(att);
	}
	/**
	 * @param temporaryStoragePath
	 */
	public void addExternalAttachment(final String temporaryStoragePath) {
	    addExternalAttachment(temporaryStoragePath, null);
	}
	/**
	 * @param temporaryStoragePath
	 * @param userPrefix
	 */
	public void addExternalAttachment(final String temporaryStoragePath, final String userPrefix) {
		if (getExternal().getTitle().length() > 1) {
			final Attachment ext = getExternal();

			String postfix = "." + String.valueOf(Calendar.getInstance().getTimeInMillis());
			String prefix = "";
			if (userPrefix != null) {
			    prefix = userPrefix;
			} else if (item.getItemId() != null) {
			    // Get the item id to prefix the filename
			    prefix = String.valueOf(item.getItemId()) + "_";
			}

			File tmp = null;
			FileOutputStream outputStream = null;
			InputStream inputStream = null;
			try {
				File dir = new File(temporaryStoragePath);
				if (!dir.exists()) {
					dir.mkdir();
				}

				tmp = new File(temporaryStoragePath + "/" + prefix
					+ ext.getFile().getOriginalFilename() + postfix);

				outputStream = new FileOutputStream(tmp);
				inputStream = ext.getFile().getInputStream();
				byte[] buf = new byte[(int) ext.getFile().getSize()];
    				int len;
    				while ((len = inputStream.read(buf)) > 0) {
    				    outputStream.write(buf, 0, len);
    				}

			} catch (Exception e) {
				LOGGER.error(e, e.fillInStackTrace());
			} finally {
				try {
					if (outputStream != null) {
						outputStream.close();
					}
					if (inputStream != null) {
						inputStream.close();
					}
				} catch (IOException e) {
					LOGGER.error(e, e.fillInStackTrace());
				}
			}
			if (tmp != null) {
				ext.setTempDiskStoredFile(tmp);
				ext.setMimeType(ext.getFile().getContentType());
				getAttachments().add(ext);
			}
			setExternal(new Attachment());
		}
	}

	/**
	 *
	 */
	public void cleanExternalAttachment() {
		setExternal(new Attachment());
	}

	/**
	 *
	 */
	public void cleanInternalAttachment() {
		categoryId = Long.valueOf(-1);
		topicId = Long.valueOf(-1);
		itemId = Long.valueOf(-1);
	}


	/**
	 * @return the attachmentToUpdate
	 */
	public Attachment getAttachmentToUpdate() {
		return attachmentToUpdate;
	}

	/**
	 * @param id
	 * @param title
	 * @param desc
	 */
	public void setAttachmentToUpdate(String id, String title, String desc) {
		this.attachmentToUpdate = new Attachment();
		attachmentToUpdate.setId(id);
		attachmentToUpdate.setTitle(title);
		attachmentToUpdate.setDesc(desc);
	}

	/**
	 * modified by GIP RECIA - Julien Gribonvald
	 * 4 mai 2012
	 */
	public class Attachment {
		/** */
		private String id = "";
		/** */
		private MultipartFile file;
		/** */
		private File tempDiskStoredFile;
		/** */
		private String type;
		/** */
		private String mimeType;
		/** */
		private String title;
		/** */
		private String desc;
		/** */
		private Date insertDate = new Date();

		/**
		 * @return String
		 */
		public String getId() {
			return id;
		}
		/**
		 * @param id
		 */
		public void setId(final String id) {
			this.id = id;
		}
		/**
		 * @return String
		 */
		public String getTitle() {
			return title;
		}
		/**
		 * @param title
		 */
		public void setTitle(String title) {
			this.title = title;
		}
		/**
		 * @return String
		 */
		public String getDesc() {
			return desc;
		}
		/**
		 * @param desc
		 */
		public void setDesc(String desc) {
			this.desc = desc;
		}
		/**
		 * @return String
		 */
		public String getType() {
			return type;
		}

		/**
		 * @param type
		 */
		public void setType(String type) {
			this.type = type;
		}

		/**
		 * @param file
		 */
		public void setFile(MultipartFile file) {
			this.file = file;
			String originalFilename = file.getOriginalFilename();
			if (StringUtils.isNotEmpty(originalFilename))
			{
				String type = originalFilename.substring(originalFilename.lastIndexOf(".") + 1, originalFilename.length());
				setType(type);
			}
		}
		/**
		 * @return MultipartFile
		 */
		public MultipartFile getFile() {
			return file;
		}
		/**
		 * @return Date
		 */
		public Date getInsertDate() {
			return insertDate;
		}
		/**
		 * @param insertDate
		 */
		public void setInsertDate(Date insertDate) {
			this.insertDate = insertDate;
		}
		/**
		 * @param tempDiskStoredFile
		 */
		public void setTempDiskStoredFile(File tempDiskStoredFile) {
			this.tempDiskStoredFile = tempDiskStoredFile;
		}
		/**
		 * @return File
		 */
		public File getTempDiskStoredFile() {
			return tempDiskStoredFile;
		}
		/**
		 * @param mimeType
		 */
		public void setMimeType(String mimeType) {
			this.mimeType = mimeType;
		}
		/**
		 * @return String
		 */
		public String getMimeType() {
			return mimeType;
		}
	}


}
