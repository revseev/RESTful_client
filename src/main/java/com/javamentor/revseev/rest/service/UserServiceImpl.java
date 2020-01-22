package com.javamentor.revseev.rest.service;

import com.javamentor.revseev.rest.model.User;
import com.javamentor.revseev.rest.dao.UserRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;


@Service
@Transactional
public class UserServiceImpl implements UserService {


    @Autowired
    private UserRepository userRepository;

//CRUD operations:

    /**
     * Do not a saparate updateUser() method as long as this is a transactional service
     * */
    public void saveUser(User user){
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public User findById(long id) {
        return userRepository.findById(id).get();
    }

    @Override
    @Transactional(readOnly = true)
    public User findByUsername(String username) {
        User user = userRepository.findByUsername(username);
        //Lazy-load support
        if(user!=null) {
            Hibernate.initialize(user.getRoles());
        }
        return user;
    }

    @Transactional(readOnly = true)
    public List<User> getAllUsers(){
        List<User> users = userRepository.findAll();
        //Lazy-load support
        for(User user : users){
            Hibernate.initialize(user.getRoles());
        }
        return users;
    }

    public void deleteUser(long id){
        userRepository.deleteById(id);
    }
}
