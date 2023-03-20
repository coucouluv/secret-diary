package com.project.secretdiary.exception;

public class EmailNotMatchException extends RuntimeException {

    public EmailNotMatchException(final String message) {
        super(message);
    }
    public EmailNotMatchException() {
        this("이메일이 일치하지 않습니다.");
    }
}
