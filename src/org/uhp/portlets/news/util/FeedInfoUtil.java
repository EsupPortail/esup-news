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

import org.uhp.portlets.news.NewsConstants;
import org.uhp.portlets.news.domain.Feed;

public final class FeedInfoUtil {
	private static final String RSS = "rss";
	private static final String PUB_PATH = NewsConstants.PUBLIC_PATH +RSS;
	private static final String PRIV_PATH = NewsConstants.PRIVATE_PATH + RSS;
	
	public static Feed getFeedInfo(RenderRequest request, String feedType) {
		return new Feed(HostUtils.getHostUrl(request), feedType, PUB_PATH, PRIV_PATH);
		
	}
}
