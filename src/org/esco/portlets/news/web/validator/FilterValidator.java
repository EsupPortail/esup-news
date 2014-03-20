/**
 * ESUP-Portail News - Copyright (c) 2009 ESUP-Portail consortium
 * For any information please refer to http://esup-helpdesk.sourceforge.net
 * You may obtain a copy of the licence at http://www.esup-portail.org/license/
 */
package org.esco.portlets.news.web.validator;

import java.util.Arrays;
import org.esco.portlets.news.dao.FilterDAO;
import org.esco.portlets.news.domain.Filter;
import org.esco.portlets.news.domain.FilterOperator;
import org.esco.portlets.news.domain.FilterType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.uhp.portlets.news.web.validator.AbstractValidator;

/**
 *
 * @author GIP RECIA - Gribonvald Julien
 * 20 mai 2010
 */
public class FilterValidator extends AbstractValidator {

    /** Dao of a Filter.*/
    @Autowired
    private FilterDAO filterDao;

    /**
     * Constructor FilterValidator.java.
     */
    public FilterValidator() {
        super();
    }

    /**
     * @return <code>TypeForm.class</code>
     * @see org.uhp.portlets.news.web.validator.AbstractValidator#getValidatorSupportClass()
     */
    @SuppressWarnings("rawtypes")
	@Override
    protected Class getValidatorSupportClass() {
        return Filter.class;
    }

    /**
     * @param obj
     * @param errors
     * @see org.uhp.portlets.news.web.validator.AbstractValidator#
     * validate(java.lang.Object, org.springframework.validation.Errors)
     */
    @Override
    public void validate(final Object obj, final Errors errors) {
        final Filter filter = (Filter) obj;

        validateEntity(filter, errors);
        validateAttributes(filter, errors);
        validateFilter(filter, errors);
    }

    /**
     * Validation of the entity.
     * @param filter
     * @param errors
     */
    private void validateEntity(final Filter filter, final Errors errors) {
        ValidationUtils.rejectIfEmpty(errors, "entityId", "ENTITY_ID_REQUIRED",
                "The association with an entity is required.");

    }

    /**
     * Validation of the attribute.
     * @param filter
     * @param errors
     */
    private void validateAttributes(final Filter filter, final Errors errors) {
        ValidationUtils.rejectIfEmpty(errors, "attribute", "FILTER_ATTR_REQUIRED", "You must select a value.");
        ValidationUtils.rejectIfEmpty(errors, "operator", "FILTER_ATTR_REQUIRED", "You must select a value.");
        ValidationUtils.rejectIfEmpty(errors, "value", "FILTER_VALUE_REQUIRED", "A filter value is required");
    }

    /**
     * @param filter
     * @param errors
     */
    private void validateFilter(final Filter filter, final Errors errors) {
        if (this.filterDao.isFilterOnEntityExist(filter)) {
            errors.rejectValue("FILTER_EXIST", "A filter with same attributes already exist.");
        }
        if (FilterType.Group.equals(filter.getType()) && !Arrays.asList(FilterOperator.getGroupOperators()).contains(filter.getOperator())) {
        	errors.rejectValue("FILTER_WRONG_OPERATOR", "The selected operator doesn't correspond to the filter type.");
        }
        if (FilterType.LDAP.equals(filter.getType()) && !Arrays.asList(FilterOperator.getLdapOperators()).contains(filter.getOperator())) {
        	errors.rejectValue("FILTER_WRONG_OPERATOR", "The selected operator doesn't correspond to the filter type.");
        }
    }

    /**
     * Getter du membre filterDao.
     * @return <code>FilterDAO</code> le membre filterDao.
     */
    public FilterDAO getFilterDao() {
        return filterDao;
    }

    /**
     * Setter du membre filterDao.
     * @param filterDao la nouvelle valeur du membre filterDao.
     */
    public void setFilterDao(final FilterDAO filterDao) {
        this.filterDao = filterDao;
    }
}
