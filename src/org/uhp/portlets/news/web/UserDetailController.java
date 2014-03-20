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
package org.uhp.portlets.news.web;


import java.util.ArrayList;
import java.util.List;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esco.portlets.news.domain.IEscoUser;
import org.esco.portlets.news.services.RoleManager;
import org.esco.portlets.news.services.UserManager;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.mvc.AbstractController;
import org.uhp.portlets.news.NewsConstants;
import org.uhp.portlets.news.domain.RolePerm;
import org.uhp.portlets.news.web.support.Constants;

/**
 * modifications : GIP RECIA - Gribonvald Julien
 * 22 oct. 09
 */
public class UserDetailController extends AbstractController implements InitializingBean {	

    /** */
    @Autowired
    private UserManager um;
    /** */
	@Autowired private RoleManager rm;

    /** */
    private static final Log LOGGER = LogFactory.getLog(UserDetailController.class);

    /**
     * @throws Exception
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(um, "A userManager is required in the class " + this.getClass().getName());
    }

    /**
     * @see org.springframework.web.portlet.mvc.AbstractController#handleRenderRequestInternal(javax.portlet.RenderRequest, javax.portlet.RenderResponse)
     */
    @Override
    protected ModelAndView handleRenderRequestInternal(RenderRequest request, RenderResponse response) throws Exception {
        boolean isSuperAdmin = false;
        final String uid = request.getParameter(Constants.ATT_USER_ID);
        final IEscoUser user = this.um.findUserByUid(uid);
        if (user == null) {
            if (LOGGER.isWarnEnabled()) {
                LOGGER.warn("user with id " + uid + " does not exist in database.");
            }
            return new ModelAndView("Errors", "message", 
                    getMessageSourceAccessor().getMessage("news.alert.userRightsDeleted"));
        }
        final ModelAndView mav = new ModelAndView(Constants.ACT_VIEW_USER_D);
        mav.addObject(Constants.OBJ_USER, user);
        List<String> list = new ArrayList<String>(this.um.getLdapUserService().getSearchDisplayedAttributes());
        list.remove(this.um.getLdapUserService().getIdAttribute());
        list.remove(this.um.getAttrDisplayName());
        list.remove(this.um.getAttrMail());
        mav.addObject(Constants.ATT_LDAP_DISPLAY, list);
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("User added to view : " + user);
        }
        if ("1".equals(user.getIsSuperAdmin())) {
            mav.addObject(Constants.ATT_PU, RolePerm.ROLE_ADMIN.getMask());
        } else {
            mav.addObject(Constants.ATT_E_ROLEMAP, this.rm.loadUserEntityRoleMaps(uid));
            mav.addObject(Constants.ATT_C_ROLEMAP, this.rm.loadUserCategoryRoleMaps(uid));
            mav.addObject(Constants.ATT_T_ROLEMAP, this.rm.loadUserTopicRoleMaps(uid));
        }
        return mav;
    }

}
