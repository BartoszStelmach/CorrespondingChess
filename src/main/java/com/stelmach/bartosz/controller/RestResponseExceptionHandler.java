package com.stelmach.bartosz.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Arrays;

@RestControllerAdvice @Slf4j
public class RestResponseExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value = IllegalArgumentException.class)
    protected ResponseEntity<Object> handleClientError(RuntimeException ex, WebRequest request) {
        return processException(ex, request, "Cannot perform specified action. Please check if it's correct.\nCause: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = ChessAppException.class)
    protected ResponseEntity<Object> handleServerError(RuntimeException ex, WebRequest request) {
        return processException(ex, request, "Please contact support or try again later", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<Object> processException(RuntimeException ex, WebRequest request, String responseMessage, HttpStatus httpStatus) {
        logException(ex, request);
        return handleExceptionInternal(ex, responseMessage, new HttpHeaders(), httpStatus, request);
    }

    private void logException(RuntimeException ex, WebRequest request) {
        log.error("Request mapping: " + request.getDescription(false));
        log.error("Request input params:");
        request.getParameterMap().forEach((key, value) -> log.error(key + ": " + Arrays.toString(value)));
        log.error("Exception message: " + ex.getMessage());
        log.error("Exception stack trace: " + ExceptionUtils.getStackTrace(ex));
    }

    public static class ChessAppException extends RuntimeException {
        public ChessAppException(String errorMessage) {
            super(errorMessage);
        }
        public ChessAppException(String errorMessage, Throwable cause) {
            super(errorMessage, cause);
        }
    }
}

