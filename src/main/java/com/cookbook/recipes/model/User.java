package com.cookbook.recipes.model;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

@Entity
public class User {
    @Id
    @GeneratedValue
    private Integer id;
    @Size(min=2, message="Name should have at least 2 characters")
    private String name;
    @OneToMany(mappedBy="user")
    private List<Recipe> recipes;

    protected User() {
    }

    public User(Integer id, String name) {
        super();
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Recipe> getRecipes() {
        return recipes;
    }

    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return getId().equals(user.getId()) &&
                getName().equals(user.getName()) &&
                Objects.equals(getRecipes(), user.getRecipes());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getRecipes());
    }

    @Override
    public String toString() {
        return String.format("User [id=%s, name=%s]", id, name);
    }

}
