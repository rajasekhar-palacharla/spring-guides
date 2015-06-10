package io.spring.guides.rest_service_cors;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class SimpleCORSFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (response instanceof HttpServletResponse) {
            HttpServletResponse r = (HttpServletResponse) response;
            r.setHeader("Access-Control-Allow-Origin", "*");
            r.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
            r.setHeader("Access-Control-Max-Age", "3600");
            r.setHeader("Access-Control-Allow-Headers", "x-requested-with");
            chain.doFilter(request, response);
        } else {
            throw new ServletException("CORS works only for HTTP");
        }
    }

    @Override
    public void destroy() {
    }
}
