package com.uniquid.userclient;

/**
 * Return the proper UserClient instance to communicate with a client of specified type
 *
 */
public interface UserClientFactory {

	/**
	 * Represent generic configuration used to lookup at runtime the proper client 
	 */
	public static interface UserClientFactoryConfiguration {
		
		/**
		 * Return the protocol that the Provider can Talk
		 * @return
		 */
		public String getProviderProtocol();
		
		/**
		 * Return the provider name
		 * @return
		 */
		public String getProviderName();
		
		/**
		 * Return the user address
		 * @return
		 */
		public String getUserAddress();
		
	}
	/**
	 * Returns the UserClient compatibile with the specified configuration
	 * @param configuration
	 * @return
	 */
	public UserClient getUserClient(UserClientFactoryConfiguration configuration);
	
}
