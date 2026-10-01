package com.myProject.demo.filters;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.MDC;

import java.io.IOException;
import java.util.logging.LogRecord;

public class LoggingFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;

        try {
            MDC.put("requestURI", httpRequest.getRequestURI());
            chain.doFilter(request, response);
        } finally {
            MDC.clear();
        }
    }
}