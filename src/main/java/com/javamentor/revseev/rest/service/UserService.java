package com.javamentor.revseev.rest.service;

import com.javamentor.revseev.rest.model.User;

import java.util.List;

public interface UserService {

     void saveUser(User user);

     User findById(long id);

     User findByUsername(String username);

     List<User> getAllUsers();

     void deleteUser(long id);
}
