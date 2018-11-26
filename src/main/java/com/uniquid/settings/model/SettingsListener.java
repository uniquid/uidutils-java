/*
 * Copyright (c) 2016-2018. Uniquid Inc. or its affiliates. All Rights Reserved.
 *
 * License is in the "LICENSE" file accompanying this file.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package com.uniquid.settings.model;

/**
 * Callback used to inform a subscriber that settings changed
 */
public interface SettingsListener {

    /**
     * Invoked when settings are changed
     *
     * @param settings
     *            the settings that was changed
     */
    void settingChanged(AbstractSettings settings);

}
