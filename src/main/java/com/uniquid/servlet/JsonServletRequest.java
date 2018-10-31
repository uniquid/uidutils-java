package com.uniquid.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Beatrice Formai
 */
public class JsonServletRequest extends HttpServletRequestWrapper {

    private static final ObjectMapper mapper = new ObjectMapper();
    private Map<String, String> pathVars = new HashMap<>();

    /**
     * Constructs a request object wrapping the given request.
     *
     * @param request
     */
    public JsonServletRequest(HttpServletRequest request) {
        super(request);
    }

    /**
     * Constructs a request object wrapping the given request.
     * Additionally you can provide URI pattern to get PathVariables
     * using method JsonServletRequest.getVar().
     *
     * @param request
     * @param uriPattern
     */
    public JsonServletRequest(HttpServletRequest request, String uriPattern) {
        super(request);

        if (uriPattern != null) {
            // Process URI and get path variables
            Pattern patternKey = Pattern.compile("\\{(\\w+)}");
            Matcher matcherKey = patternKey.matcher(uriPattern);

            String regexpVal = uriPattern.replace("/", "\\/")
                    .replace("{", "(?<")
                    .replace("}", ">[^\\/]+)");
            Pattern patternVal = Pattern.compile(regexpVal);
            Matcher matcherVal = patternVal.matcher(request.getRequestURI());

            if (matcherVal.find()) {
                while (matcherKey.find()) {
                    String key = matcherKey.group(1);
                    String value = matcherVal.group(key);
                    pathVars.put(key, value);
                }
            }
        }
    }

    public <T> T readJson(Class<T> clazz) throws IOException {
        return mapper.readValue(this.getReader(), clazz);
    }

    public String getVar(String key) {
        return pathVars.get(key);

    }

}
