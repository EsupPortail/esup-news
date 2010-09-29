/**
 * ESUP-Portail News - Copyright (c) 2009 ESUP-Portail consortium
 * For any information please refer to http://esup-helpdesk.sourceforge.net
 * You may obtain a copy of the licence at http://www.esup-portail.org/license/
 */
package org.esco.portlets.news.web;

import java.io.Serializable;

import org.esco.portlets.news.domain.Entity;

/**
 * Objet for the form of an Entity.
 * @author GIP RECIA - Gribonvald Julien
 * 8 févr. 2010
 */
public class EntityForm implements Serializable {

    /** Id of serialisation. */
    private static final long serialVersionUID = -9206878843525696083L;

    /** List of type. */
    private String[] typesIds;
    
    /** An Entity. */
    private Entity entity;
    
    /** List of categories. */
    private String[] categoriesIds;
    
    /**
     * Constructeur de l'objet EntityForm.java.
     */
    public EntityForm() {
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
     * Getter du membre entity.
     * @return <code>Entity</code> le membre entity.
     */
    public Entity getEntity() {
        return entity;
    }

    /**
     * Setter du membre entity.
     * @param entity la nouvelle valeur du membre entity. 
     */
    public void setEntity(final Entity entity) {
        this.entity = entity;
    }

    /**
     * Getter du membre categoriesIds.
     * @return <code>String[]</code> le membre categoriesIds.
     */
    public String[] getCategoriesIds() {
        return categoriesIds;
    }

    /**
     * Setter du membre categoriesIds.
     * @param categoriesIds la nouvelle valeur du membre categoriesIds. 
     */
    public void setCategoriesIds(final String[] categoriesIds) {
        this.categoriesIds = categoriesIds;
    }
}
