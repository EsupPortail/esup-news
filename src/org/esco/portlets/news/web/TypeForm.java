/**
 * ESUP-Portail News - Copyright (c) 2009 ESUP-Portail consortium
 * For any information please refer to http://esup-helpdesk.sourceforge.net
 * You may obtain a copy of the licence at http://www.esup-portail.org/license/
 */
package org.esco.portlets.news.web;

import java.io.Serializable;

import org.esco.portlets.news.domain.Type;

/**
 * Objet pour la form d'un Type.
 * @author GIP RECIA - Gribonvald Julien
 * 8 févr. 2010
 */
public class TypeForm implements Serializable {

    /** */
    private static final long serialVersionUID = -7337506643213603818L;

    /** Liste des types associés. */
    private String[] entitiesIds;
    
    /** Un type. */
    private Type type;
    
    /**
     * Constructeur de l'objet TypeForm.java.
     */
    public TypeForm() {
        super();
    }

    /**
     * Getter du membre entitiesIds.
     * @return <code>String[]</code> le membre entitiesIds.
     */
    public String[] getEntitiesIds() {
        return entitiesIds;
    }

    /**
     * Setter du membre entitiesIds.
     * @param entitiesIds la nouvelle valeur du membre entitiesIds. 
     */
    public void setEntitiesIds(final String[] entitiesIds) {
        this.entitiesIds = entitiesIds;
    }

    /**
     * Getter du membre type.
     * @return <code>Type</code> le membre type.
     */
    public Type getType() {
        return type;
    }

    /**
     * Setter du membre type.
     * @param type la nouvelle valeur du membre type. 
     */
    public void setType(final Type type) {
        this.type = type;
    }
}
