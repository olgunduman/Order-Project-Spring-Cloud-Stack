package com.example.PaymentService.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GeneralExceptionHandler {

    @ExceptionHandler(value = CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException exception){
        return new ResponseEntity<>(ErrorResponse.builder()
                .message(exception.getMessage())
                .errorCode(exception.getErrorCode())
                .build(), HttpStatus.valueOf(exception.getStatus()));


    }
}
