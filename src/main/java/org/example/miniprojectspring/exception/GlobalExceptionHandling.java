package org.example.miniprojectspring.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;

@RestControllerAdvice
public class GlobalExceptionHandling {
//    @ExceptionHandler(CustomNotFoundException.class)
//    public ProblemDetail SearchNotFoundException(CustomNotFoundException e) {
//        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
//        problemDetail.setTitle("Not Found");
//        problemDetail.setType(URI.create("http://localhost:8080/api/v1/notfound"));
//        problemDetail.setProperty("timestamp", new Date());
//        return problemDetail;
//    }
@ExceptionHandler({SearchNotFoundException.class})
public ProblemDetail handlerAllNotFoundException(SearchNotFoundException e) {
    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
    problemDetail.setType(URI.create("about:blank"));
    problemDetail.setTitle("Category Not Found");
    problemDetail.setStatus(404);
    problemDetail.setDetail(e.getMessage());
    problemDetail.setProperty("timestamp", LocalDateTime.now());
    return problemDetail;
}

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        HashMap<String, String> errors = new HashMap<>();
        for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Input invalid");
        problemDetail.setTitle("Bad Request");
        problemDetail.setType(URI.create("http://localhost:8080/api/v1/badRequest"));
        problemDetail.setProperty("timestamp", new Date());
        problemDetail.setProperty("errors", errors);
        return problemDetail;
    }


    @ExceptionHandler(HandlerMethodValidationException.class)
    public ProblemDetail handleMethodValidationException(HandlerMethodValidationException e) {
        HashMap<String, String> errors = new HashMap<>();

        for (var parameterError : e.getAllValidationResults()) {
            String parameterName = parameterError.getMethodParameter().getParameterName();
            //get message error
            for (var error : parameterError.getResolvableErrors()) {
                errors.put(parameterName, error.getDefaultMessage());
            }
        }
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                "Invalid request"
        );
        problemDetail.setTitle("Bad Request");
        problemDetail.setProperty("timestamp", LocalDateTime.now());
        problemDetail.setProperty("error", errors);
        return problemDetail;
    }

}