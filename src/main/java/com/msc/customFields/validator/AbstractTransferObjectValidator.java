package com.msc.customFields.validator;


import com.msc.customFields.validator.TransferObjectValidator;
import com.msc.customFields.validator.ValidationException;
import com.msc.utils.RegExpressionUtils;
import com.msc.utils.StringUtils;

import java.util.List;

/**
 * <p/>
 * </P>
 * <p/>
 * <P>Date: Jan 11, 2005 - 12:18:43 PM</P>
 *
 * @author <a href="mailto:mscaldas@gmail.com">Marcelo Caldas</a>
 */
public abstract class AbstractTransferObjectValidator implements TransferObjectValidator {
	protected static final String ERROR_MSG_NOT_NULL_PROPERTY = "Property {0} does not allow null or empty values.";
	protected static final String ERROR_MSG_NOT_NUMERIC_VALUE = "Property {0} requires a numeric value. The value '{1}' is invalid.";
	protected static final String ERROR_MSG_MAX_LENGTH = "Value '{0}' for property {1} is too large. Maximum size is {2}.";
	protected static final String ERROR_MSG_MIN_LENGTH = "Value '{0}' for property {1} is too small. Minimum size is {2}.";
	protected static final String ERROR_MSG_INVALID_SIZE = "Property {0} has an incorrect size. Length must be {1}.";
	protected static final String ERROR_MSG_INVALID_LENGTH = "Property {0} has an incorrect size. Valid size must be between {1} and {2}.";
	protected static final String ERROR_MSG_INVALID_EXPRESSION = "Property {0} has an invalid value. Please follow the pattern {1}";
	protected static final String ERROR_MSG_INVALID_NUMERIC_RANGE = "The property {0} must have a value between {1} and {2}";
	private static final String ERROR_MSG_INVALID_MIN_RANGE = "The property {0} must have a value greater than {1}";
	private static final String ERROR_MSG_INVALID_MAX_RANGE = "The property {0} must have a value less than {1}";

	private boolean groupErrors = false;
	private ValidationException errors;

	protected AbstractTransferObjectValidator() {
		this.groupErrors = false;
		this.errors = new ValidationException();
	}
	protected AbstractTransferObjectValidator(boolean groupErrors) {
		this();
		this.groupErrors = groupErrors;
	}

	public boolean isGroupErrors() {
		return groupErrors;
	}
	public void checkPropertyNotNull(Object value, String propertyName) throws ValidationException {
		if (value == null || ((value instanceof String) && StringUtils.isEmpty((String) value))) {
			ValidationException e = new ValidationException(ERROR_MSG_NOT_NULL_PROPERTY, propertyName);
			handleError(e);
		}
	}
//	public void checkPropertySize(String value, String propertyName, int maxLength) throws BusinessException {
//		if (StringUtils.isNotEmpty(value) || value.length() > maxLength) {
//			throw new BusinessException(ERROR_MSG_MAX_LENGTH, propertyName , Integer.toString(maxLength) );
//		}
//	}

	public void checkPropertyIsNumeric(String value, String propertyName) throws ValidationException {
		if (StringUtils.isNotEmpty(value) && !StringUtils.isNumber(value)) {
			ValidationException e = new ValidationException(ERROR_MSG_NOT_NUMERIC_VALUE, propertyName, value);
			handleError(e);
		}
	}

	public void checkPropertyMaxLength(String value, String propertyName, int maxLength) throws ValidationException {
		if (StringUtils.isNotEmpty(value) && (value.length() > maxLength)) {
			ValidationException e = new ValidationException(ERROR_MSG_MAX_LENGTH, value,  propertyName, Integer.toString(maxLength));
			handleError(e);
		}
	}

	public void checkPropertyMinLength(String value, String propertyName, int minLength) throws ValidationException {
		if (StringUtils.isNotEmpty(value) && (value.length() < minLength)) {
			ValidationException e = new ValidationException(ERROR_MSG_MIN_LENGTH, value, propertyName, Integer.toString(minLength));
			handleError(e);
		}
	}

	public void checkPropertyExactLength(String value, String propertyName, int length) throws ValidationException {
		if (StringUtils.isNotEmpty(value) && (value.length() != length)) {
			ValidationException e = new ValidationException(ERROR_MSG_INVALID_SIZE, propertyName, Integer.toString(length));
			handleError(e);
		}
	}

	public void checkPropertyValidLength(String value, String propertyName, int minLength, int maxLength) throws ValidationException {
		if (StringUtils.isNotEmpty(value) && ((value.length() < minLength) || (value.length() > maxLength))) {
			ValidationException e = new ValidationException(ERROR_MSG_INVALID_LENGTH, propertyName, Integer.toString(minLength), Integer.toString(maxLength));
			handleError(e);
		}
	}

	public void checkPropertyRegExpression(String value, String propertyName, String expression) throws ValidationException {
		if (StringUtils.isNotEmpty(value) && !RegExpressionUtils.isMatch(value, expression)) {
			ValidationException e = new ValidationException(ERROR_MSG_INVALID_EXPRESSION, propertyName, expression);
			handleError(e);
		}
	}
	protected void checkValidNumericRange(Long aNumber, String propertyName, Long minLength, Long maxLength) throws ValidationException {
		if (aNumber != null) {
			if (minLength != null && aNumber < minLength) {
				if (maxLength == null) {
					ValidationException e = new ValidationException(ERROR_MSG_INVALID_MIN_RANGE, propertyName, minLength.toString());
					handleError(e);
				} else {
					ValidationException e = new ValidationException(ERROR_MSG_INVALID_NUMERIC_RANGE, propertyName, minLength.toString(), maxLength.toString());
					handleError(e);
				}
			}
			if (maxLength != null && aNumber > maxLength) {
				if (minLength == null) {
					ValidationException e = new ValidationException(ERROR_MSG_INVALID_MAX_RANGE, propertyName, maxLength.toString());
					handleError(e);
				} else {
					ValidationException e = new ValidationException(ERROR_MSG_INVALID_NUMERIC_RANGE, propertyName, maxLength.toString(), maxLength.toString());
					handleError(e);
				}
			}
		}
	}
	protected void checkValidNumericRange(Double aNumber, String propertyName, Double minLength, Double maxLength) throws ValidationException {
		if (aNumber != null) {
			if (minLength != null && aNumber < minLength) {
				if (maxLength == null) {
					ValidationException e = new ValidationException(ERROR_MSG_INVALID_MIN_RANGE, propertyName, minLength.toString());
					handleError(e);
				} else {
					ValidationException e = new ValidationException(ERROR_MSG_INVALID_NUMERIC_RANGE, propertyName, minLength.toString(), maxLength.toString());
					handleError(e);
				}
			}
			if (maxLength != null && aNumber > maxLength) {
				if (minLength == null) {
					ValidationException e = new ValidationException(ERROR_MSG_INVALID_MAX_RANGE, propertyName, maxLength.toString());
					handleError(e);
				} else {
					ValidationException e = new ValidationException(ERROR_MSG_INVALID_NUMERIC_RANGE, propertyName, maxLength.toString(), maxLength.toString());
					handleError(e);
				}
			}
		}

	}

	protected void handleError(ValidationException e) throws ValidationException {
		if (!groupErrors) {
			throw e;
		} else {
			errors.addError(e);
		}
	}

	public List getErrors() {
		return errors.getErrors();
	}

}

