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

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.uhp.portlets.news.web.SubForm;

public class SubValidator extends AbstractValidator {

	@Override
	public void validate(final Object obj, final Errors errors) {
		final SubForm subF = (SubForm) obj;
		
		validateSearch(subF, errors);
		validateSubscriberKey(subF, errors);
		validateSubType(subF, errors);
	}

	public void validateSearch(final SubForm subF, final Errors errors) {
		ValidationUtils.rejectIfEmpty(errors, "subscriber.ctxId", "CAT_Id_REQUIRED", "Cat Id is required.");
		ValidationUtils.rejectIfEmpty(errors, "token", "TOKEN_IS_REQUIRED", "token is required.");
	}

	public void validateSubscriberKey(final SubForm subF, final Errors errors) {
		ValidationUtils.rejectIfEmpty(errors, "subKey", "CAT_SUBSCRIBER_KEY_REQUIRED", 
		        "Subscriber key is required.");
	}
	
	public void validateSubType(final SubForm subF, final Errors errors) {
		ValidationUtils.rejectIfEmpty(errors, "subscriber.subType", 
		        "CAT_SUB_TYPE_REQUIRED", "SubType is required.");
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected Class getValidatorSupportClass() {		
		return SubForm.class;
	}

}
