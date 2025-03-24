package com.project.safetynet.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;

@Component
//@WebFilter("/*")
public class RequestWrapperFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(RequestWrapperFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Envelopper la requête et la réponse
        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(httpRequest);
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(httpResponse);

        try {
            chain.doFilter(wrappedRequest, wrappedResponse);
        } finally {
            // Extraire et loguer le corps de la réponse
            byte[] responseArray = wrappedResponse.getContentAsByteArray();
            String responseBody = new String(responseArray, wrappedResponse.getCharacterEncoding());
            // Utilisez logger.info() si vous voulez que le message apparaisse au niveau INFO
            logger.info("Response body: {}", responseBody);

            // Copie du contenu mis en cache dans la réponse réelle
            wrappedResponse.copyBodyToResponse();
        }
    }

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void destroy() {
    }
}
