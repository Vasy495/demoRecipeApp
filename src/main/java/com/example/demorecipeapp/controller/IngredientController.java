package com.example.demorecipeapp.controller;

import com.example.demorecipeapp.exception.ExceptionForIngredient;
import com.example.demorecipeapp.exception.ExceptionWithOperationFile;
import com.example.demorecipeapp.model.Ingredient;
import com.example.demorecipeapp.model.Recipe;
import com.example.demorecipeapp.services.IngredientService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/ingredient")
public class IngredientController {
    private final IngredientService ingredientService;

    public IngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }


    @PostMapping
    @Operation(
            summary = "Добавление нового ингредиента",
            description = "Добавит новый ингредиент в приложение"

    )
    public Ingredient addIngredient(@RequestBody Ingredient ingredient) throws ExceptionWithOperationFile, ExceptionForIngredient {
        return ingredientService.add(ingredient);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Отображение ингредиента по id",
            description = "Выгрузит определенный ингредиент"
    )
    public Ingredient getIngredient(@PathVariable("id") long id) throws ExceptionForIngredient {
        return ingredientService.get(id);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Обновление ингрединета по id",
            description = "Обновит определенный ингредиент"
    )
    public Ingredient putIngredient(@PathVariable("id") long id, @RequestBody Ingredient ingredient) throws ExceptionForIngredient, ExceptionWithOperationFile {
        return ingredientService.update(id, ingredient);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Удаление ингрединета по id",
            description = "Удалит определенный ингредиент"
    )
    public Ingredient deleteIngredient(@PathVariable("id") long id) throws ExceptionWithOperationFile, ExceptionForIngredient {
        return ingredientService.remove(id);
    }

    @GetMapping("/download")
    @Operation(
            summary = "Выгрузка всех игредиентов в формате json",
            description = "Выгрузит все имеющиеся ингредиенты в определенном формате"
    )
    public ResponseEntity<InputStreamResource> downloadRecipes() throws IOException {
        InputStreamResource inputStreamResource = ingredientService.getAllInBytes();
        if (inputStreamResource == null) {
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"ingredient.json\"")
                .body(inputStreamResource);
    }

    @PostMapping(value = "/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "Загрузка всех игредиентов в формате json",
            description = "Загрузит все ингредиенты в приложение"
    )
    public void importRecipes(MultipartFile ingredients) throws ExceptionWithOperationFile {
        ingredientService.importIngredients(ingredients);
    }
}

