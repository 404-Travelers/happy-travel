package com._travelers.happy_travel.exceptions;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@JsonPropertyOrder({"timestamp", "status", "error", "message", "path"})
public class ErrorResponse {
    @JsonFormat(pattern="yyyy-MM-DD HH:mm:ss")
    private final LocalDateTime timestamp;
    private final int status;
    private final String error;
    private final String message;
    private final String path;

    public ErrorResponse(HttpStatus status,String message, String path){
        this.timestamp = LocalDateTime.now();
        this.status = status.value();
        this.error = status.name();
        this.message = message;
        this.path = path;
    }


}
