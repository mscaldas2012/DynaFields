package com.msc.customFields.validator;


import com.msc.customFields.CustomFieldSupport;
import com.msc.utils.CollectionUtils;

/**
 * Created by IntelliJ IDEA.
 * User: mcq1
 * Date: Apr 5, 2007
 * Time: 3:16:07 PM
 * To change this template use File | Settings | File Templates.
 */
public class CustomFieldSupportValidation extends AbstractTransferObjectValidator {

    public void validate(Object object) throws ValidationException {
        //Need to add validation for Custom Fields:
        CustomFieldSupport customField = (CustomFieldSupport) object;
        if (CollectionUtils.isNotEmpty(customField.getCustomFields())) {
//            CustomNodeFieldValidator cfValidator = new CustomNodeFieldValidator();
//            for (Iterator iterator = customField.getCustomFields().iterator(); iterator.hasNext();) {
//                CustomNodeField field = (CustomNodeField) iterator.next();
//                cfValidator.validate(field);
//            }
        }
    }
}
