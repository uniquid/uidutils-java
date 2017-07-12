package com.uniquid.settings.validator;

import com.uniquid.settings.exception.SettingValidationException;

public class NotEmpty extends AbstractValidator {

	private static final long serialVersionUID = 1L;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.phoenix2.embedded.client.common.settings.model.validator.
	 * AbstractValidator#validateAsDuoble(double)
	 */
	@Override
	protected void validateAsDouble(double doubleValue)
			throws SettingValidationException {

		// Not supported
		notSupported();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.phoenix2.embedded.client.common.settings.model.validator.
	 * AbstractValidator#validateAsInteger(int)
	 */
	@Override
	protected void validateAsInteger(int integerValue)
			throws SettingValidationException {

		// Not supported
		notSupported();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.phoenix2.embedded.client.common.settings.model.validator.
	 * AbstractValidator#validateAsLong(long)
	 */
	@Override
	protected void validateAsLong(long longValue)
			throws SettingValidationException {

		// Not supported
		notSupported();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.phoenix2.embedded.client.common.settings.model.validator.
	 * AbstractValidator#stringAsString(java.lang.String)
	 */
	@Override
	protected void validateAsString(String stringValue)
			throws SettingValidationException {

		// Assert
		assertTrue(stringValue.length() > 0, "Cannot be empty");

	}

}