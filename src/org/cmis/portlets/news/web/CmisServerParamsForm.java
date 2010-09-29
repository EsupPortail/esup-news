package org.cmis.portlets.news.web;

import java.io.Serializable;

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

    public String getServerId() {
	return serverId;
    }

    public void setServerId(String serverId) {
	this.serverId = serverId;
    }

    public String getServerUrl() {
	return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
	this.serverUrl = serverUrl;
    }

    public String getServerLogin() {
	return serverLogin;
    }

    public void setServerLogin(String serverLogin) {
	this.serverLogin = serverLogin;
    }

    public String getServerPwd() {
	return serverPwd;
    }

    public void setServerPwd(String serverPwd) {
	this.serverPwd = serverPwd;
    }

    public String getRepositoryId() {
	return repositoryId;
    }

    public void setRepositoryId(String repositoryId) {
	this.repositoryId = repositoryId;
    }

    public String getDisplayedServerUrl() {
	return displayedServerUrl;
    }

    public void setDisplayedServerUrl(String displayedServerUrl) {
	this.displayedServerUrl = displayedServerUrl;
    }

    public String getDisplayedServerLogin() {
	return displayedServerLogin;
    }

    public void setDisplayedServerLogin(String displayedServerLogin) {
	this.displayedServerLogin = displayedServerLogin;
    }

    public String getDisplayedServerPwd() {
	return displayedServerPwd;
    }

    public void setDisplayedServerPwd(String displayedServerPwd) {
	this.displayedServerPwd = displayedServerPwd;
    }

    public String getDisplayedRepositoryId() {
	return displayedRepositoryId;
    }

    public void setDisplayedRepositoryId(String displayedRepositoryId) {
	this.displayedRepositoryId = displayedRepositoryId;
    }

    public void setServerPwd2(String serverPwd2) {
	this.serverPwd2 = serverPwd2;
    }

    public String getServerPwd2() {
	return serverPwd2;
    }

    public void setUseEntityServer(String useEntityServer) {
	this.useEntityServer = useEntityServer;
    }

    public String getUseEntityServer() {
	return useEntityServer;
    }

    public void setEntityId(long entityId) {
	this.entityId = entityId;
    }

    public long getEntityId() {
	return entityId;
    }
}
