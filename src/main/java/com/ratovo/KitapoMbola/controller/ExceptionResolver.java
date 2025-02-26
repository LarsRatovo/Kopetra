package com.ratovo.KitapoMbola.controller;

import com.ratovo.KitapoMbola.dto.response.ErrorResponseDto;
import com.ratovo.KitapoMbola.exception.LogicException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class ExceptionResolver extends DefaultHandlerExceptionResolver {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleConstraintViolation(MethodArgumentNotValidException exception, HttpServletRequest request) {
        List<String> messages = new ArrayList<>();
        exception.getBindingResult().getAllErrors().forEach((error) -> {
            messages.add(error.getDefaultMessage());
        });
        ErrorResponseDto errorResponseDto = ErrorResponseDto.builder()
                .messages(messages)
                .exceptionMessage(exception.getMessage())
                .build();
        return new ResponseEntity<>(errorResponseDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(LogicException.class)
    public ResponseEntity<?> handleConstraintViolation(LogicException exception, HttpServletRequest request) {
        return new ResponseEntity<>(ErrorResponseDto.builder().messages(List.of(exception.getMessage())).build(), HttpStatus.BAD_REQUEST);
    }
}
