package com.mindhub.ms_order.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlers {

    @ExceptionHandler(NotValidArgumentException.class)
    public ResponseEntity<String> handleNotValidArgumentException(NotValidArgumentException notValidArgumentException) {
        return new ResponseEntity<>(notValidArgumentException.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> handleNotFoundExceptionException(NotFoundException notFoundException) {
        return new ResponseEntity<>(notFoundException.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NotAuthorizedException.class)
    public ResponseEntity<String> handleNotAuthorizedException(NotAuthorizedException notAuthorizedException) {
        return new ResponseEntity<>(notAuthorizedException.getMessage(), HttpStatus.UNAUTHORIZED);
    }
}
