package com.example.demorecipeapp.controller;

import com.example.demorecipeapp.exception.ExceptionForRecipe;
import com.example.demorecipeapp.model.Recipe;
import com.example.demorecipeapp.services.RecipeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequestMapping("/recipe")
public class RecipeController {
    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @PostMapping
    @Operation(
            summary = "Добавление рецепта",
            description = "Добавление нового рецепта в приложение"

    )
    public ResponseEntity<Recipe> addRecipe(@RequestBody Recipe recipe) throws ExceptionForRecipe {
        if (StringUtils.isBlank(recipe.getTitle())) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(recipeService.add(recipe));
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Отображение рецепта по id",
            description = "Выгрузит определенный рецепт"

    )
    public Recipe getRecipe(@PathVariable("id") long id) throws ExceptionForRecipe {
        return recipeService.get(id);
    }

    @GetMapping
    @Operation(
            summary = "Отображение всех рецептов",
            description = "Выгрузит все имеющиеся рецепты"

    )
    public List<Recipe> getAllRecipes() {
        return this.recipeService.getAll();
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Обновление рецепта по id",
            description = "Обновит определенный рецепт"

    )
    public Recipe putRecipe(@PathVariable("id") long id, @RequestBody Recipe recipe) throws ExceptionForRecipe {
        return recipeService.update(id, recipe);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Удаление рецпта по id",
            description = "Удалит определенный рецепт"
    )
    public Recipe deleteRecipe(@PathVariable("id") long id) throws ExceptionForRecipe {
        return recipeService.remove(id);
    }

    @GetMapping("/download")
    @Operation(
            summary = "Выгрузка всех рецептов в формате json",
            description = "Выгрузит все имеющиеся рецепты в определенном формате"

    )
    public ResponseEntity<byte[]> downloadRecipes() {
        byte[] bytes = recipeService.getAllInBytes();
        if (bytes == null) {
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .contentLength(bytes.length)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"recipes.json\"")
                .body(bytes);
    }

    @PostMapping(value = "/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "Загрузка рецптов в формате json",
            description = "Загрузит рецепты в приложение"

    )
    public void importRecipes(MultipartFile recipes) throws ExceptionForRecipe {
        recipeService.importRecipes(recipes);
    }

    @GetMapping("/export")
    @Operation(
            summary = "Выгрузка всех рецептов в формате txt",
            description = "Выгрузит все имеющиеся рецепты в определенном формате"

    )
    public ResponseEntity<byte[]> exportTxt() {

        byte[] bytes = recipeService.exportTxt();
        if (bytes == null) {
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_PLAIN)
                .contentLength(bytes.length)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"info.txt\"")
                .body(bytes);
    }
}
