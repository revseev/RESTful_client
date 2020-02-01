package com.javamentor.revseev.rest.security;

import com.javamentor.revseev.rest.service.UserService;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.BaseOAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import java.util.*;

public class CustomUserInfoTokenService extends UserInfoTokenServices {

    private UserService userService;
    private PasswordEncoder passwordEncoder;

    private final String userInfoEndpointUrl;
    private final String clientId;
    private OAuth2RestOperations restTemplate;

    public CustomUserInfoTokenService(String userInfoEndpointUrl, String clientId) {
        super(userInfoEndpointUrl, clientId);
        this.userInfoEndpointUrl = userInfoEndpointUrl;
        this.clientId = clientId;
    }
}

