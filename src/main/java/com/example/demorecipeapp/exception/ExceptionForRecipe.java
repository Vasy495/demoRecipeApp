package com.example.demorecipeapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ExceptionForRecipe extends Exception{
    public ExceptionForRecipe(String message) {
                super(message);
    }

}
