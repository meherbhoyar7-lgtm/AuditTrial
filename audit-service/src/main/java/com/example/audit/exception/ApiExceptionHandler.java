package com.example.audit.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ApiExceptionHandler {

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<?> badRequest(IllegalArgumentException ex) {
    return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<?> validation(MethodArgumentNotValidException ex) {
    Map<String, Object> body = new HashMap<>();
    body.put("error", "Validation failed");
    body.put("details", ex.getBindingResult().getFieldErrors().stream()
        .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
        .toList());
    return ResponseEntity.badRequest().body(body);
  }
}
