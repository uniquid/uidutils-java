package com.uniquid.utils;

import com.uniquid.encryption.AESUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;

public class SeedUtils<T extends BackupData> {

	private File seedFile;

	public SeedUtils(File seedFile) {
		this.seedFile = seedFile;
	}

	public void readData(String password, T backupData) throws Exception {

		FileInputStream fis = new FileInputStream(seedFile);

		int content;
		StringBuffer stringBuffer = new StringBuffer();
		while ((content = fis.read()) != -1) {

			stringBuffer.append((char) content);

		}

		fis.close();

		final JSONObject jsonMessage = new JSONObject(stringBuffer.toString());
		String iv = jsonMessage.getString("initvector");
		String encoded = jsonMessage.getString("encdata");

		String decrypted = AESUtils.decrypt(encoded, iv, password);

		final JSONObject secureData = new JSONObject(decrypted);

		backupDataFromJSON(secureData, backupData);

	}

	protected void backupDataFromJSON(JSONObject secureData, T backupData) throws JSONException {

		final String mnemonic = secureData.getString("mnemonic");
		final long creationTime = secureData.getLong("creationTime");
		final String name = secureData.getString("name");

		backupData.setMnemonic(mnemonic);
		backupData.setCreationTime(creationTime);
		backupData.setName(name);

	}

	public void saveData(T backupData, String password) throws Exception {

		BufferedWriter writer = new BufferedWriter(new FileWriter(seedFile));

		JSONObject jsonObject = new JSONObject();

		backupDataToJSON(backupData, jsonObject);

		String[] encryptionResult = AESUtils.encrypt(jsonObject.toString(), password);

		jsonObject = new JSONObject();
		jsonObject.put("initvector", encryptionResult[0]);
		jsonObject.put("encdata", encryptionResult[1]);

		writer.write(jsonObject.toString());

		writer.flush();

		writer.close();

	}

	protected void backupDataToJSON(T backupData, JSONObject jsonObject) throws JSONException {

		jsonObject.put("mnemonic", backupData.getMnemonic());
		jsonObject.put("creationTime", backupData.getCreationTime());
		jsonObject.put("name", backupData.getName());

	}

	public static void main(String[] args) throws Exception {
		
		SeedUtils<BackupData> seedUtils = new SeedUtils<>(new File(args[0]));

		BackupData backupData = new BackupData();

		seedUtils.readData(args[1], backupData);

		System.out.println("Mnemonics: " + backupData.getMnemonic());
		System.out.println("Creation time: " + backupData.getCreationTime());
		System.out.println("Node name: " + backupData.getName());
	}

}
