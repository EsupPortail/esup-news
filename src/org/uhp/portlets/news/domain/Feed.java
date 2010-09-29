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

public class Feed implements Serializable {

private static final long serialVersionUID = 1L;
private String portalURL;
private String feedType;
private String pubPath;
private String privPath;


public Feed(String portalURL, String feedType, String pubPath, String privPath) {
	super();
	this.portalURL = portalURL;
	this.feedType = feedType;
	this.pubPath = pubPath;
	this.privPath = privPath;
}

public String getPortalURL() {
	return portalURL;
}

public void setPortalURL(String portalURL) {
	this.portalURL = portalURL;
}

public String getFeedType() {
	return feedType;
}
public void setFeedType(String feedType) {
	this.feedType = feedType;
}
public String getPrivPath() {
	return privPath;
}
public void setPrivPath(String privPath) {
	this.privPath = privPath;
}
public String getPubPath() {
	return pubPath;
}
public void setPubPath(String pubPath) {
	this.pubPath = pubPath;
}

}
