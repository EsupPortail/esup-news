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

public class ItemForm implements Serializable {
	private static final Log LOGGER = LogFactory.getLog(ItemForm.class);

	private static final long serialVersionUID = 1L;

	private Item item = new Item();

	private String[] topicIds;

	private List<Attachment> attachments;

	// Values for external attachment
	private Attachment external = new Attachment();
	// Values for attachment to update
	private Attachment attachmentToUpdate;

	// Values for internal attachment
	private Long categoryId;
	private Long topicId;
	private Long itemId;

	public Item getItem() {
		return this.item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public String[] getTopicIds() {
		return this.topicIds;
	}

	public void setTopicIds(String[] topicIds) {
		this.topicIds = new String[topicIds.length];
		System.arraycopy(topicIds, 0, this.topicIds, 0, topicIds.length);
	}

	public Attachment getExternal() {
		return external;
	}

	public void setExternal(Attachment external) {
		this.external = external;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public Long getTopicId() {
		return topicId;
	}

	public void setTopicId(Long topicId) {
		this.topicId = topicId;
	}

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public List<Attachment> getAttachments() {
		if (attachments == null) {
			attachments = new ArrayList<Attachment>();
		}
		return attachments;
	}

	public void setAttachments(List<Attachment> externalAttachments) {
		this.attachments = externalAttachments;
	}

	public void addInternalAttachment(org.cmis.portlets.news.domain.Attachment attachment) {
		Attachment att = new Attachment();
		att.setId(attachment.getAttachmentId().toString());
		att.setTitle(attachment.getTitle());
		att.setDesc(attachment.getDescription());
		att.setInsertDate(attachment.getInsertDate());

		String filename = attachment.getFileName();
		att.setType(filename.substring(filename.lastIndexOf(".") + 1, filename.length()));

		getAttachments().add(att);
	}
	
	public void addExternalAttachment(final String temporaryStoragePath) {
	    addExternalAttachment(temporaryStoragePath, null);
	}
	
	public void addExternalAttachment(final String temporaryStoragePath, final String userPrefix) {
		if (getExternal().getTitle().length() > 1) {
			Attachment external = getExternal();
			
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
					+ external.getFile().getOriginalFilename() + postfix);

				outputStream = new FileOutputStream(tmp);
				inputStream = external.getFile().getInputStream();
				byte[] buf = new byte[(int) external.getFile().getSize()];
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
				external.setTempDiskStoredFile(tmp);
				external.setMimeType(external.getFile().getContentType());
				getAttachments().add(external);
			}
			setExternal(new Attachment());
		}
	}

	public void cleanExternalAttachment() {
		setExternal(new Attachment());
	}

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
	 * @param attachmentToUpdate the attachmentToUpdate to set
	 */
	public void setAttachmentToUpdate(String id, String title, String desc) {
		this.attachmentToUpdate = new Attachment();
		attachmentToUpdate.setId(id);
		attachmentToUpdate.setTitle(title);
		attachmentToUpdate.setDesc(desc);
	}

	public class Attachment {
		private String id = "";
		private MultipartFile file;
		private File tempDiskStoredFile;
		private String type;
		private String mimeType;
		private String title;
		private String desc;
		private Date insertDate = new Date();

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getDesc() {
			return desc;
		}

		public void setDesc(String desc) {
			this.desc = desc;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public void setFile(MultipartFile file) {
			this.file = file;
			String originalFilename = file.getOriginalFilename();
			if (StringUtils.isNotEmpty(originalFilename))
			{
				String type = originalFilename.substring(originalFilename.lastIndexOf(".") + 1, originalFilename.length());
				setType(type);
			}
		}

		public MultipartFile getFile() {
			return file;
		}

		public Date getInsertDate() {
			return insertDate;
		}

		public void setInsertDate(Date insertDate) {
			this.insertDate = insertDate;
		}

		public void setTempDiskStoredFile(File tempDiskStoredFile) {
			this.tempDiskStoredFile = tempDiskStoredFile;
		}

		public File getTempDiskStoredFile() {
			return tempDiskStoredFile;
		}

		public void setMimeType(String mimeType) {
			this.mimeType = mimeType;
		}

		public String getMimeType() {
			return mimeType;
		}
	}


}
