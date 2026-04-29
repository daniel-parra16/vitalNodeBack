package com.daniel_parra16.vitalNode.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    // ----------------------------------------------------------------------------------------------
    // Configuracion de variables JWT desde el aplication.properties
    // ----------------------------------------------------------------------------------------------

    // La clave secreta para firmar los tokens JWT, debe ser una cadena larga y
    // segura
    @Value("${JWT.secret}")
    private String secret;

    // El tiempo de expiracion del access token en milisegundos, por ejemplo 15
    // minutos = 900000 ms
    @Value("${JWT.expiration.access}")
    private Long accessExpiration;

    // El tiempo de expiracion del refresh token en milisegundos, por ejemplo 7 dias
    // = 604800000 ms
    @Value("${JWT.expiration.refresh}")
    private Long refreshExpiration;

    // El tiempo de inactividad en minutos, despues del cual se considera que el
    // usuario esta inactivo y se deben invalidar los tokens
    @Value("${JWT.inactividad.minutos}")
    public Integer inactividadMinutos;

    // ----------------------------------------------------------------------------------------------
    // Generacion de los tokens JWT usando la libreria jjwt
    // ----------------------------------------------------------------------------------------------

    // Generacion del access token, que es un token de corta duracion que se usa
    // para autenticar las peticiones al backend
    public String generarAccessToken(String usuarioId, List<String> roles, String nombres, String numDoc) {
        return Jwts.builder()
                .subject(usuarioId)
                .claim("roles", roles)
                .claim("nombres", nombres)
                .claim("numDoc", numDoc)
                .claim("tipo", "ACCESS")
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + accessExpiration))
                .signWith(getSigningKey())
                .compact();
    }

    // Generacion del refresh token, que es un token de larga duracion que se usa
    // para obtener nuevos access tokens
    public String generarRefreshToken(String usuarioId) {
        return Jwts.builder()
                .subject(usuarioId)
                .claim("tipo", "REFRESH")
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + refreshExpiration))
                .signWith(getSigningKey())
                .compact();
    }

    // ----------------------------------------------------------------------------------------------
    // Extracción de datos del token JWT
    // ----------------------------------------------------------------------------------------------

    public String extraerUsuarioId(String token) {
        return extraerClaims(token, Claims::getSubject);
    }

    @SuppressWarnings("unchecked")
    public List<String> extraerRoles(String token) {
        return extraerClaims(token, claims -> claims.get("roles", List.class));
    }

    public String extraerNombres(String token) {
        return extraerClaims(token, claims -> claims.get("nombres", String.class));
    }

    public String extraerNumDoc(String token) {
        return extraerClaims(token, claims -> claims.get("numDoc", String.class));
    }

    public String extraerTipoToken(String token) {
        return extraerClaims(token, claims -> claims.get("tipo", String.class));
    }

    public Date extraerFechaExpiracion(String token) {
        return extraerClaims(token, Claims::getExpiration);
    }

    // ----------------------------------------------------------------------------------------------
    // Metodo para validar si el token JWT es valido, verificando su firma y su
    // fecha de expiracion
    // ----------------------------------------------------------------------------------------------

    public boolean esAccessTokenValido(String token) {
        try {
            return "ACCESS".equals(extraerTipoToken(token)) && !estaExpirado(token);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean esRefreshTokenValido(String token) {
        try {
            return "REFRESH".equals(extraerTipoToken(token)) && !estaExpirado(token);
        } catch (Exception e) {
            return false;
        }
    }

    // ----------------------------------------------------------------------------------------------
    // Metodos para extraer uno o mas claims del token JWT
    // ----------------------------------------------------------------------------------------------
    // Método genérico para extraer cualquier claim del token JWT
    public <T> T extraerClaims(String token, Function<Claims, T> resolver) {
        Claims claims = extraerTodosLosClaims(token);
        return resolver.apply(claims);
    }

    // Método para extraer todos los claims del token JWT
    private Claims extraerTodosLosClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // ----------------------------------------------------------------------------------------------
    // Método para firmar y verificar los tokens JWT usando la clave secreta
    // ----------------------------------------------------------------------------------------------
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    // ----------------------------------------------------------------------------------------------
    // Metodo para validar si el token JWT es valido, verificando su firma, su fecha
    // de expiracion
    // ----------------------------------------------------------------------------------------------

    private boolean estaExpirado(String token) {
        return extraerFechaExpiracion(token).before(new Date());
    }

}
