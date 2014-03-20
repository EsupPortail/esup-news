/**
* ESUP-Portail News - Copyright (c) 2009 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.cmis.portlets.news.dao;

import org.apache.chemistry.opencmis.client.api.Session;
import org.cmis.portlets.news.services.exceptions.CmisException;

/**
 * created by Anyware Services - Delphine Gavalda.
 *
 * 19 mai 2010
 */
public interface CmisSessionFactory {

    /**
     * Return the CMIS Session object according to the entity id parameter.
     *
     * @param entityId
     * @return Session
     * @throws CmisException
     */
    Session getSession(Long entityId) throws CmisException;

    /**
     * Remove a CMIS Session object following a change of the server.
     * configuration parameters
     *
     * @param entityId
     * @throws CmisException
     */
    void removeSession(Long entityId) throws CmisException;

    /**
     * Remove the default CMIS Session object following a change of the server.
     * configuration parameters
     *
     * @throws CmisException
     */
    void removeDefaultSession() throws CmisException;
}
