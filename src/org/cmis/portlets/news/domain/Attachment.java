package org.cmis.portlets.news.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * Attachment bean created by Anyware Services - Delphine Gavalda.
 * 
 * 10 mai 2010
 */
public class Attachment implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * data base ID.
     */
    private Long attachmentId;
    /**
     * CMIS ID.
     */
    private String cmisUid;
    /**
     * File name.
     */
    private String fileName;
    /**
     * File title.
     */
    private String title;
    /**
     * File description.
     */
    private String description;
    /**
     * InsertDate.
     */
    private Date insertDate;
    /**
     * File path.
     */
    private String path;
    /**
     * File size.
     */
    private Long size;

    /**
     * @return the attachmentId
     */
    public Long getAttachmentId() {
	return attachmentId;
    }

    /**
     * @param attachmentId
     *            the attachmentId to set
     */
    public void setAttachmentId(Long attachmentId) {
	this.attachmentId = attachmentId;
    }

    /**
     * @return the cmisUid
     */
    public String getCmisUid() {
	return cmisUid;
    }

    /**
     * @param cmisUid
     *            the cmisUid to set
     */
    public void setCmisUid(String cmisUid) {
	this.cmisUid = cmisUid;
    }

    /**
     * @return the fileName
     */
    public String getFileName() {
	return fileName;
    }

    /**
     * @param fileName
     *            the fileName to set
     */
    public void setFileName(String fileName) {
	this.fileName = fileName;
    }

    /**
     * @return the title
     */
    public String getTitle() {
	return title;
    }

    /**
     * @param title
     *            the title to set
     */
    public void setTitle(String title) {
	this.title = title;
    }

    /**
     * @return the description
     */
    public String getDescription() {
	return description;
    }

    /**
     * @param description
     *            the description to set
     */
    public void setDescription(String description) {
	this.description = description;
    }

    /**
     * @return the insertDate
     */
    public Date getInsertDate() {
	return insertDate;
    }

    /**
     * @param insertDate
     *            the insertDate to set
     */
    public void setInsertDate(Date insertDate) {
	this.insertDate = insertDate;
    }

    /**
     * @return the path
     */
    public String getPath() {
	return path;
    }

    /**
     * @param path
     *            the path to set
     */
    public void setPath(String path) {
	this.path = path;
    }

    /**
     * Get the file size
     * 
     * @return
     */
    public Long getSize() {
	return size;
    }

    /**
     * Set the file size
     * 
     * @param size
     */
    public void setSize(Long size) {
	this.size = size;
    }

    /**
     * File type
     * 
     * @return
     */
    public String getType() {
	return this.fileName.substring(this.fileName.lastIndexOf(".") + 1, this.fileName.length());
    }
}
