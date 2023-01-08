package com.example.demorecipeapp.services;

import com.example.demorecipeapp.model.Recipe;

import java.util.List;

public interface RecipeService {
    Recipe add(Recipe recipe);

    Recipe get(long id);

    Recipe update(long id, Recipe recipe);

    Recipe remove(long id);

    List<Recipe> getAll();
}
