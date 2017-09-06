package com.uniquid.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;

import org.json.JSONException;
import org.json.JSONObject;

import com.uniquid.encryption.AESUtils;

public class SeedUtils {
	
	private File seedFile;
	
	public SeedUtils(File seedFile) {
		this.seedFile = seedFile;
	}
	
	
	public BackupData readData(String password) throws Exception {
		
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
		
		return backupDataFromJSON(secureData);
		
	}
	
	protected BackupData backupDataFromJSON(JSONObject secureData) throws JSONException {
		final String mnemonic = secureData.getString("mnemonic");
		final long creationTime = secureData.getLong("creationTime");
		final String name = secureData.getString("name");
		
		BackupData backupData = createInstance();
		backupData.setMnemonic(mnemonic);
		backupData.setCreationTime(creationTime);
		backupData.setName(name);
		
		return backupData;
	}
	
	protected BackupData createInstance() {
		return new BackupData();
	}
	
	public void saveData(BackupData backupData, String password) throws Exception {		
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(seedFile));
		
		JSONObject jsonObject = backupDataToJSON(backupData);
		
		String[] encryptionResult = AESUtils.encrypt(jsonObject.toString(), password);
		
		jsonObject = new JSONObject();
		jsonObject.put("initvector", encryptionResult[0]);
		jsonObject.put("encdata", encryptionResult[1]);
		
		writer.write(jsonObject.toString());
		
		writer.flush();
		
		writer.close();
		
	}
	
	protected JSONObject backupDataToJSON(BackupData backupData) throws JSONException {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("mnemonic", backupData.getMnemonic());
		jsonObject.put("creationTime", backupData.getCreationTime());
		jsonObject.put("name", backupData.getName());
		
		return jsonObject;
			
	}
	
	public static void main(String[] args) throws Exception {
		SeedUtils seedUtils = new SeedUtils(new File(args[0]));
		
		BackupData backupData = seedUtils.readData(args[1]);
		
		System.out.println("Mnemonics: " + backupData.getMnemonic());
		System.out.println("Creation time: " + backupData.getCreationTime());
		System.out.println("Node name: " + backupData.getName());
	}

}
