/**
* ESUP-Portail News - Copyright (c) 2009 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esco.portlets.news.dao.iBatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esco.portlets.news.dao.TypeDAO;
import org.esco.portlets.news.domain.Type;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.stereotype.Repository;
import org.uhp.portlets.news.NewsConstants;
import org.uhp.portlets.news.dao.Constants;

/**
 * Implémentation de la dao.
 * @author GIP RECIA - Gribonvald Julien
 * 8 déc. 2009
 */
@Repository("typeDao")
public class TypeDAOImpl extends SqlMapClientDaoSupport implements TypeDAO {
    
    /** Logger. */
    private static final Log LOG = LogFactory.getLog(TypeDAOImpl.class);

    /**
     * Constructeur.
     */
    public TypeDAOImpl() {
        super();
    }
    
    /**
     * Obtenir un Type à partir de son id.
     * @param typeId Id du type recherché.
     * @return <code>Type</code> Type trouvé.
     * @throws DataAccessException 
     */
    public Type getTypeById(final Long typeId) throws DataAccessException {
        final Type t = (Type) getSqlMapClientTemplate().queryForObject("selectTypeById", typeId);
        if (t == null) {
            LOG.error("type [" + typeId + "] not found");
            throw new ObjectRetrievalFailureException(Type.class, typeId);
        }
        return t;
    }
    
    /**
     * Permet de savoir si un type peut être supprimée. 
     * Cela est possible quand aucune catégorie ou entity n'est liée à ce type.
     * @param typeId
     * @return <code>boolean</code> vrai si cela est possible faux sinon.
     * @throws DataAccessException
     */
    public boolean canDeleteType(final Long typeId) throws DataAccessException {
        Integer t = (Integer) getSqlMapClientTemplate().queryForObject("canDeleteTypeForCategory", typeId);
        t += (Integer) getSqlMapClientTemplate().queryForObject("canDeleteTypeForEntity", typeId);
        return t.intValue() < 1;
    }

    /**
     * Delete un Type.
     * @param typeId Id du type a supprimer.
     * @return <code>boolean</code> True si supprimé, false sinon ou si non trouvé.
     * @throws DataAccessException 
     */
    public boolean deleteTypeById(final Long typeId) throws DataAccessException {
        if (canDeleteType(typeId)) {
            int rows = getSqlMapClientTemplate().delete("deleteTypeById", typeId);
            return rows > 0;
        } 
        return false;
    }

    /**
     * Ajoute un nouveau type avec gestion automatique de l'identifiant.
     * @param type Le type à ajouter.
     * @throws DataAccessException 
     */
    private void insertType(final Type type) throws DataAccessException {
        getSqlMapClientTemplate().insert("insertType", type);
    }

    /**
     * Mettre à jour complètement le type.
     * @param type 
     * @throws DataAccessException 
     */
    private void updateTypeById(final Type type) throws DataAccessException {
       getSqlMapClientTemplate().update("updateTypeById", type);
    }
    
    /**
     * Ajoute un nouveau type avec gestion automatique de l'identifiant.
     * @param type Le type à ajouter.
     * @throws DataAccessException 
     */
    public void saveType(final Type type) throws DataAccessException {
        if (type.getTypeId() == null) {
            this.insertType(type);
        } else {
            this.updateTypeById(type);
        }
    }
    
    /**
     * Obtenir un Type à partir de son nom.
     * @param name Nom du type recherché.
     * @return <code>Type</code> Type trouvé.
     * @throws DataAccessException 
     */
    public Type getTypeByName(final String name) throws DataAccessException {
        final Type t = (Type) getSqlMapClientTemplate().queryForObject("selectTypeByName", name);
        if (t == null) {
            LOG.error("type with name [" + name + "] not found");
        }
        return t;
    }
    
    /**
     * Liste des types.
     * @return <code>List<Type></code>
     * @throws DataAccessException
     */
    @SuppressWarnings("unchecked")
    public List<Type> getAllTypes() throws DataAccessException {
        return getSqlMapClientTemplate().queryForList("getAllType");
    }

    /**
     * Permet de savoir si le nom d'un type existe déjà.
     * @param name Nom du type à tester.
     * @param id Identifiant du type à ne pas inclure dans la vérification.(optionnel peut être mis à null)
     * @return <code>boolean</code>
     * @throws DataAccessException
     * @see org.esco.portlets.news.dao.TypeDAO#isTypeNameExist(java.lang.String, java.lang.Long)
     */
    public boolean isTypeNameExist(final String name, final Long id) throws DataAccessException {
        if (id == null || id < 1) {
            return ((Integer) getSqlMapClientTemplate().queryForObject("existTypeName", name)) > 0;
        }
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(Constants.NAME, name);   
        params.put(Constants.ID, id);
        return ((Integer) getSqlMapClientTemplate().queryForObject("sameTypeNameExist", params)) > 0;
    }
    
    /**
     * Lie des entités à un type.
     * @param typeId
     * @param entitiesIds
     * @throws DataAccessException
     * @see org.esco.portlets.news.dao.TypeDAO#addEntitiesToType(java.lang.Long, java.util.List)
     */
    public void addEntitiesToType(final Long typeId, final List<Long> entitiesIds) throws DataAccessException {
        for (Long eId : entitiesIds) {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put(NewsConstants.TYPE_ID, typeId );
            params.put(NewsConstants.ENTITY_ID, eId);
            try {
                getSqlMapClientTemplate().insert("insertOneAuthorizedTypeOfEntity", params);
            } catch (DataAccessException e) {
                LOG.warn("TypeDaoImpl:: insertOneAuthorizedTypeOfEntity : Error : " + e.getMessage()); 
                throw e;
            }
        }
    }

    /**
     * Supprime le lien entre une liste d'entité et un type.
     * @param typeId
     * @param entitiesIds
     * @throws DataAccessException
     * @see org.esco.portlets.news.dao.TypeDAO#deleteEntitiesToType(java.lang.Long, java.util.List)
     */
    public void deleteEntitiesToType(final Long typeId, final List<Long> entitiesIds) 
    throws DataAccessException {
        for (Long eId : entitiesIds) {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put(NewsConstants.TYPE_ID, typeId );
            params.put(NewsConstants.ENTITY_ID, eId);
            try {
                getSqlMapClientTemplate().delete("deleteOneAuthorizedTypeOnEntity", params);
            } catch (DataAccessException e) {
                LOG.warn("TypeDaoImpl:: deleteOneAuthorizedTypeOnEntity : Error : " + e.getMessage()); 
                throw e;
            }
        }
    }

}