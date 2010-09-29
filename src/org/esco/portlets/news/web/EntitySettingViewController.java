/**
 * ESUP-Portail News - Copyright (c) 2009 ESUP-Portail consortium
 * For any information please refer to http://esup-helpdesk.sourceforge.net
 * You may obtain a copy of the licence at http://www.esup-portail.org/license/
 */
package org.esco.portlets.news.web;

import java.util.ArrayList;
import java.util.List;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.esco.portlets.news.domain.Entity;
import org.esco.portlets.news.services.EntityManager;
import org.esco.portlets.news.services.TypeManager;
import org.esco.portlets.news.services.UserManager;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.util.Assert;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.mvc.AbstractController;
import org.uhp.portlets.news.NewsConstants;
import org.uhp.portlets.news.domain.RolePerm;
import org.uhp.portlets.news.util.HostUtils;
import org.uhp.portlets.news.web.support.Constants;

/**
 * Controller.
 * @author GIP RECIA - Gribonvald Julien
 * 31 mars 2010
 */
public class EntitySettingViewController extends AbstractController implements InitializingBean {

    /** Manager d'une Entity. */
    @Autowired
    private EntityManager em;
    /** Manager d'un type. */
    @Autowired
    private TypeManager tm;
    /** Manager des Users. */
    @Autowired
    private UserManager um;
	
	/**
     * Constructeur de l'objet ShowCategorySettingController.java.
     */
    public EntitySettingViewController() {
        super();
    }

    /**
	 * @param request
	 * @param response
	 * @return <code>ModelAndView</code>
	 * @throws Exception
	 * @see org.springframework.web.portlet.mvc.AbstractController#
	 * handleRenderRequestInternal(javax.portlet.RenderRequest, javax.portlet.RenderResponse)
	 */
	@Override
	protected ModelAndView handleRenderRequestInternal(final RenderRequest request, final RenderResponse response) 
	throws Exception {
	    Long id = Long.valueOf(request.getParameter(Constants.ATT_ENTITY_ID));
		Entity entity = this.getEm().getEntityById(id);
		if (entity == null) {
			throw new ObjectRetrievalFailureException(Entity.class, "Entity does not exist.");
		}
		List<String> usersUid = new ArrayList<String>();
        usersUid.add(entity.getCreatedBy());
		ModelAndView mav = new ModelAndView(Constants.ACT_VIEW_E_SETTING);
		mav.addObject(Constants.OBJ_ENTITY, entity);
		mav.addObject(Constants.ATT_TYPE_LIST, this.getEm().getAutorizedTypesOfEntity(entity.getEntityId()));
		mav.addObject(Constants.ATT_PM, RolePerm.valueOf(this.um.getUserRoleInCtx(
		        entity.getEntityId(), NewsConstants.CTX_E, request.getRemoteUser())).getMask());
		mav.addObject(Constants.ATT_USER_LIST, this.um.getUsersByListUid(usersUid));
		 // Usefull for xml and opm links
        mav.addObject(Constants.ATT_PORTAL_URL,  HostUtils.getHostUrl(request));
		return mav;					
	}

    /**
     * Getter du membre um.
     * @return <code>UserManager</code> le membre um.
     */
    public UserManager getUm() {
        return um;
    }

    /**
     * Setter du membre um.
     * @param um la nouvelle valeur du membre um. 
     */
    public void setUm(final UserManager um) {
        this.um = um;
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
     * @throws Exception
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(this.getTm(), "The property TypeManager tm in class " 
                + getClass().getSimpleName() + " must not be null.");
        Assert.notNull(this.getUm(), "The property UserManager um in class " 
                + getClass().getSimpleName() + " must not be null.");
        Assert.notNull(this.getEm(), "The property EntityManager em in class " 
                + getClass().getSimpleName() + " must not be null.");
    }

	
}
