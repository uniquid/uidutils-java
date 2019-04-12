package com.uniquid.servlet.controller;

import java.util.Map;

public class ResponseEntity<T> {

    private T body;
    private int status;
    private Map<String, String> headers;

    public ResponseEntity(int status) {
        this(null, null, status);
    }

    public ResponseEntity(T body, int status) {
        this(body, null, status);
    }

    public ResponseEntity(T body, Map<String, String> headers, int status) {
        this.body = body;
        this.headers = headers;
        this.status = status;
    }

    public T getBody() {
        return body;
    }

    public int getStatus() {
        return status;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }
}
