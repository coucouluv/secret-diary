package com.project.secretdiary.error;

import com.project.secretdiary.exception.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequiredArgsConstructor
public class ExceptionAdvice {

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorResponse> HandleValidationExceptions(BindException e) {
        String message = "";

        for(ObjectError error: e.getBindingResult().getAllErrors()) {
            message = error.getDefaultMessage();
            break;
        }
        ErrorResponse errorResponse = new ErrorResponse(message);
        return ResponseEntity.badRequest().body(errorResponse);
    }


    @ExceptionHandler({UserNotFoundException.class, DiaryNotFoundException.class, FriendNotFoundException.class})
    public ResponseEntity<ErrorResponse> HandleNotFoundExceptions(RuntimeException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler({InvalidTokenException.class, PasswordNotMatchException.class,
                        EmailNotMatchException.class, NotDiaryMemberException.class,
                        InvalidFriendStatusException.class, RegisterFailedException.class, ExistingUserException.class})
    public ResponseEntity<ErrorResponse> HandleBadRequestException(RuntimeException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
        return ResponseEntity.badRequest().body(errorResponse);
    }

}