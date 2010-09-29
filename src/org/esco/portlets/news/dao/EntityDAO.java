/**
* ESUP-Portail News - Copyright (c) 2009 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/

package org.esco.portlets.news.dao;

import java.util.List;

import org.esco.portlets.news.domain.Entity;
import org.esco.portlets.news.domain.Type;
import org.springframework.dao.DataAccessException;

/**
 * Dao de l'Entity.
 * @author GIP RECIA - Gribonvald Julien
 * 2 déc. 2009
 */
public interface EntityDAO {
    
    /**
     * Obtenir une entité à partir de son id.
     * @param entityId Id de l'entité recherchée.
     * @return <code>Entity</code> Entité trouvée.
     */
    Entity getEntityById(final Long entityId);

    /**
     * Delete une Entity.
     * @param entityId Identifiant de l'entité à supprimer.
     * @return <code>boolean</code> True si supprimé, false sinon ou si non trouvé.
     * @throws DataAccessException 
     */
    boolean deleteEntityById(final Long entityId) throws DataAccessException;
    
    /**
     * Permet de savoir si une entité peut être supprimée. 
     * Cela est possible quand aucune catégorie n'est liée à cette entité.
     * @param entityId
     * @return <code>boolean</code> vrai si cela est possible faux sinon.
     * @throws DataAccessException
     */
    boolean canDeleteEntity(final Long entityId) throws DataAccessException;
    
    /**
     * Ajoute ou mets à jour une entité.
     * @param entity L'entité à ajouter.
     * @throws DataAccessException 
     */
    void saveEntity(final Entity entity) throws DataAccessException;
    
    /**
     * Liste des entités.
     * @return <code>List<Entity></code>
     * @throws DataAccessException
     */
    List<Entity> getAllEntities() throws DataAccessException;
    
    /**
     * Liste les entités autorisées à un utilisateur.
     * @param uid Identifiant de l'utilisateur.
     * @return <code>List<Entity></code>
     * @throws DataAccessException
     */
    List<Entity> getEntitiesByUser(final String uid) throws DataAccessException;
    
    /**
     * Liste les entités autorisées à un type.
     * @param typeId Identifiant du type.
     * @return <code>List<Entity></code>
     * @throws DataAccessException
     */
    List<Entity> getEntitiesByType(final Long typeId) throws DataAccessException;
    
    /** Obtenir la Liste des types de l'entité. 
     * @param entityId
     * @return <code>List<Type></code>
     * @throws DataAccessException */
    List<Type> getAuthorizedTypesOfEntity(final Long entityId) throws DataAccessException;

    /**
     * Lie une entité à une liste de type.
     * @param typeIds
     * @param entityId 
     * @throws DataAccessException
     */
    void addAuthorizedTypesToEntity(final List<Long> typeIds, final Long entityId) throws DataAccessException;

    /**
     * Supprime le lien entre une entité et une liste de type.
     * @param typeIds
     * @param entityId 
     * @throws DataAccessException
     */
    void deleteAuthorizedTypesToEntity(final List<Long> typeIds, final Long entityId) throws DataAccessException;
    
    /**
     * Permet de savoir si le nom d'une entité existe déjà.
     * @param name
     * @param id Identifiant de l'entité à ne pas inclure dans la vérification. (optionnel peut être mis à null)
     * @return <code>boolean</code> vrai si existe déjà, faux sinon.
     * @throws DataAccessException
     */
    boolean isEntityNameExist(final String name, final Long id) throws DataAccessException;
}