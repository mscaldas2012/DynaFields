package com.msc.customFields.validator;

import com.msc.exception.ResourceMessageException;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 * <p/>
 * </P>
 * <p/>
 * <p/>
 * <P>Date: Feb 20, 2007</P>
 *
 * @author <a href="mailto:mscaldas@gmail.com">Marcelo Caldas</a>
 */
public class ValidationException extends ResourceMessageException {
	/** List of all ValidationExceptions that were identified */
	private List errors = new ArrayList();

	public ValidationException() {
		super();
	}
	public ValidationException(String msg) {
		super(msg);
	}
	public ValidationException(String msg, String... args) {
		super(msg, null, args);
	}

	public ValidationException(String msg, Exception cause) {
		super(msg, cause, null);
	}
	public ValidationException(String msg, Exception cause, String... args) {
		super(msg, cause, args);
	}

    @Override
    public String getLogLevel() {
        return Level.WARNING.toString();
    }

    public List getErrors() {
		return errors;
	}
	public void setErrors(List errors) {
		this.errors = errors;
	}
	public void addError(ValidationException e) {
		this.errors.add(e);
	}
}
