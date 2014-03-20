/**
* ESUP-Portail News - Copyright (c) 2009 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.cmis.portlets.news.dao.iBatis;

import java.util.HashMap;
import java.util.Map;

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

	/** sequenceDao. */
    @Autowired
    private SequenceDao sequenceDao;

    /** Constructeur.  */
    public SqlMapClientAttachmentOptionsDaoImpl() {
        super();
    }

    /**
     * @param sequenceDao
     */
    public void setSequenceDao(final SequenceDao sequenceDao) {
	this.sequenceDao = sequenceDao;
    }

    /**
     * @see org.cmis.portlets.news.dao.AttachmentOptionsDao#getApplicationOptions()
     */
    public AttachmentOptions getApplicationOptions() throws DataAccessException {
	return (AttachmentOptions) getSqlMapClientTemplate().queryForObject("getApplicationOptions");
    }

    /**
     * @see org.cmis.portlets.news.dao.AttachmentOptionsDao#getEntityOptions(java.lang.Long)
     */
    public AttachmentOptions getEntityOptions(final Long entityId) throws DataAccessException {
	return (AttachmentOptions) getSqlMapClientTemplate().queryForObject("getEntityOptions", entityId);
    }

    /**
     * @see org.cmis.portlets.news.dao.AttachmentOptionsDao#insertAttachmentOptions(org.cmis.portlets.news.domain.AttachmentOptions)
     */
    public Long insertAttachmentOptions(final AttachmentOptions options) throws DataAccessException {
	options.setOptionId(this.sequenceDao.getNextId(Constants.SEQ_ATT_OPTIONS));
	getSqlMapClientTemplate().insert("insertAttachmentOptions", options);
	return options.getOptionId();
    }

    /**
     * @see org.cmis.portlets.news.dao.AttachmentOptionsDao#linkAttachmentOptionsToEntity(java.lang.Long, java.lang.Long)
     */
    public void linkAttachmentOptionsToEntity(final Long optionsId, final Long entityId) throws DataAccessException {
	Map<String, Object> params = new HashMap<String, Object>(2);
	params.put(NewsConstants.A_OPTIONS_ID, optionsId);
	params.put(NewsConstants.ENTITY_ID, entityId);
	getSqlMapClientTemplate().insert("linkAttachmentOptionsToEntity", params);
    }

    /**
     * @see org.cmis.portlets.news.dao.AttachmentOptionsDao#updateAttachmentOptions(org.cmis.portlets.news.domain.AttachmentOptions)
     */
    public void updateAttachmentOptions(final AttachmentOptions options) throws DataAccessException {
	getSqlMapClientTemplate().update("updateAttachmentOptions", options);
    }

    /**
     * @see org.cmis.portlets.news.dao.AttachmentOptionsDao#deleteAttachmentOptsLinkToEntity(java.lang.Long)
     */
    public void deleteAttachmentOptsLinkToEntity(final Long entityId) {
	getSqlMapClientTemplate().delete("deleteAttachmentOptsLinkToEntity", entityId);
    }

    /**
     * @see org.cmis.portlets.news.dao.AttachmentOptionsDao#deleteAttachmentOptions(java.lang.Long)
     */
    public void deleteAttachmentOptions(final Long optionsId) {
	getSqlMapClientTemplate().delete("deleteAttachmentOptions", optionsId);
    }
}
