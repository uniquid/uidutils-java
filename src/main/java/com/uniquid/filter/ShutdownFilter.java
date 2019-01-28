/*
 * Copyright (c) 2016-2018. Uniquid Inc. or its affiliates. All Rights Reserved.
 *
 * License is in the "LICENSE" file accompanying this file.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package com.uniquid.filter;

import org.eclipse.jetty.http.HttpStatus;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.atomic.LongAdder;

public class ShutdownFilter implements Filter {

    private final LongAdder requestCounter = new LongAdder();
    private boolean shutdown = false;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Do nothing
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (shutdown) {
            HttpServletResponse resp = (HttpServletResponse) response;
            resp.reset();
            resp.sendError(HttpStatus.SERVICE_UNAVAILABLE_503);
        }
        requestCounter.increment();
        chain.doFilter(request, response);
        requestCounter.decrement();
    }

    @Override
    public void destroy() {
        // Do nothing
    }

    public void shutdown() {
        this.shutdown = true;
    }

    public long getActiveRequestCount() {
        return requestCounter.longValue();
    }
}
