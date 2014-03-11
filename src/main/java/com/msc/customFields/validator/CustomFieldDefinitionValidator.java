package com.msc.customFields.validator;


import com.msc.customFields.CustomFieldDefList;
import com.msc.customFields.CustomFieldDefinition;
import com.msc.customFields.FieldType;
import com.msc.utils.CollectionUtils;

import java.util.Iterator;

/**
 * <p/>
 * </P>
 * <p/>
 * <p/>
 * <P>Date: Jan 5, 2007</P>
 *
 * @author <a href="mailto:mscaldas@gmail.com">Marcelo Caldas</a>
 */
public class CustomFieldDefinitionValidator extends AbstractTransferObjectValidator {
	private static final Long MAXIMUM_NUMBER_OF_VALUES_ALLOWED = new Long(100);
	private static final String ERROR_MSG_EMPTY_LIST = "The list does not contain any values for this field definition.";
	private static final String ERROR_MSG_DUPLICATE_CODES = "The list {0} contains duplicate items. Please remove those and try to save again.";
	private static final String ERROR_MSG_INVALID_FIELD_TYPE = "The field type {0} is invalid. Pleasse choose a valid field type.";
	private static final String ERROR_MSG_INNER_GROUPS = "Groups are not allowed to contain other groups.";
	private static final String ERROR_MSG_COLUMN_IS_REQUIRED = "A group requires at least one column defined.";


	public void validate(Object object) throws ValidationException {
		CustomFieldDefinition cfDef = (CustomFieldDefinition) object;
//		this.checkPropertyNotNull(cfDef.getClazz(), "Object Type");
//		this.checkPropertyMaxLength(cfDef.getObjectSubType(), "Sub Type", 50);
//		this.checkPropertyNotNull(cfDef.getOwnerId(), "ApplicationId");

		this.checkPropertyNotNull(cfDef.getName(), "Name");
		this.checkPropertyMaxLength(cfDef.getName(), "Name", 256);

		this.checkPropertyNotNull(cfDef.getFieldType(), "Field Type");

		this.checkValidNumericRange(cfDef.getMinRequired(), "MinimumRequired", new Long(0), new Long(50));
		if (cfDef.getMinRequired() != null) {
			this.checkValidNumericRange(cfDef.getMaxAllowed(), "MaximumAllowed", cfDef.getMinRequired(), MAXIMUM_NUMBER_OF_VALUES_ALLOWED);
		} else {
			this.checkValidNumericRange(cfDef.getMaxAllowed(), "MaximumAllowed", new Long(0), MAXIMUM_NUMBER_OF_VALUES_ALLOWED);
		}
		this.checkValidNumericRange(cfDef.getSequence(), "Sequence", new Long(0), MAXIMUM_NUMBER_OF_VALUES_ALLOWED);
//		this.checkPropertyNotNull(cfDef.getAppCode(), "app_code");
		if (cfDef.getFieldType() != null)  {
			FieldType fieldDefAppCode = cfDef.getFieldType();
			if (FieldType.STRING.equals(fieldDefAppCode)) {
				validateStringDef(cfDef);
			} else if (FieldType.NUMBER.equals(fieldDefAppCode)) {
				validateNumberDef(cfDef);
			} else if (FieldType.LIST.equals(fieldDefAppCode)) {
				validateListDef(cfDef);
			} else if (FieldType.DATE.equals(fieldDefAppCode)) {
				validateDateDef(cfDef);
			} else if (FieldType.BOOLEAN.equals(fieldDefAppCode)) {
				validateBooleanDef(cfDef);
			} else if (FieldType.GROUP.equals(fieldDefAppCode)) {
				validateGroupDef(cfDef);
			} else {
				throw new ValidationException(ERROR_MSG_INVALID_FIELD_TYPE, fieldDefAppCode.toString());
			}
		}

	}
	private void validateGroupDef(CustomFieldDefinition groupDef) throws ValidationException {
		if (groupDef.getBelongsToGroup() != null) {
			throw new ValidationException(ERROR_MSG_INNER_GROUPS);
		}
		groupDef.setMinValueAllowed(null);
		groupDef.setMaxValueAllowed(null);
		groupDef.setDecimalDigits(null);
		groupDef.setListValues(null);
		groupDef.setMaxLength(null);
		groupDef.setMinLength(null);
		groupDef.setRegExpression(null);

		if (CollectionUtils.isEmpty(groupDef.getGroupColumns())) {
			throw new ValidationException(ERROR_MSG_COLUMN_IS_REQUIRED);
		}
		for (Iterator iterator = groupDef.getGroupColumns().iterator(); iterator.hasNext();) {
			CustomFieldDefinition column = (CustomFieldDefinition) iterator.next();
			this.validate(column);
            column.setScope(groupDef.getScope());
//			column.setApplicationId(groupDef.getApplicationId());
//			column.setTreeId(groupDef.getTreeId());
//			column.setObjectType(groupDef.getClazz());
//			column.setObjectSubType(groupDef.getObjectSubType());
			//maxAllowed must be 1:
			column.setMaxAllowed(1L);
			//minRequired must be 0 or 1:
			this.checkValidNumericRange(column.getMinRequired(), "minimum required",  0L,  1L);
		}
	}

