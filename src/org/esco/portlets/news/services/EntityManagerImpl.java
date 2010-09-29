/**
 * ESUP-Portail News - Copyright (c) 2009 ESUP-Portail consortium
 * For any information please refer to http://esup-helpdesk.sourceforge.net
 * You may obtain a copy of the licence at http://www.esup-portail.org/license/
 */

package org.esco.portlets.news.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esco.portlets.news.dao.EntityDAO;
import org.esco.portlets.news.dao.EscoUserDao;
import org.esco.portlets.news.dao.FilterDAO;
import org.esco.portlets.news.dao.TypeDAO;
import org.esco.portlets.news.domain.Entity;
import org.esco.portlets.news.domain.Filter;
import org.esco.portlets.news.domain.FilterType;
import org.esco.portlets.news.domain.Type;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.uhp.portlets.news.NewsConstants;
import org.uhp.portlets.news.dao.CategoryDao;
import org.uhp.portlets.news.dao.SubscriberDao;
import org.uhp.portlets.news.domain.Category;
import org.uhp.portlets.news.domain.UserRole;
import org.uhp.portlets.news.service.exception.UnauthorizedException;

/**
 * Service pour une entity.
 * @author GIP RECIA - Gribonvald Julien
 * 10 déc. 2009
 */
@Service("entityManager")
@Transactional(readOnly = true) 
public class EntityManagerImpl implements EntityManager, InitializingBean  {

    /** Logger. */
    private static final Log LOG = LogFactory.getLog(EntityManagerImpl.class);

    /** Dao des Entity. */
    @Autowired
    private EntityDAO entityDao;
    /** Dao des Categories. */
    @Autowired
    private CategoryDao catDao;
    /** Dao des types.*/
    @Autowired
    private TypeDAO typeDao;
    /** Dao of filters.*/
    @Autowired
    private FilterDAO filterDao;
    /** Dao des user. */
    @Autowired  
    private EscoUserDao userDao;
    /** Dao des subscriber. */
    @Autowired  
    private SubscriberDao subDao;

    /**
     * Constructeur de l'objet EntityManagerImpl.java.
     */
    public EntityManagerImpl() {
        super();
    }

    /**
     * Sauvegarder une entité.
     * @param entity
     * @see org.esco.portlets.news.services.EntityManager#saveEntity(org.esco.portlets.news.domain.Entity)
     */
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public void saveEntity(final Entity entity) {
        if (entity == null) {   
            throw new IllegalArgumentException("Can't save an entity when given a null Entity instance.");
        } 
        try {
            this.entityDao.saveEntity(entity);
        } catch (DataIntegrityViolationException dive) {
            LOG.error("Could not save entity, duplicate entity id or name ??", dive);
            throw dive;
        } catch (DataAccessException e) {
            LOG.error("Could not save entity " + e.getLocalizedMessage(), e);
            throw e;
        }
    }
    
    /**
     * Obtenir une entité à partir de son id.
     * @param entityId
     * @return <code>Entity</code>
     * @see org.esco.portlets.news.services.EntityManager#getEntityById(java.lang.Long)
     */
    public Entity getEntityById(final Long entityId) {        
        try {
            return this.entityDao.getEntityById(entityId);    
        } catch (DataAccessException e) {
            LOG.error("Get Entity By Id error : " + e.getLocalizedMessage());
            throw e;
        }
    }
    
    /**
     * Supprimer une entité si aucune actagéorie n'y est liée.
     * @param entityId
     * @return true si la suppression s'est bien passée, faux sinon.
     * @see org.esco.portlets.news.services.EntityManager#deleteEntity(java.lang.Long)
     */
    @Transactional(readOnly = false)
    public boolean deleteEntity(final Long entityId) {
        try {   
            List<UserRole> lists = this.userDao.getUsersRolesForCtx(entityId, NewsConstants.CTX_E);
            if (this.entityDao.deleteEntityById(entityId)) {
                this.userDao.removeUsersRoleForCtx(entityId, NewsConstants.CTX_E);
                if (lists != null && !lists.isEmpty()) {
                    for (UserRole ur : lists) {
                        if (!this.userDao.isSuperAdmin(ur.getPrincipal()) 
                                && !this.userDao.userRoleExist(ur.getPrincipal())) {
                            this.userDao.deleteUser(ur.getPrincipal(), false);
                        }
                    }
                }
                this.subDao.deleteAllSubscribersByCtxId(entityId, NewsConstants.CTX_E);
                return true;
            } 
            return false;
        } catch (DataAccessException e) {
            LOG.error("Delete Entity error : " + e.getLocalizedMessage());
            throw e;
        }
    }

    /**
     * Lister les entity dans lesquelles l'utilisateurs à les droits d'accès.
     * @param uid
     * @return <code>List<Entity></code>
     * @see org.esco.portlets.news.services.EntityManager#getEntitiesByUser(java.lang.String)
     */
    public List<Entity> getEntitiesByUser(final String uid) {     
        List<Entity> entities = null;
        try {
            if (this.userDao.isSuperAdmin(uid))  {
                entities  = this.entityDao.getAllEntities();            
            } else {
                entities = this.entityDao.getEntitiesByUser(uid);
            }
        } catch (DataAccessException e) {
            LOG.error("Get Entites By User Error msg : " + e.getLocalizedMessage());
            throw e;
        }
        return entities;
    }
    
