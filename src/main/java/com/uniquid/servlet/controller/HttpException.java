/*
 * Copyright (c) 2016-2018. Uniquid Inc. or its affiliates. All Rights Reserved.
 *
 * License is in the "LICENSE" file accompanying this file.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package com.uniquid.servlet.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Formatter;

public class HttpException extends Exception {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private final int statusCode;
    private final String responseBody;

    public HttpException(int statusCode, Object responseObject) throws JsonProcessingException {
        super(String.valueOf(statusCode));
        this.statusCode = statusCode;
        this.responseBody = MAPPER.writeValueAsString(responseObject);
    }

    public HttpException(int statusCode, String responseBody) {
        super(String.valueOf(statusCode));
        this.statusCode = statusCode;
        this.responseBody = responseBody;
    }

    public HttpException(int statusCode, String responseBody, Object... args) {
        super(String.valueOf(statusCode));
        this.statusCode = statusCode;
        try (Formatter formatter = new Formatter()) {
            String fullMsg = formatter.format(responseBody, args).toString();
            this.responseBody = fullMsg;
        }
    }

    public String getResponseBody() {
        return responseBody;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
