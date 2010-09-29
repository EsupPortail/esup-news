/**
* ESUP-Portail News - Copyright (c) 2009 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/

package org.esco.portlets.news.dao;

import java.util.List;

import org.esco.portlets.news.domain.Filter;
import org.esco.portlets.news.domain.FilterType;
import org.springframework.dao.DataAccessException;

/**
 * Dao for Filter.
 * @author GIP RECIA - Gribonvald Julien
 * 19 mai 2010.
 */
public interface FilterDAO {
    
    /**
     * Obtain a filter from his id.
     * @param filterId Id of the filter.
     * @return <code>Filter</code> The filter found.
     */
    Filter getFilterById(final Long filterId);
    
    /**
    * List of all filters.
    * @return <code>List<Filter></code>
    * @throws DataAccessException
    */
    List<Filter> getAllFilters() throws DataAccessException;
    
    /**
     * Add search filter to the entity.
     * @param filter A filter.
     * @throws DataAccessException
     */
    void saveFilterOfEntity(final Filter filter) throws DataAccessException;
    
    /**
     * Return true if the filter already exist.
     * @param filter The filter, the entityId must be set.
     * @return <code>boolean</code> true if exist, false if not.
     * @throws DataAccessException
     */
    boolean isFilterOnEntityExist(final Filter filter) throws DataAccessException;
    
    /**
     * Delete a Filter of an Entity.
     * @param filterId The filter id.
     * @param entityId The entity id.
     * @return <code>boolean</code>
     * @throws DataAccessException
     */
    boolean deleteFilterOfEntity(final Long filterId, final Long entityId) throws DataAccessException;
    
    /**
     * Delete all Filters of an Entity.
     * @param entityId The entity id.
     * @throws DataAccessException
     */
    void deleteAllFiltersOfEntity(final Long entityId) throws DataAccessException;
    
    /**
     * Returns the list of filters associated to an Entity.
     * @param entityId The entity id.
     * @return <code>List<Filter></code>
     * @throws DataAccessException
     */
    List<Filter> getFiltersOfEntity(final Long entityId) throws DataAccessException;
    
    /**
     * Returns the list of filters associated to an Entity of a filter type.
     * @param entityId The entity id.
     * @param type The FilterType. 
     * @return <code>List<Filter></code>
     * @throws DataAccessException
     */
   List<Filter> getFiltersOfTypeOfEntity(final Long entityId, final FilterType type) throws DataAccessException;
}