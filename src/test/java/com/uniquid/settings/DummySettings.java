/*
 * Copyright (c) 2016-2018. Uniquid Inc. or its affiliates. All Rights Reserved.
 *
 * License is in the "LICENSE" file accompanying this file.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package com.uniquid.settings;

import com.uniquid.settings.exception.SettingValidationException;
import com.uniquid.settings.exception.UnknownSettingException;
import com.uniquid.settings.model.AbstractSettings;
import com.uniquid.settings.model.Setting;
import com.uniquid.settings.validator.NotEmpty;

import java.util.Properties;

public class DummySettings extends AbstractSettings {

    public static final Setting DUMMY = new Setting(
            "dummy",
            "Dummy Setting",
            "Dummy Setting",
            "Dummy", new NotEmpty());

    public DummySettings() throws SettingValidationException, UnknownSettingException {
        super();
    }

    public DummySettings(Properties p) throws SettingValidationException, UnknownSettingException {
        super(p);
    }

    public String getDummy() {

        return getAsString(DUMMY);

    }

}
