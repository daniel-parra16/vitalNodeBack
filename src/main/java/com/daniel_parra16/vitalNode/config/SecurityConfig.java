package com.daniel_parra16.vitalNode.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.daniel_parra16.vitalNode.security.CustomAccessDeniedHandler;
import com.daniel_parra16.vitalNode.security.JwtAuthFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // CORS permite que el front (otro dominio/puerto, ej. localhost:5173)
                // pueda consumir esta API sin ser bloqueado por el navegador.
                .cors(cors -> {
                })

                // CSRF deshabilitado — API REST con JWT no lo necesita
                .csrf(csrf -> csrf.disable())

                // Sin estado — Spring no guarda sesión en servidor
                // cada petición se autentica con el token
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .exceptionHandling(ex -> ex
                        .accessDeniedHandler(customAccessDeniedHandler))

                // Rutas públicas y protegidas
                .authorizeHttpRequests(auth -> auth
                        // Permitir preflight CORS (OPTIONS) para cualquier ruta
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // Rutas públicas — no requieren token
                        .requestMatchers(
                                "/api/auth/registro",
                                "/api/auth/login",
                                "/api/auth/refresh",
                                "/api/auth/recuperar-password",
                                "/api/auth/nueva-password",
                                "/api/auth/tipoDocumento",
                                "/api/ordenes",
                                "/api/auth/verificar-correo")
                        .permitAll()

                        // Admin y mecánico
                        .requestMatchers("/api/repuestos/**",
                                "/api/ordenes/**",
                                "/api/inventario/**",
                                "/api/usuarios/getMecanicos",
                                "/api/usuarios/getUsuarios")
                        .hasAnyAuthority("ROLE_ADMIN", "ROLE_MECANICO")

                        // Solo admin
                        .requestMatchers("/api/usuarios/**").hasAuthority("ROLE_ADMIN")

                        // Cualquier usuario autenticado
                        .anyRequest().authenticated())

                // Registrar el filtro JWT antes del filtro de autenticación de Spring
                .addFilterBefore(jwtAuthFilter,
                        UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}
