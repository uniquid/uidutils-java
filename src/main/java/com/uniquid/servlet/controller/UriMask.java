package com.uniquid.servlet.controller;

public class UriMask {

    private String mask;
    private String uri;

    public UriMask(String uri) {
        this.uri = uri;
        this.mask = uri.replaceAll("\\{(\\w+)}", ".*");
    }

    public String getMask() {
        return mask;
    }

    public String getUri() {
        return uri;
    }

    public boolean isMatch(String uri) {
        return uri.matches(mask);
    }
}
