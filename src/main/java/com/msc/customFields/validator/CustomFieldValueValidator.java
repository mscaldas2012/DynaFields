package com.msc.customFields.validator;


import com.msc.cache.CacheManager;
import com.msc.customFields.CustomFieldDefList;
import com.msc.customFields.CustomFieldDefinition;
import com.msc.customFields.CustomFieldValue;
import com.msc.customFields.FieldType;
import com.msc.utils.BooleanUtils;
import com.msc.utils.CollectionUtils;
import com.msc.utils.StringUtils;

import java.util.Calendar;
import java.util.Iterator;

/**
 * <p/>
 * </P>
 * <p/>
 * <p/>
 * <P>Date: Jan 26, 2007</P>
 *
 * @author <a href="mailto:mscaldas@gmail.com">Marcelo Caldas</a>
 */
public class CustomFieldValueValidator extends AbstractTransferObjectValidator {
	private static final String ERROR_MSG_INVALID_ITEM = "Property {0} does not match any valid value for {1}. Please provide a valid value.";
	private static final String ERROR_MSG_INVALID_MIN_DATE = "Property {0} requires a date after {1}";
	private static final String ERROR_MSG_INVALID_MAX_DATE = "Property {0} requires a date before {1}]";
	private static final String ERROR_MSG_INVALID_INTEGER = "The property {0} must contain a valid integer value. {1} is not valid.";
	private static final String ERROR_MSG_INVALID_BOOLEAN_VALUE = "Property {0} has an invalid value ({1}). You must provide 1 (true) or 0 (false) values.";

//	private static final String CUSTOM_FIELD_DEF_CACHE = "CUSTOM_FIELD_DEF_CACHE";
    //IoC:
    private CacheManager cachManager;

