package org.cmis.portlets.news.domain;

import java.io.Serializable;

/**
 * 
 * created by Anyware Services - Delphine Gavalda.
 * 
 * 8 juin 2010
 */
public class AttachmentOptions implements Serializable {

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

    public Long getOptionId() {
	return optionId;
    }

    public void setOptionId(Long optionId) {
	this.optionId = optionId;
    }

    public String getGlobalOptions() {
	return globalOptions;
    }

    public void setGlobalOptions(String globalOptions) {
	this.globalOptions = globalOptions;
    }

    public Long getMaxSize() {
	return maxSize;
    }

    public void setMaxSize(Long maxSize) {
	this.maxSize = maxSize;
    }

    public String getAuthorizedFilesExtensions() {
	return authorizedFilesExtensions;
    }

    public void setAuthorizedFilesExtensions(String authorizedFilesExtensions) {
	this.authorizedFilesExtensions = authorizedFilesExtensions;
    }

    public String getForbiddenFilesExtensions() {
	return forbiddenFilesExtensions;
    }

    public void setForbiddenFilesExtensions(String forbiddenFilesExtensions) {
	this.forbiddenFilesExtensions = forbiddenFilesExtensions;
    }
}
