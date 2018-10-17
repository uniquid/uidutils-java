package com.uniquid.settings.exception;

import com.uniquid.settings.model.Setting;

/**
 * Exception to signal a problem validating a setting
 */
public class SettingValidationException extends Exception {

    private static final long serialVersionUID = 1L;

    private String settingKey;

    /**
     * Create new setting validation exception
     *
     * @param message
     *            the message
     * @param cause
     *            the cause
     */
    public SettingValidationException(Setting setting, String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Create new setting validation exception
     *
     * @param message
     *            the message
     */
    public SettingValidationException(String message) {
        super(message);
    }

    /**
     * Get the key of setting for which validation fails
     *
     * @return the key of setting for which validation fails
     */
    public String getSettingKey() {
        return settingKey;
    }

    /**
     * Set the key of setting for which validation fails
     *
     * @param settingKey
     *            the key of setting for which validation fails
     */
    public void setSettingKey(String settingKey) {
        this.settingKey = settingKey;
    }
}
