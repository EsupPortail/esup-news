/**
 * ESUP-Portail News - Copyright (c) 2009 ESUP-Portail consortium
 * For any information please refer to http://esup-helpdesk.sourceforge.net
 * You may obtain a copy of the licence at http://www.esup-portail.org/license/
 */
package org.esco.portlets.news.services;

import java.util.List;

import org.esco.portlets.news.domain.Type;

/**
 *
 * @author GIP RECIA - Gribonvald Julien
 * 4 févr. 2010
 */
public interface TypeManager {

    /**
     * Sauvegarder un type.
     * @param type
     */
    void saveType(final Type type);
    
    /**
     * Ajouter un type associé à une liste d'entités.
     * @param type
     * @param entitiesIds
     */
    void addType(final Type type, final List<Long> entitiesIds);

    /**
     * Obtenir un type à partir de son id.
     * @param typeId
     * @return <code>Type</code>
     */
    Type getTypeById(final Long typeId);

    /**
     * Supprimer un type.
     * @param typeId
     * @return true si la suppression s'est bien passée, faux sinon.
     */
    boolean deleteType(final Long typeId);

    /**
     * Liste tous les types.
     * @return <code>List<Type></code>
     */
    List<Type> getAllTypes();
    
    /**
     * Lier des entités à un type.
     * @param typeId
     * @param entitiesIds
     */
    void addEntitiesToType(final Long typeId, final List<Long> entitiesIds);
    
    /**
     * Supprime a des entités un type, mais en vérifiant 
     * qu'aucune des catégories des entités n'aient encore le type de lié.
     * @param typeId
     * @param entitiesIds
     */
    void deleteEntitiesToType(final Long typeId, final List<Long> entitiesIds);

}