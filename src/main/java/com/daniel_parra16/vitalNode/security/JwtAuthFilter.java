package com.daniel_parra16.vitalNode.security;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.daniel_parra16.vitalNode.auth.models.UsuarioAuth;
import com.daniel_parra16.vitalNode.auth.repositories.UsuarioAuthRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UsuarioAuthRepository usuarioAuthRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        // 🔓 Rutas sin token
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        try {

            // 🔴 1. TOKEN EXPIRADO (único caso donde frontend debe hacer refresh)
            if (jwtService.estaExpirado(token)) {
                sendError(response, HttpServletResponse.SC_UNAUTHORIZED,
                        "TOKEN_EXPIRED",
                        "El token ha expirado");
                return;
            }

            // 🔴 2. TOKEN INVÁLIDO (firma incorrecta, manipulado, etc)
            if (!jwtService.esFirmaValida(token)) {
                sendError(response, HttpServletResponse.SC_UNAUTHORIZED,
                        "INVALID_TOKEN",
                        "Token inválido");
                return;
            }

            if (!"ACCESS".equals(jwtService.extraerTipoToken(token))) {
    sendError(response, HttpServletResponse.SC_UNAUTHORIZED,
            "INVALID_TOKEN",
            "Tipo de token inválido");
    return;
}

            // ✅ 3. Extraer datos
            String usuarioId = jwtService.extraerUsuarioId(token);
            List<String> roles = jwtService.extraerRoles(token);

            // 🔴 4. Usuario inválido
            UsuarioAuth user = usuarioAuthRepository
                    .findByNumeroDocumento(usuarioId)
                    .orElse(null);

            if (user == null || !user.isActivo()) {
                sendError(response, HttpServletResponse.SC_UNAUTHORIZED,
                        "USER_INVALID",
                        "Usuario no válido o inactivo");
                return;
            }

            // ✅ 5. Crear authorities
            List<SimpleGrantedAuthority> authorities = roles.stream()
                    .map(SimpleGrantedAuthority::new)
                    .toList();

            // ✅ 6. Autenticación
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            usuarioId,
                            null,
                            authorities
                    );

            authentication.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            filterChain.doFilter(request, response);

        } catch (Exception e) {
            // 🔴 fallback real
            sendError(response, HttpServletResponse.SC_UNAUTHORIZED,
                    "AUTH_ERROR",
                    "Error procesando el token");
        }
    }

    private void sendError(HttpServletResponse response, int status,
            String error, String message) throws IOException {

        response.setStatus(status);
        response.setContentType("application/json;charset=UTF-8");

        String json = String.format("""
                {
                    "status": %d,
                    "error": "%s",
                    "message": "%s"
                }
                """, status, error, message);

        response.getWriter().write(json);
        response.getWriter().flush();
    }
}