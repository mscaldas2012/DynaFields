package com.msc.customFields.validator;

/**
 * <P>Decorator to validate and massage data for specific TransferObjects. This interface holds
 * the contract for concrete Validators for specific TransferObjects. It will be used by the Managers
 * to validate the objects before persisting them.
 * </P>
 * <P>Date: Dec 27, 2004 - 3:18:27 PM</P>
 *
 * @author <a href="mailto:mscaldas@gmail.com">Marcelo Caldas</a>
 */
public interface TransferObjectValidator {
	/**
	 * Level One validation is a basic validation of all columns: length, required, correct type, etc.
	 * This validation can happen both at the client side as well as the server side.
	 *
	 * @param object The PhindirObject we want to validate.
	 *
	 */
	public void validate(Object object) throws ValidationException ;
}
