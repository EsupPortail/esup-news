/**
* ESUP-Portail News - Copyright (c) 2009 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.cmis.portlets.news.domain;

import java.io.Serializable;

/**
 *
 * created by Anyware Services - Delphine Gavalda.
 *
 * 8 juin 2010
 */
public class AttachmentOptions implements Serializable {

	/** */
    private static final long serialVersionUID = 1L;

    /**
     * ID.
     */
    private Long optionId;
    /**
     * equals "1" if are the defaults options for the application.
     */
    private String globalOptions;
    /**
     * upload max size, in bytes.
     */
    private Long maxSize;
    /**
     * Authorized files extensions.
     */
    private String authorizedFilesExtensions;
    /**
     * Forbidden files extensions.
     */
    private String forbiddenFilesExtensions;

    /**
     * Constructor of the AttachmentOptions object.
     */
    public AttachmentOptions() {
	 super();
    }

    /**
     * Get the Id.
     * @return Long
     */
    public Long getOptionId() {
	return optionId;
    }

    /**
     * Set the Id.
     * @param optionId
     */
    public void setOptionId(final Long optionId) {
	this.optionId = optionId;
    }

    /**
     * Return "1" if these values are the defaults ones defined for the application.
     * @return String
     */
    public String getGlobalOptions() {
	return globalOptions;
    }

    /**
     * Set "1" if these values are the defaults ones defined for the application.
     * @param globalOptions
     */
    public void setGlobalOptions(final String globalOptions) {
	this.globalOptions = globalOptions;
    }

    /**
     * Get the max size for files upload.
     * @return Long
     */
    public Long getMaxSize() {
	return maxSize;
    }

    /**
     * Set the max size for files upload.
     * @param maxSize
     */
    public void setMaxSize(final Long maxSize) {
	this.maxSize = maxSize;
    }

    /**
     * Get the authorized extensions, separated by ";".
     * @return String
     */
    public String getAuthorizedFilesExtensions() {
	return authorizedFilesExtensions;
    }

    /**
     * Set the authorized extensions, separated by ";".
     * @param authorizedFilesExtensions
     */
    public void setAuthorizedFilesExtensions(final String authorizedFilesExtensions) {
	this.authorizedFilesExtensions = authorizedFilesExtensions;
    }

    /**
     * Get the forbidden extensions, separated by ";".
     * @return String
     */
    public String getForbiddenFilesExtensions() {
	return forbiddenFilesExtensions;
    }

    /**
     * Set the forbidden extensions, separated by ";".
     * @param forbiddenFilesExtensions
     */
    public void setForbiddenFilesExtensions(final String forbiddenFilesExtensions) {
	this.forbiddenFilesExtensions = forbiddenFilesExtensions;
    }
}
