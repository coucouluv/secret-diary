package com.project.secretdiary.exception;

public class InvalidFriendStatusException extends RuntimeException{
    public InvalidFriendStatusException(final String message) {
        super(message);
    }
    public InvalidFriendStatusException() {
        this("친구 상태가 올바르지 않습니다.");
    }
}

