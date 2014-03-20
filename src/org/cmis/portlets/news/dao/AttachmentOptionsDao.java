/**
* ESUP-Portail News - Copyright (c) 2009 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.cmis.portlets.news.dao;

import org.cmis.portlets.news.domain.AttachmentOptions;
import org.springframework.dao.DataAccessException;

/**
 * Attachment Options DAO created by Anyware Services - Delphine Gavalda.
 *
 * 10 mai 2010
 */
public interface AttachmentOptionsDao {

    /**
     * Get the attachment parameters defined for the application.
     *
     * @return AttachmentOptions
     * @throws DataAccessException
     */
    AttachmentOptions getApplicationOptions() throws DataAccessException;

    /**
     * Get the attachment parameters defined for a given entity.
     *
     * @param entityId
     * @return AttachmentOptions
     * @throws DataAccessException
     */
    AttachmentOptions getEntityOptions(Long entityId) throws DataAccessException;

    /**
     * Save attachment parameters.
     *
     * @param options
     * @return Long
     * @throws DataAccessException
     */
    Long insertAttachmentOptions(AttachmentOptions options) throws DataAccessException;

    /**
     * Link attachment parameters to an entity.
     *
     * @param optionsId
     * @param entityId
     * @throws DataAccessException
     */
    void linkAttachmentOptionsToEntity(Long optionsId, Long entityId) throws DataAccessException;

    /**
     * Update attachment parameters.
     *
     * @param options
     * @throws DataAccessException
     */
    void updateAttachmentOptions(AttachmentOptions options) throws DataAccessException;

    /**
     * Delete attachment parameters for an entity.
     *
     * @param entityId
     */
    void deleteAttachmentOptsLinkToEntity(Long entityId);

    /**
     * Delete attachment parameters.
     *
     * @param optionsId
     */
    void deleteAttachmentOptions(Long optionsId);

}
