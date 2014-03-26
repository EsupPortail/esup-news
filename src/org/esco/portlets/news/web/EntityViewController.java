/**
 * ESUP-Portail News - Copyright (c) 2009 ESUP-Portail consortium
 * For any information please refer to http://esup-helpdesk.sourceforge.net
 * You may obtain a copy of the licence at http://www.esup-portail.org/license/
 */
package org.esco.portlets.news.web;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esco.portlets.news.domain.Entity;
import org.esco.portlets.news.services.EntityManager;
import org.esco.portlets.news.services.UserManager;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.util.Assert;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.mvc.AbstractController;
import org.uhp.portlets.news.NewsConstants;
import org.uhp.portlets.news.domain.RolePerm;
import org.uhp.portlets.news.service.CategoryManager;
import org.uhp.portlets.news.util.HostUtils;
import org.uhp.portlets.news.web.support.Constants;

/**
 *
 * @author GIP RECIA - Gribonvald Julien
 * 4 mars 2010
 */
public class EntityViewController extends AbstractController implements InitializingBean {

	/** Logger. */
	private static final Log LOG = LogFactory.getLog(EntityViewController.class);

	/** The EntityManager.*/
	@Autowired
	private EntityManager em;
	/** The CategoryManager. */
	@Autowired
	private CategoryManager cm;
	/** The UserManager. */
	@Autowired
	private UserManager um;

	/**
	 * Constructeur de l'objet EntityViewController.java.
	 */
	public EntityViewController() {
		super();
	}


	/**
	 * @param request
	 * @param response
	 * @return <code>ModelAndView</code>
	 * @throws Exception
	 * @see org.springframework.web.portlet.mvc.AbstractController
	 * #handleRenderRequest(javax.portlet.RenderRequest, javax.portlet.RenderResponse)
	 */
	@Override
	public ModelAndView handleRenderRequest(final RenderRequest request,
			final RenderResponse response) throws Exception {
		final Long id = Long.valueOf(request.getParameter(Constants.ATT_ENTITY_ID));
		final String uid = request.getRemoteUser();
		if (LOG.isDebugEnabled()) {
			LOG.debug("EntityViewController:: entering method handleRenderRequest EntityId = " + id);
		}

		final Entity entity = this.em.getEntityById(id);
		if (entity == null) {
			throw new ObjectRetrievalFailureException(Entity.class, id);
		}
		ModelAndView mav = new ModelAndView(Constants.ACT_VIEW_ENTITY);
		mav.addObject(Constants.OBJ_ENTITY, entity);
		// Get all category for the user.
		mav.addObject(Constants.ATT_C_LIST,
				this.getCm().getListCategoryOfEntityByUser(uid, id));
		// Get rigths of the user in the context
		mav.addObject(Constants.ATT_PM, RolePerm.valueOf(
				this.um.getUserRoleInCtx(entity.getEntityId(), NewsConstants.CTX_E, uid)).getMask());
		// Usefull for xml and opm links
		mav.addObject(Constants.ATT_PORTAL_URL,  HostUtils.getHostUrl(request));
		return mav;
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
	 * Getter du membre cm.
	 * @return <code>CategoryManager</code> le membre cm.
	 */
	public CategoryManager getCm() {
		return cm;
	}

	/**
	 * Setter du membre cm.
	 * @param cm la nouvelle valeur du membre cm.
	 */
	public void setCm(final CategoryManager cm) {
		this.cm = cm;
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
	 * @throws Exception
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(this.getEm(), "The property EntityManager em in class " + getClass().getSimpleName()
				+ " must not be null.");
		Assert.notNull(this.getCm(), "The property CategoryManager cm in class " + getClass().getSimpleName()
				+ " must not be null.");
		Assert.notNull(this.getUm(), "The property UserManager um in class " + getClass().getSimpleName()
				+ " must not be null.");
	}

}
