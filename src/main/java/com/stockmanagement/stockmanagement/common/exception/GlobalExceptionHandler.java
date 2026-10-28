package com.stockmanagement.stockmanagement.common.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // ---------- 404 ----------
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(ResourceNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    // ---------- 409: duplicates ----------
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<Map<String, Object>> handleDuplicate(DuplicateResourceException ex) {
        return buildResponse(HttpStatus.CONFLICT, ex.getMessage());
    }

    // ---------- 400: business rule violations ----------
    @ExceptionHandler(BusinessRuleException.class)
    public ResponseEntity<Map<String, Object>> handleBusinessRule(BusinessRuleException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    // ---------- 400: bean validation ----------
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", Instant.now().toString());
        body.put("status", 400);
        body.put("message", "Validation failed");
        body.put("errors", errors);
        return ResponseEntity.badRequest().body(body);
    }

    // ---------- 409: optimistic locking (concurrent update) ----------
    @ExceptionHandler(OptimisticLockingFailureException.class)
    public ResponseEntity<Map<String, Object>> handleOptimisticLock(OptimisticLockingFailureException ex) {
        return buildResponse(
                HttpStatus.CONFLICT,
                "Resource was modified by another request. Reload and retry."
        );
    }

    // ---------- 409: DB constraint (slug/code uniqueness race) ----------
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> handleIntegrity(DataIntegrityViolationException ex) {
        return buildResponse(
                HttpStatus.CONFLICT,
                "Operation conflicts with existing data"
        );
    }

    // ---------- helper ----------
    private ResponseEntity<Map<String, Object>> buildResponse(HttpStatus status, String message) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", Instant.now().toString());
        body.put("status", status.value());
        body.put("message", message);
        return ResponseEntity.status(status).body(body);
    }
}

//package com.stockmanagement.stockmanagement.common.exception;
//
//
//
//import org.springframework.http.*;
//import org.springframework.web.bind.MethodArgumentNotValidException;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@RestControllerAdvice
//public class GlobalExceptionHandler {
//
//
//    @ExceptionHandler(
//            ResourceNotFoundException.class
//    )
//    public ResponseEntity<Map<String, Object>>
//    handleNotFound(
//            ResourceNotFoundException ex) {
//
//        return buildResponse(
//                HttpStatus.NOT_FOUND,
//                ex.getMessage()
//        );
//    }
//
//
//    @ExceptionHandler(
//            DuplicateResourceException.class
//    )
//    public ResponseEntity<Map<String, Object>>
//    handleDuplicate(
//            DuplicateResourceException ex) {
//
//        return buildResponse(
//                HttpStatus.CONFLICT,
//                ex.getMessage()
//        );
//    }
//
//
//    @ExceptionHandler(
//            MethodArgumentNotValidException.class
//    )
//    public ResponseEntity<Map<String, Object>>
//    handleValidation(
//            MethodArgumentNotValidException ex) {
//
//        Map<String, String> errors =
//                new HashMap<>();
//
//        ex.getBindingResult()
//                .getFieldErrors()
//                .forEach(error ->
//                        errors.put(
//                                error.getField(),
//                                error.getDefaultMessage()
//                        )
//                );
//
//        Map<String, Object> body =
//                new HashMap<>();
//
//        body.put("status", 400);
//        body.put("message", "Validation failed");
//        body.put("errors", errors);
//
//        return ResponseEntity
//                .badRequest()
//                .body(body);
//    }
//
//
//    private ResponseEntity<Map<String, Object>>
//    buildResponse(
//            HttpStatus status,
//            String message) {
//
//        Map<String, Object> body =
//                new HashMap<>();
//
//        body.put(
//                "status",
//                status.value()
//        );
//
//        body.put(
//                "message",
//                message
//        );
//
//        return ResponseEntity
//                .status(status)
//                .body(body);
//    }
//}