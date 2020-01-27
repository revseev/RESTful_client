package com.javamentor.revseev.rest.service;


import com.javamentor.revseev.rest.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class RoleServiceRest implements RoleService {

    private final String serverUrl;
    private RestTemplate restTemplate;

    @Autowired
    public RoleServiceRest(RestTemplate restTemplate, @Value("${serverUrl}") String serverUrl) {
        this.restTemplate = restTemplate;
        this.serverUrl = serverUrl;
    }

    @Override
    public List<Role> getAllRoles() {
        return restTemplate.exchange(
                serverUrl + "/roles",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Role>>() {
                }
        ).getBody();
    }
}
