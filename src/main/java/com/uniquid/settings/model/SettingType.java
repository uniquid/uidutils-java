package com.uniquid.settings.model;

/**
 * Define valid type of {@link Setting}
 */
public enum SettingType {

	BOOLEAN("BOOLEAN", Boolean.class),
	DOUBLE("DOUBLE", Double.class),
	INTEGER("INTEGER", Integer.class),
	LONG("LONG", Long.class),
	STRING("STRING", String.class),
	ENUM("ENUM", Enum.class);

	private String id;
	private Class<?> valueClass;

	SettingType(String id, Class<?> valueClass) {

		this.id = id;
		this.valueClass = valueClass;

	}

	/**
	 * Get id of this setting type
	 *
	 * @return the id of this class
	 */
	public String getId() {
		return id;
	}

	/**
	 * Get value class for this setting type
	 *
	 * @return the value class
	 */
	public Class<?> getValueClass() {
		return valueClass;
	}

	/**
	 * Get setting type which value can be represented by an object of given
	 * value class
	 *
	 * @param valueClass
	 *            the value class
	 * @return the associated setting type
	 * @throws RuntimeException
	 *             if not setting type is found for given class
	 */
	public static SettingType valueClassOf(Class<?> valueClass) {

		// Get value of setting type
		SettingType[] values = SettingType.values();

		for (SettingType settingType : values) {

			// Get next value
			if (settingType.getValueClass().isAssignableFrom(valueClass)) {
				return settingType;
			}

		}

		throw new RuntimeException("unknow value class: " + valueClass);

	}
}
