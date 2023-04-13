package com.project.secretdiary.exception;

public class InvalidExtensionException extends RuntimeException{
    public InvalidExtensionException(final String message) {
        super(message);
    }
    public InvalidExtensionException() {
        this("지원하지 않은 확장자입니다.");
    }
}