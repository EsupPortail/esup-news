package org.uhp.portlets.news.dao.iBatis;

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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esco.portlets.news.domain.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.stereotype.Repository;
import org.uhp.portlets.news.NewsConstants;
import org.uhp.portlets.news.dao.CategoryDao;
import org.uhp.portlets.news.dao.Constants;
import org.uhp.portlets.news.dao.SequenceDao;
import org.uhp.portlets.news.domain.Category;

@Repository("categoryDao")
public class SqlMapClientCategoryDaoImpl extends SqlMapClientDaoSupport implements CategoryDao {

	private static final Log LOGGER = LogFactory.getLog(SqlMapClientCategoryDaoImpl.class);

	@Autowired private SequenceDao sequenceDao;

	public void setSequenceDao(final SequenceDao sequenceDao) {
        this.sequenceDao = sequenceDao;
    }

	@SuppressWarnings("unchecked")
	public List<Category> getAllCategory() throws DataAccessException {
		List<Category> categories = getSqlMapClientTemplate().queryForList("getAllCategory", null);
		List<Category> cats = new ArrayList<Category>();
        for (Category c : categories) {
            cats.add(setCounts(c));
        }
        return cats;
	}

	@SuppressWarnings("unchecked")
    public List<Category> getCategoriesByUser(final String uid) throws DataAccessException {
		final List<Category> categories =
		    getSqlMapClientTemplate().queryForList("getAvailableCategoriesByUser", uid);
		List<Category> cats = new ArrayList<Category>();
		for (Category c : categories) {
			cats.add(setCounts(c));
		}
		return cats;
	}

	public Category getCategoryById(final Long categoryId) throws DataAccessException {
		Category category = (Category) getSqlMapClientTemplate()
		    .queryForObject("getCategoryById", categoryId);
		if (category == null) {
			LOGGER.error("category [" + categoryId + "] not found");
			throw new ObjectRetrievalFailureException(Category.class, categoryId);
		}
		category.setTotalCount((Integer) getSqlMapClientTemplate()
		        .queryForObject("getTotalItemsCountByCategory", categoryId));
		category.setPendingCount((Integer) getSqlMapClientTemplate()
		        .queryForObject("getPendingItemsCountByCategory", categoryId));

		return category;
	}

	private Category setCounts(final Category category) {
        Category c = category;
        c.setTotalCount((Integer) getSqlMapClientTemplate().queryForObject(
                "getTotalItemsCountByCategory", c.getCategoryId()));
        c.setPendingCount((Integer) getSqlMapClientTemplate().queryForObject(
                "getPendingItemsCountByCategory", c.getCategoryId()));
        return c;
    }

	private void insert(final Category category) throws DataAccessException {
		category.setCreationDate(new Date());
		category.setLastUpdateDate(new Date());
		int dOrder = 1;
		if (getMaxDisplayOrder(category.getEntityId()) != null) {
		    dOrder = getMaxDisplayOrder(category.getEntityId()).intValue() + 1;
		}
		category.setDisplayOrder(dOrder);
		category.setCategoryId(this.sequenceDao.getNextId(Constants.SEQ_CAT));
		getSqlMapClientTemplate().insert("insertCategory", category);

	}

	private void update(final Category category) throws DataAccessException {
		category.setLastUpdateDate(new Date());
		getSqlMapClientTemplate().update("updateCategory", category);
	}

	public void save(final Category category) throws DataAccessException {
		if (category.getCategoryId() == null) {
			insert(category);
		} else {
			update(category);
		}

	}

	public boolean canDeleteCategory(final Long categoryId) throws DataAccessException {
		Integer t = (Integer) getSqlMapClientTemplate().queryForObject("canDeleteCategory", categoryId);
		return t.intValue() < 1;
	}

	public boolean delete(final Long categoryId) throws DataAccessException {
		if (canDeleteCategory(categoryId)) {
		    getSqlMapClientTemplate().delete("deleteAllTypeOfCategory", categoryId);
			getSqlMapClientTemplate().delete("deleteCategory", categoryId);
			return true;
		}
		return false;
	}

	@SuppressWarnings("unchecked")
   /*public List<Category> getMostRecentForEntity(final Long entityId, final Integer dayCount)  throws DataAccessException {
	    Map<String, Object> params = new HashMap<String, Object>();
	    params.put(Constants.DAY_COUNT, dayCount);
        params.put(NewsConstants.T_ID, entityId);
		return getSqlMapClientTemplate().queryForList("getMostRecentByEntity", params);
	}*/

