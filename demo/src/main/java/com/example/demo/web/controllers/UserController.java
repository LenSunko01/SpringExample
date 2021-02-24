package com.example.demo.web.controllers;

import java.util.List;
import java.util.Random;

import com.example.demo.web.exceptions.UserNotFoundException;
import com.example.demo.dao.UserRepository;
import com.example.demo.models.dto.User;
import com.example.demo.service.user.UserService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private final UserRepository repository;
    private final UserService userService;

    UserController(UserRepository repository, UserService userService) {
        this.repository = repository;
        this.userService = userService;
    }


    // Aggregate root
    // tag::get-aggregate-root[]
    @GetMapping("/users")


    List<User> all() {
        userService.getAll();
        return repository.findAll();
    }
    // end::get-aggregate-root[]

    @GetMapping("/random")
    User randomUser() {
        List<User> list = repository.findAll();
        Random rand = new Random();
        return list.get(rand.nextInt(list.size()));
    }

    @PostMapping("/users")
    User newUser(@RequestBody User newUser) {
        return repository.save(newUser);
    }

    // Single item

    @GetMapping("/users/{id}")
    User one(@PathVariable Long id) {

        return repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    @PutMapping("/users/{id}")
    User replaceUser(@RequestBody User newUser, @PathVariable Long id) {

        return repository.findById(id)
                .map(user -> {
                    user.setName(newUser.getName());
                    user.setPoints(newUser.getPoints());
                    return repository.save(user);
                })
                .orElseGet(() -> {
                    newUser.setId(id);
                    return repository.save(newUser);
                });
    }

    @DeleteMapping("/users/{id}")
    void deleteUser(@PathVariable Long id) {
        repository.deleteById(id);
    }
}