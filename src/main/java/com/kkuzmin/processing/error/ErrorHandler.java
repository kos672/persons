package com.kkuzmin.processing.error;

import com.kkuzmin.processing.field.FieldProcessingException;
import com.kkuzmin.processing.person.PersonNotExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDateTime;

@ControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        return new ResponseEntity<>(new ErrorResponse("Internal server error", ex.getMessage(), LocalDateTime.now()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(PersonNotExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handlePersonNotExistsException(Exception ex) {
        return new ResponseEntity<>(new ErrorResponse("Bad request", ex.getMessage(), LocalDateTime.now()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FieldProcessingException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> handleFieldProcessingException(Exception ex) {
        return new ResponseEntity<>(new ErrorResponse("Internal server error", ex.getMessage(), LocalDateTime.now()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
