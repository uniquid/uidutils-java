package com.uniquid.settings.exception;

import com.uniquid.settings.model.Setting;

public class StringifyException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * Create new setting validation exception
	 *
	 * @param message
	 *            the message
	 * @param cause
	 *            the cause
	 */
	public StringifyException(Setting setting, String message,
							  Throwable cause) {
		super(message, cause);
	}

	/**
	 * Create new setting validation exception
	 *
	 * @param message
	 *            the message
	 */
	public StringifyException(String message) {
		super(message);
	}

}
