package com.uniquid.userclient.impl;

import com.uniquid.register.user.UserChannel;
import com.uniquid.userclient.UserClient;
import com.uniquid.userclient.UserClientFactory;

public class DefaultUserClientFactory implements UserClientFactory {
	
	private String mqttBroker;
	private int timeout;
	private String senderTopic;
	
	public DefaultUserClientFactory(String mqttBroker, int timeout, String senderTopic) {
		this.mqttBroker = mqttBroker;
		this.timeout = timeout;
		this.senderTopic = senderTopic;
	}

	@Override
	public UserClient getUserClient(UserChannel userChannel) {
		
		return new MQTTUserClient(mqttBroker, userChannel.getProviderName(), timeout, senderTopic);
		
	}
	
}
