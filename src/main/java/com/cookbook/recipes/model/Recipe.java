package com.cookbook.recipes.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Objects;

@Entity
public class Recipe {

    @Id
    @GeneratedValue
    private Integer id;
    private String title;

    @ManyToOne(fetch=FetchType.LAZY)
    @JsonIgnore
    private User user;

    protected Recipe(){}

    public Recipe(Integer id, String title){
        this.title = title;
        this.id = id;
    }
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Recipe recipe = (Recipe) o;
        return getId().equals(recipe.getId()) &&
                getTitle().equals(recipe.getTitle()) &&
                getUser().equals(recipe.getUser());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTitle(), getUser());
    }

    @Override
    public String toString() {
        return String.format("Recipe [id=%s, title=%s]", id, title);
    }
}

