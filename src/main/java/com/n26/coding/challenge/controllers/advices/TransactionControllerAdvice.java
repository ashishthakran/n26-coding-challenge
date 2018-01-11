package com.n26.coding.challenge.controllers.advices;

import static org.springframework.http.HttpStatus.NO_CONTENT;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.n26.coding.challenge.exceptions.ExpiredTimestampException;

@ControllerAdvice
public class TransactionControllerAdvice {

    @ResponseStatus(NO_CONTENT)
    @ExceptionHandler(ExpiredTimestampException.class)
    public void handleInvalidTimestampException() {
    }
}
