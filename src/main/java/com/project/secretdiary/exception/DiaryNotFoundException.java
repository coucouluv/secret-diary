package com.project.secretdiary.exception;

public class DiaryNotFoundException extends RuntimeException {
    public DiaryNotFoundException(String message) {
        super(message);
    }
    public DiaryNotFoundException() {
        this("해당 다이어리가 존재하지 않습니다.");
    }
}
