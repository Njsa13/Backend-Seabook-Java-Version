package com.backend.seabook.handler;

import com.backend.seabook.dto.response.base.APIResponse;
import com.backend.seabook.exception.DataNotFoundException;
import com.backend.seabook.exception.ServiceBusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public APIResponse handlerNoHandlerFoundException(NoHandlerFoundException e) {
        return new APIResponse(
                HttpStatus.BAD_REQUEST,
                "Url not found: " + e.getMessage()
        );
    }

    @ExceptionHandler(DataNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public APIResponse handlerDataNotFoundException(DataNotFoundException e) {
        return new APIResponse(
                HttpStatus.NOT_FOUND,
                e.getMessage()
        );
    }

    @ExceptionHandler(ServiceBusinessException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public APIResponse handlerServiceBusinessException(ServiceBusinessException e) {
        return new APIResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                e.getMessage()
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public APIResponse handlerIllegalArgumentException(IllegalArgumentException e) {
        return new APIResponse(
                HttpStatus.BAD_REQUEST,
                e.getMessage()
        );
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public APIResponse handlerBadCredentialsException(BadCredentialsException e) {
        return new APIResponse(
                HttpStatus.UNAUTHORIZED,
                e.getMessage()
        );
    }
}
