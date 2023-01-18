package com.example.demorecipeapp.services.impl;

import com.example.demorecipeapp.model.Recipe;
import com.example.demorecipeapp.services.RecipeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RecipeServiceImpl implements RecipeService {
    private final Map<Long, Recipe> recipeMap = new HashMap<>();
    private Long counter = 0L;
    private final Path path;
    private final ObjectMapper objectMapper;

    public RecipeServiceImpl(@Value("${application.file.recipes}") String path) {
        try {
            this.path = Path.of(path);
            this.objectMapper = new ObjectMapper();
        } catch (InvalidPathException e) {
            e.printStackTrace();
            throw e;
        }
    }

    @PostConstruct
    public void init() {
        readDataFromFile();
    }

    private void readDataFromFile() {
        try {
            byte[] file = Files.readAllBytes(path);
            Map<Long, Recipe> mapFromFile = objectMapper.readValue(file, new TypeReference<>() {
            });
            recipeMap.putAll(mapFromFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeDataToFile() {
        try{
            byte[] bytes = objectMapper.writeValueAsBytes(recipeMap);
            Files.write(path, bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public Recipe add(Recipe recipe) {
        recipeMap.put(this.counter++, recipe);
        writeDataToFile();
        return recipe;
    }

    @Override
    public Recipe get(long id) {
        return recipeMap.get(id);
    }

    @Override
    public Recipe update(long id, Recipe recipe) {
        if (recipeMap.containsKey(id)) {
            recipeMap.put((id), recipe);
            writeDataToFile();
            return recipe;
        }
        return null;
    }

    @Override
    public Recipe remove(long id) {
        Recipe recipe = recipeMap.remove(id);
        writeDataToFile();
        return recipe;
    }

    @Override
    public List<Recipe> getAll() {
        return new ArrayList<>(this.recipeMap.values()); //Конвертируем вывод всех рецептов в лист, через конструктор другой коллекции
    }
}
