package com.project.secretdiary.exception;

public class FriendNotFoundException extends RuntimeException{
    public FriendNotFoundException(final String message) {
        super(message);
    }

    public FriendNotFoundException() {
        this("해당 친구가 존재하지 않습니다.");
    }
}