package com.project.secretdiary.exception;

import java.io.IOException;

public class FileIOException extends RuntimeException  {
    public FileIOException(final String message) {
        super(message);
    }
    public FileIOException() {
        this("이미지 저장 시 오류가 발생했습니다.");
    }
}