package com.uniquid.settings.model;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.uniquid.settings.exception.SettingValidationException;
import com.uniquid.settings.exception.UnknownSettingException;
import com.uniquid.utils.ParsableProperties;

/**
 * Abstract class for application settings.
 */
public abstract class AbstractSettings {
	
	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractSettings.class);

	private final List<Setting> settings;

	private final List<SettingsListener> settingsListeners;
	
	private final ParsableProperties parsableProperties;
	
	/**
	 * Create new settings using given settings store
	 * 
	 * @param settingsStore
	 *            the settings store
	 * @throws UnknownSettingException 
	 */
	public AbstractSettings() throws SettingValidationException, UnknownSettingException {
		this(new Properties(), new HashSet<Setting>());
	}
	
	public AbstractSettings(Properties properties) throws SettingValidationException, UnknownSettingException {
		this(properties, new HashSet<Setting>());
	}

	/**
	 * Create new settings using given settings store
	 * 
	 * @param settingsStore
	 *            the settings store
	 * @param excludedSettings
	 *            a set of settings that should be excluded as {@link Setting}
	 * @throws UnknownSettingException 
	 */
	public AbstractSettings(Properties properties, Set<Setting> excludedSettings) throws SettingValidationException, UnknownSettingException {

		// Get settings
		settings = getSettings(this.getClass(), excludedSettings);

		settingsListeners = new ArrayList<SettingsListener>();
		
		parsableProperties = new ParsableProperties();
		
		loadFromPropertiesWithoutFireEvent(properties);

	}
	
	/**
	 * Load settings from properties
	 * @throws UnknownSettingException 
	 * 
	 * @throws IOException
	 */
	private boolean loadFromPropertiesWithoutFireEvent(Properties properties) throws SettingValidationException, UnknownSettingException {

		Set<String> keys = properties.stringPropertyNames();
		
		boolean settingsChanged = false;
		
		for (String key : keys) {
			
			Setting setting = getSetting(key);
			
			if (setting != null) {
				
				setSettingWithoutFiringEvents(setting, properties.getProperty(key));
				
				settingsChanged = true;
				
			}
			
		}
		
		return settingsChanged;
			
	}
	
	public void loadFromProperties(Properties properties) throws SettingValidationException, UnknownSettingException {
		
		if (loadFromPropertiesWithoutFireEvent(properties)) {
			
			fireSettingsChanged();
			
		}
		
	}

	/**
	 * Get a properties contains all properties on this settings object
	 * 
	 * @return properties
	 */
	public Properties getProperties() throws Exception {

		// Create properties
		Properties properties = new Properties();
		
		List<Setting> settings = getSettings();
		
		for (Setting setting : settings) {
			
			properties.setProperty(setting.getKey(), setting.stringify());
			
		}

		// Put all properties from settings
		properties.putAll(parsableProperties);

		return properties;

	}

	/**
	 * Get list of settings
	 * 
	 * @return list of settings as {@link Setting}
	 */
	public List<Setting> getSettings() {
		
		List<Setting> newList = new ArrayList<Setting>();
		
		for (Setting setting : settings) {
			
			newList.add(setting);
			
		}
		
		return newList;
	}

	/**
	 * Get setting with given key
	 * 
	 * @param key
	 *            the setting key
	 * @return the setting with given key or null if no setting was found with
	 *         given key
	 */
	protected Setting getSetting(String key) {

		for (Setting setting : settings) {

			if (setting.getKey().equals(key)) {
				return setting;
			}

		}

		return null;

	}

	/**
	 * Add a setting listener
	 * 
	 * @param settingsListener
	 *            the setting listener
	 */
	public void addSettingsListener(SettingsListener settingsListener) {
		settingsListeners.add(settingsListener);
	}

	/**
	 * Remove a setting listener
	 * 
	 * @param settingsListener
	 *            the setting listener
	 */
	public void removeSettingsListener(SettingsListener settingsListener) {
		settingsListeners.remove(settingsListener);
	}

	/**
	 * Remove all setting listener
	 */
	public void removeSettingsListeners() {
		settingsListeners.clear();
	}

	/**
	 * Get setting value as boolean
	 * 
	 * @param setting
	 *            the setting to be retrieved
	 * @return the setting value
	 * @throws ClassCastException
	 *             if setting value class is other then {@link Boolean}
	 */
	protected boolean getAsBoolean(Setting setting) {

		// Get as boolean
		Boolean booleanValue = parsableProperties.getAsBoolean(setting.getKey());

		if (booleanValue == null) {
			booleanValue = (Boolean) setting.getDefaultValue();
		}

		// Get boolean value
		boolean value = booleanValue.booleanValue();

		return value;

	}

	/**
	 * Get setting value as double
	 * 
	 * @param setting
	 *            the setting to be retrieved
	 * @return the setting value
	 * @throws ClassCastException
	 *             if setting value class is other then {@link Double}
	 */
	protected double getAsDouble(Setting setting) {

		// Get as double
		Double doubleValue = parsableProperties.getAsDouble(setting.getKey());

		if (doubleValue == null) {
			doubleValue = (Double) setting.getDefaultValue();
		}

		// Parse to double
		double value = doubleValue.doubleValue();

		return value;

	}

	/**
	 * Get setting value as integer
	 * 
	 * @param setting
	 *            the setting to be retrieved
	 * @return the setting value
	 * @throws ClassCastException
	 *             if setting value class is other then {@link Integer}
	 */
	protected int getAsInteger(Setting setting) {

		// Get as integer
		Integer integerValue = parsableProperties.getAsInteger(setting.getKey());

		if (integerValue == null) {
			integerValue = (Integer) setting.getDefaultValue();
		}

		// Parse to int
		int value = integerValue.intValue();

		return value;

	}

	/**
	 * Get setting value as long
	 * 
	 * @param setting
	 *            the setting to be retrieved
	 * @return the setting value
	 * @throws ClassCastException
	 *             if setting value class is other then {@link Long}
	 */
	protected long getAsLong(Setting setting) {

		// Get as long
		Long longValue = parsableProperties.getAsLong(setting.getKey());

		if (longValue == null) {
			longValue = (Long) setting.getDefaultValue();
		}

		// Parse to long
		long value = longValue.longValue();

		return value;

	}

	/**
	 * Get setting value as string
	 * 
	 * @param setting
	 *            the setting to be retrieved
	 * @return the setting value
	 * @throws ClassCastException
	 *             if setting value class is other then {@link String}
	 */
	protected String getAsString(Setting setting) {

		// Get value
		String value = parsableProperties.getAsString(setting.getKey());

		if (value == null) {
			value = (String) setting.getDefaultValue();
		}

		return value;

	}

	/**
	 * Save setting
	 */
	private void setSettingWithoutFiringEvents(String key, String value) throws SettingValidationException, UnknownSettingException {

		// Get setting
		Setting managedSetting = getSetting(key);

		if (managedSetting != null) {
		
			// validate
			managedSetting.validate(value);
			
			// Set property
			parsableProperties.setProperty(key, value);
			
		} else {
			
			throw new UnknownSettingException("Unmanaged setting " + key);
			
		}
		
	}
	
	/**
	 * Save setting
	 */
	private void setSettingWithoutFiringEvents(Setting setting, String value) throws SettingValidationException, UnknownSettingException {

		setSettingWithoutFiringEvents(setting.getKey(), value);
		
	}
	
	/**
	 * Save setting
	 * @throws UnknownSettingException 
	 * 
	 */
	public void setSetting(Setting setting, String value) throws SettingValidationException, UnknownSettingException {

		setSettingWithoutFiringEvents(setting, value);
			
		fireSettingsChanged();
			
	}
	
	/**
	 * Save setting
	 * @throws UnknownSettingException 
	 * 
	 */
	public void setSetting(String setting, String value) throws SettingValidationException, UnknownSettingException {

		setSettingWithoutFiringEvents(setting, value);
			
		fireSettingsChanged();
			
	}

	/**
	 * Get list of setting of given target class. This method will access all
	 * static final field of type {@link Setting} and return a list containing
	 * those objects.
	 * 
	 * @param targetClass
	 *            the target class
	 * @param excludedSettings
	 *            a set of settings that should be excluded as {@link Setting}
	 * @return a list of setting as {@link Setting}
	 */
	private static List<Setting> getSettings(Class<?> targetClass, Set<Setting> excludedSettings) {

		List<Setting> settings = new ArrayList<Setting>();

		// Log
		LOGGER.info("analyzing class for settings: " + targetClass);

		Field[] fields = targetClass.getFields();

		for (Field field : fields) {

			// Get field name
			String fieldName = field.getName();

			// Get field class
			Class<?> fieldClass = field.getType();

			if (Setting.class.isAssignableFrom(fieldClass)) {

				// Get modifiers
				int modifiers = field.getModifiers();

				// Get is static and final
				boolean isStaticAndFinal = Modifier.isStatic(modifiers)
						&& Modifier.isFinal(modifiers);

				if (isStaticAndFinal) {

					try {

						// Get field object
						Setting setting = (Setting) field.get(null);

						// Get excluded
						boolean excluded = excludedSettings.contains(setting);

						if (!excluded) {

							// Log
							LOGGER.info("found setting: " + setting);

							// Add setting
							settings.add(setting);

						} else {

							// Log
							LOGGER.info("ignore found setting since it is on excluded set: "
									+ setting);

						}

					} catch (IllegalAccessException ex) {

						// Log
						LOGGER.error("an error occurs retrieving settings: "
								+ fieldName, ex);

					}

				} else {

					// Log
					LOGGER.warn("an error occurs retrieving setting: "
							+ fieldName + ", must be declared as static final");

				}

			}

		}

		return settings;

	}

	/**
	 * Invoke {@link SettingsListener#settingChanged(AbstractSettings)} for every
	 * setting listener added to this class
	 */
	private void fireSettingsChanged() {

		// Log
		LOGGER.info("Notify settings listeners...");

		// Clone settings listeners
		List<SettingsListener> settingsListeners = new ArrayList<SettingsListener>(this.settingsListeners);

		for (SettingsListener settingsListener : settingsListeners) {

			// Invoke settings changed
			settingsListener.settingChanged(this);

		}

	}
	
}
