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
import java.util.Arrays;
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.esco.portlets.news.domain.Entity;
import org.esco.portlets.news.domain.EscoUser;
import org.esco.portlets.news.domain.Filter;
import org.esco.portlets.news.domain.FilterType;
import org.esco.portlets.news.domain.IEscoUser;
import org.esco.portlets.news.services.EntityManager;
import org.esco.portlets.news.services.UserManager;
import org.esupportail.portal.ws.client.PortalGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.support.filter.AbstractFilter;
import org.springframework.ldap.support.filter.AndFilter;
import org.springframework.ldap.support.filter.EqualsFilter;
import org.springframework.ldap.support.filter.GreaterThanOrEqualsFilter;
import org.springframework.ldap.support.filter.LessThanOrEqualsFilter;
import org.springframework.ldap.support.filter.NotFilter;
import org.springframework.ldap.support.filter.OrFilter;
import org.springframework.ldap.support.filter.WhitespaceWildcardsFilter;
import org.springframework.util.Assert;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.bind.PortletRequestUtils;
import org.springframework.web.portlet.mvc.AbstractWizardFormController;
import org.uhp.portlets.news.NewsConstants;
import org.uhp.portlets.news.domain.Category;
import org.uhp.portlets.news.domain.RolePerm;
import org.uhp.portlets.news.domain.SubscribeType;
import org.uhp.portlets.news.domain.Topic;
import org.uhp.portlets.news.service.CategoryManager;
import org.uhp.portlets.news.service.SubscribeService;
import org.uhp.portlets.news.service.TopicManager;
import org.uhp.portlets.news.web.support.Constants;
import org.uhp.portlets.news.web.validator.SubValidator;


/**
 * Subscribe controller.
 * modified by GIP RECIA - Gribonvald Julien
 */
public class SubcribeController extends AbstractWizardFormController {

    private static final Log LOG = LogFactory.getLog(SubcribeController.class);
    private static final int DEFAULT_NB = 10;

    @Autowired private TopicManager tm;
    @Autowired private CategoryManager cm;
    @Autowired private UserManager um;
    @Autowired private SubscribeService subService;
    /** The Entity Manager. */
    @Autowired
    private EntityManager em;
    private int nbItemsToShow;

    private List<IEscoUser> users;
    private List<PortalGroup> groups;

    /**
     * Constructor of SubcribeController.java.
     */
    public SubcribeController() {
        setCommandClass(SubForm.class);
        setCommandName(Constants.CMD_SUB_F);
        setAllowDirtyBack(true);
        setAllowDirtyForward(false);
        setSessionForm(true);
        setPageAttribute(Constants.ATT_PAGE);
        setPages(new String[] {Constants.ACT_ADD_AUDIENCE, Constants.ACT_ADD_AUDIENCE, Constants.ACT_ADD_AUDIENCE});

    }

    public void afterPropertiesSet() throws Exception {
        Assert.notNull(this.subService, "A SubscribeService is required.");
        Assert.notNull(this.cm, "A CategoryManager is required.");
        Assert.notNull(this.tm, "A TopicManager is required.");
        Assert.notNull(this.um, "A UserManager is required.");
        Assert.notNull(this.em, "A EntityManager is required.");
        if (this.nbItemsToShow <= 0) {
            this.nbItemsToShow = DEFAULT_NB;
        }
    }

    @Override
    protected void processFinish(
            ActionRequest request, ActionResponse response,
            Object command, BindException errors)
    throws Exception {
        SubForm cmd = (SubForm) command;
        this.subService.addSubscribers(cmd.getSubKey(), cmd.getSubscriber());
        response.setRenderParameter(Constants.ATT_CTX_ID, String.valueOf(cmd.getSubscriber().getCtxId()));
        response.setRenderParameter(Constants.ACT, Constants.ACT_VIEW_AUDIENCE + cmd.getSubscriber().getCtxType());
        response.setRenderParameter(Constants.ATT_PM,
                Integer.toString(RolePerm.valueOf(this.um.getUserRoleInCtx(
                        cmd.getSubscriber().getCtxId(), cmd.getSubscriber().getCtxType(),
                        request.getRemoteUser())).getMask()));
    }

