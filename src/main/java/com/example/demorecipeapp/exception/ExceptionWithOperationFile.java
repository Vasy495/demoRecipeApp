package com.example.demorecipeapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class ExceptionWithOperationFile extends Exception {
    public ExceptionWithOperationFile(String message) {
        super(message);
    }
}
