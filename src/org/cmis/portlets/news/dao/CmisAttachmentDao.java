package org.cmis.portlets.news.dao;

import java.util.Map;

import org.cmis.portlets.news.domain.Attachment;
import org.cmis.portlets.news.domain.AttachmentD;
import org.cmis.portlets.news.services.exceptions.CmisException;

/**
 * CMIS Attachment DAO created by Anyware Services - Delphine Gavalda.
 * 
 * 10 mai 2010
 */
public interface CmisAttachmentDao {

    /**
     * Get an Attachment with his id.
     * 
     * @param id
     * @param entityId
     * @return Attachment
     * @throws CmisException
     */
    AttachmentD getAttachmentById(String id, Long entityId) throws CmisException;

    /**
     * Insert a new attachment.
     * 
     * @param attachment
     * @param entityId
     * @param prop
     *            Map of properties
     * @return cmis uid of the new added document
     * @throws CmisException
     */
    Attachment insertAttachment(org.uhp.portlets.news.web.ItemForm.Attachment attachment, 
	    			Long entityId, Map<String, Object> prop) throws CmisException;

    /**
     * Delete an attachment.
     * 
     * @param attachmentId
     * @param entityId
     * @throws CmisException
     */
    void deleteAttachment(String attachmentId, Long entityId) throws CmisException;

}
