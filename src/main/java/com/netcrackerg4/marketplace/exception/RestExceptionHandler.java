package com.netcrackerg4.marketplace.exception;

import com.netcrackerg4.marketplace.model.domain.ErrorBody;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {IllegalStateException.class})
    protected ResponseEntity<Object> handleIllegalStateConflict(IllegalStateException ex, WebRequest req) {
        var body =
                ErrorBody.builder()
                .message(ex.getMessage())
                .description("talk to devs")
                .build();
        return handleExceptionInternal(ex, body, new HttpHeaders(), HttpStatus.BAD_REQUEST, req);
    }
}
