package com.uniquid.userclient;

/**
 * Return the proper UserClient instance to communicate with a client of specified type
 *
 */
public interface UserClientFactory {

    /**
     * Represent generic configuration used to lookup at runtime the proper client
     */
    interface UserClientFactoryConfiguration {

        /**
         * Return the protocol that the Provider can Talk
         * @return the protocol that the Provider can Talk
         */
        String getProviderProtocol();

        /**
         * Return the provider name
         * @return the probider name
         */
        String getProviderName();

        /**
         * Return the user address
         * @return the user address
         */
        String getUserAddress();

    }
    /**
     * Returns the UserClient compatibile with the specified configuration
     * @param configuration the {@link UserClientFactoryConfiguration}
     * @return the {@link UserClient}
     */
    UserClient getUserClient(UserClientFactoryConfiguration configuration);

}
