package org.cmis.portlets.news.dao.iBatis;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cmis.portlets.news.dao.AttachmentOptionsDao;
import org.cmis.portlets.news.domain.AttachmentOptions;
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
@Repository("attachmentOptionsDao")
public class SqlMapClientAttachmentOptionsDaoImpl extends SqlMapClientDaoSupport implements AttachmentOptionsDao {

    private static final Log LOG = LogFactory.getLog(SqlMapClientAttachmentOptionsDaoImpl.class);
    @Autowired
    private SequenceDao sequenceDao;

    public void setSequenceDao(final SequenceDao sequenceDao) {
	this.sequenceDao = sequenceDao;
    }

    public AttachmentOptions getApplicationOptions() throws DataAccessException {
	return (AttachmentOptions) getSqlMapClientTemplate().queryForObject("getApplicationOptions");
    }

    public AttachmentOptions getEntityOptions(final Long entityId) throws DataAccessException {
	return (AttachmentOptions) getSqlMapClientTemplate().queryForObject("getEntityOptions", entityId);
    }

    public Long insertAttachmentOptions(AttachmentOptions options) throws DataAccessException {
	options.setOptionId(this.sequenceDao.getNextId(Constants.SEQ_ATT_OPTIONS));
	getSqlMapClientTemplate().insert("insertAttachmentOptions", options);
	return options.getOptionId();
    }

    public void linkAttachmentOptionsToEntity(final Long optionsId, final Long entityId) throws DataAccessException {
	Map<String, Object> params = new HashMap<String, Object>(2);
	params.put(NewsConstants.A_OPTIONS_ID, optionsId);
	params.put(NewsConstants.ENTITY_ID, entityId);
	getSqlMapClientTemplate().insert("linkAttachmentOptionsToEntity", params);
    }

    public void updateAttachmentOptions(AttachmentOptions options) throws DataAccessException {
	getSqlMapClientTemplate().update("updateAttachmentOptions", options);
    }

    public void deleteAttachmentOptsLinkToEntity(final Long entityId) {
	getSqlMapClientTemplate().delete("deleteAttachmentOptsLinkToEntity", entityId);
    }

    public void deleteAttachmentOptions(final Long optionsId) {
	getSqlMapClientTemplate().delete("deleteAttachmentOptions", optionsId);
    }
}
