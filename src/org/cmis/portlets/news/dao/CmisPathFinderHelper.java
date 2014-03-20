/**
* ESUP-Portail News - Copyright (c) 2009 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.cmis.portlets.news.dao;

import java.util.Map;

import org.apache.chemistry.opencmis.client.api.Session;

/**
 * Helps to choose a new valid path to insert a new attachement file created by
 * Anyware Services - Delphine Gavalda.
 *
 * 20 mai 2010
 */
public interface CmisPathFinderHelper {

    /** Constant for the insert date value. */
    String INSERT_DATE = "insertDate";

    /** Constant for the filename value. */
    String FILE_NAME = "filename";

    /** Constant for the categoryId value. */
    String CATEGORY_ID = "categoryId";

    /** Constant for the entityId value. */
    String ENTITY_ID = "entityId";

    /** Constant for the topicsId value. */
    String TOPICS_ID = "topicsId";

    /**
     * Return a valid complete path, containing a valid filename, to insert a
     * new file.
     *
     * @param cmisSession
     *            the connexion Object to the cmis server (depends on the chosen
     *            CMIS client API)
     * @param properties
     *            Map of properties required to calculate a new path
     * @return String the valid path
     */
    String getPathForAttachment(Session cmisSession, Map<String, Object> properties);
}
