package org.uhp.portlets.news.domain;

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

public class UserRole implements Serializable {

	private static final long serialVersionUID = -4162670726502929178L;
	private String principal;
	private String role;
	private String isGroup;
	private Long ctxId;
	private String ctxType;

	public UserRole() {

	}

	public UserRole(String principal, String role, String isGrp,  Long ctxId, String ctxType) {
		this.principal = principal;
		this.role = role;
		this.isGroup = isGrp;
		this.ctxId = ctxId;
		this.ctxType = ctxType;
	}

	/**
	 * @return
	 */
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((principal == null) ? 0 : principal.hashCode());
		return result;
	}

	/**
	 * @param obj
	 * @return
	 */
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (obj instanceof UserRole) return ((UserRole)obj).principal.equals(principal);
		return false;
	}

	public String toString() {
		return principal;
	}

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

	public String getIsGroup() {
		return this.isGroup;
	}

	public void setIsGroup(String isGroup) {
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
}