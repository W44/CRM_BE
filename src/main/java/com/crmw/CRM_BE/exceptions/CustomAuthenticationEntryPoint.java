package com.crmw.CRM_BE.exceptions;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {


    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, org.springframework.security.core.AuthenticationException authException) throws IOException, ServletException {

         Throwable cause = authException.getCause();

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        if (cause != null && cause.getMessage().contains("not active")) {
            response.getWriter().write("{\"error\": \"User is not active\"}");
        } else {
            response.getWriter().write("{\"error\": \"" + authException.getMessage() + "\"}");
        }

    }
}
