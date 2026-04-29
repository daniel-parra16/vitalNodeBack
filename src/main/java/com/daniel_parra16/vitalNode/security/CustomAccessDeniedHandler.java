package com.daniel_parra16.vitalNode.security;

import java.io.IOException;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException)
            throws IOException {

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        String timestamp = java.time.LocalDateTime.now().toString();

        String json = """
                {
                    "timestamp": "%s",
                    "status": 403,
                    "error": "Forbidden",
                    "message": "No tienes permisos para realizar esta acción"
                }
                """.formatted(timestamp);

        response.setContentLength(json.getBytes(java.nio.charset.StandardCharsets.UTF_8).length);
        response.getWriter().write(json);
        response.getWriter().flush();
    }
}