	private void validateBooleanDef(CustomFieldDefinition cfDef) throws ValidationException {
		this.checkValidNumericRange(cfDef.getMaxAllowed(), "MaximumAllowed for Boolean", 0L, 1L);
		cfDef.setMinValueAllowed(null);
		cfDef.setMaxValueAllowed(null);
		cfDef.setDecimalDigits(null);
		cfDef.setListValues(null);
		cfDef.setMaxLength(null);
		cfDef.setMinLength(null);
		cfDef.setRegExpression(null);
		cfDef.setMaxAllowed(1L);
	}
	private void validateDateDef(CustomFieldDefinition cfDef) {
		cfDef.setDecimalDigits(null);
		cfDef.setListValues(null);
		cfDef.setMaxLength(null);
		cfDef.setMinLength(null);
		cfDef.setRegExpression(null);
	}
	private void validateListDef(CustomFieldDefinition cfDef) throws ValidationException {
		if (CollectionUtils.isEmpty(cfDef.getListValues())) {
			throw new ValidationException(ERROR_MSG_EMPTY_LIST);
		}
		//Make sure the list contains unique codes:
		int found = 0;
		for (Iterator outer = cfDef.getListValues().iterator(); outer.hasNext() && found <= 1;) {
			found = 0;
			CustomFieldDefList customFieldDefList = (CustomFieldDefList) outer.next();
			for (Iterator inner = cfDef.getListValues().iterator(); inner.hasNext() && found <= 1;) {
				CustomFieldDefList item = (CustomFieldDefList) inner.next();
				found += customFieldDefList.getCode().equals(item.getCode())?1:0;
			}
		}
		if (found > 1) {
			throw new ValidationException(ERROR_MSG_DUPLICATE_CODES, cfDef.getName());
		}
		cfDef.setMaxLength(null);
		cfDef.setMinLength(null);
		cfDef.setRegExpression(null);
		cfDef.setMinValueAllowed(null);
		cfDef.setMaxValueAllowed(null);
	}
	private void validateNumberDef(CustomFieldDefinition cfDef) throws ValidationException {
		this.checkValidNumericRange(cfDef.getDecimalDigits(), "Decimal Digits", new Long(0), new Long(100));
		cfDef.setMaxLength(null);
		cfDef.setMinLength(null);
		cfDef.setRegExpression(null);
	}
	private void validateStringDef(CustomFieldDefinition cfDef) throws ValidationException {
		this.checkValidNumericRange(cfDef.getMinLength(), "Minimum Length", new Long(1), new Long(256));
		this.checkValidNumericRange(cfDef.getMaxLength(), "Maximum Length", new Long(1), new Long(256));
		cfDef.setMinValueAllowed(null);
		cfDef.setMaxValueAllowed(null);
		cfDef.setDecimalDigits(null);
		cfDef.setListValues(null);
	}

}
