package com.cookbook.recipes.controller;

import com.cookbook.recipes.business.RecipeService;
import com.cookbook.recipes.business.UserService;
import com.cookbook.recipes.exception.UserNotFoundException;
import com.cookbook.recipes.model.Recipe;
import com.cookbook.recipes.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerTest {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;
    @MockBean
    private RecipeService recipeService;

    @Test
    public void createRecipe() throws Exception{
        User user = new User(1,"Rick");
        Recipe recipe = new Recipe(1,"Oatmeal");
        recipe.setUser(new User(1,"Rick"));

        when(userService.findById(1)).thenReturn(user);
        when(recipeService.createRecipe(any(Recipe.class))).thenReturn(recipe);

        RequestBuilder request = MockMvcRequestBuilders
                .post("/users/1/recipes")
                .content((new ObjectMapper()).writeValueAsString(recipe))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id",is(recipe.getId())))
                .andExpect(jsonPath("$.title",is(recipe.getTitle())))
                .andReturn();
    }
    @Test
    public void createUser() throws Exception{
        User user = new User(1,"Rick");
        when(userService.createUser(any(User.class))).thenReturn(user);

        RequestBuilder request = MockMvcRequestBuilders
                .post("/users")
                .content((new ObjectMapper()).writeValueAsString(user))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(user.getId())))
                .andExpect(jsonPath("$.name", is(user.getName())))
                .andReturn();
    }

    @Test
    public void findAll_Ok() throws Exception{
        User user0 = new User(0,"Lucy");
        user0.setRecipes(List.of(new Recipe(0, "Oatmeal")));
        User user1 = new User(1,"John");
        user1.setRecipes(List.of(new Recipe(1,"Fried Rice")));
        User user2 = new User(2,"Steve");
        user2.setRecipes(List.of(new Recipe(2,"Greek Salad")));

        when(userService.findAll(0,10,"id")).thenReturn(List.of(user0,user1,user2));

        RequestBuilder request = MockMvcRequestBuilders
                .get("/users")
                .accept(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(request)
                .andExpect(status().isOk())//$..users[1]
                .andExpect(jsonPath("$._embedded.users[0].id",is(user0.getId())))
                .andExpect(jsonPath("$._embedded.users[0].name",is(user0.getName())))
                .andExpect(jsonPath("$._embedded.users[0].recipes.length()",is(user0.getRecipes().size())))
                .andExpect(jsonPath("$._embedded.users[0].recipes[0].title",is(user0.getRecipes().get(0).getTitle())))
                .andExpect(jsonPath("$._embedded.users[1].id",is(user1.getId())))
                .andExpect(jsonPath("$._embedded.users[1].name",is(user1.getName())))
                .andExpect(jsonPath("$._embedded.users[1].recipes.length()",is(user1.getRecipes().size())))
                .andExpect(jsonPath("$._embedded.users[1].recipes[0].title",is(user1.getRecipes().get(0).getTitle())))
                .andExpect(jsonPath("$._embedded.users[2].id",is(user2.getId())))
                .andExpect(jsonPath("$._embedded.users[2].name",is(user2.getName())))
                .andExpect(jsonPath("$._embedded.users[2].recipes.length()",is(user2.getRecipes().size())))
                .andExpect(jsonPath("$._embedded.users[2].recipes[0].title",is(user2.getRecipes().get(0).getTitle())))
                .andReturn();
    }

    @Test
    public void retrieveUser_Ok() throws Exception {
        User user = new User(1,"Lucy");
        user.setRecipes(List.of(new Recipe(1, "Oatmeal")));

        when(userService.findById(1)).thenReturn(user);

        RequestBuilder request = MockMvcRequestBuilders
                .get("/users/1")
                .accept(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(user)))
                .andExpect(jsonPath("$.id",is(user.getId())))
                .andExpect(jsonPath("$.name",is(user.getName())))
                .andExpect(jsonPath("$.recipes[0].title",is(user.getRecipes().get(0).getTitle())))
                .andReturn();
    }

    @Test
    public void retrieveUser_NotFound() throws Exception {
        when(userService.findById(1)).thenThrow(new UserNotFoundException(String.format("User %d not found",1)));

        RequestBuilder request = MockMvcRequestBuilders
                .get("/users/1")
                .accept(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(request)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message",is("User 1 not found")))
                .andExpect(jsonPath("$.details",is("uri=/users/1")))
                .andReturn();
    }
}
