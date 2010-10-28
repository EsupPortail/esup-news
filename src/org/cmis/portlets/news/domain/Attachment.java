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
     * Constructor of the Attachment object.
     */
    public Attachment() {
	 super();
    }
    
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
    @SuppressWarnings("hiding")
    public void setAttachmentId(final Long attachmentId) {
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
    @SuppressWarnings("hiding")
    public void setCmisUid(final String cmisUid) {
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
    @SuppressWarnings("hiding")
    public void setFileName(final String fileName) {
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
    @SuppressWarnings("hiding")
    public void setTitle(final String title) {
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
    @SuppressWarnings("hiding")
    public void setDescription(final String description) {
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
    @SuppressWarnings("hiding")
    public void setInsertDate(final Date insertDate) {
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
    @SuppressWarnings("hiding")
    public void setPath(final String path) {
	this.path = path;
    }

    /**
     * Get the file size.
     * 
     * @return Long 
     */
    public Long getSize() {
	return size;
    }

    /**
     * Set the file size.
     * 
     * @param size
     */
    @SuppressWarnings("hiding")
    public void setSize(final Long size) {
	this.size = size;
    }

    /**
     * File type.
     * 
     * @return String
     */
    public String getType() {
	return this.fileName.substring(this.fileName.lastIndexOf(".") + 1, this.fileName.length());
    }
}
