/*
 * Copyright (c) 2016-2018. Uniquid Inc. or its affiliates. All Rights Reserved.
 *
 * License is in the "LICENSE" file accompanying this file.
 * See the License for the specific language governing permissions and limitations under the License.
 */

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
