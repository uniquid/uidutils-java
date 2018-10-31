package com.uniquid.servlet;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Beatrice Formai
 */
public class ExceptionJson extends ErrorJson {

    private Throwable th;

    public ExceptionJson(Throwable th) {
        super(th.getClass().getName() + ": " + th.getMessage());
        this.th = th;
    }

    @JsonProperty
    public List<String> getTrace() {
        List<String> trace = new ArrayList<>();
        for (StackTraceElement element: th.getStackTrace()) {
            trace.add(element.getClassName() + "." + element.getMethodName() + "(" + element.getFileName() + ":" + element.getLineNumber() + ")");
        }
        return trace;
    }

}
