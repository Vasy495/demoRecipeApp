package com.example.demorecipeapp.model;

import lombok.Data;

import java.util.List;

@Data
public class Recipe {
    private String title;
    private int cookingTime;
    private List<Ingredient> ingredients;
    private List<String> steps;

}
