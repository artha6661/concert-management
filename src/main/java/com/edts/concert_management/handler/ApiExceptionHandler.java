package com.edts.concert_management.handler;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {
  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<Map<String, Object>> notFound(EntityNotFoundException e) {
    Map<String, Object> body = new LinkedHashMap<String, Object>();
    body.put("timestamp", Instant.now().toString());
    body.put("status", 404);
    body.put("error", "Not Found");
    body.put("message", e.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, Object>> validation(MethodArgumentNotValidException e) {
    List<Map<String, String>> errors =
        e.getBindingResult().getAllErrors().stream()
            .map(
                err -> {
                  Map<String, String> m = new LinkedHashMap<String, String>();
                  if (err instanceof FieldError) {
                    FieldError fe = (FieldError) err;
                    m.put("field", fe.getField());
                  } else {
                    m.put("field", err.getObjectName());
                  }
                  m.put("message", err.getDefaultMessage());
                  return m;
                })
            .collect(Collectors.toList());

    Map<String, Object> body = new LinkedHashMap<String, Object>();
    body.put("timestamp", Instant.now().toString());
    body.put("status", 400);
    body.put("error", "Bad Request");
    body.put("validationErrors", errors);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
  }
}

