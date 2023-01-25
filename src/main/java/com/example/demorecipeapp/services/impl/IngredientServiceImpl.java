package com.example.demorecipeapp.services.impl;

import com.example.demorecipeapp.exception.ExceptionForIngredient;
import com.example.demorecipeapp.exception.ExceptionWithOperationFile;
import com.example.demorecipeapp.model.Ingredient;
import com.example.demorecipeapp.model.Recipe;
import com.example.demorecipeapp.services.IngredientService;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@Service
public class IngredientServiceImpl implements IngredientService {
    private final Map<Long, Ingredient> ingredientMap = new HashMap<>();
    private long counter = 0;
    private final Path path;
    private final ObjectMapper objectMapper;

    public IngredientServiceImpl(@Value("${application.file.ingredients}") String path) {
        try {
            this.path = Paths.get(path);
            this.objectMapper = new ObjectMapper();
        } catch (InvalidPathException e) {
            e.printStackTrace();
            throw e;
        }
    }

    @PostConstruct
    public void init() throws ExceptionWithOperationFile {
        readDataFromFile();
    }

    private void readDataFromFile() throws ExceptionWithOperationFile {
        try {
            byte[] file = Files.readAllBytes(path); //Преобразуем файл в массив байт
            Map<Long, Ingredient> mapFromFile = objectMapper.readValue(file, new TypeReference<>() {
            }); //Собираем мапу через jackson (objectMapper), используем TypeReference для преобразования объект в определенный тип - нашу мапу
            ingredientMap.putAll(mapFromFile);
        } catch (IOException e) {
            e.printStackTrace();
            throw new ExceptionWithOperationFile("Ошибка чтения из файла");
        }
    }

    private void writeDataToFile(Map<Long, Ingredient> ingredientMap) {
        try {
            byte[] bytes = objectMapper.writeValueAsBytes(ingredientMap);
            Files.write(path, bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public Ingredient add(Ingredient ingredient) throws ExceptionForIngredient, ExceptionWithOperationFile {
        if (!ingredientMap.containsValue(ingredient)) {
        Ingredient newIngredient = ingredientMap.put(counter++, ingredient);
        writeDataToFile(ingredientMap);
        return newIngredient;
        } else {
            throw new ExceptionForIngredient("Такой ингредиент уже существует");
        }
    }

    @Override
    public Ingredient get(long id) throws ExceptionForIngredient {
        if (ingredientMap.containsKey(id)) {
            return ingredientMap.get(id);
        } else {
            throw new ExceptionForIngredient("Такого ингредиента нет!");
        }
    }

    @Override
    public Ingredient update(long id, Ingredient ingredient) throws ExceptionForIngredient, ExceptionWithOperationFile {
        if (ingredientMap.containsKey(id)) {
            Ingredient newIngredient = ingredientMap.put(id, ingredient);
            writeDataToFile(ingredientMap);
            return newIngredient;
        }
        throw new ExceptionForIngredient("Такого ингредиента нет!");
    }

    @Override
    public Ingredient remove(long id) throws ExceptionWithOperationFile, ExceptionForIngredient {
        if (ingredientMap.containsKey(id)) {
            Ingredient ingredient = ingredientMap.remove(id);
            writeDataToFile(ingredientMap);
            return ingredient;
        } else {
            throw new ExceptionForIngredient("Такого ингрединета нет!");
        }
    }

    @Override
    public InputStreamResource getAllInBytes() {
        try {
            return new InputStreamResource(new FileInputStream(path.toFile()));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void importIngredients(MultipartFile ingredients) throws ExceptionWithOperationFile {
        try {
            Map<Long, Ingredient> mapFromRequest = objectMapper.readValue(ingredients.getBytes(),
                    new TypeReference<>() {
                    });
            writeDataToFile(mapFromRequest);
        } catch (IOException e) {
            e.printStackTrace();
            throw new ExceptionWithOperationFile("Ошибка загрузки ингредиентов из json");
        }
    }
}
