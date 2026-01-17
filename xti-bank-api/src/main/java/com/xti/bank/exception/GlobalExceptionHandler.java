package com.xti.bank.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 404 - Resource not found
    @ExceptionHandler(com.xti.bank.exception.EntityNotFoundException.class)
    public ResponseEntity<Object> handleEntityNotFound(
            EntityNotFoundException ex,
            WebRequest request) {

        Map<String, Object> body = createProblemDetail(
                HttpStatus.NOT_FOUND,
                ex.getMessage(),
                request
        );

        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    // 400 - Validation errors (e.g. @Valid fails)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationExceptions(
            MethodArgumentNotValidException ex,
            WebRequest request) {

        Map<String, Object> body = createProblemDetail(
                HttpStatus.BAD_REQUEST,
                "Validation failed",
                request
        );

        // Add field errors
        Map<String, String> errors = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        body.put("errors", errors);

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    // Business / domain exceptions (e.g. invalid state transition)
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Object> handleIllegalState(
            IllegalStateException ex,
            WebRequest request) {

        Map<String, Object> body = createProblemDetail(
                HttpStatus.CONFLICT, // or BAD_REQUEST – your choice
                ex.getMessage(),
                request
        );

        return new ResponseEntity<>(body, HttpStatus.CONFLICT);
    }

    // Generic catch-all for unexpected errors
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAllUncaughtExceptions(
            Exception ex,
            WebRequest request) {

        Map<String, Object> body = createProblemDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "An unexpected error occurred",
                request
        );

        // In production: you should log the full stack trace here
        // log.error("Unhandled exception", ex);

        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // ───────────────────────────────────────────────────────────────
    // Helper - RFC 7807 / Problem Details style (modern standard)
    // ───────────────────────────────────────────────────────────────
    private Map<String, Object> createProblemDetail(
            HttpStatus status,
            String message,
            WebRequest request) {

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", Instant.now().toString());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", message);
        body.put("path", request.getDescription(false).replace("uri=", ""));

        return body;
    }
}
