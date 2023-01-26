package com.example.demorecipeapp.services;

import com.example.demorecipeapp.exception.ExceptionForRecipe;
import com.example.demorecipeapp.model.Recipe;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface RecipeService {
    Recipe add(Recipe recipe) throws ExceptionForRecipe;

    Recipe get(long id) throws ExceptionForRecipe;

    Recipe update(long id, Recipe recipe) throws ExceptionForRecipe;

    Recipe remove(long id) throws ExceptionForRecipe;

    List<Recipe> getAll();

    byte[] getAllInBytes();

    void importRecipes(MultipartFile recipes) throws ExceptionForRecipe;

    byte[] exportTxt();
}
