package com.example.demorecipeapp.controller;

import com.example.demorecipeapp.record.InfoRecord;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
public class InfoController {

    @GetMapping
    public String hi() {
        return "Приложение запущено";
    }

    @GetMapping("/info")
    public InfoRecord info() {
        return new InfoRecord("Maxim", "RecipesApp", LocalDate.of(2022, 12, 31), "test");
    }
}
