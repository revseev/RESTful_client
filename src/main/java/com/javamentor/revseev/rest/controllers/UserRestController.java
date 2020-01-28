package com.javamentor.revseev.rest.controllers;

import com.javamentor.revseev.rest.model.User;
import com.javamentor.revseev.rest.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class UserRestController {

    @Autowired
    private UserService userService;

    @GetMapping("users")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("users/{id}")
    public User getUser(@PathVariable String id) {
        return userService.findById(Long.parseLong(id));
    }

    @PostMapping(value = "users", produces = MediaType.APPLICATION_JSON_VALUE)
    public User addUser(@RequestBody User user){
        user.setId(0); // force saving a new entity
        userService.saveUser(user);
        return user;
    }

    @PutMapping(value = "users", produces = MediaType.APPLICATION_JSON_VALUE)
    public User updateUser(@RequestBody User user){
        userService.updateUser(user);
        return user;
    }

    @DeleteMapping("users/{id}")
    public void deleteUser(@PathVariable String id) {
        userService.deleteUser(Long.parseLong(id));
    }


}
