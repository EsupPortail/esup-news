/**
* ESUP-Portail News - Copyright (c) 2009 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esco.portlets.news.domain;


import java.io.Serializable;

/**
 * @author GIP RECIA - Julien Gribonvald
 * 9 mai 2012
 */
public class EntityRole implements Serializable {

	private static final long serialVersionUID = -4162670726502929178L;
	private String principal;
	private String role;
	private String isGroup;
	private Long ctxId;
	private String ctxType;

	public EntityRole() {

	}

	public EntityRole(String principal, String role, String isGrp,  Long ctxId, String ctxType) {
		this.principal = principal;
		this.role = role;
		this.isGroup = isGrp;
		this.ctxId = ctxId;
		this.ctxType = ctxType;
	}

	/**
	 * @return
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
		if (obj instanceof EntityRole) return ((EntityRole)obj).principal.equals(principal) && ((EntityRole)obj).isGroup.equals(isGroup);
		return false;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder("EntityRole : [");
		sb.append("principal=");
		sb.append(principal);
		sb.append(", isGroup=");
		sb.append(isGroup);
		sb.append(", role=");
		sb.append(role);
		sb.append(", ctxType=");
		sb.append(ctxType);
		sb.append(", ctxId=");
		sb.append(ctxId);
		sb.append("]");
		return sb.toString();
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