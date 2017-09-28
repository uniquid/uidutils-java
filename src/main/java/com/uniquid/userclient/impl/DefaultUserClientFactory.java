package com.uniquid.userclient.impl;

import com.uniquid.userclient.UserClient;
import com.uniquid.userclient.UserClientFactory;

/**
 * Default User Client Factory that returns currently only MQTTUserClient
 */
public class DefaultUserClientFactory implements UserClientFactory {
	
	private String mqttBroker;
	private int timeout;
	
	public DefaultUserClientFactory(String mqttBroker, int timeout) {
		this.mqttBroker = mqttBroker;
		this.timeout = timeout;
	}

	@Override
	public UserClient getUserClient(UserClientFactoryConfiguration configuration) {
		
		return new MQTTUserClient(mqttBroker, configuration.getProviderName(), timeout, configuration.getUserAddress());
		
	}
	
}
