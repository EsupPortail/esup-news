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
package org.uhp.portlets.news.service;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esco.portlets.news.dao.EntityDAO;
import org.esco.portlets.news.dao.EscoUserDao;
import org.esco.portlets.news.dao.SubjectRoleDao;
import org.esco.portlets.news.dao.TypeDAO;
import org.esco.portlets.news.domain.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.uhp.portlets.news.NewsConstants;
import org.uhp.portlets.news.dao.CategoryDao;
import org.uhp.portlets.news.dao.SubscriberDao;
import org.uhp.portlets.news.domain.Category;
import org.uhp.portlets.news.domain.UserRole;
import org.uhp.portlets.news.service.exception.CategoryException;

/**
 *
 * modification GIP RECIA - Gribonvald Julien
 * 3 févr. 2010
 */
@Service("categoryManager")
@Transactional(readOnly = true)
public class CategoryManagerImpl implements CategoryManager {

	/** Logger. */
	private static final Log LOG = LogFactory.getLog(CategoryManagerImpl.class);


	/** Dao d'une category. */
	@Autowired
	private CategoryDao categoryDao;
	/** Dao d'un subscriber. */
	@Autowired
	private SubscriberDao subDao;
	/** Dao d'un user. */
	@Autowired
	private EscoUserDao userDao;
	/** Dao d'un UserRole. */
	@Autowired
	private SubjectRoleDao roleDao;
	/** Dao d'une entity. */
	@Autowired
	private EntityDAO entityDao;
	/** Dao des types.*/
	@Autowired
	private TypeDAO typeDao;

	/**
	 * Constructeur de l'objet CategoryManagerImpl.java.
	 */
	public CategoryManagerImpl() {
		super();
	}

	/**
	 * @param categoryId
	 * @return <code>boolean</code>
	 * @see org.uhp.portlets.news.service.CategoryManager#deleteCategory(java.lang.Long)
	 */
	@Transactional(readOnly = false)
	public boolean deleteCategory(final Long categoryId) {
		try {
			List<UserRole> lists = this.roleDao.getUsersRolesForCtx(categoryId, NewsConstants.CTX_C);
			if (this.categoryDao.delete(categoryId))	{

				this.roleDao.removeUsersRoleForCtx(categoryId, NewsConstants.CTX_C);
				for (UserRole ur : lists) {
					if (ur.getIsGroup().equals("0") && !this.userDao.isSuperAdmin(ur.getPrincipal())
							&& !this.userDao.userRoleExist(ur.getPrincipal())) {
						this.userDao.deleteUser(ur.getPrincipal(), false);
					}
				}
				this.subDao.deleteAllSubscribersByCtxId(categoryId, NewsConstants.CTX_C);
				return true;
			}
		} catch (DataAccessException e) {
			LOG.error("Delete Category error : " + e.getLocalizedMessage());
		}
		return false;
	}

	/**
	 * @return <code>List<Category></code>
	 * @see org.uhp.portlets.news.service.CategoryManager#getAllCategory()
	 */
	public List<Category> getAllCategory() {
		return  this.categoryDao.getAllCategory();
	}

	/**
	 * @param categoryId
	 * @return <code>Category</code>
	 * @see org.uhp.portlets.news.service.CategoryManager#getCategoryById(java.lang.Long)
	 */
	public Category getCategoryById(final Long categoryId) {
		try {
			return this.categoryDao.getCategoryById(categoryId);
		} catch (DataAccessException e) {
			LOG.error("get category error : " + e.getLocalizedMessage());
		}
		return null;
	}

