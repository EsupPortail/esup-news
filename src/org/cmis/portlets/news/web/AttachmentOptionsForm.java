package org.cmis.portlets.news.web;

import java.io.Serializable;
import java.util.List;

/**
 * Objet for the form of an AttachmentOptions.
 * @author Anyware Services - Delphine Gavalda
 * 19 oct. 2010
 */
public class AttachmentOptionsForm implements Serializable {
    private static final long serialVersionUID = 1L;

    private long optionsId;
    /**
     * New max size in kilobytes.
     */
    private String maxSize;
    /**
     * Displayed max size in kilobytes.
     */
    private String displayedMaxSize;

    private List<String> authorizedList;

    private String authorizedExts;

    private List<String> forbiddenList;

    private String forbiddenExts;

    private long entityId;

    private String useEntityOptions;

    /**
     * Constructor of AttachmentOptionsForm object.
     */
    public AttachmentOptionsForm() {
	super();
    }
    
    /**
     * @param size
     */
    public void setMaxSize(final String size) {
	this.maxSize = size;
    }

    /**
     * @return String
     */
    public String getMaxSize() {
	return maxSize;
    }

    /**
     * @param list
     */
    public void setForbiddenList(final List<String> list) {
	this.forbiddenList = list;
    }

    /**
     * @return List&lt;String&gt;
     */
    public List<String> getForbiddenList() {
	return forbiddenList;
    }

    /**
     * @param list
     */
    public void setAuthorizedList(final List<String> list) {
	this.authorizedList = list;
    }

    /**
     * @return List&lt;String&gt;
     */
    public List<String> getAuthorizedList() {
	return authorizedList;
    }

    /**
     * @param id
     */
    public void setOptionsId(final long id) {
	this.optionsId = id;
    }

    /**
     * @return long
     */
    public long getOptionsId() {
	return optionsId;
    }

    /**
     * @param exts
     */
    public void setAuthorizedExts(final String exts) {
	this.authorizedExts = exts;
    }

    /**
     * @return String
     */
    public String getAuthorizedExts() {
	return authorizedExts;
    }

    /**
     * @param exts
     */
    public void setForbiddenExts(final String exts) {
	this.forbiddenExts = exts;
    }

    /**
     * @return String
     */
    public String getForbiddenExts() {
	return forbiddenExts;
    }

    /**
     * @return long
     */
    public long getEntityId() {
	return entityId;
    }

    /**
     * @param id
     */
    public void setEntityId(final long id) {
	this.entityId = id;
    }

    /**
     * @param useEntityOpts
     */
    public void setUseEntityOptions(final String useEntityOpts) {
	this.useEntityOptions = useEntityOpts;
    }

    /**
     * @return String
     */
    public String getUseEntityOptions() {
	return useEntityOptions;
    }

    /**
     * @return String
     */
    public String getDisplayedMaxSize() {
	return displayedMaxSize;
    }

    /**
     * @param dMaxSize
     */
    public void setDisplayedMaxSize(final String dMaxSize) {
	this.displayedMaxSize = dMaxSize;
    }

}
