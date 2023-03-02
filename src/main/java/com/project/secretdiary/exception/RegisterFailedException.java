package com.project.secretdiary.exception;

public class RegisterFailedException extends RuntimeException{

    public RegisterFailedException() {
    }
    public RegisterFailedException(String message) {
        super(message);
    }

}
