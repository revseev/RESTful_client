package com.javamentor.revseev.rest.service;

import com.github.scribejava.apis.GoogleApi20;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.oauth.OAuth20Service;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Service
public class GoogleService {

    private OAuth20Service service;
    private static final String API_KEY = "210621414596-rvolaltbic5baj5jmgq0lngmlfkjkiob.apps.googleusercontent.com";
    private static final String API_SECRET = "xZ6NwPIIZhcNraUF-kZGOh3b";
    private static final String SCOPE = "profile email";
    private static final String CALLBACK = "http://localhost:8080/auth/google";


    @PostConstruct
    private void init(){
        this.service  = new ServiceBuilder(API_KEY)
                .apiSecret(API_SECRET)
                .defaultScope(SCOPE)
                .callback(CALLBACK)
                .debug() // for testing
                .build(GoogleApi20.instance());
    }

    public OAuth20Service getService() {
        return service;
    }

    public String getMyAuthUrl(String state) {
        final Map<String, String> additionalParams = new HashMap<>();
        //pass access_type=offline to get refresh token
        additionalParams.put("access_type", "offline");
        //force to reget refresh token (if user are asked not the first time)
        additionalParams.put("prompt", "consent");

        return  service.createAuthorizationUrlBuilder()
                .state(state)
                .additionalParams(additionalParams)
                .build();
    }
}