    /**
     * Lister les entities par leur type.
     * @param typeId Identifiant du type.
     * @return <code>List<Entity></code>
     * @see org.esco.portlets.news.services.EntityManager#getEntitiesByType(java.lang.Long)
     */
    public List<Entity> getEntitiesByType(final Long typeId) {     
        List<Entity> entities = null;
        try {            
            entities = this.entityDao.getEntitiesByType(typeId);
        } catch (DataAccessException e) {
            LOG.error("Get Entites By Type Error msg : " + e.getLocalizedMessage());
            throw e;
        }
        return entities;
    }
    
    /**
     * Lister les types disponibles pour les catégories de l'entité.
     * @param entityId
     * @return <code>List<Type></code>
     * @see org.esco.portlets.news.services.EntityManager#getAutorizedTypesOfEntity(java.lang.Long)
     */
    public List<Type> getAutorizedTypesOfEntity(final Long entityId) {
        List<Type> types = null;
        try {
            types = this.entityDao.getAuthorizedTypesOfEntity(entityId);
        } catch (DataAccessException e) {
            LOG.error("Get Authorized Types Of Entity Error msg : " + e.getLocalizedMessage());
            throw e;
        }
        return types;
    }
    
    /**
     * Lier des types à une entité.
     * @param typeIds
     * @param entityId
     * @see org.esco.portlets.news.services.EntityManager#addAutorizedTypesOfEntity(java.util.List, java.lang.Long)
     */
    @Transactional(readOnly = false)
    public void addAutorizedTypesOfEntity(final List<Long> typeIds, final Long entityId) {
        try {
            if (typeIds != null && !typeIds.isEmpty()) { 
                this.entityDao.addAuthorizedTypesToEntity(typeIds, entityId);
            } else if (this.getAutorizedTypesOfEntity(entityId).isEmpty()) {
                List<Long> types = new ArrayList<Long>();
                types.add(this.typeDao.getTypeByName(NewsConstants.DEFAULT_TYPE).getTypeId());
                this.entityDao.addAuthorizedTypesToEntity(types, entityId);
            }
        } catch (DataAccessException e) {
            LOG.error("Add Authorized Types Of Entity Error msg : " + e.getLocalizedMessage());
            throw e;
        }
        
    }
    
    /**
     * Supprime les types disponibles pour les entités, mais en vérifiant 
     * qu'aucune des catégories de l'entité n'aient encore le type de lié.
     * @param typeIds
     * @param entityId
     * @see org.esco.portlets.news.services.EntityManager#deleteAutorizedTypesOfEntity(java.util.List, java.lang.Long)
     */
    @Transactional(readOnly = false)
    public void deleteAutorizedTypesOfEntity(final List<Long> typeIds, final Long entityId) {
        try {
            Type defaultType = this.typeDao.getTypeByName(NewsConstants.DEFAULT_TYPE);
            List<Long> type = new ArrayList<Long>();
            typeIds.remove(defaultType.getTypeId());
            
            boolean b = true;
            for (Long typeId : typeIds) {
                List<Category> cats = this.catDao.getCategoryByTypeOfEntity(typeId, entityId);
                if (cats == null || cats.isEmpty()) {
                    type.add(typeId);
                } else {
                    b = false;
                }
            }
            if (!type.isEmpty()) {
                this.entityDao.deleteAuthorizedTypesToEntity(typeIds, entityId);
            } 
            if (!b) {
                String msg = "Delete of some types unauthorized, somes are always attached to Categories.";
                LOG.error(msg);
                throw new UnauthorizedException(msg);
            }
        } catch (DataAccessException e) {
            LOG.error("Delete Authorized Types Of Entity error : " + e.getLocalizedMessage());
            throw e;
        }
    }
    
    /**
     * Add search filter to the entity.
     * @param filter A filter.
     * @throws DataAccessException
     */
    @Transactional(readOnly = false)
    public void addFilterToEntity(final Filter filter) {
        try {
            if (filter != null) {
                if (!this.filterDao.isFilterOnEntityExist(filter)) {
                    this.filterDao.saveFilterOfEntity(filter);
                } else {
                    LOG.error("Add Filter to Entity error : A same filter already exist.");
                    throw new IllegalArgumentException("A same filter already exist.");
                }
            }
        } catch (DataAccessException e) {
            LOG.error("Add Filter to Entity error : " + e.getLocalizedMessage());
            throw e;
        }
    }
    
    /**
     * update search filter to the entity.
     * @param filter A filter.
     * @throws DataAccessException
     */
    @Transactional(readOnly = false)
    public void updateFilterToEntity(final Filter filter) {
        try {
            if (filter != null) {
                if (!this.filterDao.isFilterOnEntityExist(filter)) {
                    this.filterDao.saveFilterOfEntity(filter);
                } else {
                    LOG.error("update Filter to Entity error : A same filter already exist "
                            + "or try to update a filter not modified.");
                    throw new IllegalArgumentException("A same filter already exist.");
                }
            } 
        } catch (DataAccessException e) {
            LOG.error("update Filter to Entity error : " + e.getLocalizedMessage());
            throw e;
        }
    }
    
