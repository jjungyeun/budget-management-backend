package com.wonjung.budget.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.wonjung.budget.exception.ErrorCode.INTERNAL_SERVER_ERROR;
import static com.wonjung.budget.exception.ErrorCode.INVALID_REQUEST;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {
        ErrorResponse response = ErrorResponse.builder()
                .errorCode(e.getErrorCode())
                .message(e.getMessage())
                .build();

        log.warn("{}", e.getMessage());
        return ResponseEntity.status(e.getStatus()).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        ErrorCode errorCode = INTERNAL_SERVER_ERROR;
        ErrorResponse response = ErrorResponse.builder()
                .errorCode(errorCode)
                .message(errorCode.getMessage())
                .build();

        log.error("Exception is occurred.", e);
        return ResponseEntity.status(errorCode.getStatus()).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        StringBuilder errorMessage = new StringBuilder();

        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            if (!errorMessage.isEmpty()) {
                errorMessage.append("; ");
            }
            errorMessage.append(fieldError.getDefaultMessage());
        }

        ErrorResponse response = ErrorResponse.builder()
                .errorCode(INVALID_REQUEST)
                .message(errorMessage.toString())
                .build();

        log.error("MethodArgumentNotValidException is occurred: {}", e.getMessage());
        return ResponseEntity.status(response.getErrorCode().getStatus()).body(response);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        ErrorCode errorCode = INVALID_REQUEST;
        ErrorResponse response = ErrorResponse.builder()
                .errorCode(errorCode)
                .message(errorCode.getMessage())
                .build();

        log.error("DataIntegrityViolationException is occurred: {}", e.getMessage());
        return ResponseEntity.status(errorCode.getStatus()).body(response);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        ErrorCode errorCode = INVALID_REQUEST;
        ErrorResponse response = ErrorResponse.builder()
                .errorCode(errorCode)
                .message(errorCode.getMessage())
                .build();

        log.error("MissingServletRequestParameterException is occurred: {}", e.getMessage());
        return ResponseEntity.status(errorCode.getStatus()).body(response);
    }

}