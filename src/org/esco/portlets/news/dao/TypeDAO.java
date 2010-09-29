/**
* ESUP-Portail News - Copyright (c) 2009 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esco.portlets.news.dao;

import java.util.List;

import org.esco.portlets.news.domain.Type;
import org.springframework.dao.DataAccessException;

/**
 * Dao du l'objet Type.
 * @author GIP RECIA - Gribonvald Julien
 * 8 déc. 2009
 */
public interface TypeDAO {

    /**
     * Obtenir un Type à partir de son id.
     * @param typeId Id du type recherché.
     * @return <code>Type</code> Type trouvé.
     * @throws DataAccessException 
     */
    Type getTypeById(final Long typeId) throws DataAccessException;
    
    /**
     * Permet de savoir si un type peut être supprimée. 
     * Cela est possible quand aucune catégorie n'est liée à ce type.
     * @param typeId
     * @return <code>boolean</code> vrai si cela est possible faux sinon.
     * @throws DataAccessException
     */
    boolean canDeleteType(final Long typeId) throws DataAccessException;

    /**
     * Delete un Type.
     * @param typeId Id du type a supprimer.
     * @return <code>boolean</code> True si supprimé, false sinon ou si non trouvé.
     * @throws DataAccessException 
     */
    boolean deleteTypeById(final Long typeId) throws DataAccessException;

    /**
     * Ajoute ou met à jour un type avec gestion automatique de l'identifiant.
     * @param type Le type à ajouter.
     * @throws DataAccessException 
     */
    void saveType(final Type type) throws DataAccessException;
    
    /**
     * Obtenir un Type à partir de son nom.
     * @param name Nom du type recherché.
     * @return <code>Type</code> Type trouvé.
     * @throws DataAccessException 
     */
    Type getTypeByName(final String name) throws DataAccessException;
    
    /**
     * Liste des types.
     * @return <code>List<Type></code>
     * @throws DataAccessException
     */
    List<Type> getAllTypes() throws DataAccessException;
    
    /**
     * Permet de savoir si le nom d'un type existe déjà.
     * @param name Nom du type à tester.
     * @param id Identifiant du type à ne pas inclure dans la vérification.(optionnel peut être mis à null)
     * @return <code>boolean</code>
     * @throws DataAccessException
     */
    boolean isTypeNameExist(final String name, final Long id) throws DataAccessException;
    
    /**
     * Lie des entités à un type.
     * @param typeId
     * @param entitiesIds
     * @throws DataAccessException
     */
    void addEntitiesToType(final Long typeId, final List<Long> entitiesIds) throws DataAccessException;
    
    /**
     * Supprime le lien entre une liste d'entité et un type.
     * @param typeId
     * @param entitiesIds
     * @throws DataAccessException
     */
    void deleteEntitiesToType(final Long typeId, final List<Long> entitiesIds) throws DataAccessException;
}