/*
 * Copyright (c) 2016-2018. Uniquid Inc. or its affiliates. All Rights Reserved.
 *
 * License is in the "LICENSE" file accompanying this file.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package com.uniquid.servlet.controller;

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

    private Map<RequestMethod, Map<UriMask, Method>> handlers = new HashMap<>();

    public ControllerServlet() {

        // Get controller Uri if exist
        Controller controllerAnnotation = this.getClass().getAnnotation(Controller.class);
        String controllerUri = "";
        if (controllerAnnotation != null) {
             controllerUri = controllerAnnotation.value();
        }

        // Scan inherit class to find methods annotated with RequestMapping
        for (Method method : this.getClass().getDeclaredMethods()) {

            RequestMapping rm = method.getAnnotation(RequestMapping.class);
            if (rm != null) {

                // Final Uri consist of controller's global Uri + method's Uri
                String methodUri = controllerUri + rm.value();

                // Scan all request methods described in annotation
                for (RequestMethod requestMethod : rm.method()) {
                    Map<UriMask, Method> methods = handlers.computeIfAbsent(requestMethod, k -> new HashMap<>());

                    // Collect all Methods according to Request Methods and Uri Masks
                    UriMask mask = new UriMask(methodUri);
                    methods.put(mask, method);
                }
            }
        }

    }


    private static <T> T parseObjectFromString(String s, Class<T> clazz) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        return clazz.getConstructor(new Class[] {String.class }).newInstance(s);
    }


    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    /**
     * Method that can be used to find primitive type for given class if (but only if)
     * it is either wrapper type or primitive type; returns `null` if type is neither.
     */
    private static Class<?> toWrappedType(Class<?> type)
    {
        if (type.isPrimitive()) {
            if (type == Integer.TYPE) {
                return Integer.class;
            }
            if (type == Long.TYPE) {
                return Long.class;
            }
            if (type == Boolean.TYPE) {
                return Boolean.class;
            }
            if (type == Double.TYPE) {
                return Double.class;
            }
            if (type == Float.TYPE) {
                return Float.class;
            }
            if (type == Byte.TYPE) {
                return Byte.class;
            }
            if (type == Short.TYPE) {
                return Short.class;
            }
            if (type == Character.TYPE) {
                return Character.class;
            }
        }
        return type;
    }

    private Map.Entry<UriMask, Method> findMethod(String uri, RequestMethod requestMethod) {
        Map<UriMask, Method> entries = handlers.get(requestMethod);
        return entries.entrySet().stream().filter(e -> e.getKey().isMatch(uri)).findFirst().orElse(null);
    }


    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException
    {
        JsonServletResponse response = new JsonServletResponse(resp);

        try {

            Map.Entry<UriMask, Method> entry = findMethod(req.getRequestURI(), RequestMethod.valueOf(req.getMethod()));

            if (entry == null) {
                throw new HttpException(HttpStatus.NOT_IMPLEMENTED_501, "HTTP method %s is not supported by this URL", req.getMethod());
            }

            UriMask mask = entry.getKey();
            Method method = entry.getValue();

            // Prepare arguments to call method
            Object[] args = prepareArguments(method.getParameters(), new JsonServletRequest(req, mask.getUri()));

            // Invoke suitable method
            Object ret = method.invoke(this, args);
            if (ret != null) {
                response.writeJson(ret);
            }

        } catch (HttpException e) {
            response.sendError(e.getStatusCode(), e.getResponseBody());
        } catch (Exception e) {
            response.sendException(HttpStatus.INTERNAL_SERVER_ERROR_500, e);
            e.printStackTrace();
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
                return parseObjectFromString(pathVarValue, toWrappedType(param.getType()));
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
                return parseObjectFromString(pathParamValue, toWrappedType(param.getType()));
            }
        }
        return null;
    }

}
