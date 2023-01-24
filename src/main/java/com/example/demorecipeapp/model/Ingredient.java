package com.example.demorecipeapp.model;

import lombok.Data;

@Data
public class Ingredient {
    private String title;
    private int quantity;
    private String measuringUnit;

    @Override
    public String toString() {
        return title + " - " + quantity + " " + measuringUnit;
    }
}