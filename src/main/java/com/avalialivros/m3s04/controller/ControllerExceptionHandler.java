package com.avalialivros.m3s04.controller;

import com.avalialivros.m3s04.exceptions.BookNotFoundException;
import com.avalialivros.m3s04.exceptions.PersonNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(PersonNotFoundException.class)
    public ResponseEntity<ProblemDetail> handlePersonNotFoundException(PersonNotFoundException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, exception.getMessage());
        return ResponseEntity.badRequest().body(problemDetail);
    }

    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<ProblemDetail> handlePersonNotFoundException(BookNotFoundException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, exception.getMessage());
        return ResponseEntity.badRequest().body(problemDetail);
    }
}
