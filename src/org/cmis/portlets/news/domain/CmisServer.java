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
 * 11 juin 2010
 */
public class CmisServer implements Serializable {

	/** */
    private static final long serialVersionUID = 1L;
    /** server Id. */
    private Long serverId;
    /** global Server. */
    private String globalServer;
    /** serverUrl. */
    private String serverUrl;
    /** serverLogin. */
    private String serverLogin;
    /** serverPwd. */
    private String serverPwd;
    /** repository Id. */
    private String repositoryId;

    /**
     * Constructor of the CmisServer object.
     */
    public CmisServer() {
	 super();
    }

    /**
     * Get the server ID.
     * @return Long
     */
    public Long getServerId() {
	return serverId;
    }

    /**
     * Set the server ID.
     * @param serverId
     */
    public void setServerId(final Long serverId) {
	this.serverId = serverId;
    }

    /**
     * Get the value to check if these parameters are the defaults values for the application.
     * @return String "1" if these parameters are the defaults values for the application.
     */
    public String getGlobalServer() {
	return globalServer;
    }

    /**
     * Set "1" if these parameters are the defaults values for the application.
     * @param globalServer
     */
    public void setGlobalServer(final String globalServer) {
	this.globalServer = globalServer;
    }

    /**
     * Set the server Url.
     * @return String
     */
    public String getServerUrl() {
	return serverUrl;
    }

    /**
     * Get the server Url.
     * @param serverUrl
     */
    public void setServerUrl(final String serverUrl) {
	this.serverUrl = serverUrl;
    }

    /**
     * Get the server connection login.
     * @return String
     */
    public String getServerLogin() {
	return serverLogin;
    }

    /**
     * Set the login to connect to the server.
     * @param serverLogin
     */
    public void setServerLogin(final String serverLogin) {
	this.serverLogin = serverLogin;
    }

    /**
     * Get the server connection password, encrypted.
     * @return String
     */
    public String getServerPwd() {
	return serverPwd;
    }

    /**
     * Set the server connection password.
     * @param serverPwd
     */
    public void setServerPwd(final String serverPwd) {
	this.serverPwd = serverPwd;
    }

    /**
     * Get the Id of the repository.
     * @return String
     */
    public String getRepositoryId() {
	return repositoryId;
    }

    /**
     * Set the Id of the repository.
     * @param repositoryId
     */
    public void setRepositoryId(final String repositoryId) {
	this.repositoryId = repositoryId;
    }

}
