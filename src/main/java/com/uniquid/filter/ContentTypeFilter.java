package com.uniquid.filter;

import org.eclipse.jetty.http.MimeTypes;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author Beatrice Formai
 */
public class ContentTypeFilter implements Filter{

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // DO NOTHING
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse httpResponse = (HttpServletResponse)response;
        httpResponse.setContentType(MimeTypes.Type.APPLICATION_JSON.toString());
        httpResponse.setCharacterEncoding(StandardCharsets.UTF_8.name());
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // DO NOTHING
    }

}
