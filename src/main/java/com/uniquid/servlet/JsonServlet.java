/*
 * Copyright (c) 2016-2018. Uniquid Inc. or its affiliates. All Rights Reserved.
 *
 * License is in the "LICENSE" file accompanying this file.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package com.uniquid.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;

@Deprecated
public class JsonServlet extends HttpServlet {

    /**
     * This method find UriPattern annotation in class inheritor
     * of JsonServlet. Annotation UriPattern can be used as a class
     * annotation or as method annotation. If code contains both
     * and class annotation and method annotation - then will be
     * used method annotation since it has highest priority.
     * If no annotations found in class - method will return null.
     *
     * @param methodName    method to search annotation
     * @return              url pattern from annotation
     */
    private String findUriPattern(String methodName) {
        String uriPattern = null;

        Class<? extends JsonServlet> clazz = this.getClass();

        // Get class annotation
        UriPattern annotation = clazz.getAnnotation(UriPattern.class);

        // Find method
        Method method = Arrays.stream(clazz.getDeclaredMethods())
                .filter(m -> m.getName().equals(methodName))
                .findAny().orElse(null);
        if (method != null) {
            UriPattern methodAnnotation = method.getAnnotation(UriPattern.class);
            if (methodAnnotation != null) {
                annotation = methodAnnotation;
            }
        }

        if (annotation != null) {
            uriPattern = annotation.value();
        }
        return uriPattern;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req instanceof HttpServletRequest && resp instanceof HttpServletResponse) {
            String uriPattern = findUriPattern("doGet");
            doGet(new JsonServletRequest(req, uriPattern), new JsonServletResponse(resp));
        } else {
            super.doGet(req, resp);
        }
    }

    protected void doGet(JsonServletRequest req, JsonServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
    }

    @Override
    protected void doHead(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req instanceof HttpServletRequest && resp instanceof HttpServletResponse) {
            String uriPattern = findUriPattern("doHead");
            doHead(new JsonServletRequest(req, uriPattern), new JsonServletResponse(resp));
        } else {
            super.doHead(req, resp);
        }
    }

    protected void doHead(JsonServletRequest req, JsonServletResponse resp) throws ServletException, IOException {
        super.doHead(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req instanceof HttpServletRequest && resp instanceof HttpServletResponse) {
            String uriPattern = findUriPattern("doPost");
            doPost(new JsonServletRequest(req, uriPattern), new JsonServletResponse(resp));
        } else {
            super.doPost(req, resp);
        }
    }

    protected void doPost(JsonServletRequest req, JsonServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req instanceof HttpServletRequest && resp instanceof HttpServletResponse) {
            String uriPattern = findUriPattern("doPut");
            doPut(new JsonServletRequest(req, uriPattern), new JsonServletResponse(resp));
        } else {
            super.doPut(req, resp);
        }
    }

    protected void doPut(JsonServletRequest req, JsonServletResponse resp) throws ServletException, IOException {
        super.doPut(req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req instanceof HttpServletRequest && resp instanceof HttpServletResponse) {
            String uriPattern = findUriPattern("doDelete");
            doDelete(new JsonServletRequest(req, uriPattern), new JsonServletResponse(resp));
        } else {
            super.doDelete(req, resp);
        }
    }

    protected void doDelete(JsonServletRequest req, JsonServletResponse resp) throws ServletException, IOException {
        super.doDelete(req, resp);
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req instanceof HttpServletRequest && resp instanceof HttpServletResponse) {
            String uriPattern = findUriPattern("doOptions");
            doOptions(new JsonServletRequest(req, uriPattern), new JsonServletResponse(resp));
        } else {
            super.doOptions(req, resp);
        }
    }

    protected void doOptions(JsonServletRequest req, JsonServletResponse resp) throws ServletException, IOException {
        super.doOptions(req, resp);
    }

    @Override
    protected void doTrace(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req instanceof HttpServletRequest && resp instanceof HttpServletResponse) {
            String uriPattern = findUriPattern("doTrace");
            doTrace(new JsonServletRequest(req, uriPattern), new JsonServletResponse(resp));
        } else {
            super.doTrace(req, resp);
        }
    }

    protected void doTrace(JsonServletRequest req, JsonServletResponse resp) throws ServletException, IOException {
        super.doTrace(req, resp);
    }

}
