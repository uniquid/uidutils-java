package com.uniquid.settings.validator;

import com.uniquid.settings.exception.SettingValidationException;

public class GreaterThan extends AbstractValidator {

    private static final long serialVersionUID = 1L;

    private final double minValue;

    /**
     * Create new validator
     *
     * @param minValue
     *            the min value
     */
    public GreaterThan(double minValue) {
        this.minValue = minValue;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.phoenix2.embedded.client.common.settings.model.validator.
     * AbstractValidator#validateAsDuoble(double)
     */
    @Override
    protected void validateAsDouble(double doubleValue)
            throws SettingValidationException {

        // Assert
        assertTrue(doubleValue > minValue, "Must be greater than " + minValue);

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

        // Assert
        assertTrue(integerValue > minValue, "Must be greater than " + minValue);

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

        // Assert
        assertTrue(longValue > minValue, "Must be greater than " + minValue);

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

        // Not supported
        notSupported();

    }

}