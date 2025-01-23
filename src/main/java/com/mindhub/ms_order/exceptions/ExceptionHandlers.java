package com.mindhub.ms_order.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlers {

    @ExceptionHandler(NotValidArgument.class)
    public ResponseEntity<String> handleNotValidArgumentException(NotValidArgument notValidArgument) {
        return new ResponseEntity<>(notValidArgument.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
