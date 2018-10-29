package com.uniquid.servlet;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.Writer;

/**
 * @author Beatrice Formai
 */
public class ErrorJson {

    private static final ObjectMapper mapper = new ObjectMapper();

    private String error;

    public ErrorJson(String error) {
        this.error = error;
    }

    public void to(Writer dest) throws IOException {
        mapper.writeValue(dest, this);
    }

    @JsonProperty
    public String getError() {
        return error;
    }

}
