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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.uhp.portlets.news.dao.TopicDao;
import org.uhp.portlets.news.domain.Topic;

public class TopicValidator extends AbstractValidator {
	@Autowired private TopicDao topicDao= null;
		
	public TopicValidator() {
		super();
	}

	@Override
	public void validate(final Object obj, final Errors errors) {	
		final Topic topic = (Topic) obj;	
		validateTitle(topic, errors);
	    validateDescription(topic, errors);
	    validateTTL(topic, errors); 
	}
   
	public void validateTitle(final Topic topic, final Errors errors) {
		ValidationUtils.rejectIfEmpty(errors, "name", "TOPIC_TITLE_REQUIRED", "Title is required.");
		if(this.topicDao.isTopicNameExistInCat(topic.getName(), topic.getCategoryId())) {
	      	  errors.rejectValue("name", "TOPIC_NAME_EXISTS", "A topic with same title exists yet.");
	        }
	}

	public void validateDescription(final Topic topic, final Errors errors) {
		ValidationUtils.rejectIfEmpty(errors, "desc", "TOPIC_DESC_REQUIRED", "Description is required.");
	}
	public void validateTTL(final Topic topic, final Errors errors) {
		if(("day".equalsIgnoreCase(topic.getRefreshPeriod()) && topic.getRefreshFrequency() > 120) || ("hour".equalsIgnoreCase(topic.getRefreshPeriod()) && topic.getRefreshFrequency() > 5))
		ValidationUtils.rejectIfEmpty(errors, "refreshFrequency", "TTL_EXCESSIVE", "TTL too excessive.");
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected Class getValidatorSupportClass() {
		return Topic.class;
	}

}
