package org.cmis.portlets.news.domain;

import java.io.Serializable;

import org.apache.chemistry.opencmis.client.api.Document;

/**
 * @author GIP RECIA - Julien Gribonvald
 * 23 mai 2012
 */
public class AttachmentD implements Serializable {

	/** */
	private static final long serialVersionUID = 1L;

	/** Id of attachment. */
	private Long attachmentId;
	/** CMIS UID. */
	private String cmisUid;
	/** FileName. */
	private String fileName;
	/** CMISDocument. */
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
	public void setCmisDocument(final Document cmisDocument) {
		this.cmisDocument = cmisDocument;
	}
}
