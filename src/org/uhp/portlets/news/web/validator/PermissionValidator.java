package org.uhp.portlets.news.web.validator;

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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esco.portlets.news.services.RoleManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.uhp.portlets.news.web.PermForm;

public class PermissionValidator extends AbstractValidator {
	private static final Log LOGGER = LogFactory.getLog(PermissionValidator.class);
	@Autowired private RoleManager userManager;

	public void validateTokenAndRole(final PermForm permForm, final Errors errors) {
		ValidationUtils.rejectIfEmpty(errors, "token", "TOKEN_REQUIRED", "Token is required.");
		ValidationUtils.rejectIfEmpty(errors, "role", "ROLE_REQUIRED", "Role should be selected.");
	}

	public void validateUser(final PermForm permForm, final Errors errors) {
		if (permForm.getIsGroup() == 0) {
			ValidationUtils.rejectIfEmpty(errors, "user.userId", "USERID_REQUIRED", "User should be selected.");
		} else {
			ValidationUtils.rejectIfEmpty(errors, "group.key", "GROUPID_REQUIRED", "Group should be selected.");
		}

	}
	public void checkUserRoleExistInCtx(final PermForm permForm, final Errors errors) {
		if (permForm.getIsGroup() == 0) {
			if (this.userManager.isRoleExistForContext(
				permForm.getCtxId(), permForm.getCtxType(), permForm.getUser().getUserId(), false)) {
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("PermissionValdate::checkUserRoleExistInCtx : user exist in ctx");
				}
				errors.rejectValue("user.userId", "USER_EXIST_IN_CONTEXT_WITH_A_ROLE",
						"User has yet a role in this context");
			}
		} else if (this.userManager.isRoleExistForContext(
				permForm.getCtxId(), permForm.getCtxType(), permForm.getGroup().getKey(), true)) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("PermissionValdate::checkGroupRoleExistInCtx : group exist in ctx");
			}
			errors.rejectValue("group.key", "GROUP_EXIST_IN_CONTEXT_WITH_A_ROLE",
					"Group has yet a role in this context");
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Class getValidatorSupportClass() {
		return PermForm.class;
	}

	@Override
	public void validate(Object obj, Errors errors) {
		PermForm permForm = (PermForm) obj;
		validateTokenAndRole(permForm, errors);
		validateUser(permForm, errors);
		checkUserRoleExistInCtx(permForm, errors);
	}

}
