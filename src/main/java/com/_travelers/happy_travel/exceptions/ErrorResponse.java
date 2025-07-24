package com._travelers.happy_travel.exceptions;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Builder
public record ErrorResponse
        (@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
         LocalDateTime timestamp,
         int status,
         String error,
         Object message,
         String path){

    public ErrorResponse (HttpStatus status, Object message, HttpServletRequest req){
        this(LocalDateTime.now(), status.value(), status.name(), message, String.valueOf(req.getRequestURI()));
    }

    public ErrorResponse (HttpStatus status, String error, Object message, HttpServletRequest req){
        this(LocalDateTime.now(), status.value(), error, message, String.valueOf(req.getRequestURI()));
    }
    public static ErrorResponse fromSecurityException(HttpStatus status, Exception exception, HttpServletRequest request) {

        HttpStatus safeStatus = status != null ? status : HttpStatus.INTERNAL_SERVER_ERROR;

        String msg = (exception != null && exception.getMessage() != null)
                ? exception.getMessage()
                : safeStatus.getReasonPhrase();

        String path = (request != null && request.getRequestURI() != null)
                ? request.getRequestURI()
                : "N/A";

        return new ErrorResponse(LocalDateTime.now(), safeStatus.value(), safeStatus.name(), msg, path);
    }
}