    /**
     * Delete a Filter of an Entity.
     * @param filterId The filter id.
     * @param entityId The entity id.
     * @return <code>boolean</code>
     */
    @Transactional(readOnly = false)
    public boolean deleteFilterOfEntity(final Long filterId, final Long entityId) {
        try {
            return this.filterDao.deleteFilterOfEntity(filterId, entityId);
        } catch (DataAccessException e) {
            LOG.error("Delete Filter of Entity error : " + e.getLocalizedMessage());
            throw e;
        }
    }
    
    /**
     * Returns the filter from id.
     * @param filterId 
     * @return <code>Filter</code>
     */
    public Filter getFilter(final Long filterId) {
        try {
            return this.filterDao.getFilterById(filterId);
        } catch (DataAccessException e) {
            LOG.error("Get Filter error : " + e.getLocalizedMessage());
            throw e;
        }
    }
    
    /**
     * Returns the list of filters associated to an Entity.
     * @param entityId The entity id.
     * @return <code>List<Filter></code>
     */
    public List<Filter> getFiltersOfEntity(final Long entityId) {
        try {
            return this.filterDao.getFiltersOfEntity(entityId);
        } catch (DataAccessException e) {
            LOG.error("Get Filters of Entity error : " + e.getLocalizedMessage());
            throw e;
        }
    }
    
    /**
     * Returns the list of filters associated to an Entity and classed by type.
     * @param entityId The entity id.
     * @return <code>List<Filter></code>
     */
    public Map<FilterType, List<Filter>> getFiltersByTypeOfEntity(final Long entityId) {
        try {
            Map<FilterType, List<Filter>> map = new HashMap<FilterType, List<Filter>>();
            for (FilterType ft : FilterType.values()) {
                map.put(ft, this.filterDao.getFiltersOfTypeOfEntity(entityId, ft));
            }
            return map;
        } catch (DataAccessException e) {
            LOG.error("Get Filters of Entity error : " + e.getLocalizedMessage());
            throw e;
        }
    }

    /**
     * Getter du membre entityDao.
     * @return <code>EntityDAO</code> le membre entityDao.
     */
    public EntityDAO getEntityDao() {
        return entityDao;
    }

    /**
     * Setter du membre entityDao.
     * @param entityDao la nouvelle valeur du membre entityDao. 
     */
    public void setEntityDao(final EntityDAO entityDao) {
        this.entityDao = entityDao;
    }

    /**
     * Getter du membre userDao.
     * @return <code>EscoUserDao</code> le membre userDao.
     */
    public EscoUserDao getUserDao() {
        return userDao;
    }

    /**
     * Setter du membre userDao.
     * @param userDao la nouvelle valeur du membre userDao. 
     */
    public void setUserDao(final EscoUserDao userDao) {
        this.userDao = userDao;
    }

    /**
     * Getter du membre catDao.
     * @return <code>CategoryDao</code> le membre catDao.
     */
    public CategoryDao getCatDao() {
        return catDao;
    }

    /**
     * Setter du membre catDao.
     * @param catDao la nouvelle valeur du membre catDao. 
     */
    public void setCatDao(final CategoryDao catDao) {
        this.catDao = catDao;
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

    /**
     * Getter du membre filterDao.
     * @return <code>FilterDAO</code> le membre filterDao.
     */
    public FilterDAO getFilterDao() {
        return filterDao;
    }

    /**
     * Setter du membre filterDao.
     * @param filterDao la nouvelle valeur du membre filterDao. 
     */
    public void setFilterDao(final FilterDAO filterDao) {
        this.filterDao = filterDao;
    }

    /**
     * Getter du membre subDao.
     * @return <code>SubscriberDao</code> le membre subDao.
     */
    public SubscriberDao getSubDao() {
        return subDao;
    }

    /**
     * Setter du membre subDao.
     * @param subDao la nouvelle valeur du membre subDao. 
     */
    public void setSubDao(final SubscriberDao subDao) {
        this.subDao = subDao;
    }

    /**
     * @throws Exception
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    public void afterPropertiesSet() throws Exception {
        final String notNull = " must not be null.";
        Assert.notNull(this.getUserDao(), "The property userDao in class " 
                + getClass().getSimpleName() + notNull);
        Assert.notNull(this.getEntityDao(), "The property entityDao in class " 
                + getClass().getSimpleName() + notNull);
        Assert.notNull(this.getCatDao(), "The property catDao in class " 
                + getClass().getSimpleName() + notNull);
        Assert.notNull(this.getTypeDao(), "The property typeDao in class " 
                + getClass().getSimpleName() + notNull);
        Assert.notNull(this.getFilterDao(), "The property filterDao in class " 
                + getClass().getSimpleName() + notNull);
        Assert.notNull(this.getSubDao(), "The property subDao in class " 
                + getClass().getSimpleName() + notNull);
    }

}
