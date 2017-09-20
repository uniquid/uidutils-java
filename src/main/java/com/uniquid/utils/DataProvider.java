package com.uniquid.utils;

public interface DataProvider<T> extends ResponseDecoder<T> {
	
	public String getContentType();
	
	public String getCharset();
	
	public byte[] getPayload();
	
}
