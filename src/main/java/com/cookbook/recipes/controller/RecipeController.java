package com.cookbook.recipes.controller;

import com.cookbook.recipes.business.RecipeService;
import com.cookbook.recipes.model.Recipe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class RecipeController {
    @Autowired
    RecipeService recipeService;

    @CrossOrigin("http://localhost:3000")
    @GetMapping("/recipes")
    public CollectionModel<EntityModel<Recipe>> findAll() {
        List<EntityModel<Recipe>> recipes = recipeService
                .findAll()
                .stream()
                .map(this::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(recipes,linkTo(methodOn(RecipeController.class).findAll()).withSelfRel());
    }
    @CrossOrigin("http://localhost:3000")
    @GetMapping("/recipe/{id}")
    public EntityModel<Recipe> findById(@RequestParam Integer id){
        Recipe recipe = recipeService.findById(id);

        return toModel(recipe);
    }
    public EntityModel<Recipe> toModel(Recipe recipe) {
        return EntityModel.of(recipe, //
                linkTo(methodOn(RecipeController.class).findById(recipe.getId())).withSelfRel(),
                linkTo(methodOn(RecipeController.class).findAll()).withRel("allrecipes"));
    }
}
