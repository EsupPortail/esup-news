package org.uhp.portlets.news.util;

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

import javax.portlet.RenderRequest;
import javax.servlet.http.HttpServletRequest;

public final class HostUtils {
	private static final int S_HTTP_PORT = 80;
	private static final int S_HTTPS_PORT = 443;
	private static final String HTTPS = "https";
	private static final String HTTP = "http";
	
	public static String getHostUrl(RenderRequest request) {
		final String ptc = request.isSecure() ? HTTPS :  HTTP; 
		StringBuilder url = new StringBuilder(ptc);
		int port = request.getServerPort();
		url.append("://"+request.getServerName());
		
		if((request.isSecure() && port != S_HTTPS_PORT) || (!request.isSecure() && port != S_HTTP_PORT)) {
			url.append(":"+ port);
	    }	    
		url.append(request.getContextPath());
		  return url.toString();  
		  }
		
	public static String getServletHostUrl(HttpServletRequest request) {
		final String ptc = request.isSecure() ? HTTPS :  HTTP; 
		StringBuilder url = new StringBuilder(ptc);
		int port = request.getServerPort();
		url.append("://"+request.getServerName());
		
		if((request.isSecure() && port != S_HTTPS_PORT) || (!request.isSecure() && port != S_HTTP_PORT)) {
			url.append(":"+ port);
	    }	    
		url.append(request.getContextPath());
		  return url.toString();  
		  }
}
	
