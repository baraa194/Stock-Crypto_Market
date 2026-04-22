package com.myProject.demo.Errors;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ErrorResponse {
    private int status;
    private String message;
    private LocalDateTime timestamp;
    private String RequestURI;

    public ErrorResponse(int status, String message,String RequestURI) {
        this.status = status;
        this.message = message;
        this.timestamp = LocalDateTime.now();
        this.RequestURI = RequestURI;
    }

}
