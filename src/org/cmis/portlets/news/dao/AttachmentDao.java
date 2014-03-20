/**
* ESUP-Portail News - Copyright (c) 2009 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.cmis.portlets.news.dao;

import java.util.List;
import java.util.Map;

import org.cmis.portlets.news.domain.Attachment;
import org.springframework.dao.DataAccessException;

/**
 * Attachment DAO
 *
 * created by Anyware Services - Delphine Gavalda.
 *
 * 10 mai 2010
 */
public interface AttachmentDao {

    /**
     * Retrieve an attachment with its id.
     *
     * @param id
     * @return Attachment
     * @throws DataAccessException
     */
    Attachment getAttachmentById(Long id) throws DataAccessException;

    /**
     * Retrieve all attachment for an item.
     *
     * @param itemId
     * @return <code> List&lt;Attachment&gt; </code>
     * @throws DataAccessException
     */
    List<Attachment> getAttachmentsListByItem(Long itemId) throws DataAccessException;

    /**
     * Get items linked to a given attachment.
     *
     * @param attachmentId
     * @return <code> List&lt;Long&gt; </code>
     * @throws DataAccessException
     */
    List<Long> getItemsLinkedToAttachment(final Long attachmentId) throws DataAccessException;

    /**
     * Insert an attachment.
     *
     * @param attachment
     * @return Long
     * @throws DataAccessException
     */
    long insertAttachment(Attachment attachment) throws DataAccessException;

    /**
     * Update an attachment.
     *
     * @param params
     * @throws DataAccessException
     */
    void updateAttachment(final Map<String, Object> params) throws DataAccessException;

    /**
     * Add an attachment to a item.
     *
     * @param attachmentId
     * @param itemId
     * @throws DataAccessException
     */
    void addAttachmentToItem(Long attachmentId, Long itemId) throws DataAccessException;

    /**
     * Delete an attachment.
     *
     * @param attachmentId
     * @throws DataAccessException
     */
    void deleteAttachment(Long attachmentId) throws DataAccessException;

    /**
     * Delete an attachmentfor a item.
     *
     * @param attachmentId
     * @param itemId
     * @throws DataAccessException
     */
    void deleteAttachmentForItem(Long attachmentId, Long itemId) throws DataAccessException;

}
