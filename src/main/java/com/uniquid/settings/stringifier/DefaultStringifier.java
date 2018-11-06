/*
 * Copyright (c) 2016-2018. Uniquid Inc. or its affiliates. All Rights Reserved.
 *
 * License is in the "LICENSE" file accompanying this file.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package com.uniquid.settings.stringifier;

import com.uniquid.settings.exception.StringifyException;
import com.uniquid.settings.model.Setting;
import com.uniquid.settings.model.SettingType;
import com.uniquid.settings.model.Stringifier;

/**
 * Default implementation for Stringify a Setting
 */
public class DefaultStringifier implements Stringifier {

    @Override
    public String stringify(Setting setting) throws StringifyException {

        // Get setting type
        SettingType settingType = setting.getSettingType();

        if (SettingType.DOUBLE.equals(settingType)) {

            // Get double value
            Double doubleValue = (Double) setting.getDefaultValue();

            // Validate as double
            return String.valueOf(doubleValue);

        } else if (SettingType.INTEGER.equals(settingType)) {

            // Get integer value
            Integer integerValue = (Integer) setting.getDefaultValue();

            // Validate as integer
            return String.valueOf(integerValue);

        } else if (SettingType.LONG.equals(settingType)) {

            // Get long value
            Long longValue = (Long) setting.getDefaultValue();

            // Validate as long
            return String.valueOf(longValue);

        } else if (SettingType.STRING.equals(settingType)) {

            // Validate as string
            return (String) setting.getDefaultValue();

        } else if (SettingType.BOOLEAN.equals(settingType)) {

            // Validate as string
            return String.valueOf(setting.getDefaultValue());

        } else {

            throw new StringifyException("Unknown type");

        }

    }


}
