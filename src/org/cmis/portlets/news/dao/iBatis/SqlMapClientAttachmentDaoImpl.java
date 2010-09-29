package org.cmis.portlets.news.dao.iBatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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

    private static final Log LOG = LogFactory.getLog(SqlMapClientAttachmentDaoImpl.class);
    @Autowired
    private SequenceDao sequenceDao;

    public void setSequenceDao(final SequenceDao sequenceDao) {
	this.sequenceDao = sequenceDao;
    }

    public Attachment getAttachmentById(final Long id) throws DataAccessException {
	return (Attachment) getSqlMapClientTemplate().queryForObject("getAttachmentById", id);
    }

    @SuppressWarnings("unchecked")
    public List<Attachment> getAttachmentsListByItem(final Long itemId) throws DataAccessException {
	return getSqlMapClientTemplate().queryForList("getAttachmentListByItem", itemId);
    }

    @SuppressWarnings("unchecked")
    public List<Long> getItemsLinkedToAttachment(final Long attachmentId) throws DataAccessException {
	return getSqlMapClientTemplate().queryForList("getItemsLinkedToAttachment", attachmentId);
    }

    public long insertAttachment(final Attachment attachment) throws DataAccessException {
	attachment.setAttachmentId(this.sequenceDao.getNextId(Constants.SEQ_ATT));
	getSqlMapClientTemplate().insert("insertAttachment", attachment);
	return attachment.getAttachmentId().longValue();
    }
    
    public void updateAttachment(final Map params) throws DataAccessException {
	getSqlMapClientTemplate().update("updateAttachment", params);	
    }

    public void addAttachmentToItem(final Long attachmentId, final Long itemId) throws DataAccessException {
	Map<String, Object> params = new HashMap<String, Object>(2);
	params.put(NewsConstants.A_ID, attachmentId);
	params.put(NewsConstants.I_ID, itemId);
	getSqlMapClientTemplate().insert("insertAttachmentToItem", params);
    }

    public void deleteAttachment(final Long attachmentId) throws DataAccessException {
	getSqlMapClientTemplate().delete("deleteAttachment", attachmentId);
    }

    public void deleteAttachmentForItem(final Long attachmentId, final Long itemId) throws DataAccessException {
	Map<String, Object> params = new HashMap<String, Object>(2);
	params.put(NewsConstants.A_ID, attachmentId);
	params.put(NewsConstants.I_ID, itemId);
	getSqlMapClientTemplate().delete("deleteAttachmentForItem", params);
    }

}
