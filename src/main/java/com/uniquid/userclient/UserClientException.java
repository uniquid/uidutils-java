package com.uniquid.userclient;

public class UserClientException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * Create new UserClientException exception
     *
     * @param message
     *            the message
     * @param cause
     *            the cause
     */
    public UserClientException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Create new UserClientException exception
     *
     * @param message
     *            the message
     */
    public UserClientException(String message) {
        super(message);
    }

}
