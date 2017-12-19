package com.uniquid.registry.impl;

import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.uniquid.registry.RegistryDAO;
import com.uniquid.registry.exception.RegistryException;
import com.uniquid.utils.DataProvider;
import com.uniquid.utils.HttpUtils;
import com.uniquid.utils.ResponseDecoder;

public class RegistryDAOImpl implements RegistryDAO {
	
	private static final String PUT_URL = "%1&s/registry";
	private static final String GET_URL = "%1&s/registry?providerAddress=%2&s";
	
	private String registryAddress;
	
	public RegistryDAOImpl(String registryAddress) {
		this.registryAddress = registryAddress;
	}

	@Override
	public void insertMapping(String providerName, String providerAddress) throws RegistryException {
		
		try {

			JSONObject jsonMessage = new JSONObject();

			jsonMessage.put("provider_name", providerName);
			jsonMessage.put("provider_address", providerAddress);

			final byte[] postDataBytes = jsonMessage.toString().getBytes("UTF-8");

			URL url = new URL(PUT_URL.replace("%1&s", registryAddress));

			HttpUtils.sendDataWithPost(url, new DataProvider<Void>() {

				@Override
				public int getExpectedResponseCode() {
					return HttpURLConnection.HTTP_CREATED;
				}

				@Override
				public Void manageResponse(String serverResponse) throws Exception {
					return null;
				}

				@Override
				public Void manageUnexpectedResponseCode(int responseCode, String responseMessage) throws Exception {
					throw new RegistryException("Error while submitting post: " + responseCode + " " + responseMessage);
				}

				@Override
				public String getContentType() {
					return "application/json";
				}

				@Override
				public String getCharset() {
					return "utf-8";
				}

				@Override
				public byte[] getPayload() {
					return postDataBytes;
				}
			
			});
			
		} catch (Throwable t) {

			throw new RegistryException("Unexpected Exception", t);

		}

	}
	
	private static String addressFromJsonString(String string, String providerAddress) throws JSONException {

		JSONArray jsonArray = new JSONArray(string);
		
		// AVOID ITERATOR! WORKAROUND FOR ANDROID!
		int elements = jsonArray.length();
		
		for (int i = 0; i < elements; i++) {
			
			JSONObject jsonMessage = jsonArray.getJSONObject(i);

			String a = jsonMessage.getString("provider_address");
			String n = jsonMessage.getString("provider_name");
	
			if (providerAddress.equals(a)) {
				
				return n;
				
			}
			
		}
		
		return null;

	}

	@Override
	public String retrieveProviderName(final String providerAddress) throws RegistryException {
		
		try {
			URL url = new URL(GET_URL.replace("%1&s", registryAddress).replace("%2&s", providerAddress));

			return HttpUtils.retrieveDataViaHttpGet(url, new ResponseDecoder<String>() {

				@Override
				public int getExpectedResponseCode() {
					return HttpURLConnection.HTTP_OK;
				}

				@Override
				public String manageResponse(String serverResponse) throws Exception {
					return addressFromJsonString(serverResponse, providerAddress);
				}

				@Override
				public String manageUnexpectedResponseCode(int responseCode, String responseMessage) throws Exception {
					if (HttpURLConnection.HTTP_NOT_FOUND == responseCode)
						return null;
					
					throw new RegistryException("Server returned " + responseCode + " " + responseMessage);
				}
			
			});
			
		} catch (Throwable t) {
			
			throw new RegistryException("Unexpected Exception", t);
			
		}
		
	}
	
}
