/**
* ESUP-Portail News - Copyright (c) 2009 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.cmis.portlets.news.dao;

import org.cmis.portlets.news.domain.CmisServer;
import org.springframework.dao.DataAccessException;

/**
 * CMIS server params DAO created by Anyware Services - Delphine Gavalda.
 *
 * 10 mai 2010
 */
public interface CmisServerParamsDao {

    /**
     * Get the server configuration defined for the application.
     *
     * @return CmisServer
     * @throws DataAccessException
     */
    CmisServer getApplicationServer() throws DataAccessException;

    /**
     * Get the server configuration for a given entity.
     *
     * @param entityId
     * @return CmisServer
     * @throws DataAccessException
     */
    CmisServer getEntityServer(Long entityId) throws DataAccessException;

    /**
     * Save a new server configuration.
     *
     * @param serverParams
     * @return Long
     * @throws DataAccessException
     */
    Long insertServerParams(CmisServer serverParams) throws DataAccessException;

    /**
     * Link a server configuration to an entity.
     *
     * @param serverId
     * @param entityId
     * @throws DataAccessException
     */
    void linkServerToEntity(Long serverId, Long entityId) throws DataAccessException;

    /**
     * update server configuration.
     *
     * @param serverParams
     * @throws DataAccessException
     */
    void updateServerInfos(CmisServer serverParams) throws DataAccessException;

    /**
     * Delete the link between entity and a server configuration.
     *
     * @param entityId
     */
    void deleteServerLinkToEntity(Long entityId);

    /**
     * Delete server params.
     *
     * @param serverId
     */
    void deleteServerParams(Long serverId);

}
