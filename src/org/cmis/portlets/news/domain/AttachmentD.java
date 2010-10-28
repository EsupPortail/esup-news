package org.cmis.portlets.news.domain;

import java.io.Serializable;

import org.apache.chemistry.opencmis.client.api.Document;

/**
 * Attachment to download
 * 
 * created by Anyware Services - Delphine Gavalda.
 * 
 * 4 juin 2010
 */
public class AttachmentD implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long attachmentId;

    private String cmisUid;

    private String fileName;

    private Document cmisDocument;

    /**
     * Constructor of the AttachmentD object.
     */
    public AttachmentD() {
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
     * Get the Cmis document.
     * @return Document
     */
    public Document getCmisDocument() {
	return cmisDocument;
    }

    /**
     * Set the Cmis document.
     * @param cmisDocument
     */
    @SuppressWarnings("hiding")
    public void setCmisDocument(final Document cmisDocument) {
	this.cmisDocument = cmisDocument;
    }
}
