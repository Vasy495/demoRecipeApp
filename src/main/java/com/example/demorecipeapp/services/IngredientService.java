package com.example.demorecipeapp.services;

import com.example.demorecipeapp.exception.ExceptionForIngredient;
import com.example.demorecipeapp.exception.ExceptionWithOperationFile;
import com.example.demorecipeapp.model.Ingredient;
import org.springframework.core.io.InputStreamResource;
import org.springframework.web.multipart.MultipartFile;

public interface IngredientService {
    Ingredient add(Ingredient ingredient) throws ExceptionForIngredient, ExceptionWithOperationFile;

    Ingredient get(long id) throws ExceptionForIngredient;

    Ingredient update(long id, Ingredient ingredient) throws ExceptionForIngredient, ExceptionWithOperationFile;

    Ingredient remove(long id) throws ExceptionWithOperationFile, ExceptionForIngredient;

    InputStreamResource getAllInBytes();

    void importIngredients(MultipartFile ingredients) throws ExceptionWithOperationFile;
}
