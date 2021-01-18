package com.cookbook.recipes.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.cookbook.recipes.business.RecipeService;
import com.cookbook.recipes.business.UserService;
import com.cookbook.recipes.model.Recipe;
import com.cookbook.recipes.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//The components are identified by the @RestController annotation,
//which combines the @Controller and @ResponseBody annotations.
//@Controller
//@RequestBody
@RestController
public class UserController {
    @Autowired
    UserService userService;
    @Autowired(required = false)
    RecipeService recipeService;

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/users")
    public CollectionModel<EntityModel<User>> findAll(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy) {
        List<EntityModel<User>> users = userService
                .findAll(pageNo, pageSize, sortBy)
                .stream()
                .map( user -> this.toModel(user,pageNo,pageSize,sortBy))
                .collect(Collectors.toList());

        return CollectionModel
                .of(users, linkTo(methodOn(UserController.class)
                        .findAll(pageNo, pageSize, sortBy))
                        .withSelfRel());
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/users/{id}")
    public EntityModel<User> findById(@PathVariable int id) {
        User user = userService.findById(id);

        return toModel(user);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable int id) {
        userService.deleteById(id);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/users")
    public ResponseEntity<?> createUser(@Valid @RequestBody User user) {
        EntityModel<User> entityModel = toModel(userService.createUser(user));

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()) //
                .body(entityModel);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/users/{id}/recipes")
    public CollectionModel<EntityModel<Recipe>> findAll(@PathVariable int id,
                                                        @RequestParam(defaultValue = "0") Integer pageNo,
                                                        @RequestParam(defaultValue = "10") Integer pageSize,
                                                        @RequestParam(defaultValue = "id") String sortBy) {
        List<EntityModel<Recipe>> recipes = userService
                .findById(id)
                .getRecipes()
                .stream()
                .map(recipe -> this.toModel(recipe, pageNo, pageSize, sortBy))
                .collect(Collectors.toList());

        return CollectionModel
                .of(recipes, linkTo(methodOn(UserController.class)
                        .findAll(pageNo, pageSize, sortBy))
                        .withSelfRel());
    }


    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/users/{id}/recipes")
    public ResponseEntity<?> createRecipe(@PathVariable int id, @RequestBody Recipe recipe) {
        User user = userService.findById(id);
        recipe.setUser(user);

        EntityModel<Recipe> entityModel = toModel(recipeService.createRecipe(recipe));

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()) //
                .body(entityModel);
    }

    public EntityModel<User> toModel(User user) {

        return EntityModel.of(user, //
                linkTo(methodOn(UserController.class)
                        .findById(user.getId()))
                        .withSelfRel());
    }

    public EntityModel<Recipe> toModel(Recipe recipe) {
        return EntityModel.of(recipe, //
                linkTo(methodOn(UserController.class)
                        .findById(recipe.getId()))
                        .withSelfRel());
    }

    public EntityModel<User> toModel(User user,
                                     @RequestParam(defaultValue = "0") Integer pageNo,
                                     @RequestParam(defaultValue = "10") Integer pageSize,
                                     @RequestParam(defaultValue = "id") String sortBy) {

        return EntityModel.of(user, //
                linkTo(methodOn(UserController.class)
                        .findById(user.getId()))
                        .withSelfRel(),
                linkTo(methodOn(UserController.class)
                        .findAll(pageNo, pageSize, sortBy))
                        .withRel("allusers"));
    }

    public EntityModel<Recipe> toModel(Recipe recipe,
                                       @RequestParam(defaultValue = "0") Integer pageNo,
                                       @RequestParam(defaultValue = "10") Integer pageSize,
                                       @RequestParam(defaultValue = "id") String sortBy) {
        return EntityModel.of(recipe, //
                linkTo(methodOn(UserController.class)
                        .findById(recipe.getId()))
                        .withSelfRel(),
                linkTo(methodOn(UserController.class)
                        .findAll(pageNo, pageSize, sortBy))
                        .withRel("allusers"));
    }
}

