package com.daniel_parra16.vitalNode.exceptions;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 400 — validaciones @Valid
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidacion(
            MethodArgumentNotValidException ex) {

        Map<String, String> erroresCampos = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(e -> erroresCampos.put(
                        e.getField(),
                        e.getDefaultMessage()));

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Error de validación");
        body.put("message", "Error de validación");
        body.put("errores", erroresCampos);
        return ResponseEntity.badRequest().body(body);
    }

    // 400 — lógica de negocio inválida
    @ExceptionHandler({ BadRequestException.class, IllegalArgumentException.class })
    public ResponseEntity<Map<String, Object>> handleBadRequest(RuntimeException ex) {
        return buildError(HttpStatus.BAD_REQUEST, "Solicitud inválida", ex.getMessage());
    }

    // 401 — no autenticado
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<Map<String, Object>> handleUnauthorized(UnauthorizedException ex) {
        return buildError(HttpStatus.UNAUTHORIZED, "No autorizado", ex.getMessage());
    }

    // 403 — sin permisos
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, Object>> handleAccessDenied(AccessDeniedException ex) {
        return buildError(HttpStatus.FORBIDDEN, "Acceso denegado",
                "No tienes permisos para realizar esta accion.");
    }

    // 404 — recurso no encontrado
    @ExceptionHandler({ NotFoundException.class, NoSuchElementException.class })
    public ResponseEntity<Map<String, Object>> handleNotFound(RuntimeException ex) {
        return buildError(HttpStatus.NOT_FOUND, "Recurso no encontrado", ex.getMessage());
    }

    // 409 — conflicto, dato duplicado
    @ExceptionHandler({ ConflictException.class, DataIntegrityViolationException.class })
    public ResponseEntity<Map<String, Object>> handleConflict(RuntimeException ex) {
        return buildError(HttpStatus.CONFLICT, "Conflicto de datos",
                ex.getMessage());
    }

    // 500 — cualquier otro error no controlado
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneral(Exception ex) {
        return buildError(HttpStatus.INTERNAL_SERVER_ERROR,
                "Error interno del servidor",
                ex.getMessage());
    }

    private ResponseEntity<Map<String, Object>> buildError(
            HttpStatus status, String error, String mensaje) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", error);
        body.put("message", mensaje);
        return ResponseEntity.status(status).body(body);
    }
}