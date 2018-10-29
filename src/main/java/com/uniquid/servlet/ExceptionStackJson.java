package com.uniquid.servlet;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Beatrice Formai
 */
public class ExceptionStackJson {

    private List<ExceptionJson> stack;

    public ExceptionStackJson(Throwable th) {
        stack = new ArrayList<>();
        processCause(th);
    }

    private void processCause(Throwable th) {
        Throwable cause = th.getCause();
        if (cause != null) {
            processCause(cause);
        }
        stack.add(new ExceptionJson(th));
    }

    @JsonProperty
    public List<ExceptionJson> getStack() {
        return stack;
    }

}
