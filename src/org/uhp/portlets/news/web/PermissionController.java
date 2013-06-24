package org.uhp.portlets.news.web;

/**
 * @Project NewsPortlet : http://sourcesup.cru.fr/newsportlet/
 * Copyright (C) 2007-2008 University Nancy 1
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation version 2 of the License.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletRequest;
import javax.portlet.PortletSecurityException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.esco.portlets.news.domain.Entity;
import org.esco.portlets.news.domain.EscoUser;
import org.esco.portlets.news.domain.Filter;
import org.esco.portlets.news.domain.FilterType;
import org.esco.portlets.news.domain.IEscoUser;
import org.esco.portlets.news.services.EntityManager;
import org.esco.portlets.news.services.UserManager;
import org.esco.portlets.news.services.group.GroupService;
import org.esupportail.portal.ws.client.PortalGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.support.filter.AbstractFilter;
import org.springframework.ldap.support.filter.AndFilter;
import org.springframework.ldap.support.filter.EqualsFilter;
import org.springframework.ldap.support.filter.GreaterThanOrEqualsFilter;
import org.springframework.ldap.support.filter.LessThanOrEqualsFilter;
import org.springframework.ldap.support.filter.NotFilter;
import org.springframework.ldap.support.filter.WhitespaceWildcardsFilter;
import org.springframework.util.Assert;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.mvc.AbstractWizardFormController;
import org.uhp.portlets.news.NewsConstants;
import org.uhp.portlets.news.domain.Category;
import org.uhp.portlets.news.domain.RoleEnum;
import org.uhp.portlets.news.domain.RolePerm;
import org.uhp.portlets.news.domain.User;
import org.uhp.portlets.news.service.CategoryManager;
import org.uhp.portlets.news.service.TopicManager;
import org.uhp.portlets.news.web.support.Constants;
import org.uhp.portlets.news.web.validator.PermissionValidator;


/**
 * modified by GIP RECIA - Gribonvald Julien.
 */
public class PermissionController extends AbstractWizardFormController {

    /** */
    private static final int DEFAULT_NB = 10;
    /** */
    private static final Log LOG = LogFactory.getLog(PermissionController.class);

    /** Liste des utilisateurs obtenus par recherche. */
    private List<IEscoUser> users;
    /** */
    private List<PortalGroup> groups;

    @Autowired private EntityManager em;
    @Autowired private TopicManager tm;
    @Autowired private CategoryManager cm;
    @Autowired private UserManager um;
    @Autowired private GroupService gs;
    private int nbItemsToShow;

    public PermissionController() {
        setCommandClass(PermForm.class);
        setCommandName(Constants.CMD_PERM);
        setAllowDirtyBack(true);
        setAllowDirtyForward(false);
        setSessionForm(true);
        setPageAttribute(Constants.ATT_PAGE);
        setPages(new String[] {Constants.ACT_ADD_PERM, Constants.ACT_ADD_PERM, Constants.ACT_ADD_PERM});
    }

    public void afterPropertiesSet() throws Exception {
        Assert.notNull(this.em, "An EntityManager is required.");
        Assert.notNull(this.cm, "A CategoryManager is required.");
        Assert.notNull(this.tm, "A TopicManager is required.");
        Assert.notNull(this.um, "A UserManager is required.");
        Assert.notNull(this.gs, "A GroupService is required.");
        if (this.nbItemsToShow <= 0) {
            this.nbItemsToShow = DEFAULT_NB;
        }
    }

    @Override
    protected void processFinish(
            ActionRequest request, ActionResponse response,
            Object command, BindException errors)
    throws Exception {
        PermForm permForm = (PermForm) command;
        IEscoUser u = permForm.getUser();
        for (User user : users) {
            if (user.getUserId().equalsIgnoreCase(u.getUserId())) {
                u.setDisplayName(user.getDisplayName());
                u.setEmail(user.getEmail());
            }
        }
        this.um.addUserCtxRole(permForm.getUser(), permForm.getRole(), permForm.getCtxType(), permForm.getCtxId());
        response.setRenderParameter(Constants.ATT_CTX_ID, String.valueOf(permForm.getCtxId()));
        response.setRenderParameter(Constants.ACT, Constants.ACT_VIEW_PERM + permForm.getCtxType());
        response.setRenderParameter(Constants.ATT_PM,
                Integer.toString(RolePerm.valueOf(this.um.getUserRoleInCtx(
                        permForm.getCtxId(), permForm.getCtxType(), request.getRemoteUser())).getMask()));

    }

