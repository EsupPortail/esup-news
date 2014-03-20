/**
* ESUP-Portail News - Copyright (c) 2009 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.cmis.portlets.news.dao.iBatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cmis.portlets.news.dao.AttachmentDao;
import org.cmis.portlets.news.domain.Attachment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.stereotype.Repository;
import org.uhp.portlets.news.NewsConstants;
import org.uhp.portlets.news.dao.Constants;
import org.uhp.portlets.news.dao.SequenceDao;

/**
 *
 * created by Anyware Services - Delphine Gavalda.
 *
 * 10 mai 2010
 */
@Repository("attachmentDao")
public class SqlMapClientAttachmentDaoImpl extends SqlMapClientDaoSupport implements AttachmentDao {

    /** SequenceDao.   */
    @Autowired
    private SequenceDao sequenceDao;

    /** Constructeur.  */
    public SqlMapClientAttachmentDaoImpl() {
        super();
    }

    /**
     * @param sequenceDao
     */
    public void setSequenceDao(final SequenceDao sequenceDao) {
	this.sequenceDao = sequenceDao;
    }

    /**
     * @see org.cmis.portlets.news.dao.AttachmentDao#getAttachmentById(java.lang.Long)
     */
    public Attachment getAttachmentById(final Long id) throws DataAccessException {
	return (Attachment) getSqlMapClientTemplate().queryForObject("getAttachmentById", id);
    }

    /**
     * @see org.cmis.portlets.news.dao.AttachmentDao#getAttachmentsListByItem(java.lang.Long)
     */
    @SuppressWarnings("unchecked")
    public List<Attachment> getAttachmentsListByItem(final Long itemId) throws DataAccessException {
	return getSqlMapClientTemplate().queryForList("getAttachmentListByItem", itemId);
    }

    /**
     * @see org.cmis.portlets.news.dao.AttachmentDao#getItemsLinkedToAttachment(java.lang.Long)
     */
    @SuppressWarnings("unchecked")
    public List<Long> getItemsLinkedToAttachment(final Long attachmentId) throws DataAccessException {
	return getSqlMapClientTemplate().queryForList("getItemsLinkedToAttachment", attachmentId);
    }

    /**
     * @see org.cmis.portlets.news.dao.AttachmentDao#insertAttachment(org.cmis.portlets.news.domain.Attachment)
     */
    public long insertAttachment(final Attachment attachment) throws DataAccessException {
	attachment.setAttachmentId(this.sequenceDao.getNextId(Constants.SEQ_ATT));
	getSqlMapClientTemplate().insert("insertAttachment", attachment);
	return attachment.getAttachmentId().longValue();
    }

    /**
     * @see org.cmis.portlets.news.dao.AttachmentDao#updateAttachment(java.util.Map)
     */
    public void updateAttachment(final Map<String, Object> params) throws DataAccessException {
	getSqlMapClientTemplate().update("updateAttachment", params);
    }

    /**
     * @see org.cmis.portlets.news.dao.AttachmentDao#addAttachmentToItem(java.lang.Long, java.lang.Long)
     */
    public void addAttachmentToItem(final Long attachmentId, final Long itemId) throws DataAccessException {
	Map<String, Object> params = new HashMap<String, Object>(2);
	params.put(NewsConstants.A_ID, attachmentId);
	params.put(NewsConstants.I_ID, itemId);
	getSqlMapClientTemplate().insert("insertAttachmentToItem", params);
    }

    /**
     * @see org.cmis.portlets.news.dao.AttachmentDao#deleteAttachment(java.lang.Long)
     */
    public void deleteAttachment(final Long attachmentId) throws DataAccessException {
	getSqlMapClientTemplate().delete("deleteAttachment", attachmentId);
    }

    /**
     * @see org.cmis.portlets.news.dao.AttachmentDao#deleteAttachmentForItem(java.lang.Long, java.lang.Long)
     */
    public void deleteAttachmentForItem(final Long attachmentId, final Long itemId) throws DataAccessException {
	Map<String, Object> params = new HashMap<String, Object>(2);
	params.put(NewsConstants.A_ID, attachmentId);
	params.put(NewsConstants.I_ID, itemId);
	getSqlMapClientTemplate().delete("deleteAttachmentForItem", params);
    }

}
