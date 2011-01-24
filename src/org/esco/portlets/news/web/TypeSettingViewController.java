/**
 * ESUP-Portail News - Copyright (c) 2009 ESUP-Portail consortium
 * For any information please refer to http://esup-helpdesk.sourceforge.net
 * You may obtain a copy of the licence at http://www.esup-portail.org/license/
 */
package org.esco.portlets.news.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esco.portlets.news.domain.Entity;
import org.esco.portlets.news.domain.Type;
import org.esco.portlets.news.services.EntityManager;
import org.esco.portlets.news.services.TypeManager;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.mvc.AbstractController;
import org.uhp.portlets.news.domain.Category;
import org.uhp.portlets.news.service.CategoryManager;
import org.uhp.portlets.news.web.support.Constants;

/**
 * Controller.
 * @author GIP RECIA - Gribonvald Julien
 * 5 févr. 2010
 */
@Controller
public class TypeSettingViewController extends AbstractController implements InitializingBean {
    
    /** Logger. */
    private static final Log LOG = LogFactory.getLog(TypeSettingViewController.class);
    
    /** The Type Manager.*/
    @Autowired 
    private TypeManager tm;
    /** The Entity Manager.*/
    @Autowired 
    private EntityManager em;
    /** The CategoryManager. */
    @Autowired
    private CategoryManager cm;

    /**
     * Constructeur de l'objet TypeSettingViewController.java.
     */
    public TypeSettingViewController() {
        super();
    }
    
    /**
     * @param request
     * @param response
     * @return <code>ModelAndView</code>
     * @throws Exception
     * @see org.springframework.web.portlet.mvc.AbstractController#
     * handleRenderRequest(javax.portlet.RenderRequest, javax.portlet.RenderResponse)
     */
    @Override
    public ModelAndView handleRenderRequest(final RenderRequest request,
            final RenderResponse response) throws Exception {
        Long id = Long.valueOf(request.getParameter(Constants.ATT_TYPE_ID));
        if (LOG.isDebugEnabled()) {
            LOG.debug("TypeSettingViewController:: entering method handleRenderRequestInternal: type Id=" + id);
        }
        final Type type = this.getTm().getTypeById(id);
        if (type != null) {
            TypeForm typeF = new TypeForm();
            typeF.setType(type);
            List<Entity> entities = this.getEm().getEntitiesByType(id);
            List<String> entitiesIds = new ArrayList<String>();
            Map<Long, List<Category>> categoriesOfTypeInEntity = new HashMap <Long, List<Category>>();
            if (entities != null && !entities.isEmpty()) {
                for (Entity e : entities) {
                    entitiesIds.add(String.valueOf(e.getEntityId()));
                    List<Category> catList = this.cm.getCategoryByTypeOfEntity(type.getTypeId(), e.getEntityId());
                    categoriesOfTypeInEntity.put(e.getEntityId(), catList);
                }
                typeF.setEntitiesIds(entitiesIds.toArray(new String[0]));
            }
            
            ModelAndView mav = new ModelAndView(Constants.ACT_VIEW_TYPE_SETTING);
            mav.addObject(Constants.CMD_TYPE, typeF);
            mav.addObject(Constants.ATT_E_LIST, entities);
            mav.addObject(Constants.ATT_MAP_C_E, categoriesOfTypeInEntity);
            return mav;
        } 
        throw new ObjectRetrievalFailureException(Type.class, id);
    }



    /**
     * Getter du membre tm.
     * @return <code>TypeManager</code> le membre tm.
     */
    public TypeManager getTm() {
        return tm;
    }

    /**
     * Setter du membre tm.
     * @param tm la nouvelle valeur du membre tm. 
     */
    public void setTm(final TypeManager tm) {
        this.tm = tm;
    }

    /**
     * Getter du membre em.
     * @return <code>EntityManager</code> le membre em.
     */
    public EntityManager getEm() {
        return em;
    }

    /**
     * Setter du membre em.
     * @param em la nouvelle valeur du membre em. 
     */
    public void setEm(final EntityManager em) {
        this.em = em;
    }

    /**
     * @throws Exception
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(this.getTm(), "The property TypeManager tm in class " + getClass().getSimpleName()
                + " must not be null.");
        Assert.notNull(this.getEm(), "The property EntityManager em in class " + getClass().getSimpleName()
                + " must not be null.");
    }
}