    @Override
    protected void processCancel(
            ActionRequest request, ActionResponse response,
            Object command, BindException errors)
    throws Exception {
        PermForm cmd = (PermForm)command;

        response.setRenderParameter(Constants.ACT, Constants.ACT_VIEW_PERM + cmd.getCtxType());
        response.setRenderParameter(Constants.ATT_CTX_ID, String.valueOf((cmd.getCtxId())));
        response.setRenderParameter(Constants.ATT_PM,
                Integer.toString(RolePerm.valueOf(this.um.getUserRoleInCtx(
                        cmd.getCtxId(), cmd.getCtxType(), request.getRemoteUser())).getMask()));
    }

    @Override
    protected void validatePage(
            Object command, Errors errors, int page, boolean finish) {
        PermForm permForm = (PermForm) command;
        PermissionValidator permValidator = (PermissionValidator) getValidator();
        switch (page) {
        case 0 : permValidator.validateTokenAndRole(permForm, errors); break;
        case 1 : permValidator.validateUser(permForm, errors); break;
        default : break;
        }
    }
    @Override
    protected Object formBackingObject(PortletRequest request) throws Exception {

        PermForm permForm = new PermForm();
        String ctxId = request.getParameter(Constants.ATT_CTX_ID);
        String ctx = request.getParameter(Constants.ATT_CTX);
        permForm.setCtxType(ctx);
        permForm.setCtxId(Long.valueOf(ctxId));
        return permForm;
    }

