package com.uniquid.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;

import org.json.JSONObject;

import com.uniquid.encryption.AESUtils;

public class SeedUtils {
	
	private File seedFile;
	
	public SeedUtils(File seedFile) {
		this.seedFile = seedFile;
	}
	
	
	public Object[] readData(String password) throws Exception {
		
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

		final String mnemonic = secureData.getString("mnemonic");
		final int creationTime = secureData.getInt("creationTime");
		
		Object[] readData = new Object[2];
		readData[0] = mnemonic;
		readData[1] = creationTime;
		
		return readData;
		
	}
	
	public void saveData(Object[] sensitiveData, String password) throws Exception {
		
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("mnemonic", sensitiveData[0]);
		jsonObject.put("creationTime", sensitiveData[1]);
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(seedFile));
		
		String[] encryptionResult = AESUtils.encrypt(jsonObject.toString(), password);
		
		jsonObject = new JSONObject();
		jsonObject.put("initvector", encryptionResult[0]);
		jsonObject.put("encdata", encryptionResult[1]);
		
		writer.write(jsonObject.toString());
		
		writer.flush();
		
		writer.close();
		
	}
	
	public static void main(String[] args) throws Exception {
		SeedUtils seedUtils = new SeedUtils(new File(args[0]));
		
		Object[] readData = seedUtils.readData(args[1]);
		
		System.out.println("Mnemonics: " + readData[0]);
		System.out.println("Creation time: " + readData[1]);
	}

}
