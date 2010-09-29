/**
* ESUP-Portail News - Copyright (c) 2009 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esco.portlets.news.dao.iBatis;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esco.portlets.news.dao.EntityDAO;
import org.esco.portlets.news.domain.Entity;
import org.esco.portlets.news.domain.Type;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.stereotype.Repository;
import org.uhp.portlets.news.NewsConstants;
import org.uhp.portlets.news.dao.Constants;

/**
 * Implémentation de la Dao.
 * @author GIP RECIA - Gribonvald Julien
 * 8 déc. 2009
 */
@Repository("entityDao")
public class EntityDAOImpl extends SqlMapClientDaoSupport implements EntityDAO {
    
    /** Logger. */
    private static final Log LOG = LogFactory.getLog(EntityDAOImpl.class);
    
    /** Constructeur.  */
    public EntityDAOImpl() {
        super();
    }

    /**
     * Obtenir une entité à partir de son id.
     * @param entityId Id de l'entité recherchée.
     * @return <code>Entity</code> Entité trouvée.
     */
    public Entity getEntityById(final Long entityId) {
        final Entity e = (Entity) getSqlMapClientTemplate().queryForObject("selectEntityById", entityId);
        if (e == null) {
            LOG.error("entity [" + entityId + "] not found");
            throw new ObjectRetrievalFailureException(Entity.class, entityId);
        }
        return e;
    }
    
    /**
     * Permet de savoir si une entité peut être supprimée. 
     * Cela est possible quand aucune catégorie n'est liée à cette entité.
     * @param entityId
     * @return <code>boolean</code> vrai si cela est possible faux sinon.
     * @throws DataAccessException
     */
    public boolean canDeleteEntity(final Long entityId) throws DataAccessException {
        Integer t = (Integer) getSqlMapClientTemplate().queryForObject("canDeleteEntityForCategory", entityId);
        return t.intValue() < 1;
    }

    /**
     * Delete une Entity.
     * @param entityId Identifiant de l'entité à supprimer.
     * @return <code>boolean</code> True si supprimé, false sinon ou si non trouvé.
     * @throws DataAccessException 
     */
    public boolean deleteEntityById(final Long entityId) throws DataAccessException {  
        if (this.canDeleteEntity(entityId)) {
            getSqlMapClientTemplate().delete("deleteAllAuthorizedTypeOnEntity", entityId);
            getSqlMapClientTemplate().delete("deleteAllFilterOnEntity", entityId);
            int i = getSqlMapClientTemplate().delete("deleteEntityById", entityId);
            return i > 0;
        }
        return false;
    }

    /**
     * Mettre à jour complètement l'entité.
     * @param entity L'entité à mettre à jour.
     * @throws DataAccessException 
     */
    private void updateEntityById(final Entity entity) throws DataAccessException {
        getSqlMapClientTemplate().update("updateEntityById", entity);
    }

    /**
     * Ajoute une nouvelle entité avec gestion automatique de l'identifiant.
     * @param entity L'entité à ajouter.
     * @throws DataAccessException 
     */
    private void insertEntity(final Entity entity) throws DataAccessException {
        entity.setCreationDate(Calendar.getInstance().getTime());
        getSqlMapClientTemplate().insert("insertEntity", entity);
    }
    
    /**
     * Ajoute ou mets à jour une entité.
     * @param entity L'entité à sauvegarder.
     * @throws DataAccessException
     * @see org.esco.portlets.news.dao.EntityDAO#saveEntity(org.esco.portlets.news.domain.Entity)
     */
    public void saveEntity(final Entity entity) throws DataAccessException {
        if (entity.getEntityId() == null) {
            insertEntity(entity);
        } else {
            updateEntityById(entity);
        }
    }
    
