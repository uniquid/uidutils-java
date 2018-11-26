/*
 * Copyright (c) 2016-2018. Uniquid Inc. or its affiliates. All Rights Reserved.
 *
 * License is in the "LICENSE" file accompanying this file.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package com.uniquid.utils;

import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertEquals;

public class BackupTest {

    @Test
    public void testBackup() throws Exception {
        BackupData backupData = new BackupData();
        backupData.setName("test");
        backupData.setMnemonic("mnemonic test");
        backupData.setCreationTime(1540479039);

        File backup = new File("test");
        SeedUtils<BackupData> seedUtils = new SeedUtils<>(backup);
        seedUtils.saveData(backupData, "password");

        BackupData restored = new BackupData();
        seedUtils.readData("password", restored);

        assertEquals(backupData.getName(), restored.getName());
        assertEquals(backupData.getMnemonic(), restored.getMnemonic());
        assertEquals(backupData.getCreationTime(), restored.getCreationTime());

        backup.deleteOnExit();

    }
}
