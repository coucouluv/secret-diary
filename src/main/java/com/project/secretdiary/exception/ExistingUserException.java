package com.project.secretdiary.exception;

public class ExistingUserException extends RuntimeException {
    public ExistingUserException(final String message) {
        super(message);
    }
    public ExistingUserException() {
        this("이미 존재하는 멤버입니다.");
    }
}
