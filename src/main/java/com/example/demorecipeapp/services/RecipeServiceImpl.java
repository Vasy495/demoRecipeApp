package com.example.demorecipeapp.services;

import com.example.demorecipeapp.model.Recipe;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RecipeServiceImpl implements RecipeService{
//    private final IngredientService ingredientService;
    private final Map<Long, Recipe> recipeMap = new HashMap<>();
    private Long counter = 0L;

    @Override
    public Recipe add(Recipe recipe) {
        recipeMap.put(this.counter++, recipe);
/*        recipe.getIngredients().forEach(i ->{
            ingredientService.add(i);
        });*/
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
            return recipe;
        }
        return null;
    }

    @Override
    public Recipe remove(long id) {
        return recipeMap.remove(id);
    }

    @Override
    public List<Recipe> getAll() {
        return new ArrayList<>(this.recipeMap.values()); //Конвертируем вывод всех рецептов в лист, через конструктор другой коллекции
    }
}
