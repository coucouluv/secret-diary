package com.project.secretdiary.exception;

import com.project.secretdiary.response.CommonResponse;
import com.project.secretdiary.response.ResponseService;
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

    private final ResponseService responseService;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public CommonResponse HandleValidationExceptions(MethodArgumentNotValidException e) {
        String message = "";

        for(ObjectError error: e.getBindingResult().getAllErrors()) {
            message = error.getDefaultMessage();
            break;
        }
        return responseService.setFailedResponse(message);
    }

    @ExceptionHandler(RegisterFailedException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public CommonResponse HandleRegisterFailedExceptions(RegisterFailedException e) {
        return responseService.setFailedResponse(e.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public CommonResponse HandleUserNotFoundExceptions(UserNotFoundException e) {
        return responseService.setFailedResponse(e.getMessage());
    }

    @ExceptionHandler({CustomJwtException.class, PasswordNotMatchException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonResponse HandlerJwtException(CustomJwtException e) {
        return responseService.setFailedResponse(e.getMessage());
    }

    @ExceptionHandler(DiaryException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public CommonResponse HandlerDiaryException(DiaryException e) {
        return responseService.setFailedResponse(e.getMessage());
    }
}