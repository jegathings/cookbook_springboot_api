package com.cookbook.recipes.business;

import com.cookbook.recipes.data.UserRepository;
import com.cookbook.recipes.exception.UserNotFoundException;
import com.cookbook.recipes.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    RecipeService recipeService;
    /**
     * findById is a methond on the CrudRepository.
     * orElseThrow uses the Supplier functional interface to throw an exception.
     * if the user is not found.
     * @param userId
     * @return
     * @throws UserNotFoundException
     */
    public User findById(Integer userId) throws UserNotFoundException{
         return userRepository
                .findById(userId)
                .orElseThrow(() -> new UserNotFoundException(String.format("User %d not found.",userId)));
    }
    public List<User> findAll(Integer pageNo, Integer pageSize, String sortBy){
        List<User> users = new ArrayList<>();
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));

        userRepository
                .findAll(paging)
                .forEach(users::add);
        return users;
    }
    public User createUser(User user){
        return userRepository.save(user);
    }
    public void deleteById(Integer id){
        userRepository.deleteById(id);
    }
}
