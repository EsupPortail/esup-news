package org.uhp.portlets.news.service;

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
import org.uhp.portlets.news.domain.Category;
import org.uhp.portlets.news.service.exception.CategoryException;

/**
 * Category manager.
 * modified by GIP RECIA - Gribonvald Julien
 */
public interface CategoryManager {

	/**
	 * @return List<Category>
	 */
	List<Category> getAllCategory();
	
	/**
	 * @param uid
	 * @return List<Category>
	 */
	List<Category> getListCategoryByUser(String uid);
	
	/**
	 * @param categoryId
	 * @return Category
	 */
	Category getCategoryById(Long categoryId);

	
	/**
	 * @param category
	 * @throws CategoryException
	 */
	void saveCategory(final Category category) throws CategoryException;
	
	/**
	 * @param categoryId
	 * @return boolean
	 */
	boolean deleteCategory(Long categoryId);
		
	//List<Category> getMostRecentCategory(Integer dayCount);
	
	/**
	 * @param id
	 * @param entityId
	 * @param up
	 */
	void updateCategoryOrdering(Long id, Long entityId, boolean up);

    /**
     * @param id
     * @param entityId
     * @param up
     */
    void updateCategoryOrderingToFirstOrLast(Long id, Long entityId, boolean up);
    
    /**
     * Get the list of type of a Category.
     * @param categoryId
     * @return <code>List<Type></code>
     */
    List<Type> getTypesOfCategory(final Long categoryId);

    /**
     * Add Type of a Category.
     * @param typeIds
     * @param categoryId
     * @throws CategoryException 
     */
    void addAuthorizedTypeToCategory(final List<Long> typeIds, final Long categoryId) throws CategoryException;
    
    /**
     * Delete Type of a Category.
     * @param typeIds
     * @param categoryId
     */
    void deleteTypeOfCategory(final List<Long> typeIds, final Long categoryId);
    
    /**
     * Get the list of Category of a Type of an Entity .
     * @param typeId
     * @param entityId
     * @return <code>List<Category></code>
     */
    List<Category> getCategoryByTypeOfEntity(final Long typeId, final Long entityId);
    
    /**
     * Get all Category of a Type.
     * @param typeId
     * @return <code>List<Category></code>
     */
    List<Category> getAllCategoryOfType(final Long typeId);
    
    /**
     * Get all Category of on Entity for an authorized User.
     * @param uid
     * @param entityId
     * @return <code>List<Category></code>
     */
    List<Category> getListCategoryOfEntityByUser(final String uid, final Long entityId);
    
    /** 
     * List of Category without Entity associated.
     * @return <code>List<Category></code>
     */
    List<Category> getAloneCategory();
}
