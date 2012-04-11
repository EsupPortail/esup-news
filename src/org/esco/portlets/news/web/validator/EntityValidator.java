/**
 * ESUP-Portail News - Copyright (c) 2009 ESUP-Portail consortium
 * For any information please refer to http://esup-helpdesk.sourceforge.net
 * You may obtain a copy of the licence at http://www.esup-portail.org/license/
 */
package org.esco.portlets.news.web.validator;

import org.esco.portlets.news.dao.EntityDAO;
import org.esco.portlets.news.web.EntityForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.uhp.portlets.news.web.validator.AbstractValidator;

/**
 *
 * @author GIP RECIA - Gribonvald Julien
 * 8 févr. 2010
 */
public class EntityValidator extends AbstractValidator {

    /** DAO d'une entité. */
    @Autowired
    private EntityDAO entityDao;

    /**
     * Constructeur de l'objet EntityValidator.java.
     */
    public EntityValidator() {
        super();
    }

    /**
     * @return <code>EntityForm.class;</code>
     * @see org.uhp.portlets.news.web.validator.AbstractValidator#getValidatorSupportClass()
     */
    @SuppressWarnings("rawtypes")
	@Override
    protected Class getValidatorSupportClass() {
        return EntityForm.class;
    }

    /**
     * @param obj
     * @param errors
     * @see org.uhp.portlets.news.web.validator.AbstractValidator#
     * validate(java.lang.Object, org.springframework.validation.Errors)
     */
    @Override
    public void validate(final Object obj, final Errors errors) {
        final EntityForm entityF = (EntityForm) obj;

        validateName(entityF, errors);
        validateDescription(entityF, errors);
        validateTypesIds(entityF.getTypesIds(), errors);
    }

    /**
     * @param entityF
     * @param errors
     */
    private void validateName(final EntityForm entityF, final Errors errors) {
        ValidationUtils.rejectIfEmpty(errors, "entity.name", "ENTITY_NAME_REQUIRED", "Name is required.");
        if (this.entityDao.isEntityNameExist(entityF.getEntity().getName(), entityF.getEntity().getEntityId())) {
            errors.rejectValue("entity.name", "ENTITY_NAME_EXISTS", "A type with same name exists yet.");
        }
    }

    /**
     * @param entityF
     * @param errors
     */
    private void validateDescription(final EntityForm entityF, final Errors errors) {
        ValidationUtils.rejectIfEmpty(errors, "entity.description", "ENTITY_DESC_REQUIRED", "Description is required.");
    }

    /**
     * Valide l'association d'une entité à un ensemble de types.
     * @param ids
     * @param errors
     */
    private void validateTypesIds(final String[] ids, final Errors errors) {
        if (ids.length < 1) {
            errors.rejectValue("typesIds", "TYPE_ENTITY_REQUIRED", "Type's entity is required.");
        }
    }
}
