package org.cmis.portlets.news.web.validator;

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

import org.cmis.portlets.news.web.CmisServerParamsForm;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.uhp.portlets.news.web.validator.AbstractValidator;

/**
 * 
 * created by Anyware Services - Delphine Gavalda.
 * 
 * 8 juin 2010
 */
public class CmisServerParamsValidator extends AbstractValidator {

    /**
     * Constructor of the CmisServerParamsValidator object.
     */
    public CmisServerParamsValidator() {
        super();
    }
    
    @Override
    public void validate(final Object obj, final Errors errors) {

	final CmisServerParamsForm form = (CmisServerParamsForm) obj;

	String useEntityOptions = form.getUseEntityServer();
	boolean valid = false;
	if (useEntityOptions != null) {
	    if (Boolean.parseBoolean(useEntityOptions) && form.getEntityId() > 0) {
		valid = true;
	    }
	} else {
	    valid = true;
	}
	if (valid) {
	    ValidationUtils.rejectIfEmpty(errors, "serverUrl", "CMIS_SERVER_URL_REQUIRED", 
		    "The server URL is required");
	    ValidationUtils.rejectIfEmpty(errors, "serverLogin", "CMIS_SERVER_LOGIN_REQUIRED", "The login is required");
	    ValidationUtils.rejectIfEmpty(errors, "serverPwd", "CMIS_SERVER_PWD_REQUIRED", "The password is required ");
	    if (!form.getServerPwd().equalsIgnoreCase(form.getServerPwd2())) {
		errors.rejectValue("serverPwd", "CMIS_SERVER_PWDS_NOT_EQUALS", "The two passwords are differents.");
	    }
	    ValidationUtils.rejectIfEmpty(errors, "repositoryId", "CMIS_SERVER_REPOID_REQUIRED", 
		    "The repository identifier is required");
	}
    }

    @Override
    protected Class getValidatorSupportClass() {
	return CmisServerParamsForm.class;
    }

}
