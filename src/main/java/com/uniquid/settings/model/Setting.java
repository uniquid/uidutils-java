/*
 * Copyright (c) 2016-2018. Uniquid Inc. or its affiliates. All Rights Reserved.
 *
 * License is in the "LICENSE" file accompanying this file.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package com.uniquid.settings.model;

import com.uniquid.settings.exception.SettingValidationException;
import com.uniquid.settings.stringifier.DefaultStringifier;

import java.io.Serializable;

/**
 * Represents an application setting
 */
public class Setting implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String key;
    private final String name;
    private final String description;
    private final Class<?> valueClass;
    private final Object defaultValue;
    private final SettingType settingType;
    private final Validator validator;
    private final Stringifier stringifier;

    /**
     * Create new setting. The provided setting value define the type of this
     * setting and the value class. The default value class is the class of
     * given default value object. The setting type is provided by
     * {@link SettingType#valueClassOf(Class)} giving the default value class.
     *
     * @throws RuntimeException
     *             if a not supported default value object is given
     * @see SettingType for supported value class and setting type
     *
     * @param key
     *            the setting key
     * @param name
     *            the setting name
     * @param description
     *            the setting description
     * @param defaultValue
     *            the setting value
     * @param validator
     *            the setting validator or null if validation is not required
     */
    public Setting(String key, String name, String description, Object defaultValue, Validator validator,
                   Stringifier stringifier) {

        this.key = key;
        this.name = name;
        this.description = description;
        valueClass = defaultValue.getClass();
        this.defaultValue = defaultValue;
        settingType = SettingType.valueClassOf(defaultValue.getClass());
        this.validator = validator;
        this.stringifier = stringifier;

    }

    public Setting(String key, String name, String description, Object defaultValue, Validator validator) {
        this(key, name, description, defaultValue, null, new DefaultStringifier());
    }

    /**
     * Create new setting. The provided setting value define the type of this
     * setting and the value class. The default value class is the class of
     * given default value object. The setting type is provided by
     * {@link SettingType#valueClassOf(Class)} giving the default value class.
     *
     * @throws RuntimeException
     *             if a not supported default value object is given
     * @see SettingType for supported value class and setting type
     *
     * @param key
     *            the setting key
     * @param name
     *            the setting name
     * @param description
     *            the setting description
     * @param defaultValue
     *            the setting value
     */
    public Setting(String key, String name, String description, Object defaultValue) {
        this(key, name, description, defaultValue, null, new DefaultStringifier());
    }

    /**
     * Get setting key
     *
     * @return the setting key
     */
    public String getKey() {
        return key;
    }

    /**
     * Get setting name
     *
     * @return the setting name
     */
    public String getName() {
        return name;
    }

    /**
     * Get setting description
     *
     * @return the setting description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Get settings value class
     *
     * @return the setting value class
     */
    public Class<?> getValueClass() {
        return valueClass;
    }

    /**
     * Get default value
     *
     * @return the default value as object of same class returned by
     *         {@link #getValueClass()}
     */
    public Object getDefaultValue() {
        return defaultValue;
    }

    /**
     * Get setting type
     *
     * @return the same setting type returned by
     *         {@link SettingType#valueClassOf(Class)} using the object returned
     *         by {@link #getDefaultValue()}
     */
    public SettingType getSettingType() {
        return settingType;
    }

    /**
     * Get setting validator
     *
     * @return the setting validator or null if no validation is needed
     */
    public Validator getValidator() {
        return validator;
    }

    /**
     * Validate given string value as value candidate for this setting
     *
     * @param stringValue
     *            the value to validate
     * @throws SettingValidationException
     *             if given value fail validation
     */
    public void validate(String stringValue) throws SettingValidationException {

        if (validator != null) {

            // Validate
            validator.validate(this, stringValue);

        }

    }

    public String stringify() throws Exception {

        return stringifier.stringify(this);

    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return "Setting [key=" + key + ", settingType=" + settingType
                + "]";
    }
}
