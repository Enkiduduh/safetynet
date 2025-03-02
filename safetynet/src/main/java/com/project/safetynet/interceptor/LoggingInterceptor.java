package com.project.safetynet.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Enumeration;

@Component
public class LoggingInterceptor implements HandlerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(LoggingInterceptor.class);


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        logger.info("Request received : {} {}", request.getMethod(), request.getRequestURI());
        logRequestHeaders(request);
        logRequestBody(request);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        if (ex != null) {
            logger.error("Error detected : {}", ex.getMessage(), ex);
        } else if (response.getStatus() >= HttpStatus.BAD_REQUEST.value()) {
            logger.error("Response with error : {} {}", response.getStatus(), response.getLocale());
        } else {
            logger.info("Response successful : {} {}", response.getStatus(), request.getRequestURI());
        }
    }

    private void logRequestHeaders(HttpServletRequest request) {
        Enumeration<String> headerNames = request.getHeaderNames();
        if (headerNames != null) {
            while (headerNames.hasMoreElements()) {
                String headerName = headerNames.nextElement();
                logger.debug("Header: {} = {}", headerName, request.getHeader(headerName));
            }
        }
    }

    private void logRequestBody(HttpServletRequest request) {
        try (BufferedReader reader = request.getReader()) {
            StringBuilder body = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                body.append(line);
            }
            logger.debug("Request body : {}", body.toString());
        } catch (IOException e) {
            logger.warn("Impossible to read the body request");
        }
    }

}