	/**
	 * @param id
	 * @param categories
	 * @return <code>int</code>
	 */
	private int getIdxForCategory(final Long id, final List<Category> categories) {
		int n = -1;
		Iterator<Category> it = categories.iterator();
		boolean b = false;
		while (!b && it.hasNext()) {
			if (it.next().getCategoryId().compareTo(id) == 0) {
				b = true;
			}
			n++;
		}
		return n;
	}
	/**
	 * @param uid
	 * @return <code>List<Category></code>
	 * @see org.uhp.portlets.news.service.CategoryManager#getListCategoryByUser(java.lang.String)
	 */
	public List<Category> getListCategoryByUser(final String uid) {
		List<Category>  categories = null;
		try {
			if (this.userDao.isSuperAdmin(uid))  {
				categories  = this.categoryDao.getAllCategory();
			} else {
				categories = this.categoryDao.getCategoriesByUser(uid);
			}
		} catch (DataAccessException e) {
			LOG.warn("Error msg : " + e.getLocalizedMessage());
		}
		return categories;
	}

	/**
	 * @param entityId
	 * @param dayCount
	 * @return <code>List<Category></code>
	 * @see org.uhp.portlets.news.service.CategoryManager#getMostRecentCategory(java.lang.Integer)
	 */
	/*public List<Category> getMostRecentCategory(final Long entityId, final Integer dayCount) {
		return this.categoryDao.getMostRecentForEntity(entityId, dayCount);
	}*/

	/**
	 * @param category
	 * @throws CategoryException
	 * @see org.uhp.portlets.news.service.CategoryManager#saveCategory(org.uhp.portlets.news.domain.Category)
	 */
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void saveCategory(final Category category) throws CategoryException {
		if (category == null) {
			throw new IllegalArgumentException("Cant save a category when given a null Category instance.");
		}
		try {
			this.categoryDao.save(category);
		} catch (DataIntegrityViolationException dive) {
			LOG.error("Could not save category, duplicate category id ??", dive);

		} catch (Exception e) {
			String msg = "Could not save category " + e.toString();
			LOG.error(msg, e);
			throw new CategoryException(msg, e);
		}
	}

	/**
	 * @param id
	 * @param up
	 * @see org.uhp.portlets.news.service.CategoryManager#
	 * updateCategoryOrdering(java.lang.Long, java.lang.Long, boolean)
	 */
	@Transactional(readOnly = false)
	public void updateCategoryOrdering(final Long id, final Long entityId, final boolean up) {
		Category c1 = this.categoryDao.getCategoryById(id);
		List<Category> categories = this.categoryDao.getAllCategoryOfEntity(entityId);
		int idx = this.getIdxForCategory(id, categories);
		if ((idx == -1) || (!up && (idx == categories.size()-1)) || (up && (idx == 0)) ) {
			LOG.debug("updateCategoryOrdering:: nothing to do ..." );
		} else {
			if (up) {
				idx--;
			} else {
				idx++;
			}
			this.categoryDao.updateCategoryOrdering(c1, categories.get(idx));
		}

	}

	/**
	 * Put to the first or the last position a category and in this case move other categories.
	 * @param id
	 * @param up
	 * @see org.uhp.portlets.news.service.CategoryManager#
	 * updateCategoryOrderingToFirstOrLast(java.lang.Long, java.lang.Long, boolean)
	 */
	@Transactional(readOnly = false)
	public void updateCategoryOrderingToFirstOrLast(final Long id, final Long entityId, final boolean up) {
		try {
			List<Category> categories = this.categoryDao.getAllCategoryOfEntity(entityId);
			for (int i = 0; i < categories.size(); i++) {
				this.updateCategoryOrdering(id, entityId, up);
			}
		} catch (DataAccessException e) {
			LOG.error("Update Category Ordering To First Or Last error : " + e.getLocalizedMessage());
			throw e;
		}
	}

	/** Ajouts ESCO. */

	/**
	 * Retourne la liste des types associés à une catégorie.
	 * @param categoryId
	 * @return <code>List<Type></code>
	 */
	public List<Type> getTypesOfCategory(final Long categoryId) {
		try {
			return this.categoryDao.getTypesOfCategory(categoryId);
		} catch (DataAccessException e) {
			LOG.error("Get Type Of Category error : " + e.getLocalizedMessage());
			throw e;
		}
	}

