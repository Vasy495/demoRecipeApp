package com.example.demorecipeapp.controller;

import com.example.demorecipeapp.model.Recipe;
import com.example.demorecipeapp.services.RecipeService;
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
    public ResponseEntity<Recipe> addRecipe(@RequestBody Recipe recipe) {
        if (StringUtils.isBlank(recipe.getTitle())) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(recipeService.add(recipe));
    }

    @GetMapping("/{id}")
    public Recipe getRecipe(@PathVariable("id") long id) {
        return recipeService.get(id);
    }

    @GetMapping
    public List<Recipe> getAllRecipes() {
        return this.recipeService.getAll();
    }

    @PutMapping("/{id}")
    public Recipe putRecipe(@PathVariable("id") long id, @RequestBody Recipe recipe) {
        return recipeService.update(id, recipe);
    }

    @DeleteMapping("/{id}")
    public Recipe deleteRecipe(@PathVariable("id") long id) {
        return recipeService.remove(id);
    }

    @GetMapping("/download")
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
    public void importRecipes(MultipartFile recipes) {
        recipeService.importRecipes(recipes);
    }
}
