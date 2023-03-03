package com.project.secretdiary.exception;

import org.springframework.http.HttpStatus;

public class CustomJwtException extends RuntimeException {
    private final String message;
    private HttpStatus httpStatus;
    public CustomJwtException(String message) {
        this.message = message;
    }
    public CustomJwtException(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }

    @Override
    public String getMessage() {
        return message;
    }

}

