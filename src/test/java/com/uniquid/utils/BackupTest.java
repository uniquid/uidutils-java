package com.uniquid.utils;

import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertEquals;

/**
 * @author Beatrice Formai
 */
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
