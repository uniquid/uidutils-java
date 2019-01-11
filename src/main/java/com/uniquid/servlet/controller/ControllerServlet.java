/*
 * Copyright (c) 2016-2018. Uniquid Inc. or its affiliates. All Rights Reserved.
 *
 * License is in the "LICENSE" file accompanying this file.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package com.uniquid.servlet.controller;

import com.google.common.base.Strings;
import com.uniquid.servlet.JsonServletRequest;
import com.uniquid.servlet.JsonServletResponse;
import org.eclipse.jetty.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

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

    private static final Logger LOGGER = LoggerFactory.getLogger(ControllerServlet.class.getName());
    private static final Marker CONSOLE = MarkerFactory.getMarker("CONSOLE");

    private static final Map<RequestMethod, Map<UriMask, Method>> handlers = new EnumMap<>(RequestMethod.class);

    /**
     * Constructor
     */
    public ControllerServlet() {

        // Get controller Uri if exist
        Controller controllerAnnotation = this.getClass().getAnnotation(Controller.class);
        String controllerUri = "";
        if (controllerAnnotation != null) {
             controllerUri = controllerAnnotation.value();
        }

        // Scan inherit class to find methods annotated with RequestMapping
        for (Method method : this.getClass().getDeclaredMethods()) {

            // If method has RequestMapping annotation
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

            // If method has GetMapping annotation
            GetMapping gm = method.getAnnotation(GetMapping.class);
            if (gm != null) {
                // Final Uri consist of controller's global Uri + method's Uri
                String methodUri = controllerUri + gm.value();

                Map<UriMask, Method> methods = handlers.computeIfAbsent(RequestMethod.GET, k -> new HashMap<>());
                UriMask mask = new UriMask(methodUri);
                methods.put(mask, method);
            }

            // If method has PostMapping annotation
            PostMapping pm = method.getAnnotation(PostMapping.class);
            if (pm != null) {
                // Final Uri consist of controller's global Uri + method's Uri
                String methodUri = controllerUri + pm.value();

                Map<UriMask, Method> methods = handlers.computeIfAbsent(RequestMethod.POST, k -> new HashMap<>());
                UriMask mask = new UriMask(methodUri);
                methods.put(mask, method);
            }

            // If method has PutMapping annotation
            PutMapping pt = method.getAnnotation(PutMapping.class);
            if (pt != null) {
                // Final Uri consist of controller's global Uri + method's Uri
                String methodUri = controllerUri + pt.value();

                Map<UriMask, Method> methods = handlers.computeIfAbsent(RequestMethod.PUT, k -> new HashMap<>());
                UriMask mask = new UriMask(methodUri);
                methods.put(mask, method);
            }

            // If method has DeleteMapping annotation
            DeleteMapping dm = method.getAnnotation(DeleteMapping.class);
            if (dm != null) {
                // Final Uri consist of controller's global Uri + method's Uri
                String methodUri = controllerUri + dm.value();

                Map<UriMask, Method> methods = handlers.computeIfAbsent(RequestMethod.DELETE, k -> new HashMap<>());
                UriMask mask = new UriMask(methodUri);
                methods.put(mask, method);
            }
        }

    }


    /**
     * Method to parse string to object according to given type
     *
     * @param s     string that have to be parsed
     * @param clazz class type to which need to parse given string
     * @return
     */
    private static <T> T parseObjectFromString(String s, Class<T> clazz) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        String strObj = s;
        // Force enum to uppercase
        if (Enum.class.isAssignableFrom(clazz)) {
            strObj = s.toUpperCase();
        }
        return (T) clazz.getMethod("valueOf", String.class).invoke(clazz, strObj);
    }


    /**
     * Method that can be used to find wrapped class type for given primitive
     * it there is no wrapped class or given non-primitive type - return same value
     *
     * @param type      primitive type to convert
     * @return          wrapped type analog to given primitive type
     */
    private static Class<?> toWrappedType(Class<?> type)
    {
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
        return type;
    }

    /**
     * Method to find handler method suitable with current URI and current RequestMethod
     *
     * @param uri               current Uri to search
     * @param requestMethod     current RequestMethod to search
     * @return                  keypair with Uri and Method
     */
    private Map.Entry<UriMask, Method> findMethod(String uri, RequestMethod requestMethod) {
        Map<UriMask, Method> entries = handlers.get(requestMethod);
        if (entries == null) {
            return null;
        }
        return entries.entrySet().stream().filter(e -> e.getKey().isMatch(uri)).findFirst().orElse(null);
    }


    /**
     * Method handler to process incoming http request, find suitable
     * handler method according to Uri and RequestMethod in annotations
     * and invoke the method.
     *
     * @param req       http request
     * @param resp      http response
     * @throws ServletException
     * @throws IOException
     */
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
            Object[] args = prepareArguments(method, new JsonServletRequest(req, mask.getUri()));

            // Invoke suitable method
            Object ret = method.invoke(this, args);
            if (ret != null) {
                if (ret instanceof ResponseEntity) {
                    ResponseEntity entity = (ResponseEntity)ret;

                    Map<String, String> headers = entity.getHeaders();
                    if (headers != null) {
                        headers.forEach(response::addHeader);
                    }
                    response.setStatus(entity.getStatus());
                    response.writeJson(entity.getBody());
                } else {
                    response.writeJson(ret);
                }
            }

        } catch (HttpException e) {
            response.sendError(e.getStatusCode(), e.getResponseBody());
        } catch (Exception e) {
            response.sendException(HttpStatus.INTERNAL_SERVER_ERROR_500, e);
            LOGGER.error(CONSOLE, "HTTP handling error:", e);
        }
    }


    /**
     * Method to prepare arguments values for method invocation based on
     * current http request
     *
     * @param method        method for which you want to prepare the arguments
     * @param request       current request based on what will be prepared arguments values
     * @return              array of objects of arguments values
     */
    private Object[] prepareArguments(Method method, JsonServletRequest request)
            throws NoSuchMethodException, HttpException, IOException, IllegalAccessException, InvocationTargetException {
        List<Object> args = new ArrayList<>();

        for (Parameter param : method.getParameters()) {
            args.add(prepareArgument(param, request));
        }

        return args.toArray();
    }


    /**
     * Method to prepare argument value for parameter based on http request
     *
     * @param param         parameter for which you want to prepare argument
     * @param request       current http request based to get values for argument
     * @return              object of argument value
     */
    private Object prepareArgument(Parameter param, JsonServletRequest request)
            throws IOException, HttpException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        // If param marked as RequestBody
        RequestBody rb = param.getDeclaredAnnotation(RequestBody.class);
        if (rb != null) {
            Object arg = request.readJson(param.getType());
            if (arg != null) {
                return arg;
            }
            if (rb.required()) {
                throw new HttpException(HttpStatus.BAD_REQUEST_400, "Request body is required valid json.");
            }
        }

        // If param marked as PathVariable
        PathVariable pv = param.getDeclaredAnnotation(PathVariable.class);
        if (pv != null) {
            String pathVarValue = request.getVariable(pv.value());
            if (!Strings.isNullOrEmpty(pathVarValue)) {
                return parseObjectFromString(pathVarValue, toWrappedType(param.getType()));
            }
            if (pv.required()) {
                throw new HttpException(HttpStatus.BAD_REQUEST_400, "PathVariable '%s' is required.", pv.value());
            }
        }

        // If param marked as PathParam
        PathParam pp = param.getDeclaredAnnotation(PathParam.class);
        if (pp != null) {
            String pathParamValue = request.getParameter(pp.value());
            if (!Strings.isNullOrEmpty(pathParamValue)) {
                return parseObjectFromString(pathParamValue, toWrappedType(param.getType()));
            }
            if (pp.required()) {
                throw new HttpException(HttpStatus.BAD_REQUEST_400, "PathParam '%s' is required.", pp.value());
            }
        }
        return null;
    }

    public void requireNotNull(Object obj, String errorMsg) throws HttpException {
        if (obj == null) {
            throw new HttpException(HttpStatus.BAD_REQUEST_400, errorMsg);
        }
    }

    public void requireNotEmpty(String str, String errorMsg) throws HttpException {
        if (Strings.isNullOrEmpty(str)) {
            throw new HttpException(HttpStatus.BAD_REQUEST_400, errorMsg);
        }
    }
}
