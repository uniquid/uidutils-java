/*
 * Copyright (c) 2016-2018. Uniquid Inc. or its affiliates. All Rights Reserved.
 *
 * License is in the "LICENSE" file accompanying this file.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package com.uniquid.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.jetty.http.MimeTypes;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.ErrorHandler;
import org.eclipse.jetty.util.StringUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;

/**
 * The JsonErrorHandler prepare the HttpServletResponse
 * with error in JSON format. Error can be send
 * using {@code JsonServletResponse.sendException}
 */
public class JsonErrorHandler extends ErrorHandler {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void generateAcceptableResponse(Request baseRequest, HttpServletRequest request, HttpServletResponse response, int code, String message, String mimeType)
            throws IOException
    {
        switch(mimeType)
        {
            case "application/json":
            case "*/*":
                baseRequest.setHandled(true);
                Writer writer = getAcceptableWriter(baseRequest,request,response);
                if (writer!=null)
                {
                    response.setContentType(MimeTypes.Type.APPLICATION_JSON.asString());
                    response.setStatus(code);
                    if (!StringUtil.isBlank(message)) {
                        ErrorJson errorJson = new ErrorJson(message);
                        mapper.writeValue(response.getWriter(), errorJson);
                    }
                }
                break;
            default:
                super.generateAcceptableResponse(baseRequest, request, response, code, message, mimeType);
        }
    }

}
