package org.uhp.portlets.news.web;

/**
 * @Project NewsPortlet : http://sourcesup.cru.fr/newsportlet/
 * Copyright (C) 2007-2008 University Nancy 1
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation version 2 of the License.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */

import java.io.Serializable;

import org.esco.portlets.news.domain.EscoUser;
import org.esco.portlets.news.domain.IEscoUser;
import org.uhp.portlets.news.domain.Group;

/**
 * modified by GIP RECIA - Julien Gribonvald
 * 4 mai 2012
 */
public final class PermForm implements Serializable {
	/** */
	private static final long serialVersionUID = 1L;
	/** */
	private String token;
	/** */
	private Long ctxId;
	/** */
	private String ctxType;
	/** */
	private String role;
	/** */
	private int isGroup;
	/** */
	private String principal;
	/** */
	private IEscoUser user = new EscoUser();
	/** */
	private Group group;


	/**
	 * @return Long
	 */
	public Long getCtxId() {
		return ctxId;
	}
	/**
	 * @param ctxId
	 */
	public void setCtxId(final Long ctxId) {
		this.ctxId = ctxId;
	}
	/**
	 * @return String
	 */
	public String getCtxType() {
		return this.ctxType;
	}
	/**
	 * @param ctxType
	 */
	public void setCtxType(final String ctxType) {
		this.ctxType = ctxType;
	}

	/**
	 * @return int
	 */
	public int getIsGroup() {
		return this.isGroup;
	}
	/**
	 * @param isGroup
	 */
	public void setIsGroup(final int isGroup) {
		this.isGroup = isGroup;
	}
	/**
	 * @return String
	 */
	public String getPrincipal() {
		return this.principal;
	}
	/**
	 * @param principal
	 */
	public void setPrincipal(final String principal) {
		this.principal = principal;
	}
	/**
	 * @return String
	 */
	public String getRole() {
		return this.role;
	}
	/**
	 * @param role
	 */
	public void setRole(final String role) {
		this.role = role;
	}
	/**
	 * @return String
	 */
	public String getToken() {
		return this.token;
	}
	/**
	 * @param token
	 */
	public void setToken(final String token) {
		this.token = token;
	}
	/**
	 * @return IEscoUser
	 */
	public IEscoUser getUser() {
		return this.user;
	}
	/**
	 * @param user
	 */
	public void setUser(final IEscoUser user) {
		this.user = user;
	}
	/**
	 * @return Group
	 */
	public Group getGroup() {
		return group;
	}
	/**
	 * @param group
	 */
	public void setGroup(final Group group) {
		this.group = group;
	}
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
	    StringBuilder sb = new StringBuilder(PermForm.class.getName() + "[");
	    sb.append(this.ctxId);
	    sb.append(", ");
	    sb.append(this.ctxType);
	    sb.append(", ");
	    sb.append(this.isGroup);
        sb.append(", ");
	    sb.append(this.principal);
        sb.append(", ");
	    sb.append(this.role);
        sb.append(", ");
	    sb.append(this.token);
        sb.append(", User :");
	    sb.append(this.user.toString());
	    sb.append(", Group :");
	    sb.append(this.group.toString());
        sb.append("]");

	    return sb.toString();
    }

}
