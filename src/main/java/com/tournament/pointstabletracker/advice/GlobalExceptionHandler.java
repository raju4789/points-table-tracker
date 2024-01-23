package com.tournament.pointstabletracker.advice;

import com.tournament.pointstabletracker.dto.CommonApiResponse;
import com.tournament.pointstabletracker.dto.ErrorDetails;
import com.tournament.pointstabletracker.exceptions.InvalidRequestException;
import com.tournament.pointstabletracker.exceptions.RecordNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.validation.FieldError;


import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<CommonApiResponse<String>> handleInvalidRequestException(InvalidRequestException ex) {
        CommonApiResponse<String> commonApiResponse = new CommonApiResponse<>("false", null, new ErrorDetails(HttpStatus.BAD_REQUEST.value(), ex.getMessage()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(commonApiResponse);
    }

    @ExceptionHandler(RecordNotFoundException.class)
    public ResponseEntity<CommonApiResponse<String>> handleRecordNotFoundException(RecordNotFoundException ex) {
        CommonApiResponse<String> commonApiResponse = new CommonApiResponse<>("false", null, new ErrorDetails(HttpStatus.BAD_REQUEST.value(), ex.getMessage()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(commonApiResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CommonApiResponse<List<String>>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<String> fieldErrorMessages = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .toList();

        CommonApiResponse<List<String>> commonApiResponse = new CommonApiResponse<List<String>>("false", null,
                new ErrorDetails(HttpStatus.BAD_REQUEST.value(), fieldErrorMessages.toString()));

        return ResponseEntity.badRequest().body(commonApiResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CommonApiResponse<String>> handleDefaultException(Exception ex) {
        ex.printStackTrace();
        CommonApiResponse<String> commonApiResponse = new CommonApiResponse<>("false", null, new ErrorDetails(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage()));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(commonApiResponse);
    }
}

