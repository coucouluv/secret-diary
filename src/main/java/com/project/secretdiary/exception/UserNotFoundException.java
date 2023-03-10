package com.project.secretdiary.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(final String message) {
        super(message);
    }
    public UserNotFoundException() {
        this("멤버가 존재하지 않습니다.");
    }

}
