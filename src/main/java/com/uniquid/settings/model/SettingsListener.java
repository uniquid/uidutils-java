package com.uniquid.settings.model;

/**
 * Callback used to inform a subscriber that settings changed
 */
public interface SettingsListener {

    /**
     * Invoked when settings are changed
     *
     * @param settings
     *            the settings that was changed
     */
    void settingChanged(AbstractSettings settings);

}
