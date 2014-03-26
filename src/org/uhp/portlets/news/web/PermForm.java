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

public final class PermForm implements Serializable {
	private static final long serialVersionUID = 1L;
	private String token;
	private Long ctxId;
	private String ctxType;
	private String role;
	private int isGroup;
	private String principal;
	private IEscoUser user = new EscoUser();
	private Group group = new Group();


	public Long getCtxId() {
		return ctxId;
	}
	public void setCtxId(Long ctxId) {
		this.ctxId = ctxId;
	}
	public String getCtxType() {
		return this.ctxType;
	}
	public void setCtxType(String ctxType) {
		this.ctxType = ctxType;
	}

	public int getIsGroup() {
		return this.isGroup;
	}
	public void setIsGroup(int isGroup) {
		this.isGroup = isGroup;
	}
	public String getPrincipal() {
		return this.principal;
	}
	public void setPrincipal(String principal) {
		this.principal = principal;
	}
	public String getRole() {
		return this.role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getToken() {
		return this.token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public IEscoUser getUser() {
		return this.user;
	}
	public void setUser(IEscoUser user) {
		this.user = user;
	}

	/**
	 * Getter of member group.
	 * @return <code>Group</code> the attribute group
	 */
	public Group getGroup() {
		return group;
	}
	/**
	 * Setter of attribute group.
	 * @param group the attribute group to set
	 */
	public void setGroup(Group group) {
		this.group = group;
	}
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
		if (isGroup == 0) {
			sb.append(", User :");
			sb.append(this.user.toString());
		} else {
			sb.append(", Group :");
			sb.append(this.group.toString());
		}
		sb.append("]");

		return sb.toString();
	}

}
