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
package org.uhp.portlets.news.domain;

import java.util.Comparator;

/**
 * @modifier GIP RECIA - Julien Gribonvald
 * 25 mars 2014
 */
public enum RolePerm implements Comparator<RolePerm>, Comparable<RolePerm> {
	ROLE_ADMIN("ROLE_ADMIN",128),
	ROLE_MANAGER("ROLE_MANAGER", 64),
	ROLE_EDITOR("ROLE_EDITOR", 32),
	ROLE_CONTRIBUTOR("ROLE_CONTRIBUTOR", 16),
	ROLE_USER("ROLE_USER", 0);
	String name;
	int mask;

	RolePerm(final String name, final int mask) {
		this.name = name;
		this.mask = mask;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getMask() {
		return mask;
	}

	public void setMask(int mask) {
		this.mask = mask;
	}

	/**
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(RolePerm o1, RolePerm o2) {
		return new Integer(o1.getMask()).compareTo(new Integer(o2.getMask()));
	}


}