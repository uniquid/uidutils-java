package com.uniquid.utils;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Extension of Properties class that allows to perform a get and transform automatically the value to a primitive type 
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

		// Get as boolean
		Boolean booleanValue = getAsBoolean(key);

		if (booleanValue == null) {
			return defaultValue;
		}

		// Get boolean value
		boolean value = booleanValue.booleanValue();

		return value;

	}
	
	/**
	 * Get value as boolean
	 * 
	 * @param key
	 *            key
	 * @return boolean value according to Boolean(String) constructor; null if
	 *         property value is null;
	 */
	public Boolean getAsBoolean(String key) {

		// Get value
		String stringValue = getProperty(key);

		if (stringValue == null) {
			return null;
		}

		// Parse to boolean
		Boolean value = new Boolean(stringValue);

		return value;

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

		// Get as double
		Double doubleValue = getAsDouble(key);

		if (doubleValue == null) {
			return defaultValue;
		}

		// Parse to double
		double value = doubleValue.doubleValue();

		return value;

	}
	
	/**
	 * Get as double
	 * 
	 * @param key
	 *            key
	 * @return double value or null if property value is null or property value
	 *         cannot be parsed
	 */
	public Double getAsDouble(String key) {

		// Get value
		String stringValue = getProperty(key);

		try {

			if (stringValue == null) {
				return null;
			}

			// Parse to double
			Double value = new Double(stringValue);

			return value;

		} catch (NumberFormatException ex) {

			// Log
			LOGGER.warn("unable to get value for key: " + key + ": ", ex);

			return null;

		}

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

		// Get as integer
		Integer integerValue = getAsInteger(key);

		if (integerValue == null) {
			return defaultValue;
		}

		// Parse to int
		int value = integerValue.intValue();

		return value;

	}
	
	/**
	 * Get value as integer
	 * 
	 * @param key
	 *            key
	 * @return int value or null if property value is null or property value
	 *         cannot be parsed
	 */
	public Integer getAsInteger(String key) {

		// Get setting value
		String stringValue = getProperty(key);

		try {

			// Parse to int
			Integer value = new Integer(stringValue);

			return value;

		} catch (NumberFormatException ex) {

			// Log
			LOGGER.warn("unable to get value for key: " + key + ": ", ex);

			return null;

		}

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

		// Get as long
		Long longValue = getAsLong(key);

		if (longValue == null) {
			return defaultValue;
		}

		// Parse to long
		long value = longValue.longValue();

		return value;

	}
	
	/**
	 * Get a value as long
	 * 
	 * @param key
	 *            key
	 * @return long value or null if property value is null or property value
	 *         cannot be parsed
	 */
	public Long getAsLong(String key) {

		// Get value
		String stringValue = getProperty(key);

		try {

			// Parse to long
			Long value = new Long(stringValue);

			return value;

		} catch (NumberFormatException ex) {

			// Log
			LOGGER.warn("unable to get value for key: " + key + ": ", ex);

			return null;

		}

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

		// Get value
		String value = getProperty(key);

		if (value == null) {
			return defaultValue;
		}

		return value;

	}
	
	/**
	 * Get a value as string
	 * 
	 * @param key
	 *            key
	 * @return string value or null if property value is null
	 */
	public String getAsString(String key) {

		// Get value
		String value = getProperty(key);

		if (value != null) {
			return value;
		} else {

			// Log
			LOGGER.warn("unable to get value for key: " + key);

			return null;

		}

	}

	/**
	 * Get value as boolean
	 * 
	 * @param key
	 *            key
	 * @return boolean value according to Boolean(String) constructor
	 * @throws RuntimeException
	 *             if no value found for given key
	 */
	public boolean getMandatoryAsBoolean(String key) {

		// Get as boolean
		Boolean booleanValue = getAsBoolean(key);

		if (booleanValue == null) {
			throw new RuntimeException("unable to get value for key: " + key);
		}

		// Get boolean value
		boolean value = booleanValue.booleanValue();

		return value;

	}

	/**
	 * Get as double
	 * 
	 * @param key
	 *            key
	 * @return double value
	 * @throws RuntimeException
	 *             if no value found for given key or value cannot be parsed
	 */
	public double getMandatoryAsDouble(String key) {

		// Get as double
		Double doubleValue = getAsDouble(key);

		if (doubleValue == null) {
			throw new RuntimeException("unable to get value for key: " + key);
		}

		// Parse to double
		double value = doubleValue.doubleValue();

		return value;

	}

	/**
	 * Get value as integer
	 * 
	 * @param key
	 *            key
	 * @return int value
	 * @throws RuntimeException
	 *             if no value found for given key or value cannot be parsed
	 */
	public int getMandatoryAsInteger(String key) {

		// Get as integer
		Integer integerValue = getAsInteger(key);

		if (integerValue == null) {
			throw new RuntimeException("unable to get value for key: " + key);
		}

		// Parse to int
		int value = integerValue.intValue();

		return value;

	}

	/**
	 * Get a value as long
	 * 
	 * @param key
	 *            key
	 * @return long value
	 * @throws RuntimeException
	 *             if no value found for given key or value cannot be parsed
	 */
	public long getMandatoryAsLong(String key) {

		// Get as long
		Long longValue = getAsLong(key);

		if (longValue == null) {
			throw new RuntimeException("unable to get value for key: " + key);
		}

		// Parse to long
		long value = longValue.longValue();

		return value;

	}

	/**
	 * Get a value as string
	 * 
	 * @param key
	 *            key
	 * @return string value
	 * @throws RuntimeException
	 *             if no value found for given key
	 */
	public String getMandatoryAsString(String key) {

		// Get value
		String value = getProperty(key);

		if (value == null) {
			throw new RuntimeException("unable to get value for key: " + key);
		}

		return value;

	}

}
