/**
 * 
 */
package org.cmis.portlets.news.services;

import java.util.List;

import org.cmis.portlets.news.domain.Attachment;
import org.cmis.portlets.news.domain.AttachmentOptions;
import org.cmis.portlets.news.domain.CmisServer;
import org.cmis.portlets.news.services.exceptions.CmisException;
import org.springframework.dao.DataAccessException;
import org.uhp.portlets.news.web.ItemForm;

/**
 * created by Anyware Services - Delphine Gavalda.
 * 
 * 10 mai 2010
 */
public interface AttachmentManager {

    /**
     * Get an attachment with its id.
     * 
     * @param id
     * @return Attachment
     * @throws DataAccessException
     */
    Attachment getAttachmentById(Long id) throws DataAccessException;

    /**
     * Get all the attachments for an item.
     * 
     * @param itemId
     * @return <code> List&lt;Attachment&gt; </code>
     * @throws DataAccessException
     */
    List<Attachment> getAttachmentsListByItem(Long itemId) throws DataAccessException;

    /**
     * Add an attachement to an item.
     * 
     * @param itemForm
     * @param itemId
     * @param entityId
     * @param userID
     * @throws DataAccessException
     * @throws CmisException
     */
    void addAttachmentToItem(ItemForm itemForm, Long itemId, Long entityId, String userID) 
    	throws DataAccessException, CmisException;

    /**
     * Update an attachement.
     * 
     * @param itemForm
     * @param itemId
     * @param entityId
     * @throws DataAccessException
     * @throws CmisException
     */
    void updateItemAttachment(ItemForm itemForm, Long itemId, Long entityId) throws DataAccessException, CmisException;

    /**
     * Delete a given attachement for an item.
     * 
     * @param attachment
     * @param itemId
     * @param entityId
     * @throws DataAccessException
     * @throws CmisException
     */
    void deleteAttachment(Attachment attachment, Long itemId, Long entityId) throws DataAccessException, CmisException;

    /**
     * Delete attachements for an item.
     * 
     * @param itemId
     * @param entityId
     * @throws DataAccessException
     * @throws CmisException
     */
    void deleteItemAttachments(Long itemId, Long entityId) throws DataAccessException, CmisException;

    /**
     * Get attachments options defined for an entity.
     * 
     * @param entityId
     * @return AttachmentOptions
     */
    AttachmentOptions getEntityAttachmentOptions(Long entityId);

    /**
     * Get attachments options defined for the application.
     * 
     * @return AttachmentOptions
     */
    AttachmentOptions getApplicationAttachmentOptions();

    /**
     * Update attachments options.
     * 
     * @param options
     */
    void updateAttachmentOptions(AttachmentOptions options);

    /**
     * Save attachments options.
     * 
     * @param options
     * @return Long
     */
    Long insertAttachmentOptions(AttachmentOptions options);

    /**
     * Add attachments options to an entity.
     * 
     * @param optionsId
     * @param entityId
     */
    void linkAttachmentOptionsToEntity(Long optionsId, Long entityId);

    /**
     * Delete attachments options.
     * 
     * @param optionsId
     */
    void deleteAttachmentOptions(Long optionsId);

    /**
     * @param entityId
     */
    void deleteAttachmentOptsLinkToEntity(Long entityId);

    /**
     * Get the CMIS server params defined for the application.
     * 
     * @return CmisServer
     */
    CmisServer getApplicationServer();

    /**
     * Get the CMIS server params defined for a given entity.
     * 
     * @param entityId
     * @return CmisServer
     */
    CmisServer getEntityServer(Long entityId);

    /**
     * Delete CMIS server params.
     * 
     * @param serverId
     */
    void deleteServerParams(Long serverId);

    /**
     * Delete CMIS server params for a given entity.
     * 
     * @param entityId
     */
    void deleteServerLinkToEntity(Long entityId);

    /**
     * Update CMIS server params.
     * 
     * @param serverParams
     */
    void updateServerInfos(CmisServer serverParams);

    /**
     * Add CMIS server params to a given entity.
     * 
     * @param serverId
     * @param entityId
     */
    void linkServerToEntity(Long serverId, Long entityId);

    /**
     * Save CMIS server params.
     * 
     * @param serverParams
     * @return Long
     */
    Long insertServerParams(CmisServer serverParams);

    /**
     * Clean the temporary directory : delete all olders files.
     * 
     * @param path
     */
    void cleanTempStorageDirectory(String path);

    /**
     * Clean the temporary directory : delete all olders files.
     * 
     * @param path
     * @param prefix
     */
    void cleanTempStorageDirectory(String path, String prefix);
}
