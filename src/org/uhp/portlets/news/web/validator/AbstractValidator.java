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
import org.springframework.validation.Validator;

public abstract class AbstractValidator implements Validator {

	    @SuppressWarnings("unchecked")
		public boolean supports(final Class clazz) {
	        return clazz.isAssignableFrom(getValidatorSupportClass());
	    }

	    public abstract void validate(final Object obj, final Errors errors);		

	    @SuppressWarnings("unchecked")
		protected abstract Class getValidatorSupportClass();
	     
	}
