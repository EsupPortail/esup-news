/**
* ESUP-Portail News - Copyright (c) 2009 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.cmis.portlets.news.dao.iBatis;

import java.util.HashMap;
import java.util.Map;

import org.cmis.portlets.news.dao.CmisServerParamsDao;
import org.cmis.portlets.news.domain.CmisServer;
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
@Repository("cmisServerParamsDao")
public class SqlMapClientCmisServerDaoImpl extends SqlMapClientDaoSupport implements CmisServerParamsDao {

	/** sequenceDao */
    @Autowired
    private SequenceDao sequenceDao;


    /** Constructeur.  */
    public SqlMapClientCmisServerDaoImpl() {
        super();
    }

    /**
     * @param seqDao
     */
    public void setSequenceDao(final SequenceDao seqDao) {
	this.sequenceDao = seqDao;
    }

    /**
     * @see org.cmis.portlets.news.dao.CmisServerParamsDao#getApplicationServer()
     */
    public CmisServer getApplicationServer() throws DataAccessException {
	return (CmisServer) getSqlMapClientTemplate().queryForObject("getApplicationDefaultServer");
    }

    /**
     * @see org.cmis.portlets.news.dao.CmisServerParamsDao#getEntityServer(java.lang.Long)
     */
    public CmisServer getEntityServer(final Long entityId) throws DataAccessException {
	return (CmisServer) getSqlMapClientTemplate().queryForObject("getEntityServer", entityId);
    }

    /**
     * @see org.cmis.portlets.news.dao.CmisServerParamsDao#insertServerParams(org.cmis.portlets.news.domain.CmisServer)
     */
    public Long insertServerParams(final CmisServer serverParams) throws DataAccessException {
	serverParams.setServerId(this.sequenceDao.getNextId(Constants.SEQ_CMIS_SERVER_PARAMS));
	getSqlMapClientTemplate().insert("insertServerInfos", serverParams);
	return serverParams.getServerId();
    }

    /**
     * @see org.cmis.portlets.news.dao.CmisServerParamsDao#linkServerToEntity(java.lang.Long, java.lang.Long)
     */
    public void linkServerToEntity(final Long serverId, final Long entityId) throws DataAccessException {
	Map<String, Object> params = new HashMap<String, Object>(2);
	params.put(NewsConstants.CMIS_SERVER_ID, serverId);
	params.put(NewsConstants.ENTITY_ID, entityId);
	getSqlMapClientTemplate().insert("linkServerToEntity", params);
    }

    /**
     * @see org.cmis.portlets.news.dao.CmisServerParamsDao#updateServerInfos(org.cmis.portlets.news.domain.CmisServer)
     */
    public void updateServerInfos(final CmisServer serverParams) throws DataAccessException {
	getSqlMapClientTemplate().update("updateServerInfos", serverParams);
    }

    /**
     * @see org.cmis.portlets.news.dao.CmisServerParamsDao#deleteServerLinkToEntity(java.lang.Long)
     */
    public void deleteServerLinkToEntity(final Long entityId) {
	getSqlMapClientTemplate().delete("deleteServerLinkToEntity", entityId);
    }

    /**
     * @see org.cmis.portlets.news.dao.CmisServerParamsDao#deleteServerParams(java.lang.Long)
     */
    public void deleteServerParams(final Long serverId) {
	getSqlMapClientTemplate().delete("deleteServerInfos", serverId);
    }
}
