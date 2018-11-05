/*
 * Copyright (c) 2016-2018. Uniquid Inc. or its affiliates. All Rights Reserved.
 *
 * License is in the "LICENSE" file accompanying this file.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package com.uniquid.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;
import java.util.Formatter;

/**
 * @author Beatrice Formai
 */
public class JsonServletResponse extends HttpServletResponseWrapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonServletResponse.class.getName());
    private static final Marker CONSOLE = MarkerFactory.getMarker("CONSOLE");
    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * Constructs a response adaptor wrapping the given response.
     *
     * @param response
     * @throws IllegalArgumentException if the response is null
     */
    public JsonServletResponse(HttpServletResponse response) {
        super(response);
    }


    public void writeJson(Object obj) throws IOException {
        mapper.writeValue(this.getWriter(), obj);
    }

    public void sendError(int sc, String msg, Object... args) throws IOException {
        try (Formatter formatter = new Formatter()) {
            String fullMsg = formatter.format(msg, args).toString();
            ErrorJson errorJson = new ErrorJson(fullMsg);
            this.setStatus(sc);
            this.writeJson(errorJson);
        }
    }

    public void sendException(int sc, Exception e) {
        try {
            ExceptionStackJson exceptionStackJson = new ExceptionStackJson(e);
            this.setStatus(sc);
            this.writeJson(exceptionStackJson);
        } catch (IOException ex) {
            LOGGER.error(CONSOLE, "sendException error: ", ex);
        }
    }

}
