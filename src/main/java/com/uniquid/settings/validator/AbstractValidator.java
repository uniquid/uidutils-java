package com.uniquid.settings.validator;

import com.uniquid.settings.exception.SettingValidationException;
import com.uniquid.settings.model.Setting;
import com.uniquid.settings.model.SettingType;
import com.uniquid.settings.model.Validator;

public abstract class AbstractValidator implements Validator {

    /*
     * (non-Javadoc)
     *
     * @see
     * com.phoenix2.embedded.client.common.settings.model.Validator#validate
     * (com.phoenix2.embedded.client.common.settings.model.Setting,
     * java.lang.String)
     */
    @Override
    public void validate(Setting setting, String stringValue) throws SettingValidationException {

        // Get setting type
        SettingType settingType = setting.getSettingType();

        try {

            if (SettingType.DOUBLE.equals(settingType)) {

                // Get double value
                double doubleValue = Double.parseDouble(stringValue);

                // Validate as double
                validateAsDouble(doubleValue);

            } else if (SettingType.INTEGER.equals(settingType)) {

                // Get integer value
                int integerValue = Integer.parseInt(stringValue);

                // Validate as integer
                validateAsInteger(integerValue);

            } else if (SettingType.LONG.equals(settingType)) {

                // Get long value
                long longValue = Long.parseLong(stringValue);

                // Validate as long
                validateAsLong(longValue);

            } else if (SettingType.STRING.equals(settingType)) {

                // Validate as string
                validateAsString(stringValue);

            }

        } catch (NumberFormatException ex) {

            // Create exception
            SettingValidationException settingValidationException = new SettingValidationException(
                    "Not a number");

            // Get setting key
            String settingKey = setting.getKey();

            // Set setting key on exception
            settingValidationException.setSettingKey(settingKey);

            throw settingValidationException;

        } catch (SettingValidationException ex) {

            // Get setting key
            String settingKey = setting.getKey();

            // Set setting key on exception
            ex.setSettingKey(settingKey);

            throw ex;

        }

    }

    /**
     * Assert condition for true
     *
     * @param conditionResult
     *            the condition result
     * @param message
     *            the error message
     * @throws SettingValidationException
     *             if <i>conditionResult</i> is false
     */
    protected void assertTrue(boolean conditionResult, String message)
            throws SettingValidationException {

        if (!conditionResult) {
            throw new SettingValidationException(message);
        }

    }

    /**
     * Throw a runtime exception with <i>setting not supported for
     * validation</i> message
     */
    protected void notSupported() {
        throw new RuntimeException("setting not supported for validation");
    }

    /**
     * Validate given value
     *
     * @param doubleValue
     *            the value
     * @throws SettingValidationException
     *             if validation fail
     */
    protected abstract void validateAsDouble(double doubleValue)
            throws SettingValidationException;

    /**
     * Validate given value
     *
     * @param integerValue
     *            the value
     * @throws SettingValidationException
     *             if validation fail
     */
    protected abstract void validateAsInteger(int integerValue)
            throws SettingValidationException;

    /**
     * Validate given value
     *
     * @param longValue
     *            the value
     * @throws SettingValidationException
     *             if validation fail
     */
    protected abstract void validateAsLong(long longValue)
            throws SettingValidationException;

    /**
     * Validate given value
     *
     * @param stringValue
     *            the value
     * @throws SettingValidationException
     *             if validation fail
     */
    protected abstract void validateAsString(String stringValue)
            throws SettingValidationException;

}
