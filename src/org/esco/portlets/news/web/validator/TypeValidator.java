/**
 * ESUP-Portail News - Copyright (c) 2009 ESUP-Portail consortium
 * For any information please refer to http://esup-helpdesk.sourceforge.net
 * You may obtain a copy of the licence at http://www.esup-portail.org/license/
 */
package org.esco.portlets.news.web.validator;

import org.esco.portlets.news.dao.TypeDAO;
import org.esco.portlets.news.web.TypeForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.uhp.portlets.news.web.validator.AbstractValidator;

/**
 *
 * @author GIP RECIA - Gribonvald Julien
 * 8 févr. 2010
 */
public class TypeValidator extends AbstractValidator {
    
    /** Dao d'un Type.*/
    @Autowired
    private TypeDAO typeDao;

    /**
     * Constructeur de l'objet TypeValidator.java.
     */
    public TypeValidator() {
        super();
    }
    
    /**
     * @return <code>TypeForm.class</code>
     * @see org.uhp.portlets.news.web.validator.AbstractValidator#getValidatorSupportClass()
     */
    @SuppressWarnings("unchecked")
    @Override
    protected Class getValidatorSupportClass() {
        return TypeForm.class;
    }

    /**
     * @param obj
     * @param errors
     * @see org.uhp.portlets.news.web.validator.AbstractValidator#
     * validate(java.lang.Object, org.springframework.validation.Errors)
     */
    @Override
    public void validate(final Object obj, final Errors errors) {
        final TypeForm typeF = (TypeForm) obj;
        
        validateName(typeF, errors);
        validateDescription(typeF, errors);
    }
    
    /**
     * Validation du nom du type.
     * @param typeF
     * @param errors
     */
    private void validateName(final TypeForm typeF, final Errors errors) {           
        ValidationUtils.rejectIfEmpty(errors, "type.name", "TYPE_NAME_REQUIRED", "Name is required.");
        if (this.typeDao.isTypeNameExist(typeF.getType().getName(), typeF.getType().getTypeId())) {
            errors.rejectValue("type.name", "TYPE_NAME_EXISTS", "A type with same name exists yet.");
        }
    }
    
    /**
     * Vérifie la description.
     * @param typeF
     * @param errors
     */
    private void validateDescription(final TypeForm typeF, final Errors errors) {
        ValidationUtils.rejectIfEmpty(errors, "type.description", "TYPE_DESC_REQUIRED", "Description is required.");
    }

    /**
     * Getter du membre typeDao.
     * @return <code>TypeDAO</code> le membre typeDao.
     */
    public TypeDAO getTypeDao() {
        return typeDao;
    }

    /**
     * Setter du membre typeDao.
     * @param typeDao la nouvelle valeur du membre typeDao. 
     */
    public void setTypeDao(final TypeDAO typeDao) {
        this.typeDao = typeDao;
    }
}
