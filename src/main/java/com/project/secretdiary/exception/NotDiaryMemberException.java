package com.project.secretdiary.exception;

public class NotDiaryMemberException extends RuntimeException {
    public NotDiaryMemberException(String message) {
        super(message);
    }
    public NotDiaryMemberException() {
        this("다이어리의 멤버가 일치하지 않습니다.");
    }
}