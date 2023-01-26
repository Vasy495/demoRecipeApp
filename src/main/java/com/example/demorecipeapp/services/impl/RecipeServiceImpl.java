package com.example.demorecipeapp.services.impl;

import com.example.demorecipeapp.exception.ExceptionForRecipe;
import com.example.demorecipeapp.exception.ExceptionWithOperationFile;
import com.example.demorecipeapp.model.Ingredient;
import com.example.demorecipeapp.model.Recipe;
import com.example.demorecipeapp.services.RecipeService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RecipeServiceImpl implements RecipeService {
    private final Map<Long, Recipe> recipeMap = new HashMap<>();
    private Long counter = 0L;
    private final Path path;
    private final Path pathToTxtTemplate;
    private final ObjectMapper objectMapper;

    public RecipeServiceImpl(@Value("${application.file.recipes}") String path, @Value("${application.file.txtRecipe}") String pathToTxtTemplate) {
        try {
            this.path = Paths.get(path);
            this.pathToTxtTemplate = Paths.get(pathToTxtTemplate);//Paths.get(RecipeServiceImpl.class.getResource("recipesTemplate.txt").toURI());
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
            byte[] file = Files.readAllBytes(path);
            Map<Long, Recipe> mapFromFile = objectMapper.readValue(file, new TypeReference<>() {
            });
            recipeMap.putAll(mapFromFile);
        } catch (IOException e) {
            e.printStackTrace();
            throw new ExceptionWithOperationFile("Ошибка чтения из файла");
        }
    }

    private void writeDataToFile(Map<Long, Recipe> recipeMap) {

        try {
            byte[] bytes = objectMapper.writeValueAsBytes(recipeMap);
            Files.write(path, bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public Recipe add(Recipe recipe) throws ExceptionForRecipe {
        if (!recipeMap.containsValue(recipe)) {
            recipeMap.put(this.counter++, recipe);
            writeDataToFile(recipeMap);
            return recipe;
        } else {
            throw new ExceptionForRecipe("Такой рецепт уже существует!");
        }
    }

    @Override
    public Recipe get(long id) throws ExceptionForRecipe {
        if (recipeMap.containsKey(id)) {
            return recipeMap.get(id);
        } else {
            throw new ExceptionForRecipe("Такого рецепта нет!");
        }
    }

    @Override
    public Recipe update(long id, Recipe recipe) throws ExceptionForRecipe {
        if (recipeMap.containsKey(id)) {
            recipeMap.put((id), recipe);
            writeDataToFile(recipeMap);
            return recipe;
        }
        throw new ExceptionForRecipe("Такого рецепта нет!");
    }

    @Override
    public Recipe remove(long id) throws ExceptionForRecipe {
        if (recipeMap.containsKey(id)) {
            Recipe recipe = recipeMap.remove(id);
            writeDataToFile(recipeMap);
            return recipe;
        }
        throw new ExceptionForRecipe("Такого рецепта нет!");
    }

    @Override
    public List<Recipe> getAll() {
        return new ArrayList<>(this.recipeMap.values()); //Конвертируем вывод всех рецептов в лист, через конструктор другой коллекции
    }

    @Override
    public byte[] getAllInBytes() {
        try {
            return Files.readAllBytes(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void importRecipes(MultipartFile recipes) throws ExceptionForRecipe {
        try {
            Map<Long, Recipe> mapFromRequest = objectMapper.readValue(recipes.getBytes(),
                    new TypeReference<>() {
                    });
            writeDataToFile(mapFromRequest);
        } catch (IOException e) {
            e.printStackTrace();
            throw new ExceptionForRecipe("Ошибка загрузки рецептов из json");
        }
    }

    @Override
    public byte[] exportTxt() {
        try {
            String template = Files.readString(pathToTxtTemplate, StandardCharsets.UTF_8);
            StringBuilder stringBuilder = new StringBuilder();
            for (Recipe recipe : recipeMap.values()) {
                StringBuilder ingredients = new StringBuilder();
                StringBuilder steps = new StringBuilder();
                for (Ingredient ingredient : recipe.getIngredients()) {
                    ingredients.append(" - ").append(ingredient).append("\n");
                }
                int stepCounter = 1;
                for (String step : recipe.getSteps()) {
                    steps.append(stepCounter++).append(". ").append(step).append("\n");

                }
                String recipeData = template.replace("%title%", recipe.getTitle())
                        .replace("%cookingTime%", String.valueOf(recipe.getCookingTime()))
                        .replace("%ingredients%", ingredients.toString())
                        .replace("%steps%", steps.toString());
                stringBuilder.append(recipeData).append("\n\n\n");
            }
            return stringBuilder.toString().getBytes(StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
