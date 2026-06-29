package com.hvb.loyalty.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Errores de negocio que lanzamos a propósito (mensajes claros para el usuario)
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntime(RuntimeException ex) {
        Map<String, String> error = new HashMap<>();
        String mensaje = ex.getMessage();

        // Si el mensaje es nulo o parece técnico, mandamos uno genérico
        if (mensaje == null || mensaje.isBlank()
                || mensaje.contains("Exception")
                || mensaje.contains("null")
                || mensaje.length() > 200) {
            error.put("error", "Ocurrió un error al procesar la solicitud. Intenta de nuevo.");
        } else {
            error.put("error", mensaje); // mensaje claro que nosotros escribimos
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    // Errores de validación (@NotBlank, @Email, etc.)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidacion(MethodArgumentNotValidException ex) {
        Map<String, String> errores = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(e -> errores.put(e.getField(), e.getDefaultMessage()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errores);
    }

    // Cualquier otro error inesperado (red de seguridad) — nunca muestra detalles técnicos
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGeneral(Exception ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Ocurrió un error inesperado. Por favor intenta más tarde.");
        // Para ti (desarrollo), el error real se imprime en la consola del backend:
        System.err.println("Error inesperado: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}