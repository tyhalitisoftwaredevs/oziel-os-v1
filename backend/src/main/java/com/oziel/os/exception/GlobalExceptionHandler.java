package com.oziel.os.exception;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import java.time.Instant;
import java.util.Map;
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String,Object>> notFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("timestamp",Instant.now(),"error",ex.getMessage()));
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,Object>> invalid(MethodArgumentNotValidException ex) {
        return ResponseEntity.badRequest().body(Map.of("timestamp",Instant.now(),"error","Validation failed"));
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String,Object>> general(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("timestamp",Instant.now(),"error","An unexpected error occurred"));
    }
}
