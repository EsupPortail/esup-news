package org.cmis.portlets.news.web;

import java.io.Serializable;

/**
 * Objet for the form of an CmisServer.
 * @author Anyware Services - Delphine Gavalda
 * 19 oct. 2010
 */
public class CmisServerParamsForm implements Serializable {
    private static final long serialVersionUID = 1L;

    private String serverId;

    private String serverUrl;
    private String serverLogin;
    private String serverPwd;
    private String serverPwd2;
    private String repositoryId;

    private String displayedServerUrl;
    private String displayedServerLogin;
    private String displayedServerPwd;
    private String displayedRepositoryId;

    private String useEntityServer;
    private long entityId;

    /**
     * Constructor of CmisServerParamsForm object.
     */
    public CmisServerParamsForm() {
	super();
    }
    
    /**
     * @return String
     */
    public String getServerId() {
	return serverId;
    }

    /**
     * @param serverId
     */
    @SuppressWarnings("hiding")
    public void setServerId(final String serverId) {
	this.serverId = serverId;
    }

    /**
     * @return String
     */
    public String getServerUrl() {
	return serverUrl;
    }

    /**
     * @param serverUrl
     */
    @SuppressWarnings("hiding")
    public void setServerUrl(final String serverUrl) {
	this.serverUrl = serverUrl;
    }

    /**
     * @return String
     */
    public String getServerLogin() {
	return serverLogin;
    }

    /**
     * @param serverLogin
     */
    @SuppressWarnings("hiding")
    public void setServerLogin(final String serverLogin) {
	this.serverLogin = serverLogin;
    }

    /**
     * @return String
     */
    public String getServerPwd() {
	return serverPwd;
    }

    /**
     * @param serverPwd
     */
    @SuppressWarnings("hiding")
    public void setServerPwd(final String serverPwd) {
	this.serverPwd = serverPwd;
    }

    /**
     * @return String
     */
    public String getRepositoryId() {
	return repositoryId;
    }

    /**
     * @param repositoryId
     */
    @SuppressWarnings("hiding")
    public void setRepositoryId(final String repositoryId) {
	this.repositoryId = repositoryId;
    }

    /**
     * @return String
     */
    public String getDisplayedServerUrl() {
	return displayedServerUrl;
    }

    /**
     * @param displayedServerUrl
     */
    @SuppressWarnings("hiding")
    public void setDisplayedServerUrl(final String displayedServerUrl) {
	this.displayedServerUrl = displayedServerUrl;
    }

    /**
     * @return String
     */
    public String getDisplayedServerLogin() {
	return displayedServerLogin;
    }

    /**
     * @param displayedServerLogin
     */
    @SuppressWarnings("hiding")
    public void setDisplayedServerLogin(final String displayedServerLogin) {
	this.displayedServerLogin = displayedServerLogin;
    }

    /**
     * @return String
     */
    public String getDisplayedServerPwd() {
	return displayedServerPwd;
    }

    /**
     * @param displayedServerPwd
     */
    @SuppressWarnings("hiding")
    public void setDisplayedServerPwd(final String displayedServerPwd) {
	this.displayedServerPwd = displayedServerPwd;
    }

    /**
     * @return String
     */
    public String getDisplayedRepositoryId() {
	return displayedRepositoryId;
    }

    /**
     * @param displayedRepositoryId
     */
    @SuppressWarnings("hiding")
    public void setDisplayedRepositoryId(final String displayedRepositoryId) {
	this.displayedRepositoryId = displayedRepositoryId;
    }

    /**
     * @param serverPwd2
     */
    @SuppressWarnings("hiding")
    public void setServerPwd2(final String serverPwd2) {
	this.serverPwd2 = serverPwd2;
    }

    /**
     * @return String
     */
    public String getServerPwd2() {
	return serverPwd2;
    }

    /**
     * @param useEntityServer
     */
    @SuppressWarnings("hiding")
    public void setUseEntityServer(final String useEntityServer) {
	this.useEntityServer = useEntityServer;
    }

    /**
     * @return String
     */
    public String getUseEntityServer() {
	return useEntityServer;
    }

    /**
     * @param entityId
     */
    @SuppressWarnings("hiding")
    public void setEntityId(final long entityId) {
	this.entityId = entityId;
    }

    /**
     * @return long
     */
    public long getEntityId() {
	return entityId;
    }
}
