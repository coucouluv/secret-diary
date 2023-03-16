package com.project.secretdiary.exception;

public class FriendException extends RuntimeException{
    public FriendException(final String message) {
        super(message);
    }
    public FriendException() {
        this("해당 친구가 존재하지 않습니다.");
    }
}

