package com.cookbook.recipes.business;

import com.cookbook.recipes.data.RecipeRepository;
import com.cookbook.recipes.exception.RecipeNotFoundException;
import com.cookbook.recipes.model.Recipe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class RecipeService {
    @Autowired
    RecipeRepository recipeRepository;

    public Recipe createRecipe(Recipe recipe){
        return recipeRepository.save(recipe);
    }

    public List<Recipe> findAll(){
        List<Recipe> recipes = new ArrayList<>();
        recipeRepository
            .findAll()
            .forEach(recipes::add);
        return recipes;
    }
    public Recipe findById(Integer id) throws RecipeNotFoundException{
        return recipeRepository.findById(id)
                .orElseThrow(() -> new RecipeNotFoundException(String.format("Recipe %d not found.", id)));
    }
}
