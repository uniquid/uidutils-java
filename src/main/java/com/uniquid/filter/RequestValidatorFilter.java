package com.uniquid.filter;

import com.uniquid.servlet.ErrorJson;
import org.eclipse.jetty.http.HttpStatus;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Beatrice Formai
 */
public class RequestValidatorFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // DO NOTHING
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest)request;
        HttpServletResponse httpResponse = (HttpServletResponse)response;
        String method = httpRequest.getMethod();
        if (("POST".equals(method) || "PUT".equals(method)) && request.getContentLengthLong() == 0) {
            httpResponse.setStatus(HttpStatus.BAD_REQUEST_400);
            new ErrorJson("Body can't be empty!").to(response.getWriter());
        } else {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {
        // DO NOTHING
    }

}
