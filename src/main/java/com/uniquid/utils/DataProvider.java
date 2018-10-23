package com.uniquid.utils;

public interface DataProvider<T> extends ResponseDecoder<T> {

    String getContentType();

    String getCharset();

    byte[] getPayload();

}
