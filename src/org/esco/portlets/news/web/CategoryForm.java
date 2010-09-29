/**
 * ESUP-Portail News - Copyright (c) 2009 ESUP-Portail consortium
 * For any information please refer to http://esup-helpdesk.sourceforge.net
 * You may obtain a copy of the licence at http://www.esup-portail.org/license/
 */
package org.esco.portlets.news.web;

import java.io.Serializable;

import org.uhp.portlets.news.domain.Category;

/**
 * Category object for the form.
 * @author GIP RECIA - Gribonvald Julien
 * 15 avr. 2010
 */
public class CategoryForm implements Serializable {

    /** Id of serialisation. */
    private static final long serialVersionUID = -3763137226280316695L;
    
    /** List of type. */
    private String[] typesIds;
    
    /** The category. */
    private Category category;

    /**
     * Constructeur de l'objet CategoryForm.java.
     */
    public CategoryForm() {
        super();
    }

    /**
     * Getter du membre typesIds.
     * @return <code>String[]</code> le membre typesIds.
     */
    public String[] getTypesIds() {
        return typesIds;
    }

    /**
     * Setter du membre typesIds.
     * @param typesIds la nouvelle valeur du membre typesIds. 
     */
    public void setTypesIds(final String[] typesIds) {
        this.typesIds = typesIds;
    }

    /**
     * Getter du membre category.
     * @return <code>Category</code> le membre category.
     */
    public Category getCategory() {
        return category;
    }

    /**
     * Setter du membre category.
     * @param category la nouvelle valeur du membre category. 
     */
    public void setCategory(final Category category) {
        this.category = category;
    }

}
