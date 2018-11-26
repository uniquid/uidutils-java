/*
 * Copyright (c) 2016-2018. Uniquid Inc. or its affiliates. All Rights Reserved.
 *
 * License is in the "LICENSE" file accompanying this file.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package com.uniquid.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * Extension of Properties class that allows to perform a get and transform
 * automatically the value to a primitive type
 */
public class ParsableProperties extends Properties {

    private static final Logger LOGGER = LoggerFactory.getLogger(ParsableProperties.class);

    private static final long serialVersionUID = 1L;

    /**
     * Get value as boolean
     *
     * @param key
     *            key
     * @param defaultValue
     *            default value
     * @return boolean value according to Boolean(String) constructor; default
     *         value if property value is null
     */
    public boolean getAsBoolean(String key, boolean defaultValue) {

        String stringValue = getProperty(key);

        if (stringValue != null) {
            return Boolean.valueOf(stringValue);
        }

        return defaultValue;
    }

    /**
     * Get as double
     *
     * @param key
     *            key
     * @param defaultValue
     *            default value
     * @return double value or default value if value cannot be parse to double
     */
    public double getAsDouble(String key, double defaultValue) {

        String stringValue = getProperty(key);

        if (stringValue != null) {
            try {
                return Double.valueOf(stringValue);

            } catch (NumberFormatException ex) {
                LOGGER.warn("unable to get value for key: " + key + ": ", ex);
            }
        }

        return defaultValue;
    }

    /**
     * Get value as integer
     *
     * @param key
     *            key
     * @param defaultValue
     *            default value
     * @return int value or default value if value cannot be parse to integer
     */
    public int getAsInteger(String key, int defaultValue) {

        String stringValue = getProperty(key);

        if (stringValue != null) {
            try {
                return Integer.valueOf(stringValue);

            } catch (NumberFormatException ex) {
                LOGGER.warn("unable to get value for key: " + key + ": ", ex);
            }
        }
        return defaultValue;

    }

    /**
     * Get a value as long
     *
     * @param key
     *            key
     * @param defaultValue
     *            default value
     * @return long value or default value if value cannot be parse to long
     */
    public long getAsLong(String key, long defaultValue) {

        String stringValue = getProperty(key);

        if (stringValue != null) {
            try {
                return Long.valueOf(stringValue);

            } catch (NumberFormatException ex) {
                LOGGER.warn("unable to get value for key: " + key + ": ", ex);
            }
        }
        return defaultValue;

    }

    /**
     * Get a value as string
     *
     * @param key
     *            key
     * @param defaultValue
     *            default value
     * @return string value or default value if value is null
     */
    public String getAsString(String key, String defaultValue) {

        String value = getProperty(key);

        if (value != null) {
            return value;
        } else {
            LOGGER.warn("unable to get value for key: " + key);
            return defaultValue;
        }
    }

    /**
     * Get value as enum
     *
     * @param enumType the {@link Class} object of the enum type from which to return a constant
     * @param key the key of the property to retrieve
     * @param defaultValue the default value to return in case there is no value for the given key
     * @return the value of the given key property
     */
    public Enum<?> getAsEnum(Class enumType, String key, Enum defaultValue) {

        String value = getProperty(key);

        if (value != null) {
            try {
                return Enum.valueOf(enumType, value);

            } catch (NullPointerException | IllegalArgumentException ex) {
                LOGGER.warn("unable to get value for key: " + key + ": ", ex);
            }
        }
        return defaultValue;
    }
}
