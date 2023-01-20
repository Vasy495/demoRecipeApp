package com.example.demorecipeapp.controller;

import com.example.demorecipeapp.model.Ingredient;
import com.example.demorecipeapp.model.Recipe;
import com.example.demorecipeapp.services.IngredientService;
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

    @GetMapping("/download")
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
    public void importRecipes(MultipartFile ingredients) {
        ingredientService.importIngredients(ingredients);
    }
}

