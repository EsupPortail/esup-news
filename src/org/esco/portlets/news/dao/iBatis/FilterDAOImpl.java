/**
* ESUP-Portail News - Copyright (c) 2009 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esco.portlets.news.dao.iBatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esco.portlets.news.dao.FilterDAO;
import org.esco.portlets.news.domain.Filter;
import org.esco.portlets.news.domain.FilterType;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.stereotype.Repository;
import org.uhp.portlets.news.NewsConstants;

/**
 * Implementation of the Dao.
 * @author GIP RECIA - Gribonvald Julien
 * 8 déc. 2009
 */
@Repository("filterDao")
public class FilterDAOImpl extends SqlMapClientDaoSupport implements FilterDAO {

    /** Logger. */
    private static final Log LOG = LogFactory.getLog(FilterDAOImpl.class);

    /** Constructor.  */
    public FilterDAOImpl() {
        super();
    }

    /**
     * Obtain a filter from the id.
     * @param filterId Id of the filter.
     * @return <code>Filter</code>
     */
    public Filter getFilterById(final Long filterId) {
        final Filter f = (Filter) getSqlMapClientTemplate().queryForObject("selectFilterById", filterId);
        if (f == null) {
            LOG.error("filter [" + filterId + "] not found");
            throw new ObjectRetrievalFailureException(Filter.class, filterId);
        }
        return f;
    }


    /**
     * List of all filters.
     * @return <code>List<Filter></code>
     * @throws DataAccessException
     */
    @SuppressWarnings("unchecked")
    public List<Filter> getAllFilters() throws DataAccessException {
        return getSqlMapClientTemplate().queryForList("getAllFilter");
    }

    /**
     * Add search filters to the entity.
     * @param filter A list of filters.
     * @throws DataAccessException
     */
    public void saveFilterOfEntity(final Filter filter) throws DataAccessException {
        try {
            if (filter.getFilterId() == null || filter.getFilterId() < 1) {
                getSqlMapClientTemplate().insert("insertFilterOfEntity", filter);
            } else {
                getSqlMapClientTemplate().insert("updateFilterById", filter);
            }
        } catch (DataAccessException e) {
            LOG.warn("EntityDaoImpl:: insertFilterOfEntity : Error : " + e.getMessage());
            throw e;
        }
    }

    /**
     * Delete all Filters of an Entity.
     * @param entityId The entity id.
     * @throws DataAccessException
     */
    public void deleteAllFiltersOfEntity(final Long entityId) throws DataAccessException {
        try {
            getSqlMapClientTemplate().delete("deleteAllFilterOnEntity", entityId);
        } catch (DataAccessException e) {
            LOG.warn("EntityDaoImpl:: deleteAllFiltersOfEntity : Error : " + e.getMessage());
            throw e;
        }
    }

    /**
     * Delete a Filter of an Entity.
     * @param filterId The filter id.
     * @param entityId The entity id.
     * @return <code>boolean</code>
     * @throws DataAccessException
     */
    public boolean deleteFilterOfEntity(final Long filterId, final Long entityId) throws DataAccessException {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(NewsConstants.FILTER_ID, filterId);
        params.put(NewsConstants.ENTITY_ID, entityId);
        try {
            return getSqlMapClientTemplate().delete("deleteFilterOnEntity", params) == 1;
        } catch (DataAccessException e) {
            LOG.warn("EntityDaoImpl:: deleteFilterOnEntity : Error : " + e.getMessage());
            throw e;
        }
    }

    /**
     * Returns the list of filters associated to an Entity.
     * @param entityId The entity id.
     * @return <code>List<Filter></code>
     * @throws DataAccessException
     */
    @SuppressWarnings("unchecked")
    public List<Filter> getFiltersOfEntity(final Long entityId) throws DataAccessException {
        return getSqlMapClientTemplate().queryForList("getFilterListByEntity", entityId);
    }

    /**
     * Returns the list of filters associated to an Entity of a filter type.
     * @param entityId The entity id.
     * @param type The FilterType.
     * @return <code>List<Filter></code>
     * @throws DataAccessException
     */
    @SuppressWarnings("unchecked")
    public List<Filter> getFiltersOfTypeOfEntity(final Long entityId, final FilterType type)
    throws DataAccessException {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(NewsConstants.FILTER_TYPE, type);
        params.put(NewsConstants.ENTITY_ID, entityId);
        return getSqlMapClientTemplate().queryForList("getFilterOfTypeOfEntity", params);
    }

    /**
     * Return true if the filter already exist.
     * @param filter The filter, the entityId must be set.
     * @return <code>boolean</code> true if exist, false if not.
     * @throws DataAccessException
     */
    public boolean isFilterOnEntityExist(final Filter filter) throws DataAccessException {
        if (filter == null || filter.getEntityId() == null) {
            throw new IllegalArgumentException(
                    "The filter was not initialized correctly, check your filter initialisation.");
        }
        if (filter.getFilterId() == null || filter.getFilterId() < 1) {
        	return ((Integer) getSqlMapClientTemplate().queryForObject("existFilterOfEntity", filter)) > 0;
        }
        return ((Integer) getSqlMapClientTemplate().queryForObject("sameFilterOfEntity", filter)) > 0;
    }

}