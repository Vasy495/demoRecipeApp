package com.example.demorecipeapp.controller;

import com.example.demorecipeapp.record.InfoRecord;
import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(
            summary = "Информация о приложении"
    )
    public InfoRecord info() {
        return new InfoRecord("Maxim", "RecipesApp", LocalDate.of(2022, 12, 31), "test");
    }
}
