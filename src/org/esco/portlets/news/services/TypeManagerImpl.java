/**
 * ESUP-Portail News - Copyright (c) 2009 ESUP-Portail consortium
 * For any information please refer to http://esup-helpdesk.sourceforge.net
 * You may obtain a copy of the licence at http://www.esup-portail.org/license/
 */
package org.esco.portlets.news.services;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esco.portlets.news.dao.TypeDAO;
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
import org.uhp.portlets.news.domain.Category;
import org.uhp.portlets.news.service.exception.UnauthorizedException;

/**
 * Manager des types.
 * @author GIP RECIA - Gribonvald Julien
 * 3 févr. 2010
 */
@Service("typeManager")
@Transactional(readOnly = true) 
public class TypeManagerImpl implements InitializingBean, TypeManager {
    
    /** Logger. */
    private static final Log LOG = LogFactory.getLog(TypeManagerImpl.class);
    
    /** Dao des Types. */
    @Autowired
    private TypeDAO typeDao;
    
    /** Dao des Categories. */
    @Autowired
    private CategoryDao catDao;

    /**
     * Constructeur de l'objet TypeManagerImpl.java.
     */
    public TypeManagerImpl() {
        super();
    }
    
    /**
     * Sauvegarder un type.
     * @param type
     */
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public void saveType(final Type type) {
        if (type == null) {   
            throw new IllegalArgumentException("Can't save a type when given a null type instance.");
        } 
        try {
            this.typeDao.saveType(type);
        } catch (DataIntegrityViolationException dive) {
            LOG.error("Could not save entity, duplicate entity id or name ??", dive);
            throw dive;
        } catch (DataAccessException e) {
            LOG.error("Could not save entity " + e.getLocalizedMessage(), e);
            throw e;
        }
    }
    
    /**
     * Obtenir un type à partir de son id.
     * @param typeId
     * @return <code>Type</code>
     */
    public Type getTypeById(final Long typeId) {        
        try {
            return this.typeDao.getTypeById(typeId);    
        } catch (DataAccessException e) {
            LOG.error("Get Type By Id error : " + e.getLocalizedMessage());
            throw e;
        }
    }
    
    /**
     * Ajouter un type associé à une liste d'entités.
     * @param type
     * @param entitiesIds
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addType(final Type type, final List<Long> entitiesIds) {
        this.saveType(type);  
        if (entitiesIds != null && !entitiesIds.isEmpty()) {
            this.addEntitiesToType(type.getTypeId(), entitiesIds);
        }
    }
    
    /**
     * Supprimer un type.
     * @param typeId
     * @return true si la suppression s'est bien passée, faux sinon.
     */
    @Transactional(readOnly = false)
    public boolean deleteType(final Long typeId) {
        try {   
            Type t = this.typeDao.getTypeById(typeId);
            if (t.equals(typeDao.getTypeByName(NewsConstants.DEFAULT_TYPE))) {
                throw new IllegalArgumentException("This isn't authorized to delete the default type.");
            }
            return this.typeDao.deleteTypeById(typeId);
        } catch (DataAccessException e) {
            LOG.error("Delete Type error : " + e.getLocalizedMessage());
            throw e;
        }
    }
    
    /**
     * Liste tous les types.
     * @return <code>List<Type></code>
     */
    public List<Type> getAllTypes() {
        return this.typeDao.getAllTypes();
    }
    
    /**
     * Lier des entités à un type.
     * @param typeId
     * @param entitiesIds
     * @see org.esco.portlets.news.services.TypeManager#addEntitiesToType(java.lang.Long, java.util.List)
     */
    @Transactional(readOnly = false)
    public void addEntitiesToType(final Long typeId, final List<Long> entitiesIds) {
        try {
            this.typeDao.addEntitiesToType(typeId, entitiesIds);
        } catch (DataAccessException e) {
            LOG.error("Add Entities to Type Error msg : " + e.getLocalizedMessage());
            throw e;
        }
        
    }
    
    /**
     * Supprime a des entités un type, mais en vérifiant 
     * qu'aucune des catégories des entités n'aient encore le type de lié.
     * @param typeId
     * @param entitiesIds
     * @see org.esco.portlets.news.services.TypeManager#deleteEntitiesToType(java.lang.Long, java.util.List)
     */
    @Transactional(readOnly = false)
    public void deleteEntitiesToType(final Long typeId, final List<Long> entitiesIds) {
        try {
            List<Long> entities = new ArrayList<Long>();
            
            boolean b = true;
            for (Long eId : entitiesIds) {
                List<Category> cats = this.catDao.getCategoryByTypeOfEntity(typeId, eId);
                if (cats == null || cats.isEmpty()) {
                    entities.add(eId);
                } else {
                    b = false;
                }
            }
            if (!entities.isEmpty()) {
                this.getTypeDao().deleteEntitiesToType(typeId, entities);
            } 
            if (!b) {
                String msg = "Delete unauthorized, the type is always attached to some Categories of Entities.";
                LOG.error(msg);
                throw new UnauthorizedException(msg);
            }
        } catch (DataAccessException e) {
            LOG.error("Delete Entities to Type error : " + e.getLocalizedMessage());
            throw e;
        }
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
     * @throws Exception
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(this.getTypeDao(), "The property typeDao in class " 
                + getClass().getSimpleName() + " must not be null.");
        Assert.notNull(this.getCatDao(), "The property catDao in class " 
                + getClass().getSimpleName() + " must not be null.");
    }
    
    
}
