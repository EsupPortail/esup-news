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

    public Document getCmisDocument() {
	return cmisDocument;
    }

    public void setCmisDocument(Document cmisDocument) {
	this.cmisDocument = cmisDocument;
    }
}
