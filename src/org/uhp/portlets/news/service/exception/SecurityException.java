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
package org.uhp.portlets.news.service.exception;


public class SecurityException extends Exception {
	private static final long serialVersionUID = 1L;

	public SecurityException() {
		super();

	}

	/**
	 * @param msg
	 */
	public SecurityException(String msg) {
		super(msg);
	}

	/**
	 * @param msg
	 * @param cause
	 */
	public SecurityException(String msg, Throwable cause) {
		super(msg, cause);
	}

	/**
	 * @param cause
	 */
	public SecurityException(Throwable cause) {
		super(cause);		
	}

}
