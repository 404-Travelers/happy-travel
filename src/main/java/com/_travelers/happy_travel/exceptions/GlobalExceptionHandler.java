package com._travelers.happy_travel.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEventNotFoundException (EntityNotFoundException exception, HttpServletRequest req) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        ErrorResponse errorResponse = new ErrorResponse(status, exception.getMessage(), req);
        return new ResponseEntity<>(errorResponse,status);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException exception, HttpServletRequest req) {
        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ErrorResponse errorResponse =new ErrorResponse(status, exception.getMessage(), req);
        return new ResponseEntity<>(errorResponse, status);
    }

    @ExceptionHandler(EntityAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handlerEntityAlreadyExistsException(EntityAlreadyExistsException exception, HttpServletRequest req) {
        HttpStatus status = HttpStatus.CONFLICT;
        ErrorResponse errorResponse =new ErrorResponse(status, exception.getMessage(), req);
        return new ResponseEntity<>(errorResponse, status);
    }
}
