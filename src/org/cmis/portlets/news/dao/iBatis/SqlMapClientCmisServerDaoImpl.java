package org.cmis.portlets.news.dao.iBatis;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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

    private static final Log LOG = LogFactory.getLog(SqlMapClientCmisServerDaoImpl.class);
    @Autowired
    private SequenceDao sequenceDao;

    public void setSequenceDao(final SequenceDao sequenceDao) {
	this.sequenceDao = sequenceDao;
    }

    public CmisServer getApplicationServer() throws DataAccessException {
	return (CmisServer) getSqlMapClientTemplate().queryForObject("getApplicationDefaultServer");
    }

    public CmisServer getEntityServer(final Long entityId) throws DataAccessException {
	return (CmisServer) getSqlMapClientTemplate().queryForObject("getEntityServer", entityId);
    }

    public Long insertServerParams(CmisServer serverParams) throws DataAccessException {
	serverParams.setServerId(this.sequenceDao.getNextId(Constants.SEQ_CMIS_SERVER_PARAMS));
	getSqlMapClientTemplate().insert("insertServerInfos", serverParams);
	return serverParams.getServerId();
    }

    public void linkServerToEntity(final Long serverId, final Long entityId) throws DataAccessException {
	Map<String, Object> params = new HashMap<String, Object>(2);
	params.put(NewsConstants.CMIS_SERVER_ID, serverId);
	params.put(NewsConstants.ENTITY_ID, entityId);
	getSqlMapClientTemplate().insert("linkServerToEntity", params);
    }

    public void updateServerInfos(CmisServer serverParams) throws DataAccessException {
	getSqlMapClientTemplate().update("updateServerInfos", serverParams);
    }

    public void deleteServerLinkToEntity(final Long entityId) {
	getSqlMapClientTemplate().delete("deleteServerLinkToEntity", entityId);
    }

    public void deleteServerParams(final Long serverId) {
	getSqlMapClientTemplate().delete("deleteServerInfos", serverId);
    }
}
