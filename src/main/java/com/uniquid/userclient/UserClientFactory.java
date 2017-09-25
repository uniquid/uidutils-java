package com.uniquid.userclient;

import com.uniquid.register.user.UserChannel;

/**
 * Return the proper UserClient instance to communicate with a client of specified type
 *
 */
public interface UserClientFactory {

	public UserClient getUserClient(UserChannel userChannel);
	
}