	public Integer getMaxDisplayOrder(final Long entityId) throws DataAccessException {
		return (Integer) getSqlMapClientTemplate().queryForObject("getMaxCategoryOrder", entityId);
	}

	public boolean isCategoryNameExist(final String name, final Long entityId) throws DataAccessException {
	    Map<String, Object> params = new HashMap<String, Object>(2);
        params.put(Constants.NAME, name);
        params.put(Constants.ENTITY_ID, entityId);
		Integer t = (Integer) getSqlMapClientTemplate().queryForObject("isCategoryNameExist", params);
		return t.intValue() > 0;
	}

	public boolean isCategoryNameExist(final String name, final Long catId, final Long entityId)
	throws DataAccessException {
		Map<String, Object> params = new HashMap<String, Object>(2);
		params.put(Constants.NAME, name);
		params.put(Constants.CAT_ID, catId);
		params.put(Constants.ENTITY_ID, entityId);
		Integer t = (Integer) getSqlMapClientTemplate().queryForObject("isSameCategoryNameExist", params);
		return t.intValue() > 0;
	}

	public void updateCategoryOrdering(final Category c1, final Category c2)
			throws DataAccessException {
		int tmp = c1.getDisplayOrder();
		this.updateCategoryOrder(c1, c2.getDisplayOrder());
		this.updateCategoryOrder(c2, tmp);

	}

	private void updateCategoryOrder(final Category cat, final int order) throws DataAccessException  {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(NewsConstants.C_ID, cat.getCategoryId());
		params.put(Constants.DISPLAY_ORDER, Integer.valueOf(order));
		try {
			getSqlMapClientTemplate().update("updateCategoryOrderById", params);
		} catch (DataAccessException e) {
		    LOGGER.warn("SqlMapClientCategoryDaoImpl:: updateCategoryOrder : Error : " + e.getMessage());
		}
	}

	/**
     * Join a Category with a list of Type.
     * @param typeIds
     * @param categoryId
     * @throws DataAccessException
     * @see org.uhp.portlets.news.dao.CategoryDao#addTypesToCategory(java.util.List, Long)
     */
    public void addTypesToCategory(final List<Long> typeIds, final Long categoryId) throws DataAccessException {
        for (Long tId : typeIds) {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put(NewsConstants.TYPE_ID, tId );
            params.put(NewsConstants.C_ID, categoryId);
            try {
                getSqlMapClientTemplate().insert("insertOneTypeOfCategory", params);
            } catch (DataAccessException e) {
                LOGGER.warn("SqlMapClientCategoryDaoImpl:: insertOneTypeOfCategory : Error : " + e.getMessage());
                throw e;
            }
        }
    }


    /**
     * Delete the link between a Category and a list of Type.
     * @param typeIds
     * @param categoryId
     * @throws DataAccessException
     * @see org.uhp.portlets.news.dao.CategoryDao#deleteTypesOfCategory(java.util.List, java.lang.Long)
     */
    public void deleteTypesOfCategory(final List<Long> typeIds, final Long categoryId)
            throws DataAccessException {
        for (Long tId : typeIds) {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put(NewsConstants.TYPE_ID, tId );
            params.put(NewsConstants.C_ID, categoryId);
            try {
                getSqlMapClientTemplate().delete("deleteOneTypeOfCategory", params);
            } catch (DataAccessException e) {
                LOGGER.warn("SqlMapClientCategoryDaoImpl:: insertOneTypeOfCategory : Error : " + e.getMessage());
                throw e;
            }
        }
    }

    /**
     * Get the list of Type of the Category.
     * @param categoryId
     * @return <code>List<Type></code>
     * @throws DataAccessException
     * @see org.uhp.portlets.news.dao.CategoryDao#getTypesOfCategory(java.lang.Long)
     */
    @SuppressWarnings("unchecked")
    public List<Type> getTypesOfCategory(final Long categoryId)
            throws DataAccessException {
        return getSqlMapClientTemplate().queryForList("getTypeListByCategory", categoryId);
    }

