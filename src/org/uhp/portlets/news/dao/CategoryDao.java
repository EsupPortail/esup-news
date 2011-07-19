package org.uhp.portlets.news.dao;
/**
 * @Project NewsPortlet : http://sourcesup.cru.fr/newsportlet/
 * Copyright (C) 2007-2008 University Nancy 1
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation version 2 of the License.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */

import java.util.List;

import org.esco.portlets.news.domain.Type;
import org.springframework.dao.DataAccessException;
import org.uhp.portlets.news.domain.Category;

/**
 * Modifications : GIP RECIA - Gribonvald Julien
 * 4 févr. 2010
 */
public interface CategoryDao {

    /**
     * @return <code>List<Category></code>
     * @throws DataAccessException
     */
    List<Category> getAllCategory() throws DataAccessException;

    /**
     * @param uid
     * @return <code>List<Category></code>
     * @throws DataAccessException
     */
    List<Category> getCategoriesByUser(String uid) throws DataAccessException;

    /**
     * @param categoryId
     * @return <code>List<Category></code>
     * @throws DataAccessException
     */
    Category getCategoryById(Long categoryId) throws DataAccessException;

    /**
     * @param category
     * @throws DataAccessException
     */
    void save(Category category) throws DataAccessException;

    /**
     * @param categoryId
     * @return <code>List<Category></code>
     * @throws DataAccessException
     */
    boolean delete(Long categoryId) throws DataAccessException;

    /**
     * @param entityId
     * @return <code>List<Category></code>
     * @throws DataAccessException
     */
    Integer getMaxDisplayOrder(final Long entityId) throws DataAccessException;

    /**
     * @param entityId
     * @param dayCount
     * @return <code>List<Category></code>
     * @throws DataAccessException
     */
    //List<Category> getMostRecentForEntity(final Long entityId, final Integer dayCount) throws DataAccessException;

    /**
     * @param name
     * @param entityId
     * @return <code>List<Category></code>
     * @throws DataAccessException
     */
    boolean isCategoryNameExist(final String name, final Long entityId) throws DataAccessException;

    /**
     * @param name
     * @param categoryId
     * @param entityId
     * @return <code>List<Category></code>
     * @throws DataAccessException
     */
    boolean isCategoryNameExist(final String name, final Long categoryId, final Long entityId)
    throws DataAccessException;

    /**
     * @param c1
     * @param c2
     * @throws DataAccessException
     */
    void updateCategoryOrdering(Category c1, Category c2) throws DataAccessException;

    /** ESCO Adds*/

    /** Get the list of Type of the Category.
     * @param categoryId
     * @return <code>List<Type></code>
     * @throws DataAccessException */
    List<Type> getTypesOfCategory(Long categoryId) throws DataAccessException;

    /**
     * Join a Category with a list of Type.
     * @param typeIds
     * @param categoryId
     * @throws DataAccessException
     */
    void addTypesToCategory(List<Long> typeIds, Long categoryId) throws DataAccessException;

    /**
     * Delete the link between a Category and a list of Type.
     * @param typeIds
     * @param categoryId
     * @throws DataAccessException
     */
    void deleteTypesOfCategory(List<Long> typeIds, Long categoryId) throws DataAccessException;

    /**
     * Get the list of Category of a given Type in an Entity ordered by name.
     * @param typeId
     * @param entityId
     * @return <code>List<Category></code>
     * @throws DataAccessException
     */
    List<Category> getCategoryByTypeOfEntity(Long typeId, Long entityId) throws DataAccessException;

    /**
     * Get the list of Category of a given Type in an Entity ordered by display order.
     * @param typeId
     * @param entityId
     * @return <code>List<Category></code>
     * @throws DataAccessException
     */
    List<Category> getCategoryByTypeOfEntityInDisplayOrder(Long typeId, Long entityId) throws DataAccessException;

    /**
     * Get the list of all Category of a Type (for all Entity).
     * @param typeId
     * @return <code>List<Category></code>
     * @throws DataAccessException
     */
    List<Category> getAllCategoryOfType(Long typeId) throws DataAccessException;

    /**
     * Get the list of all Category in an Entity.
     * @param entityId
     * @return <code>List<Category></code>
     * @throws DataAccessException
     */
    List<Category> getAllCategoryOfEntity(Long entityId) throws DataAccessException;

    /**
     * Get the list of all available Category in an Entity for a user.
     * @param uid
     * @param entityId
     * @return <code>List<Category></code>
     * @throws DataAccessException
     */
    List<Category> getCategoriesOfEntityByUser(String uid, Long entityId) throws DataAccessException;

    /**
     * Get the list of all Category in an Entity.
     * @return <code>List<Category></code>
     * @throws DataAccessException
     */
   List<Category> getAllCategoriesWithoutEntity() throws DataAccessException;
}
