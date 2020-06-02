package org.gel.transactionsystem.advice;

import org.gel.transactionsystem.expcetion.NotEnoughBalanceException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class NotEnoughBalanceAdvice {

    @ResponseBody
    @ExceptionHandler(NotEnoughBalanceException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String notEnoughBalanceHandler(NotEnoughBalanceException ex) {
        return ex.getMessage();
    }
}
