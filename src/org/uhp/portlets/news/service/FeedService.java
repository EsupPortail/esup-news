package org.uhp.portlets.news.service;
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

import com.sun.syndication.feed.synd.SyndFeed;

import java.util.List;
import java.util.Map;

import org.cmis.portlets.news.domain.AttachmentD;
import org.cmis.portlets.news.services.exceptions.CmisException;
import org.esco.portlets.news.domain.IEscoUser;
import org.uhp.portlets.news.domain.ItemV;
import org.uhp.portlets.news.domain.ItemsView;

/**
 * Feed Services
 * 22 oct. 09
 */
public interface FeedService {
    /**
     * @param id 
     * @param feedType 
     * @param urlEntry 
     * @return <code>SyndFeed</code> 
     */
	SyndFeed getTopicFeed(Long id,  String feedType, String urlEntry);
	
	/**
	 * @param id 
	 * @param fType 
	 * @param feedUrl 
	 * @return <code>String</code>     
	 */
	String getCategoryFeed(Long id, String fType, String feedUrl);
    
    /**
     * Return all news of an Entity in categories of a specific type.
     * @param id
     * @param fType 
     * @param feedUrl
     * @param type
     * @return <code>String</code> */
    String getEntityTypeFeed(Long id, String fType, String feedUrl, String type);
    
    /**
     * Return all news in categories of the specified type, organized by entity.
     * @param type
     * @param fType
     * @param feedUrl 
     * @return <code>String</code> */
    String getFeedsOfType(String type, String fType, String feedUrl);

	/**
	 * @param id
	 * @param feedUrl
	 * @param fType
	 * @return <code>String</code>
	 */
	String generateOpml(Long id, String feedUrl, String fType);

    /**
     * @param id of the category
     * @param feedType
     * @param nbLastDays
     * @param urlEntry
     * @return <code>String</code>
     */
    SyndFeed getMostRecentItemsFeedOfCategory(Long id, String feedType, Integer nbLastDays, String urlEntry);
    
    /**
     * @param id of the Entity
     * @param feedType
     * @param nbLastDays
     * @param urlEntry
     * @return <code>String</code>
     */
    SyndFeed getMostRecentItemsFeedOfEntity(Long id, String feedType, Integer nbLastDays, String urlEntry);

    /**
     * @param id
     * @return <code>boolean</code>
     */
    boolean allowTopicPublicView(Long id);

    /**
     * @param id
     * @return <code>boolean</code>
     */
    boolean allowTopicRSS(Long id);

    /**
     * @param id
     * @param isProtected
     * @return <code>String</code>
     */
    String getFeedNotAvailableMsg(Long id, boolean isProtected);

    /**
     * @param id
     * @param isProtected
     * @return <code>String</code>
     */
    String getTopicFeedNotAvailableMsg(Long id, boolean isProtected);

    /**
     * @param id
     * @param status
     * @param uid
     * @return <code>ItemsView</code>
     */
    ItemsView getItems(Long id, int status, String uid);

    /**
     * @param id
     * @param isProtected
     * @return <code>ItemsV</code>
     */
    ItemV getItem(Long id, boolean isProtected);

    /** 
     * Obtains user's details from a list of user's id. 
     * @param usersUid A list of id/uid to retrieve in the LDAP.
     * @return <code>Map< String, IEscoUser ></code> A Map of LdapUser (details of users) with id/uid as key.
     */
    Map<String, IEscoUser> getUsersByListUid(List<String> usersUid);

    /**
     * Get the attachment file to download.
     * 
     * @param fileUid
     * @param itemId
     * @param isProtected
     * @return AttachmentD
     */
    AttachmentD getAttachmentToDownload(String fileUid, Long itemId, boolean isProtected);
}
