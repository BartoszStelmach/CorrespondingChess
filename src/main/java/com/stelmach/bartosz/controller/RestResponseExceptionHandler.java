package com.stelmach.bartosz.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice @Slf4j
public class RestResponseExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value = IllegalArgumentException.class)
    protected ResponseEntity<Object> handleClientError(IllegalArgumentException ex, WebRequest request) {
        return processException(ex, request, "Cannot perform specified action. Please check if it's correct.\nCause: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = ChessAppException.class)
    protected ResponseEntity<Object> handleServerError(ChessAppException ex, WebRequest request) {
        return processException(ex, request, "Please contact support or try again later", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Map<String, String> errorMap = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errorMap.put(error.getField(),error.getDefaultMessage());
        });
        return new ResponseEntity<>(errorMap, HttpStatus.BAD_REQUEST);

    }

    private ResponseEntity<Object> processException(Exception ex, WebRequest request, String responseMessage, HttpStatus httpStatus) {
        logException(ex, request);
        return handleExceptionInternal(ex, responseMessage, new HttpHeaders(), httpStatus, request);
    }

    private void logException(Exception ex, WebRequest request) {
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

