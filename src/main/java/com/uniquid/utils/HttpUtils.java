package com.uniquid.utils;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpUtils {
	
	static String postRequest(String server, String contentType, String payload, int expectedResponseCode)
			throws MalformedURLException, IOException {

		URL url = new URL(server);
		HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
		httpURLConnection.setDoOutput(true);
		httpURLConnection.setRequestMethod("POST");
		httpURLConnection.setRequestProperty("Content-Type", contentType);
		httpURLConnection.connect();
		DataOutputStream dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());
		dataOutputStream.writeBytes(payload);
		dataOutputStream.flush();
		dataOutputStream.close();

		if (httpURLConnection.getResponseCode() == expectedResponseCode) {

			return httpURLConnection.getResponseMessage();

		} else {

			return "responseCode: " + httpURLConnection.getResponseCode();

		}

	}

}
