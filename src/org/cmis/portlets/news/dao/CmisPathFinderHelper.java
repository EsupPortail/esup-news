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

    public static final String INSERT_DATE = "insertDate";

    public static final String FILE_NAME = "filename";

    public static final String CATEGORY_ID = "categoryId";

    public static final String ENTITY_ID = "entityId";

    public static final String TOPICS_ID = "topicsId";

    /**
     * Return a valid complete path, containing a valid filename, to insert a
     * new file
     * 
     * @param cmisSession
     *            the connexion Object to the cmis server (depends on the chosen
     *            CMIS client API)
     * @param properties
     *            Map of properties required to calculate a new path
     * @return String the valid path
     */
    public String getPathForAttachment(Session cmisSession, Map<String, Object> properties);
}
