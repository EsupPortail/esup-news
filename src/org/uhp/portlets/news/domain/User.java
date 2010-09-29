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

import java.util.Date;

/**
 * Modification of the User Object to an interface.
 * @author GIP RECIA - Gribonvald Julien
 * 20 juil. 09
 */
public interface User {
    /**
     * @return <code>String</code> */
	String toString();
	/**
	 * @return <code>String</code> */
	String getUserId();
	/**
	 * @param userId  */
	void setUserId(final String userId);
	/**
	 * @return <code>String</code> */
	String getDisplayName();
	/**
	 * @param displayName  */
	void setDisplayName(final String displayName);
	/**
	 * @return <code>String</code> */
	String getEmail();
	/**
	 * @param email  */
	void setEmail(final String email);
	/**
	 * @return <code>String</code> */
	String getIsSuperAdmin();
	/**
	 * @param isSuperAdmin  */
	void setIsSuperAdmin(final String isSuperAdmin);
	/**
	 * @return <code>String</code> */
	String getEnabled();
	/**
	 * @param enabled  */
	void setEnabled(final String enabled);
	/**
	 * @return <code>String</code> */
	Date getRegisterDate();
	/**
	 * @param registerDate  */
	 void setRegisterDate(final Date registerDate);
	/**
	 * @return <code>String</code> */
	Date getLastAccess();
	/**
	 * @param lastAccess  */
	void setLastAccess(final Date lastAccess);
	/**
	 * @return <code>int</code> */
	int hashCode();
	/**
	 * @param obj 
	 * @return <code>boolean</code> */
	boolean equals(final Object obj);
}