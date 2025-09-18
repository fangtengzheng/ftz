package com.example.a.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(
            IllegalArgumentException ex, WebRequest request) {

        Map<String, Object> body = new HashMap<>();
        body.put("status", "error");
        body.put("message", ex.getMessage());
        body.put("error", "BAD_REQUEST");

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<Object> handleSecurityException(
            SecurityException ex, WebRequest request) {

        Map<String, Object> body = new HashMap<>();
        body.put("status", "error");
        body.put("message", ex.getMessage());
        body.put("error", "FORBIDDEN");

        return new ResponseEntity<>(body, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Map<String, Object> handleException(Exception e) {
        e.printStackTrace(); // 确保异常会打印到控制台
        Map<String, Object> result = new HashMap<>();
        result.put("status", 500);
        result.put("msg", e.getMessage());
        return result;
    }
}