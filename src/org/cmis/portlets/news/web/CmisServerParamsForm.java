/**
 * ESUP-Portail News - Copyright (c) 2009 ESUP-Portail consortium
 * For any information please refer to http://esup-helpdesk.sourceforge.net
 * You may obtain a copy of the licence at http://www.esup-portail.org/license/
 */
package org.cmis.portlets.news.web;

import java.io.Serializable;

/**
 * Objet for the form of an CmisServer.
 * @author Anyware Services - Delphine Gavalda
 * 19 oct. 2010
 */
public class CmisServerParamsForm implements Serializable {
	/** */
	private static final long serialVersionUID = 1L;
	/** serverId. */
	private String serverId;
	/** serverUrl. */
	private String serverUrl;
	/** serverLogin */
	private String serverLogin;
	/** serverPwd */
	private String serverPwd;
	/** serverPwd2. */
	private String serverPwd2;
	/** repositoryId. */
	private String repositoryId;

	/** displayedServerUrl. */
	private String displayedServerUrl;
	/** displayedServerLogin. */
	private String displayedServerLogin;
	/** displayedServerPwd. */
	private String displayedServerPwd;
	/** displayedRepositoryId. */
	private String displayedRepositoryId;

	/** useEntityServer. */
	private String useEntityServer;
	/** entityId. */
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
	public void setDisplayedRepositoryId(final String displayedRepositoryId) {
		this.displayedRepositoryId = displayedRepositoryId;
	}

	/**
	 * @param serverPwd2
	 */
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
