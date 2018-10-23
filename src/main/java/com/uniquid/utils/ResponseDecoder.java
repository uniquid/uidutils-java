package com.uniquid.utils;

public interface ResponseDecoder<T> {

    int getExpectedResponseCode();

    T manageResponse(String serverResponse) throws Exception;

    T manageUnexpectedResponseCode(int responseCode, String responseMessage) throws Exception;

}
