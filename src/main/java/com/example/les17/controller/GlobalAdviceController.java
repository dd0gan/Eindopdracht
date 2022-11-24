package com.example.les17.controller;

import com.example.les17.dto.ErrorDto;
import com.example.les17.exception.InvalidApplicationException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Date;

@ControllerAdvice
public class GlobalAdviceController {

    @ExceptionHandler({ InvalidApplicationException.class })
    public ResponseEntity<ErrorDto> handleException(Exception ex) {
        return new ResponseEntity<>(new ErrorDto(new Date(), HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), ex.getMessage(), ExceptionUtils.getStackTrace(ex)), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