    @Override
    protected void processCancel(
            ActionRequest request, ActionResponse response,
            Object command, BindException errors)
    throws Exception {
        SubForm cmd = (SubForm) command;
        response.setRenderParameter(Constants.ACT, Constants.ACT_VIEW_AUDIENCE + cmd.getSubscriber().getCtxType());
        response.setRenderParameter(Constants.ATT_CTX_ID, String.valueOf(cmd.getSubscriber().getCtxId()));
        response.setRenderParameter(Constants.ATT_PM,
                Integer.toString(RolePerm.valueOf(this.um.getUserRoleInCtx(
                        cmd.getSubscriber().getCtxId(), cmd.getSubscriber().getCtxType(),
                        request.getRemoteUser())).getMask()));
    }

    @Override
    protected void validatePage(
            Object command, Errors errors, int page, boolean finish) {

        SubForm subF = (SubForm)command;
        SubValidator subValidator = (SubValidator) getValidator();
        if (finish) {
            this.getValidator().validate(command, errors);
            return;
        }
        switch (page) {
            case 0: subValidator.validateSearch(subF, errors);        break;
            case 1: subValidator.validateSubscriberKey(subF, errors); break;
            default : break;
          //if (subF.getNavigate().equals("false")) { //}
        }

    }

    @Override
    protected Object formBackingObject(PortletRequest request) throws Exception {
        SubForm subForm = new SubForm();
        String ctx = request.getParameter(Constants.ATT_CTX);
        subForm.getSubscriber().setCtxType(ctx);
        subForm.getSubscriber().setCtxId(PortletRequestUtils.getLongParameter(request, Constants.ATT_CTX_ID));
        return subForm;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Map referenceData(PortletRequest request, Object command, Errors errors, int page) throws Exception {

        boolean isGrp = ((SubForm) command).getSubscriber().getIsGroup() == 1 ? true : false;
        Long ctxId = ((SubForm) command).getSubscriber().getCtxId();
        String ctx = ((SubForm) command).getSubscriber().getCtxType();

        if (!this.um.isUserAdminInCtx(ctxId, ctx, request.getRemoteUser())) {
            LOG.warn("SubcribeController:: user " + request.getRemoteUser() + " has no role admin");
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
            Topic t = this.tm.getTopicById(ctxId);
            Category c = this.cm.getCategoryById(t.getCategoryId());
            e = this.em.getEntityById(c.getEntityId());
            model.put(Constants.OBJ_TOPIC, t);
            model.put(Constants.OBJ_CATEGORY, c);
            model.put(Constants.OBJ_ENTITY, e);
        }
        Map<FilterType, List<Filter>> mapFilters;
        Set<String> tokens = new HashSet<String>();
        AbstractFilter filter = null;
        if (e != null) {
            mapFilters = this.em.getFiltersByTypeOfEntity(e.getEntityId());
            if (!ctx.equalsIgnoreCase(NewsConstants.CTX_E) && isGrp
                    && mapFilters.containsKey(FilterType.Group) && !mapFilters.get(FilterType.Group).isEmpty()) {
                    for (Filter f : mapFilters.get(FilterType.Group)) {
                        tokens.add(f.getValue() + "%"
                                + ((SubForm) command).getToken().replaceAll("\\*+", "%"));
                    }
            } else if (isGrp) {
                tokens.add(((SubForm) command).getToken().replaceAll("\\*+", "%"));
            } else if (mapFilters.containsKey(FilterType.LDAP) ) {
                if (mapFilters.get(FilterType.LDAP).size() > 1) {
                    filter = new OrFilter();
                    for (Filter f : mapFilters.get(FilterType.LDAP)) {
                        AbstractFilter ftmp = this.getLdapFilterFromFilter(f);
                        if (ftmp != null) {
                            ((OrFilter) filter).or(ftmp);
                        }
                    }
                } else if (mapFilters.get(FilterType.LDAP).size() == 1) {
                    Filter f = mapFilters.get(FilterType.LDAP).get(0);
                    filter = this.getLdapFilterFromFilter(f);
                }
            }
        }

        model.put(Constants.ATT_PM,
                RolePerm.valueOf(this.um.getUserRoleInCtx(ctxId, ctx, request.getRemoteUser())).getMask());
        String[] keys = ((SubForm) command).getSubKey();
        if (keys != null && keys.length > 0) {
            Set<String> skeys = new HashSet<String>(Arrays.asList(keys));
            ((SubForm) command).setSubKey(skeys.toArray(new String[skeys.size()]));
        }
        if (page == 0) {
            if (ctx.equalsIgnoreCase(NewsConstants.CTX_E)) {
                List <String> tmp = new ArrayList<String>();
                tmp.add(SubscribeType.SUB_FORCED.name());
                model.put("subTypeList", tmp );
            } else {
                model.put("subTypeList",  SubscribeType.values());
            }
        } else if (page == 1) {
            model.put(Constants.CMD_SUB_F, (SubForm) command);
            if (isGrp) {
                groups = new ArrayList<PortalGroup>();
                for (String token : tokens) {
                    groups.addAll(this.subService.searchGroups(token));
                }
                model.put("grps", groups);
            } else {
                users = this.um.findPersonsByTokenAndFilter(((SubForm) command).getToken(), filter);
                model.put(Constants.ATT_USER_LIST, users);
            }
            model.put(Constants.ATT_LDAP_DISPLAY, um.getLdapUserService().getSearchDisplayedAttributes());
            model.put(Constants.ATT_NB_ITEM_TO_SHOW, this.nbItemsToShow);
            model.put(Constants.ERRORS, errors);
        } else if (page == 2) {
            model.put(Constants.CMD_SUB_F, (SubForm) command);
            List<EscoUser> lu = null;
            if (((SubForm) command).getSubscriber().getIsGroup() == 0) {
                lu = new ArrayList<EscoUser>();
                for (String id : ((SubForm) command).getSubKey()) {
                    for (IEscoUser user : users) {
                        if (user.getUserId().equalsIgnoreCase(id)) {
                            lu.add((EscoUser) user);
                        }
                    }
                }
            }
            model.put(Constants.ATT_USER_LIST, lu);
            model.put(Constants.ATT_LDAP_DISPLAY, um.getLdapUserService().getSearchDisplayedAttributes());
        }
        return model;
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

    @Override
    protected ModelAndView renderInvalidSubmit(RenderRequest request, RenderResponse response)
    throws Exception {
        return null;
    }
    @Override
    protected void handleInvalidSubmit(ActionRequest request, ActionResponse response)
    throws Exception {
        LOG.warn("SubcribeController:: handleInvalidSubmit: goto home page");
        response.setRenderParameter(Constants.ACT, Constants.ACT_VIEW_NEWSSTORE);

    }



    public void setNbItemsToShow(final int nbItemsToShow) {
        this.nbItemsToShow = nbItemsToShow;
    }

    /**
     * Getter du membre users.
     * @return <code>List < IEscoUser ></code> le membre users.
     */
    public List<IEscoUser> getUsers() {
        return users;
    }

    /**
     * Setter du membre users.
     * @param users la nouvelle valeur du membre users.
     */
    public void setUsers(final List<IEscoUser> users) {
        this.users = users;
    }

    /**
     * Getter du membre groups.
     * @return <code>List<PortalGroup></code> le membre groups.
     */
    public List<PortalGroup> getGroups() {
        return groups;
    }

    /**
     * Setter du membre groups.
     * @param groups la nouvelle valeur du membre groups.
     */
    public void setGroups(List<PortalGroup> groups) {
        this.groups = groups;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected boolean isFormSubmission(PortletRequest request) {
        for (Enumeration params = request.getParameterNames(); params.hasMoreElements();) {
            String paramName = (String) params.nextElement ();
            if (paramName.startsWith(PARAM_TARGET) || paramName.equals(PARAM_FINISH) || paramName.equals(PARAM_FINISH))   {
                return true;
            }
        }
        return super.isFormSubmission (request);
    }

}
