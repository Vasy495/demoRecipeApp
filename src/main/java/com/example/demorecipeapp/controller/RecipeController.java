package com.example.demorecipeapp.controller;

import com.example.demorecipeapp.model.Recipe;
import com.example.demorecipeapp.services.RecipeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
