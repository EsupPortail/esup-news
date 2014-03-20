/**
 * ESUP-Portail News - Copyright (c) 2009 ESUP-Portail consortium
 * For any information please refer to http://esup-helpdesk.sourceforge.net
 * You may obtain a copy of the licence at http://www.esup-portail.org/license/
 */
package org.cmis.portlets.news.web.validator;

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

	/**
	 * @see org.uhp.portlets.news.web.validator.AbstractValidator#validate(java.lang.Object, org.springframework.validation.Errors)
	 */
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

	/**
	 * @see org.uhp.portlets.news.web.validator.AbstractValidator#getValidatorSupportClass()
	 */
	@SuppressWarnings("rawtypes")
	@Override
	protected Class getValidatorSupportClass() {
		return CmisServerParamsForm.class;
	}

}
