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

/**
 * @modifier GIP RECIA - Julien Gribonvald
 * 21 mars 2014
 */
public class Group implements Serializable {


	private static final long serialVersionUID = 1L;

	private String key;
	private String name;
	private boolean isUportalGroup;

	/**
	 * Getter of member key.
	 * @return <code>String</code> the attribute key
	 */
	public String getKey() {
		return key;
	}
	/**
	 * Setter of attribute key.
	 * @param key the attribute key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}
	/**
	 * Getter of member name.
	 * @return <code>String</code> the attribute name
	 */
	public String getName() {
		return name;
	}
	/**
	 * Setter of attribute name.
	 * @param name the attribute name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * Getter of member isUportalGroup.
	 * @return <code>boolean</code> the attribute isUportalGroup
	 */
	public boolean isUportalGroup() {
		return isUportalGroup;
	}
	/**
	 * Setter of attribute isUportalGroup.
	 * @param isUportalGroup the attribute isUportalGroup to set
	 */
	public void setUportalGroup(boolean isUportalGroup) {
		this.isUportalGroup = isUportalGroup;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Group [key=");
		builder.append(key);
		builder.append(", name=");
		builder.append(name);
		builder.append(", isUportalGroup=");
		builder.append(isUportalGroup);
		builder.append("]");
		return builder.toString();
	}



}
