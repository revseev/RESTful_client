package com.javamentor.revseev.rest.service;

import com.javamentor.revseev.rest.model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;


import java.util.List;


@Service
@Transactional
public class UserServiceRest implements UserService {

    private final String serverUrl;
    private RestTemplate restTemplate;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceRest(@Value("${serverUrl}") String serverUrl,
                           RestTemplate restTemplate,
                           @Qualifier("noOpPasswordEncoder") PasswordEncoder passwordEncoder) {
        this.serverUrl = serverUrl;
        this.restTemplate = restTemplate;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void saveUser(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        restTemplate.postForObject(
                serverUrl + "/users",
                new HttpEntity<>(user),
                User.class);
    }

    @Override
    public void updateUser(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        restTemplate.put(
                serverUrl + "/users",
                new HttpEntity<>(user),
                User.class);
    }

    @Override
    public User findById(long id) {
        String userId = String.valueOf(id);
        return restTemplate.exchange(
                serverUrl + "/users/" + userId,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<User>() {
                }
        ).getBody();
    }

    @Override
    public User findByUsername(String username) {
        return restTemplate.exchange(
                serverUrl + "/users/?username=" + username,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<User>() {
                }
        ).getBody();
    }

    @Override
    public List<User> getAllUsers(){
        return restTemplate.exchange(
                serverUrl + "/users",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<User>>() {
                }
        ).getBody();
    }

    @Override
    public void deleteUser(long id){
        String userId = String.valueOf(id);
        restTemplate.delete(serverUrl + "/users/" + userId);
    }
}
