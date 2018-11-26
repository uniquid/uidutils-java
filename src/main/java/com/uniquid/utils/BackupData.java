/*
 * Copyright (c) 2016-2018. Uniquid Inc. or its affiliates. All Rights Reserved.
 *
 * License is in the "LICENSE" file accompanying this file.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package com.uniquid.utils;

public class BackupData {

    private String mnemonic;
    private String name;
    private long creationTime;

    public String getMnemonic() {
        return mnemonic;
    }


    public void setMnemonic(String mnemonic) {
        this.mnemonic = mnemonic;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public long getCreationTime() {
        return creationTime;
    }


    public void setCreationTime(long creationTime) {
        this.creationTime = creationTime;
    }

}
