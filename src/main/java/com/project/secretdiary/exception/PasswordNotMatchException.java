package com.project.secretdiary.exception;

public class PasswordNotMatchException extends RuntimeException{
    public PasswordNotMatchException(final String message) {
        super(message);
    }
    public PasswordNotMatchException() {
        this("비밀번호가 일치하지 않습니다.");
    }
}