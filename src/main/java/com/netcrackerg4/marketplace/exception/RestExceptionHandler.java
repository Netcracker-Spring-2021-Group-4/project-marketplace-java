package com.netcrackerg4.marketplace.exception;

import com.netcrackerg4.marketplace.model.domain.error.ErrorBody;
import com.netcrackerg4.marketplace.model.domain.error.ErrorListBody;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.NoSuchElementException;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {IllegalStateException.class})
    public ResponseEntity<Object> handleIllegalStateConflict(IllegalStateException ex, WebRequest req) {
        var body =
                ErrorBody.builder()
                        .message(ex.getMessage())
                        .description("talk to devs")
                        .build();
        return handleExceptionInternal(ex, body, new HttpHeaders(), HttpStatus.BAD_REQUEST, req);
    }

    @ExceptionHandler(value = {UsernameNotFoundException.class})
    public ResponseEntity<Object> handleIllegalStateConflict(UsernameNotFoundException ex, WebRequest req) {
        var body =
                ErrorBody.builder()
                        .message("Account linked to that email not found")
                        .build();
        return handleExceptionInternal(ex, body, new HttpHeaders(), HttpStatus.BAD_REQUEST, req);
    }

    @ExceptionHandler(value = {InvalidTokenException.class})
    public ResponseEntity<Object> HandleActivatedTokenReuseConflict(InvalidTokenException ex, WebRequest req) {
        var body =
                ErrorBody.builder()
                        .message(ex.getMessage())
                        .build();
        return handleExceptionInternal(ex, body, new HttpHeaders(), HttpStatus.EXPECTATION_FAILED, req);
    }

    @ExceptionHandler(value = {NoSuchElementException.class})
    public ResponseEntity<?> handleEmptyOptional(NoSuchElementException e, WebRequest req) {
        var body = ErrorBody.builder()
                .message("The data needed to process your request is missing.")
                .description("You are trying to access a resource that does not exist.")
                .build();
        return handleExceptionInternal(e, body, new HttpHeaders(), HttpStatus.NOT_ACCEPTABLE, req);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        HashMap<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

        var body =
                ErrorListBody.builder()
                        .error(errors)
                        .build();
        return handleExceptionInternal(ex, body, headers, status, request);
    }
}
