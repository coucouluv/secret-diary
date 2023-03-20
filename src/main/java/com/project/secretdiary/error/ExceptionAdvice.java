package com.project.secretdiary.error;

import com.project.secretdiary.exception.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequiredArgsConstructor
public class ExceptionAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse HandleValidationExceptions(MethodArgumentNotValidException e) {
        String message = "";

        for(ObjectError error: e.getBindingResult().getAllErrors()) {
            message = error.getDefaultMessage();
            break;
        }
        return new ErrorResponse(message);
    }

    @ExceptionHandler(RegisterFailedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse HandleRegisterFailedExceptions(RegisterFailedException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler({UserNotFoundException.class, DiaryNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse HandleUserNotFoundExceptions(RuntimeException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler({CustomJwtException.class, PasswordNotMatchException.class,
                        EmailNotMatchException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse HandlerJwtException(RuntimeException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler(DiaryException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse HandlerDiaryException(DiaryException e) {
        return new ErrorResponse(e.getMessage());
    }
}