    /**
     * Get the list of Category of a given Type in an Entity order by name.
     * @param typeId
     * @param entityId
     * @return <code>List<Category></code>
     * @throws DataAccessException
     * @see org.uhp.portlets.news.dao.CategoryDao#getCategoryByTypeOfEntity(Long, Long)
     */
    @SuppressWarnings("unchecked")
    public List<Category> getCategoryByTypeOfEntity(final Long typeId, final Long entityId) throws DataAccessException {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(NewsConstants.TYPE_ID, typeId );
        params.put(NewsConstants.ENTITY_ID, entityId);
        List<Category> categories = getSqlMapClientTemplate().queryForList("getCategoryByTypeOfEntity", params);
        List<Category> cats = new ArrayList<Category>();
        for (Category c : categories) {
            cats.add(setCounts(c));
        }
        return cats;
    }

    /**
     * Get the list of Category of a given Type in an Entity ordered by display order.
     * @param typeId
     * @param entityId
     * @return <code>List<Category></code>
     * @throws DataAccessException
     * @see org.uhp.portlets.news.dao.CategoryDao#getCategoryByTypeOfEntityInDisplayOrder(Long, Long)
     */
    @SuppressWarnings("unchecked")
    public List<Category> getCategoryByTypeOfEntityInDisplayOrder(final Long typeId, final Long entityId) throws DataAccessException {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(NewsConstants.TYPE_ID, typeId );
        params.put(NewsConstants.ENTITY_ID, entityId);
        List<Category> categories = getSqlMapClientTemplate().queryForList("getCategoryByTypeOfEntityInDiplayOrder", params);
        List<Category> cats = new ArrayList<Category>();
        for (Category c : categories) {
            cats.add(setCounts(c));
        }
        return cats;
    }

    /**
     * Get the list of all Category of a Type (for all Entity).
     * @param typeId
     * @return <code>List<Category></code>
     * @throws DataAccessException
     * @see org.uhp.portlets.news.dao.CategoryDao#getAllCategoryOfType(Long)
     */
    @SuppressWarnings("unchecked")
    public List<Category> getAllCategoryOfType(final Long typeId) throws DataAccessException {
        List<Category> categories = getSqlMapClientTemplate().queryForList("getAllCategoryOfType", typeId);
        List<Category> cats = new ArrayList<Category>();
        for (Category c : categories) {
            cats.add(setCounts(c));
        }
        return cats;
    }

    /**
     * Get the list of all Category in an Entity.
     * @param entityId
     * @return <code>List<Category></code>
     * @throws DataAccessException
     * @see org.uhp.portlets.news.dao.CategoryDao#getAllCategoryOfEntity(Long)
     */
    @SuppressWarnings("unchecked")
    public List<Category> getAllCategoryOfEntity(final Long entityId) throws DataAccessException {
        List<Category> categories = getSqlMapClientTemplate().queryForList("getAllCategoryOfEntity", entityId);
        List<Category> cats = new ArrayList<Category>();
        for (Category c : categories) {
            cats.add(setCounts(c));
        }
        return cats;
    }
    /**
     * Get the list of all available Category in an Entity for a user.
     * @param uid
     * @param entityId
     * @return <code>List<Category></code>
     * @throws DataAccessException
     * @see org.uhp.portlets.news.dao.CategoryDao#getCategoriesOfEntityByUser(String, Long)
     */
    @SuppressWarnings("unchecked")
    public List<Category> getCategoriesOfEntityByUser(final String uid, final Long entityId)
    throws DataAccessException {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(NewsConstants.UID, uid );
        params.put(NewsConstants.ENTITY_ID, entityId);
        List<Category> categories =
            getSqlMapClientTemplate().queryForList("getAvailableCategoriesByUserFromEntity", params);
        List<Category> cats = new ArrayList<Category>();
        for (Category c : categories) {
            cats.add(setCounts(c));
        }
        return cats;
    }

    /**
     * Get the list of all Category without an attachment of an Entity.
     * @return <code>List<Category></code>
     * @throws DataAccessException
     * @see org.uhp.portlets.news.dao.CategoryDao#getAllCategoriesWithoutEntity()
     */
    @SuppressWarnings("unchecked")
    public List<Category> getAllCategoriesWithoutEntity() throws DataAccessException {
        return getSqlMapClientTemplate().queryForList("getAllCategoriesWithoutEntity");
    }
}