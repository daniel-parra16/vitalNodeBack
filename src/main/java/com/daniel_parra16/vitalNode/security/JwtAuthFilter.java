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

        // 1. Leer el header Authorization
        String authHeader = request.getHeader("Authorization");

        // 2. Si no hay header o no empieza con "Bearer " — dejar pasar sin autenticar
        // Las rutas públicas como /auth/login no necesitan token
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3. Extraer el token quitando "Bearer "
        String token = authHeader.substring(7);

        // 4. Validar que sea un Access Token válido y no expirado
        // Si ya expiro retorna un error 401 para que el front haga una peticion de
        // refresh y obtener un nuevo token de acceso.
        if (!jwtService.esAccessTokenValido(token)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
            response.setContentType("application/json;charset=UTF-8");
            response.setCharacterEncoding("UTF-8");

            String json = """
                    {
                        "status": 401,
                        "error": "Unauthorized",
                        "message": "El token ha expirado o es inválido"
                    }
                    """;

            response.getWriter().write(json);
            response.getWriter().flush();
            return;
        }

        // 5. Extraer el usuarioId y los roles del token
        String usuarioId = jwtService.extraerUsuarioId(token);
        List<String> roles = jwtService.extraerRoles(token);

        // 6. Verificar que el usuario aún existe y está activo en la BD
        // Esto protege el caso donde se elimina o desactiva un usuario
        // pero su token aún no ha expirado

        UsuarioAuth user = usuarioAuthRepository
                .findByNumeroDocumento(usuarioId)
                .orElse(null);

        System.out.println("TOKEN: " + token);
        System.out.println("SUBJECT: " + usuarioId);
        System.out.println("ROLES: " + roles);
        System.out.println("USER FOUND: " + (user != null));

        if (user == null || !user.isActivo()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");

            String json = """
                    {
                        "status": 401,
                        "error": "Unauthorized",
                        "message": "Usuario no encontrado o inactivo"
                    }
                    """;

            response.getWriter().write(json);
            response.getWriter().flush();
            return;
        }

        // 7. Convertir los roles a GrantedAuthority — formato que entiende Spring
        // Security
        List<SimpleGrantedAuthority> authorities = roles.stream()
                .map(SimpleGrantedAuthority::new)
                .toList();

        // 8. Crear el objeto de autenticación
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                usuarioId, // principal — lo puedes recuperar en el controller
                null, // credentials — null porque ya validamos con JWT
                authorities // roles
        );

        // 9. Agregar detalles de la petición (IP, session, etc.)
        authentication.setDetails(
                new WebAuthenticationDetailsSource().buildDetails(request));

        // 10. Registrar la autenticación en el contexto de Spring Security
        // A partir de aquí Spring sabe quién es el usuario en esta petición
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 11. Continuar con la cadena de filtros
        filterChain.doFilter(request, response);
    }

}
