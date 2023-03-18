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
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonResponse HandleValidationExceptions(MethodArgumentNotValidException e) {
        String message = "";

        for(ObjectError error: e.getBindingResult().getAllErrors()) {
            message = error.getDefaultMessage();
            break;
        }
        return responseService.setFailedResponse(message);
    }

    @ExceptionHandler(RegisterFailedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonResponse HandleRegisterFailedExceptions(RegisterFailedException e) {
        return responseService.setFailedResponse(e.getMessage());
    }

    @ExceptionHandler({UserNotFoundException.class, DiaryNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public CommonResponse HandleUserNotFoundExceptions(UserNotFoundException e) {
        return responseService.setFailedResponse(e.getMessage());
    }

    @ExceptionHandler({CustomJwtException.class, PasswordNotMatchException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonResponse HandlerJwtException(CustomJwtException e) {
        return responseService.setFailedResponse(e.getMessage());
    }

    @ExceptionHandler(DiaryException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonResponse HandlerDiaryException(DiaryException e) {
        return responseService.setFailedResponse(e.getMessage());
    }
}