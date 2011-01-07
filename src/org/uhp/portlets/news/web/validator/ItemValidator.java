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

import java.util.Calendar;

import org.apache.commons.lang.StringUtils;
import org.cmis.portlets.news.domain.AttachmentOptions;
import org.cmis.portlets.news.services.AttachmentManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.multipart.MultipartFile;
import org.uhp.portlets.news.web.ItemForm;

public class ItemValidator extends AbstractValidator {

    @Autowired
    private AttachmentManager am;

    @Override
    public void validate(final Object obj, final Errors errors) {
        // TODO Auto-generated method stub

    }

    // ---------------------------------------------------------------------------
    // -- Item form page validation
    // --
    // ----------------------------------------------------------------------------
    /**
     * Validate page 1 of the item form.
     * 
     * @param obj
     * @param errors
     */
    public void validate1stPart(final Object obj, final Errors errors) {

        final ItemForm itemF = (ItemForm) obj;

        validateTitle(itemF, errors);
        validateMessage(itemF, errors);
        validateTopicIds(itemF.getTopicIds(), errors);
        validateEndDate(itemF, errors);
    }

    public void validateTitle(final ItemForm itemForm, final Errors errors) {
        ValidationUtils.rejectIfEmpty(errors, "item.title", "ITEM_TITLE_REQUIRED", "Title is required.");
    }

    public void validateMessage(final ItemForm item, final Errors errors) {
        ValidationUtils.rejectIfEmpty(errors, "item.body", "ITEM_MSG_REQUIRED", "Message is required.");
    }

    public void validateTopicIds(final String[] ids, final Errors errors) {

        if (ids.length < 1) {
            errors.rejectValue("topicIds", "ITEM_TOPIC_REQUIRED", "Item's topic is required.");
        }
    }

    public void validateEndDate(final ItemForm itemForm, final Errors errors) {

        Calendar endD = null;
        if (itemForm.getItem().getEndDate() != null) {
            endD = Calendar.getInstance();
            endD.setTime(itemForm.getItem().getEndDate());
            final Calendar today = Calendar.getInstance();
            today.set(Calendar.HOUR_OF_DAY, 0);
            today.set(Calendar.MINUTE, 0);
            today.set(Calendar.SECOND, 0);
            today.set(Calendar.MILLISECOND, 0);
            if (today.after(endD)) {
                errors.rejectValue("item.endDate", "ITEM_END_DATE_NOT_BEFORE_TODAY", 
                        "End date should not before today");
            }
        }
        if ((itemForm.getItem().getStartDate() != null) && (endD != null)) {
            Calendar startD = Calendar.getInstance();
            startD.setTime(itemForm.getItem().getStartDate());
            if (startD.after(endD)) {
                errors.rejectValue("item.endDate", "ITEM_END_DATE_NOT_BEFORE_START_DAY", 
                        "End date should be after start day");
            }
        }
    }

    // ---------------------------------------------------------------------------
    // -- External attachment page validation
    // --
    // ----------------------------------------------------------------------------
    
    /**
     * Validate page2 of the item form.
     * @param entityID 
     * @param obj
     * @param errors
     */
    public void validate2ndPart(final String entityID, final Object obj, final Errors errors) {

        final ItemForm itemF = (ItemForm) obj;

        AttachmentOptions options = am.getEntityAttachmentOptions(Long.parseLong(entityID));
        if (options == null) {
            options = am.getApplicationAttachmentOptions();
        }
        if (options != null) {
            validateFileSize(options.getMaxSize(), itemF, errors);
            validateFileType(options, itemF, errors);
        }
        validateFileTitle(itemF, errors);
    }

    public void validateFileSize(final long maxSize, final ItemForm itemForm, final Errors errors) {
        MultipartFile file = itemForm.getExternal().getFile();

        if (file != null) {
            long size = file.getSize();
            if (size >= maxSize) {
                errors.rejectValue("external.file", "ITEM_FILE_WRONG_SIZE", "The file chosen is too big.");
            } else if (size == 0) {
                errors.rejectValue("external.file", "ITEM_FILE_IS_EMPTY", "You must select a file");
            }
        } else {
            errors.rejectValue("external.file", "ITEM_FILE_IS_EMPTY", "You must select a file");
        }
    }

    public void validateFileType(final AttachmentOptions options, final ItemForm itemForm, final Errors errors) {
        MultipartFile file = itemForm.getExternal().getFile();

        String authorizedFilesExtensions = options.getAuthorizedFilesExtensions();
        String forbiddenFilesExtensions = options.getForbiddenFilesExtensions();

        if (file != null) {
            String originalFilename = file.getOriginalFilename();
            if (StringUtils.isNotEmpty(originalFilename)) {
                String type = originalFilename.substring(originalFilename.lastIndexOf(".") + 1, 
                        originalFilename.length());
                if (StringUtils.isNotEmpty(forbiddenFilesExtensions)) {
                    if (forbiddenFilesExtensions.contains(type)) {
                        errors.rejectValue("external.file", "ITEM_FILE_WRONG_TYPE", "The file type is not allowed");
                    }
                }
                if (StringUtils.isNotEmpty(authorizedFilesExtensions)) {
                    if (!authorizedFilesExtensions.contains(type)) {
                        errors.rejectValue("external.file", "ITEM_FILE_WRONG_TYPE", "The file type is not allowed");
                    }
                }
            }
        }
    }

    public void validateFileTitle(final ItemForm itemForm, final Errors errors) {
        if (StringUtils.isEmpty(itemForm.getExternal().getTitle())) {
            errors.rejectValue("external.title", "ITEM_FILE_TITLE_REQUIRED", "Title is required.");
        }
    }

    // ---------------------------------------------------------------------------
    // -- internal attachment page validation
    // --
    // ----------------------------------------------------------------------------
    /**
     * Validate page 3 of the item form.
     * 
     * @param obj
     * @param errors
     */
    public void validate3rdPart(final Object obj, final Errors errors) {
        final ItemForm itemF = (ItemForm) obj;
    }

    // ---------------------------------------------------------------------------
    // -- update attachment page validation
    // --
    // ----------------------------------------------------------------------------
    /**
     * Validate page 4 of the item form.
     * 
     * @param obj
     * @param errors
     */
    public void validate4rdPart(final Object obj, final Errors errors) {

        final ItemForm itemF = (ItemForm) obj;

        if (StringUtils.isEmpty(itemF.getAttachmentToUpdate().getTitle())) {
            errors.rejectValue("external.title", "ITEM_FILE_TITLE_REQUIRED", "Title is required.");
        }
    }
    
    // -----------------------------------------------------------------------

    /**
     * @return
     * @see org.uhp.portlets.news.web.validator.AbstractValidator#getValidatorSupportClass()
     */
    @SuppressWarnings("unchecked")
    @Override
    protected Class getValidatorSupportClass() {
        return ItemForm.class;
    }

}
