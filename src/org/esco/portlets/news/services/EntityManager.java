/**
 * ESUP-Portail News - Copyright (c) 2009 ESUP-Portail consortium
 * For any information please refer to http://esup-helpdesk.sourceforge.net
 * You may obtain a copy of the licence at http://www.esup-portail.org/license/
 */
package org.esco.portlets.news.services;

import java.util.List;
import java.util.Map;

import org.esco.portlets.news.domain.Entity;
import org.esco.portlets.news.domain.Filter;
import org.esco.portlets.news.domain.FilterType;
import org.esco.portlets.news.domain.Type;

/**
 * Interface d'une entité.
 * @author GIP RECIA - Gribonvald Julien
 * 3 févr. 2010
 */
public interface EntityManager {

    /**
     * Sauvegarder une entité.
     * @param entity
     */
    void saveEntity(final Entity entity);

    /**
     * Obtenir une entité à partir de son id.
     * @param entityId 
     * @return <code>Entity</code>
     */
    Entity getEntityById(final Long entityId);

    /**
     * Supprimer une entité, les catégories, les thèmes et les annonces associées.
     * @param entityId
     * @return true si la suppression s'est bien passée, faux sinon.
     */
    boolean deleteEntity(final Long entityId);

    /**
     * Lister les entity dans lesquelles l'utilisateurs à les droits d'accès.
     * @param uid
     * @return <code>List<Entity></code>
     */
    List<Entity> getEntitiesByUser(final String uid);
    
    /**
     * Lister les entities par leur type.
     * @param typeId Identifiant du type.
     * @return <code>List<Entity></code>
     */
    List<Entity> getEntitiesByType(final Long typeId);

    /**
     * Lister les types disponibles pour les catégories de l'entité.
     * @param entityId
     * @return <code>List<Type></code>
     */
    List<Type> getAutorizedTypesOfEntity(final Long entityId);

    /**
     * Lier des types à une entité.
     * @param typeIds
     * @param entityId
     */
    void addAutorizedTypesOfEntity(final List<Long> typeIds, final Long entityId);

    /**
     * Supprime les types disponibles pour les catégories de l'entité. Et place a default les types utilisés.
     * @param typeIds
     * @param entityId
     */
    void deleteAutorizedTypesOfEntity(final List<Long> typeIds, final Long entityId);
    
    /**
     * Add search filters to the entity.
     * @param filter A list of filters.
     */
    void addFilterToEntity(final Filter filter);
    
    /**
     * update search filter to the entity.
     * @param filter A filter.
     */
    void updateFilterToEntity(final Filter filter);
    
    /**
     * Delete a Filter of an Entity.
     * @param filterId The filter id.
     * @param entityId The entity id.
     * @return <code>boolean</code>
     */
    boolean deleteFilterOfEntity(final Long filterId, final Long entityId);
    
    /**
     * Returns the filter from id.
     * @param filterId 
     * @return <code>Filter</code>
     */
    Filter getFilter(final Long filterId);
    
    /**
     * Returns the list of filters associated to an Entity.
     * @param entityId The entity id.
     * @return <code>List<Filter></code>
     */
    List<Filter> getFiltersOfEntity(final Long entityId);
    
    /**
     * Returns the list of filters associated to an Entity and classed by type.
     * @param entityId The entity id.
     * @return <code>List<Filter></code>
     */
    Map<FilterType, List<Filter>> getFiltersByTypeOfEntity(final Long entityId);

}