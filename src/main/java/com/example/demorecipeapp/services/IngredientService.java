package com.example.demorecipeapp.services;

import com.example.demorecipeapp.model.Ingredient;

public interface IngredientService {
    Ingredient add(Ingredient ingredient);

    Ingredient get(long id);

    Ingredient update(long id, Ingredient ingredient);

    Ingredient remove(long id);
}
