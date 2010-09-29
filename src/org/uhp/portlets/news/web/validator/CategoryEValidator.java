package org.uhp.portlets.news.web.validator;

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

import org.esco.portlets.news.web.CategoryForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.uhp.portlets.news.dao.CategoryDao;



public class CategoryEValidator extends AbstractValidator {
    @Autowired private CategoryDao categoryDao; 

    public CategoryEValidator() {
        super();
    }

    @Override
    public void validate(final Object obj, final Errors errors) {        
        final CategoryForm categoryForm = (CategoryForm) obj;        
        validateTitle(categoryForm, errors);
        validateDescription(categoryForm, errors); 
        validateTypesIds(categoryForm.getTypesIds(), errors);
    }

    public void validateTitle(final CategoryForm categoryForm, final Errors errors) {
        ValidationUtils.rejectIfEmpty(errors, "category.name", "CAT_TITLE_REQUIRED", "Title is required.");
        if (this.categoryDao.isCategoryNameExist(categoryForm.getCategory().getName(), 
                categoryForm.getCategory().getCategoryId(), categoryForm.getCategory().getEntityId())) {
            errors.rejectValue("category.name", "CAT_NAME_EXISTS", "A category with same title exists yet.");
        }
    }

    public void validateDescription(final CategoryForm categoryForm, final Errors errors) {
        ValidationUtils.rejectIfEmpty(errors, "category.desc", "CAT_DESC_REQUIRED", "Description is required.");
    }
    
    /**
     * Validate the association with one or more types.
     * @param ids
     * @param errors
     */
    private void validateTypesIds(final String[] ids, final Errors errors) {
        if (ids.length < 1) {
            errors.rejectValue("typesIds", "TYPE_CATEGORY_REQUIRED", "Type's category is required.");
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Class getValidatorSupportClass() {
        return CategoryForm.class;
    }

}
