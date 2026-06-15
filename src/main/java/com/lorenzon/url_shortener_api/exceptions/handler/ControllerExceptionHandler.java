package com.lorenzon.url_shortener_api.exceptions.handler;

import com.lorenzon.url_shortener_api.exceptions.ShortCodeNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.net.URI;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail methodArgumentNotValidException(MethodArgumentNotValidException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setTitle("Invalid field");
        problemDetail.setDetail("Original URL cannot be blank");
        problemDetail.setType(URI.create("https://url-shortener-api.com/errors/invalid-fields"));

        return problemDetail;
    }

    @ExceptionHandler(ShortCodeNotFoundException.class)
    public ProblemDetail shortCodeNotFoundException(ShortCodeNotFoundException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        problemDetail.setTitle("Short code not found");
        problemDetail.setDetail(e.getMessage());
        problemDetail.setType(URI.create("https://url-shortener-api.com/errors/not-found"));

        return problemDetail;
    }
}
