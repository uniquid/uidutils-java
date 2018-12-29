/*
 * Copyright (c) 2016-2018. Uniquid Inc. or its affiliates. All Rights Reserved.
 *
 * License is in the "LICENSE" file accompanying this file.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package com.uniquid.servlet.controller;

import com.fasterxml.jackson.databind.util.ClassUtil;
import com.uniquid.servlet.JsonServletRequest;
import com.uniquid.servlet.JsonServletResponse;
import org.eclipse.jetty.http.HttpStatus;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

/**
 * This ControllerServlet provide Spring-like rest controller
 * to easy and fast build http controllers. It supports only
 * limited amount of functions. For information how to use,
 * please see spring web documentation
 * @see <a href="https://docs.spring.io/spring/docs/current/spring-framework-reference/web.html#spring-web">Spring-web</a>
 */
public class ControllerServlet extends HttpServlet {

    private static <T> T parseObjectFromString(String s, Class<T> clazz) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Class<T> type = clazz;
        if (type.isPrimitive()) {
            type = (Class<T>)ClassUtil.wrapperType(type);
        }
        return parseWrappedObjectFromString(s, type);
    }

    private static <T> T parseWrappedObjectFromString(String s, Class<T> clazz) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        return clazz.getConstructor(new Class[] {String.class }).newInstance(s);
    }

    private String getControllerUri() {
        Controller controllerAnnotation = this.getClass().getAnnotation(Controller.class);
        if (controllerAnnotation != null) {
            return controllerAnnotation.value();
        }
        return "";
    }

    private boolean isMatchedUri(String requestUri, String requestMask) {
        String regexpMask = requestMask.replaceAll("\\{(\\w+)}", ".*");
        return requestUri.matches(regexpMask);
    }

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException
    {
        JsonServletResponse response = new JsonServletResponse(resp);

        try {

            // Find suitable method
            for (Method method : this.getClass().getDeclaredMethods()) {

                RequestMapping methodAnnotation = method.getDeclaredAnnotation(RequestMapping.class);
                if (methodAnnotation != null && methodAnnotation.method() == RequestMethod.valueOf(req.getMethod())) {

                    // Final Uri consist of controller's global Uri + method's Uri
                    String methodUri = getControllerUri() + methodAnnotation.value();

                    // Try to match URI with method
                    if (isMatchedUri(req.getRequestURI(), methodUri)) {

                        // Prepare arguments to call method
                        Object[] args = prepareArguments(method.getParameters(), new JsonServletRequest(req, methodUri));

                        // Invoke suitable method
                        Object ret = method.invoke(this, args);
                        if (ret != null) {
                            response.writeJson(ret);
                        }
                        return;
                    }
                }
            }

            throw new HttpException(HttpStatus.NOT_IMPLEMENTED_501, "HTTP method %s is not supported by this URL", req.getMethod());
        } catch (HttpException e) {
            response.sendError(e.getStatusCode(), e.getResponseBody());
        } catch (Exception e) {
            response.sendException(HttpStatus.INTERNAL_SERVER_ERROR_500, e);
        }
    }

    private Object[] prepareArguments(Parameter[] params, JsonServletRequest request) throws Exception {
        List<Object> args = new ArrayList<>();

        for (Parameter param : params) {
            args.add(prepareArgument(param, request));
        }

        return args.toArray();
    }

    private Object prepareArgument(Parameter param, JsonServletRequest request) throws Exception {
        // If param marked as RequestBody
        RequestBody rb = param.getDeclaredAnnotation(RequestBody.class);
        if (rb != null) {
            Object arg = request.readJson(param.getType());
            if (arg == null && rb.required()) {
                throw new HttpException(HttpStatus.BAD_REQUEST_400, "Request body is required valid json.");
            }
            return arg;
        }

        // If param marked as PathVariable
        PathVariable pv = param.getDeclaredAnnotation(PathVariable.class);
        if (pv != null) {
            String pathVarValue = request.getVariable(pv.value());
            if (isBlank(pathVarValue)) {
                if (pv.required()) {
                    throw new HttpException(HttpStatus.BAD_REQUEST_400, "PathVariable '%s' is required.", pv.value());
                }
            } else {
                return parseObjectFromString(pathVarValue, param.getType());
            }
        }

        // If param marked as PathParam
        PathParam pp = param.getDeclaredAnnotation(PathParam.class);
        if (pp != null) {
            String pathParamValue = request.getParameter(pp.value());
            if (isBlank(pathParamValue)) {
                if (pp.required()) {
                    throw new HttpException(HttpStatus.BAD_REQUEST_400, "PathParam '%s' is required.", pp.value());
                }
            } else {
                return parseObjectFromString(pathParamValue, param.getType());
            }
        }
        return null;
    }

}