	public void validate(Object object) throws ValidationException {
		//get Custom Field Definition from a cache:
		CustomFieldValue cfValue = (CustomFieldValue) object;
        CustomFieldDefinition def;
        try {
            def = (CustomFieldDefinition) cachManager.get(cfValue.getCfDefId());
            this.validate(def, cfValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
	}

	public void validate(CustomFieldDefinition def, CustomFieldValue cfValue) throws ValidationException {
		FieldType fieldDefAppCode = def.getFieldType();
//		if (EnumVocabulary.FIELD_DEF_GROUP.equals(fieldDefAppCode)) {
//			cfValue.setDeleted(cfValue.isDeleted() || CollectionUtils.isEmpty(cfValue.getCells()));
//			if (!cfValue.isDeleted()) {
//				//Still have to verify whether there's valid cells:
//				boolean found = false;
//				for (Iterator iterator = cfValue.getCells().iterator(); iterator.hasNext() && !found;) {
//					CustomFieldValue cell = (CustomFieldValue) iterator.next();
//					found = !cell.isDeleted();
//				}
//				//If did not find any valid cell, then the group can be marked as deleted!
//				cfValue.setDeleted(!found);
//			}
//		} else {
//			cfValue.setDeleted(cfValue.isDeleted() || StringUtils.isEmpty(cfValue.getValue()));
//		}
		if (!cfValue.isDeleted()) {
			this.checkPropertyNotNull(cfValue.getCfDefId(), "custom Field Definition ID");
			String value = cfValue.getValue();
			//get Custom Field Definition from a cache:
			if (FieldType.STRING.equals(fieldDefAppCode)) {
				validateStringField(value, def);
			} else if (FieldType.NUMBER.equals(fieldDefAppCode)) {
				validateNumericField(value, def);
			} else if (FieldType.LIST.equals(fieldDefAppCode)) {
				validateListField(value, def);
			} else if (FieldType.BOOLEAN.equals(fieldDefAppCode)) {
				validateBooleanField(value, def);
			} else if (FieldType.DATE.equals(fieldDefAppCode)) {
				validateDateField(cfValue.getDateValue(), def);
			} else if (FieldType.GROUP.equals(fieldDefAppCode)) {
				validateGroupFields(cfValue, def);
			}
		}
	}
	private void validateGroupFields(CustomFieldValue value, CustomFieldDefinition def) throws ValidationException {
		if (CollectionUtils.isNotEmpty(value.getCells())) {
            for (Object o : value.getCells()) {
                CustomFieldValue fieldNode = (CustomFieldValue) o;
                CustomFieldDefinition cellDef = getDefForCell(def, fieldNode.getCfDefId());
                if (cellDef == null) {
                    throw new ValidationException("Could not find definition for cell {0} to validate", fieldNode.getCfDefId().toString());
                }
                this.validate(cellDef, fieldNode);
            }
		}
	}

	private CustomFieldDefinition getDefForCell(CustomFieldDefinition def, Long defPk) {
		CustomFieldDefinition cellDef = null;
		boolean found = false;
		for (Iterator iterator1 = def.getGroupColumns().iterator(); iterator1.hasNext() && !found;) {
			cellDef = (CustomFieldDefinition) iterator1.next();
			found = cellDef.getPk().equals(defPk);
		}
		return cellDef;
	}

	private void validateBooleanField(String value, CustomFieldDefinition def) throws ValidationException {
		if (!BooleanUtils.isCharABooleanValue(value)) {
			throw new ValidationException(ERROR_MSG_INVALID_BOOLEAN_VALUE, def.getName(), value);
		}

	}
	private void validateDateField(Calendar value, CustomFieldDefinition def) throws ValidationException {
		this.checkPropertyNotNull(value, "date value");
		if (def.getMinValueAllowed() != null) {
			Calendar minDate = Calendar.getInstance();
			minDate.setTimeInMillis(def.getMinValueAllowed().longValue());
			if (value.before(minDate)) {
				throw new ValidationException(ERROR_MSG_INVALID_MIN_DATE, def.getName(), minDate.getTime().toString());
			}
		}
		if (def.getMaxValueAllowed() != null) {
			Calendar maxDate = Calendar.getInstance();
			maxDate.setTimeInMillis(def.getMaxValueAllowed().longValue());
			if (value.after(maxDate)) {
				throw new ValidationException(ERROR_MSG_INVALID_MAX_DATE, def.getName(), maxDate.getTime().toString());
			}
		}

	}
	private void validateListField(String value, CustomFieldDefinition def) throws ValidationException {
		//Got to find value on the list of possible values:
		if (CollectionUtils.isNotEmpty(def.getListValues())) {
			boolean found = false;
			for (Iterator iterator = def.getListValues().iterator(); iterator.hasNext() && !found;) {
				CustomFieldDefList item = (CustomFieldDefList) iterator.next();
				found = value.equals(item.getCode());
			}
			if (!found) {
				throw new ValidationException(ERROR_MSG_INVALID_ITEM, def.getName(), value);
			}
		}
	}
	private void validateStringField(String value, CustomFieldDefinition def) throws ValidationException {
		if (def.getMaxLength() != null && def.getMaxLength().intValue() > 0) {
			this.checkPropertyMaxLength(value, def.getName(), def.getMaxLength().intValue());
		}
		if (def.getMinLength() != null && def.getMinLength().intValue() > 0) {
			this.checkPropertyMinLength(value, def.getName(), def.getMinLength().intValue());
		}
		if (StringUtils.isNotEmpty(def.getRegExpression())) {
			this.checkPropertyRegExpression(value, def.getName(), def.getRegExpression());
		}
	}
	private void validateNumericField(String value, CustomFieldDefinition def) throws ValidationException {
		this.checkPropertyIsNumeric(value, def.getName());
		Double numberValue = null;
		try {
			if (StringUtils.isNumber(value)) {
				numberValue = new Double(value);
			}
		} catch (NumberFormatException e) {
			throw new ValidationException(ERROR_MSG_NOT_NUMERIC_VALUE, value);
		}
		this.checkValidNumericRange(numberValue, def.getName(), def.getMinValueAllowed(), def.getMaxValueAllowed());
		if (def.getDecimalDigits() != null && def.getDecimalDigits() == 0 && value.contains(".")) {
			throw new ValidationException(ERROR_MSG_INVALID_INTEGER, def.getName(), value);
		}
	}
}
