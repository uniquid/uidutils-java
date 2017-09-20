package com.uniquid.utils;

public interface ResponseDecoder<T> {
	
	public int getExpectedResponseCode();
	
	public T manageResponse(String serverResponse) throws Exception;
	
	public T manageUnexpectedResponseCode(int responseCode, String responseMessage) throws Exception;

}
