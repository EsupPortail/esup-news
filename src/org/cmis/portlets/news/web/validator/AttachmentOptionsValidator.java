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

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.cmis.portlets.news.web.AttachmentOptionsForm;
import org.springframework.validation.Errors;
import org.uhp.portlets.news.web.validator.AbstractValidator;

/**
 *
 * created by Anyware Services - Delphine Gavalda.
 *
 * 8 juin 2010
 */
public class AttachmentOptionsValidator extends AbstractValidator {

	private static final int MIN_SIZE_VALUE = 1000;

	/**
	 * Constructor of the AttachmentOptionsValidator object.
	 */
	public AttachmentOptionsValidator() {
		super();
	}

	@Override
	public void validate(final Object obj, final Errors errors) {

		final AttachmentOptionsForm form = (AttachmentOptionsForm) obj;

		String useEntityOptions = form.getUseEntityOptions();
		boolean valid = false;
		if (useEntityOptions != null) {
			if (Boolean.parseBoolean(useEntityOptions) && form.getEntityId() > 0) {
				valid = true;
			}
		} else {
			valid = true;
		}
		if (valid) {
			validateMaxSize(form, errors);
			validateAuthorizedExts(form, errors);
			validateForbiddenExts(form, errors);
		}
	}

	/**
	 * Validate the max size value.
	 * @param form
	 * @param errors
	 */
	public void validateMaxSize(final AttachmentOptionsForm form, final Errors errors) {
		long maxSize = 0;
		if (StringUtils.isNotEmpty(form.getMaxSize())) {
			maxSize = Long.parseLong(form.getMaxSize());
		}

		// file size can not be less than 1000 bytes
		if (maxSize < MIN_SIZE_VALUE) {
			form.setMaxSize("" + maxSize);
			errors.rejectValue("maxSize", "ATTACHMENT_MAXSIZE_REQUIRED",
					"Max size can not be smaller than 1000 bytes.");
		}
	}

	/**
	 * Validate the authorized extensions.
	 * @param form
	 * @param errors
	 */
	public void validateAuthorizedExts(final AttachmentOptionsForm form, final Errors errors) {
		String[] authorizedList = form.getAuthorizedExts().split(";");
		List<String> displayed = new ArrayList<String>();
		boolean valid = true;
		if (authorizedList != null) {
			for (String ext : authorizedList) {
				if (StringUtils.isNotEmpty(ext)) {
					if (!StringUtils.isAlphanumeric(ext)) {
						valid = false;
					}
					displayed.add(ext);
				}
			}
		}
		if (!valid) {
			form.setAuthorizedList(displayed);
			errors.rejectValue("authorizedExts", "ATTACHMENT_EXT_WRONG_CHAR",
					"Files Extensions must only contain alpha numeric characters");
		}
	}

	/**
	 * Validate the forbidden extensions.
	 * @param form
	 * @param errors
	 */
	public void validateForbiddenExts(final AttachmentOptionsForm form, final Errors errors) {
		String[] forbiddenList = form.getForbiddenExts().split(";");
		List<String> displayed = new ArrayList<String>();
		boolean valid = true;
		if (forbiddenList != null) {
			for (String ext : forbiddenList) {
				if (StringUtils.isNotEmpty(ext)) {
					if (!StringUtils.isAlphanumeric(ext)) {
						valid = false;
					}
					displayed.add(ext);
				}
			}
		}
		if (!valid) {
			form.setForbiddenList(displayed);
			errors.rejectValue("forbiddenExts", "ATTACHMENT_EXT_WRONG_CHAR",
					"Files Extensions must only contain alpha numeric characters");
		}
	}

	@Override
	protected Class getValidatorSupportClass() {
		return AttachmentOptionsForm.class;
	}

}
