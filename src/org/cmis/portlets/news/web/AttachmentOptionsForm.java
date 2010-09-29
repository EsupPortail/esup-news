package org.cmis.portlets.news.web;

import java.io.Serializable;
import java.util.List;

public class AttachmentOptionsForm implements Serializable {
    private static final long serialVersionUID = 1L;

    private long optionsId;
    /**
     * New max size in kilobytes
     */
    private String maxSize;
    /**
     * Displayed max size in kilobytes
     */
    private String displayedMaxSize;

    private List<String> authorizedList;

    private String authorizedExts;

    private List<String> forbiddenList;

    private String forbiddenExts;

    private long entityId;

    private String useEntityOptions;

    public void setMaxSize(String maxSize) {
	this.maxSize = maxSize;
    }

    public String getMaxSize() {
	return maxSize;
    }

    public void setForbiddenList(List<String> forbiddenList) {
	this.forbiddenList = forbiddenList;
    }

    public List<String> getForbiddenList() {
	return forbiddenList;
    }

    public void setAuthorizedList(List<String> authorizedList) {
	this.authorizedList = authorizedList;
    }

    public List<String> getAuthorizedList() {
	return authorizedList;
    }

    public void setOptionsId(long optionsId) {
	this.optionsId = optionsId;
    }

    public long getOptionsId() {
	return optionsId;
    }

    public void setAuthorizedExts(String authorizedExts) {
	this.authorizedExts = authorizedExts;
    }

    public String getAuthorizedExts() {
	return authorizedExts;
    }

    public void setForbiddenExts(String forbiddenExts) {
	this.forbiddenExts = forbiddenExts;
    }

    public String getForbiddenExts() {
	return forbiddenExts;
    }

    public long getEntityId() {
	return entityId;
    }

    public void setEntityId(long entityId) {
	this.entityId = entityId;
    }

    public void setUseEntityOptions(String useEntityOptions) {
	this.useEntityOptions = useEntityOptions;
    }

    public String getUseEntityOptions() {
	return useEntityOptions;
    }

    public String getDisplayedMaxSize() {
	return displayedMaxSize;
    }

    public void setDisplayedMaxSize(String displayedMaxSize) {
	this.displayedMaxSize = displayedMaxSize;
    }

}
