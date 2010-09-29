package org.cmis.portlets.news.domain;

import java.io.Serializable;

/**
 * 
 * created by Anyware Services - Delphine Gavalda.
 * 
 * 11 juin 2010
 */
public class CmisServer implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long serverId;
    private String globalServer;
    private String serverUrl;
    private String serverLogin;
    private String serverPwd;
    private String repositoryId;

    public Long getServerId() {
	return serverId;
    }

    public void setServerId(Long serverId) {
	this.serverId = serverId;
    }

    public String getGlobalServer() {
	return globalServer;
    }

    public void setGlobalServer(String globalServer) {
	this.globalServer = globalServer;
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

}
