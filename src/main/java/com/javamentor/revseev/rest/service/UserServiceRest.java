package com.javamentor.revseev.rest.service;

import com.javamentor.revseev.rest.model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;


import java.util.List;


@Service
@Transactional
public class UserServiceRest implements UserService {

    private final String serverUrl;
    private RestTemplate restTemplate;

    @Autowired
    public UserServiceRest(RestTemplate restTemplate, @Value("${serverUrl}") String serverUrl) {
        this.restTemplate = restTemplate;
        this.serverUrl = serverUrl;
    }

    @Override
    public void saveUser(User user){
        restTemplate.postForObject(
                serverUrl + "/users",
                new HttpEntity<>(user),
                User.class);
    }

    @Override
    public User findById(long id) {
        return restTemplate.exchange(
                serverUrl + "/users/" + String.valueOf(id),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<User>() {
                }
        ).getBody();
    }

    @Override
    public User findByUsername(String username) {
        return getAllUsers()
                .stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst()
                .orElseThrow(() -> new UsernameNotFoundException("No user found with username " + username));
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
        restTemplate.delete(serverUrl + "/users/" + String.valueOf(id));
    }
}
