package com.project.secretdiary.error;

import com.project.secretdiary.exception.*;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.apache.tomcat.util.http.fileupload.impl.SizeException;
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

    @ExceptionHandler(FileSizeLimitExceededException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse HandleValidationExceptions(SizeException e) {
        return new ErrorResponse("10MB 이하의 파일만 업로드 가능합니다.");
    }

    @ExceptionHandler(RegisterFailedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse HandleRegisterFailedExceptions(RegisterFailedException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler(InvalidExtensionException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse HandleInvalidExceptions(InvalidExtensionException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler(FileIOException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse HandleFileIOExceptions(FileIOException e) {
        return new ErrorResponse(e.getMessage());
    }


    @ExceptionHandler({UserNotFoundException.class, DiaryNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse HandleNotFoundExceptions(RuntimeException e) {
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