    /**
     * Lie une entité à une liste de type.
     * @param typeIds
     * @param entityId
     * @throws DataAccessException
     * @see org.esco.portlets.news.dao.EntityDAO#addAuthorizedTypesToEntity(java.util.List, java.lang.Long)
     */
    public void addAuthorizedTypesToEntity(final List<Long> typeIds, final Long entityId) throws DataAccessException {
        for (Long tId : typeIds) {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put(NewsConstants.TYPE_ID, tId );
            params.put(NewsConstants.ENTITY_ID, entityId);
            try {
                getSqlMapClientTemplate().insert("insertOneAuthorizedTypeOfEntity", params);
            } catch (DataAccessException e) {
                LOG.warn("EntityDaoImpl:: insertOneAuthorizedTypeOfEntity : Error : " + e.getMessage()); 
                throw e;
            }
        }
    }

    /**
     * Supprime le lien entre une entité et une liste de type.
     * @param typeIds
     * @param entityId
     * @throws DataAccessException
     * @see org.esco.portlets.news.dao.EntityDAO#deleteAuthorizedTypesToEntity(java.util.List, java.lang.Long)
     */
    public void deleteAuthorizedTypesToEntity(final List<Long> typeIds, final Long entityId) 
    throws DataAccessException {
        for (Long tId : typeIds) {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put(NewsConstants.TYPE_ID, tId );
            params.put(NewsConstants.ENTITY_ID, entityId);
            try {
                getSqlMapClientTemplate().delete("deleteOneAuthorizedTypeOnEntity", params);
            } catch (DataAccessException e) {
                LOG.warn("EntityDaoImpl:: deleteOneAuthorizedTypeOnEntity : Error : " + e.getMessage()); 
                throw e;
            }
        }
    }

    /**
     * Liste des entités.
     * @return <code>List<Entity></code>
     * @throws DataAccessException
     * @see org.esco.portlets.news.dao.EntityDAO#getAllEntities()
     */
    @SuppressWarnings("unchecked")
    public List<Entity> getAllEntities() throws DataAccessException {
        return getSqlMapClientTemplate().queryForList("getAllEntity");
    }
    
    /**
     * Liste les entités autorisées à un utilisateur.
     * @param uid Identifiant de l'utilisateur.
     * @return <code>List<Entity></code>
     * @throws DataAccessException
     */
    @SuppressWarnings("unchecked")
    public List<Entity> getEntitiesByUser(final String uid) throws DataAccessException {
        return getSqlMapClientTemplate().queryForList("getEntityByUser", uid);
    }
    
    /**
     * Liste les entités autorisées à un type.
     * @param typeId Identifiant du type.
     * @return <code>List<Entity></code>
     * @throws DataAccessException
     */
    @SuppressWarnings("unchecked")
    public List<Entity> getEntitiesByType(final Long typeId) throws DataAccessException {
        return getSqlMapClientTemplate().queryForList("getEntitiesByType", typeId);
    }

    /**
     * Liste les types de l'entité.
     * @param entityId
     * @return <code>List<Type></code>
     * @throws DataAccessException
     * @see org.esco.portlets.news.dao.EntityDAO#getAuthorizedTypesOfEntity(java.lang.Long)
     */
    @SuppressWarnings("unchecked")
    public List<Type> getAuthorizedTypesOfEntity(final Long entityId) throws DataAccessException {
        return getSqlMapClientTemplate().queryForList("getAuthorizedTypeListByEntity", entityId);
    }

    /**
     * Permet de savoir si le nom d'une entité existe déjà.
     * @param name
     * @param id Identifiant de l'entité à ne pas inclure dans la vérification. (optionnel peut être mis à null)
     * @return <code>boolean</code> vrai si existe déjà, faux sinon.
     * @throws DataAccessException
     * @see org.esco.portlets.news.dao.EntityDAO#isEntityNameExist(java.lang.String, java.lang.Long)
     */
    public boolean isEntityNameExist(final String name, final Long id) throws DataAccessException {
        if (id == null || id < 1) {
            return ((Integer) getSqlMapClientTemplate().queryForObject("existEntityName", name)) > 0;
        }
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(Constants.NAME, name);   
        params.put(Constants.ID, id);
        return ((Integer) getSqlMapClientTemplate().queryForObject("sameEntityNameExist", params)) > 0;
    }

}