package com.uniquid.settings;

import java.util.Properties;

import com.uniquid.settings.exception.SettingValidationException;
import com.uniquid.settings.exception.UnknownSettingException;
import com.uniquid.settings.model.AbstractSettings;
import com.uniquid.settings.model.Setting;
import com.uniquid.settings.validator.NotEmpty;

public class DummySettings extends AbstractSettings {
	
	public static final Setting DUMMY = new Setting(
			"dummy",
			"Dummy Setting",
			"Dummy Setting",
			"Dummy", new NotEmpty());

	public DummySettings() throws SettingValidationException, UnknownSettingException {
		super();
	}
	
	public DummySettings(Properties p) throws SettingValidationException, UnknownSettingException {
		super(p);
	}

	public String getDummy() {
		
		return getAsString(DUMMY);
		
	}

}