    @Override
    protected Map<String, Object> referenceData(PortletRequest request, Object command, Errors errors, int page) throws Exception {

        Long ctxId = StringUtils.defaultIfEmpty(
                request.getParameter(Constants.ATT_CTX_ID), null) == null ? ((PermForm) command).getCtxId() :
                    Long.valueOf(request.getParameter(Constants.ATT_CTX_ID));
        String ctx = ((PermForm) command).getCtxType();
        boolean isGrp = ((PermForm) command).getIsGroup() == 1 ? true : false;

        if (!this.um.isUserAdminInCtx(ctxId, ctx, request.getRemoteUser())) {
            LOG.warn("PermissionController:: user " + request.getRemoteUser() + " has no role admin");
            throw new PortletSecurityException(
                    getMessageSourceAccessor().getMessage("exception.notAuthorized.action"));
        }
        Map<String, Object> model = new HashMap<String, Object>();
        Entity e = null;
        if (ctx.equalsIgnoreCase(NewsConstants.CTX_E)) {
            e = this.em.getEntityById(ctxId);
            model.put(Constants.OBJ_ENTITY, e);
        } else if (ctx.equalsIgnoreCase(NewsConstants.CTX_C)) {
            Category c = this.cm.getCategoryById(ctxId);
            e = this.em.getEntityById(c.getEntityId());
            model.put(Constants.OBJ_CATEGORY, c);
            model.put(Constants.OBJ_ENTITY, e);
        } else if (ctx.equalsIgnoreCase(NewsConstants.CTX_T)) {
            model.put(Constants.OBJ_TOPIC, this.tm.getTopicById(ctxId));
            Category c = this.cm.getCategoryById(this.tm.getTopicById(ctxId).getCategoryId());
            e = this.em.getEntityById(c.getEntityId());
            model.put(Constants.OBJ_CATEGORY, c);
            model.put(Constants.OBJ_ENTITY, e);
        }
        model.put(Constants.ATT_CTX_ID, ctxId);
        model.put(Constants.ATT_PM,
                RolePerm.valueOf(this.um.getUserRoleInCtx(ctxId, ctx, request.getRemoteUser())).getMask());

        Map<FilterType, List<Filter>> mapFilters = null;
        AbstractFilter ldapFilter = null;
        if (e != null) {
            mapFilters = this.em.getFiltersByTypeOfEntity(e.getEntityId());
            if (mapFilters.containsKey(FilterType.LDAP)) {
                if (mapFilters.get(FilterType.LDAP).size() > 1) {
                    ldapFilter = new AndFilter();
                    for (Filter f : mapFilters.get(FilterType.LDAP)) {
                        AbstractFilter ftmp = this.getLdapFilterFromFilter(f);
                        if (ftmp != null) {
                            ((AndFilter) ldapFilter).and(ftmp);
                        }
                    }
                } else if (mapFilters.get(FilterType.LDAP).size() == 1) {
                    Filter f = mapFilters.get(FilterType.LDAP).get(0);
                    ldapFilter = this.getLdapFilterFromFilter(f);
                }
            }
        }


        if (page == 0) {
            model.put(Constants.ATT_ROLE_LIST,  RoleEnum.values());
            return model;
        } else if (page == 1) {
            PermForm permForm = (PermForm) command;
            model.put(Constants.ATT_IS_GRP, permForm.getIsGroup());
            if (isGrp) {
                String token = ((SubForm) command).getToken();
                HashSet<PortalGroup> tmpList = new HashSet<PortalGroup>();
                if (mapFilters != null && mapFilters.containsKey(FilterType.Group) && !mapFilters.get(FilterType.Group).isEmpty()) {
                    for (Filter f : mapFilters.get(FilterType.Group)) {
                    	tmpList.addAll(this.gs.searchPortalGroups(f.getValue(), token));
                    }
                } else {
                	tmpList.addAll(this.gs.searchPortalGroups(token));
                }
                groups = new ArrayList<PortalGroup>(tmpList);
                model.put("grps", groups);
            } else {
                users = this.um.findPersonsByTokenAndFilter(((PermForm) command).getToken(), ldapFilter);
                model.put(Constants.ATT_USER_LIST, users);
            }

            //users = this.um.findPersonsByToken(permForm.getToken());
            if (LOG.isDebugEnabled()) {
                LOG.debug("List of users found : " + users);
            }
            model.put(Constants.ATT_USER_LIST, users);
            model.put(Constants.ATT_NB_ITEM_TO_SHOW, this.nbItemsToShow);
            model.put(Constants.ATT_LDAP_DISPLAY, um.getLdapUserService().getSearchDisplayedAttributes());
            model.put(Constants.ERRORS, errors);
            return model;
        } else if (page == 2) {
            PermForm pf = (PermForm) command;
            if (this.um.isUserRoleExistForContext(
                    pf.getCtxId(), pf.getCtxType(), pf.getUser().getUserId())) {
                LOG.warn("Warning : user has already a role in this context !");
                model.put("oldRole", this.um.getUserRoleInCtx(
                        pf.getCtxId(), pf.getCtxType(), pf.getUser().getUserId()));
            }
            model.put(Constants.ATT_ROLE, pf.getRole());
            model.put(Constants.ATT_IS_GRP, pf.getIsGroup());
            EscoUser u = null;
            for (IEscoUser user : users) {
                if (user.getUserId().equalsIgnoreCase(pf.getUser().getUserId())) {
                    u = (EscoUser) user;
                }
            }
            model.put(Constants.OBJ_USER, u);
            model.put(Constants.ATT_LDAP_DISPLAY, um.getLdapUserService().getSearchDisplayedAttributes());
            return model;
        }

        return null;

    }
    @Override
    protected ModelAndView renderInvalidSubmit(RenderRequest request, RenderResponse response)
    throws Exception {
        return null;
    }
    @Override
    protected void handleInvalidSubmit(ActionRequest request, ActionResponse response)
    throws Exception {
        LOG.warn("PermissionController:: handleInvalidSubmit: goto home page");
        response.setRenderParameter(Constants.ACT, Constants.ACT_VIEW_NEWSSTORE);

    }
    /**
     * From a filter return an LDAP filter.
     * @param f
     * @return AbstractFilter
     */
    private AbstractFilter getLdapFilterFromFilter(final Filter f) {
        AbstractFilter ftmp;
        switch (f.getOperator()) {
        case APPROX : ftmp = new WhitespaceWildcardsFilter(f.getAttribute(), f.getValue()); break;
        case NOT_EQUAL : ftmp = new NotFilter(
                new EqualsFilter(f.getAttribute(), f.getValue())); break;
        case EQUAL : ftmp = new EqualsFilter(f.getAttribute(), f.getValue()); break;
        case GE : ftmp = new GreaterThanOrEqualsFilter(f.getAttribute(), f.getValue()); break;
        case LE : ftmp = new LessThanOrEqualsFilter(f.getAttribute(), f.getValue()); break;
        default : ftmp = new WhitespaceWildcardsFilter(f.getAttribute(), f.getValue()); break;
        }
        return ftmp;
    }

    public void setNbItemsToShow(int nbItemsToShow) {
        this.nbItemsToShow = nbItemsToShow;
    }

    @SuppressWarnings("unchecked")
    protected boolean isFormSubmission(PortletRequest request) {
        for (Enumeration params = request.getParameterNames(); params.hasMoreElements();) {
            String paramName = (String) params.nextElement();
            LOG.debug("Attribut Form : " + paramName);
            if (paramName.startsWith(PARAM_TARGET)
                    || paramName.equals(PARAM_FINISH)
                    || paramName.equals(PARAM_FINISH))   {
                return true;
            }
        }
        return super.isFormSubmission(request);
    }

}
