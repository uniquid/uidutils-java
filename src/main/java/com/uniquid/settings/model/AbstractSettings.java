package com.uniquid.settings.model;

import com.uniquid.settings.exception.SettingValidationException;
import com.uniquid.settings.exception.UnknownSettingException;
import com.uniquid.utils.ParsableProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * Abstract class for application settings.
 */
public abstract class AbstractSettings {

    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractSettings.class);

    private final Map<String, Setting> settings;

    private final List<SettingsListener> settingsListeners;

    private final ParsableProperties parsableProperties;

    /**
     * Create new settings
     *
     * @throws UnknownSettingException in case a problem occurs
     */
    public AbstractSettings() throws SettingValidationException, UnknownSettingException {
        this(new Properties(), new HashSet<>());
    }

    public AbstractSettings(Properties properties) throws SettingValidationException, UnknownSettingException {
        this(properties, new HashSet<>());
    }

    /**
     * Create new settings
     *
     * @param excludedSettings
     *            a set of settings that should be excluded as {@link Setting}
     * @throws UnknownSettingException in case a problem occurs
     */
    public AbstractSettings(Properties properties, Set<Setting> excludedSettings)
            throws SettingValidationException, UnknownSettingException {

        // Get settings
        settings = getSettings(this.getClass(), excludedSettings);

        settingsListeners = new ArrayList<>();

        parsableProperties = new ParsableProperties();

        loadFromPropertiesWithoutFireEvent(properties);

    }

    /**
     * Load settings from properties
     *
     * @throws UnknownSettingException in case a problem occurs
     *
     * @throws SettingValidationException in case occurs a problem validating the setting
     */
    private boolean loadFromPropertiesWithoutFireEvent(Properties properties)
            throws SettingValidationException, UnknownSettingException {

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

        Collection<Setting> settings = getSettings();

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
    public Collection<Setting> getSettings() {

        return settings.values();

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

        return settings.get(key);

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
        return parsableProperties.getAsBoolean(setting.getKey(), (Boolean)setting.getDefaultValue());
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
        return parsableProperties.getAsDouble(setting.getKey(), (Double)setting.getDefaultValue());
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
        return parsableProperties.getAsInteger(setting.getKey(), (Integer)setting.getDefaultValue());
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
        return parsableProperties.getAsLong(setting.getKey(), (Long)setting.getDefaultValue());
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
        return parsableProperties.getAsString(setting.getKey(), (String)setting.getDefaultValue());
    }

    /**
     * Get setting value as Enum
     *
     * @param setting the setting to retrieve
     * @return the value of the setting
     */
    protected Enum getAsEnum(Setting setting) {
        return parsableProperties.getAsEnum(setting.getDefaultValue().getClass(), setting.getKey(), (Enum)setting.getDefaultValue());
    }

    /**
     * Save setting
     */
    private void setSettingWithoutFiringEvents(String key, String value)
            throws SettingValidationException, UnknownSettingException {

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
    private void setSettingWithoutFiringEvents(Setting setting, String value)
            throws SettingValidationException, UnknownSettingException {

        setSettingWithoutFiringEvents(setting.getKey(), value);

    }

    /**
     * Save setting
     *
     * @param setting the {@link Setting} to save
     * @param value the value of the setting to save
     *
     * @throws SettingValidationException in case occurs a problem validating the setting
     * @throws UnknownSettingException in case the setting is unknown
     *
     */
    public void setSetting(Setting setting, String value) throws SettingValidationException, UnknownSettingException {

        setSettingWithoutFiringEvents(setting, value);

        fireSettingsChanged();

    }

    /**
     * Save setting
     *
     * @param setting the name of the setting to save
     * @param value the value of the setting to save
     *
     * @throws UnknownSettingException in case the setting is unknown
     * @throws SettingValidationException in case occurs a problem validating a setting
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
    private static Map<String, Setting> getSettings(Class<?> targetClass, Set<Setting> excludedSettings) {

        Map<String, Setting> settings = new HashMap<>();

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
                boolean isStaticAndFinal = Modifier.isStatic(modifiers) && Modifier.isFinal(modifiers);

                if (isStaticAndFinal) {

                    try {

                        // Get field object
                        Setting setting = (Setting) field.get(null);

                        // Get excluded
                        boolean excluded = excludedSettings.contains(setting);

                        if (!excluded) {

                            // Log
                            LOGGER.debug("found setting: " + setting);

                            // Add setting
                            settings.put(setting.getKey(), setting);

                        } else {

                            // Log
                            LOGGER.debug("ignore found setting since it is on excluded set: " + setting);

                        }

                    } catch (IllegalAccessException ex) {

                        // Log
                        LOGGER.error("an error occurs retrieving settings: " + fieldName, ex);

                    }

                } else {

                    // Log
                    LOGGER.warn(
                            "an error occurs retrieving setting: " + fieldName + ", must be declared as static final");

                }

            }

        }

        return settings;

    }

    /**
     * Invoke {@link SettingsListener#settingChanged(AbstractSettings)} for
     * every setting listener added to this class
     */
    private void fireSettingsChanged() {

        // Log
        LOGGER.info("Notify settings listeners...");

        // Clone settings listeners
        List<SettingsListener> settingsListeners = new ArrayList<>(this.settingsListeners);

        for (SettingsListener settingsListener : settingsListeners) {

            // Invoke settings changed
            settingsListener.settingChanged(this);

        }

    }

}
