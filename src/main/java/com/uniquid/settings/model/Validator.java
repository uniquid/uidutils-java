package com.uniquid.settings.model;

import com.uniquid.settings.exception.SettingValidationException;

/**
 * Define common methods to validate a {@link Setting}
 */
public interface Validator {

	/**
	 * Validate given setting value
	 * 
	 * @param setting
	 *            the setting
	 * @param stringValue
	 *            the setting value to be validated as string
	 * @throws SettingValidationException
	 *             if the validation fail. Be sure, in your implementation, the
	 *             setting key are set in this exception using
	 *             {@link SettingValidationException#setSettingKey(String)}
	 *             method.
	 */
	void validate(Setting setting, String stringValue) throws SettingValidationException;
	
}
