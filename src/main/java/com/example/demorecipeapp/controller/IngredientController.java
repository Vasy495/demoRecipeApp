package com.example.demorecipeapp.controller;

import com.example.demorecipeapp.model.Ingredient;
import com.example.demorecipeapp.model.Recipe;
import com.example.demorecipeapp.services.IngredientService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ingredient")
public class IngredientController {
    private final IngredientService ingredientService;

    public IngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }


    @PostMapping
    public Ingredient addIngredient(@RequestBody Ingredient ingredient) {
        return ingredientService.add(ingredient);
    }

    @GetMapping("/{id}")
    public Ingredient getIngredient(@PathVariable("id") long id) {
        return ingredientService.get(id);
    }

    @PutMapping("/{id}")
    public Ingredient putIngredient(@PathVariable("id") long id, @RequestBody Ingredient ingredient) {
        return ingredientService.update(id, ingredient);
    }

    @DeleteMapping("/{id}")
    public Ingredient deleteIngredient(@PathVariable("id") long id) {
        return ingredientService.remove(id);
    }
}