	/**
	 * Associer une liste de types à une catégorie.
	 * @param typeIds
	 * @param categoryId
	 * @throws CategoryException
	 */
	@Transactional(readOnly = false)
	public void addAuthorizedTypeToCategory(final List<Long> typeIds, final Long categoryId)
	throws CategoryException {
		try {
			List<Type> types = this.entityDao.getAuthorizedTypesOfEntity(
					this.categoryDao.getCategoryById(categoryId).getEntityId());
			List<Long> tids = new ArrayList<Long>();
			for (Type t : types) {
				tids.add(t.getTypeId());
			}
			if (tids.containsAll(typeIds)) {
				this.categoryDao.addTypesToCategory(typeIds, categoryId);
			} else {
				throw new CategoryException("Certains des types spécifiés ne sont pas autorisés pour la catégorie.");
			}
		} catch (DataAccessException e) {
			LOG.error("Add Authorized Type To Category error : " + e.getLocalizedMessage());
			throw e;
		}
	}

	/**
	 * Supprime les associations d'une catégorie.
	 * @param typeIds
	 * @param categoryId
	 */
	@Transactional(readOnly = false)
	public void deleteTypeOfCategory(final List<Long> typeIds, final Long categoryId) {
		try {
			Type defaultType = this.typeDao.getTypeByName(NewsConstants.DEFAULT_TYPE);
			List<Long> type = new ArrayList<Long>();
			type.add(defaultType.getTypeId());

			this.categoryDao.deleteTypesOfCategory(typeIds, categoryId);
			List<Type> types = this.categoryDao.getTypesOfCategory(categoryId);
			if (types == null || types.isEmpty()) {
				this.categoryDao.addTypesToCategory(type, categoryId);
			}

		} catch (DataAccessException e) {
			LOG.error("Delete Types Of Category error : " + e.getLocalizedMessage());
			throw e;
		}
	}

	/**
	 * Liste toutes les catégories d'un certain type (pour toutes les entités.
	 * @param typeId
	 * @return <code>List<Category></code>
	 * @see org.uhp.portlets.news.service.CategoryManager#getAllCategoryOfType(java.lang.Long)
	 */
	public List<Category> getAllCategoryOfType(final Long typeId) {
		return this.categoryDao.getAllCategoryOfType(typeId);
	}

	/** Liste les catégories d'une entité d'un certain type.
	 * @param typeId
	 * @param entityId
	 * @return <code>List<Category></code>
	 * @see org.uhp.portlets.news.service.CategoryManager#getCategoryByTypeOfEntity(java.lang.Long, java.lang.Long)
	 */
	public List<Category> getCategoryByTypeOfEntity(final Long typeId, final Long entityId) {
		return this.categoryDao.getCategoryByTypeOfEntity(typeId, entityId);
	}

	/**
	 * Liste les catégories d'une entité pour un utilisateur.
	 * @param uid
	 * @param entityId
	 * @return <code>List<Category></code>
	 * @see org.uhp.portlets.news.service.CategoryManager#getListCategoryByUser(java.lang.String)
	 */
	public List<Category> getListCategoryOfEntityByUser(final String uid, final Long entityId) {
		List<Category>  categories = null;
		try {
			if (this.userDao.isSuperAdmin(uid))  {
				categories  = this.categoryDao.getAllCategoryOfEntity(entityId);
			} else {
				categories = this.categoryDao.getCategoriesOfEntityByUser(uid, entityId);
			}
		} catch (DataAccessException e) {
			LOG.warn("Error msg : " + e.getLocalizedMessage());
		}
		return categories;
	}

	/** List of Category without Entity associated.
	 * @return <code>List<Category></code>
	 * @see org.uhp.portlets.news.service.CategoryManager#getAloneCategory()
	 */
	public List<Category> getAloneCategory() {
		return this.categoryDao.getAllCategoriesWithoutEntity();
	}
}
