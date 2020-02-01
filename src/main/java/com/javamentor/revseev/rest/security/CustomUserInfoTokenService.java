package com.javamentor.revseev.rest.security;

import com.javamentor.revseev.rest.model.Role;
import com.javamentor.revseev.rest.model.User;
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
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public OAuth2Authentication loadAuthentication(String accessToken) throws AuthenticationException, InvalidTokenException {
        // достаем map из токена
        Map<String, Object> map = getMap(this.userInfoEndpointUrl, accessToken);
        // логика поиска существующего юзера в БД и запись туда юзера на основе логина из гугла
        String principalName = map.get("email").toString();
        User user = userService.findByUsername(principalName);

        if (user == null) {
            String principalPassword = passwordEncoder.encode(map.get("at_hash").toString());
            long principalMoney = Long.parseLong(map.get("sub").toString());

            user = new User(principalName, principalPassword, principalMoney);
            user.setRoles(Collections.singleton(new Role("USER")));

            userService.saveUser(user);
        }
        // формирование
        Authentication authentication = new UsernamePasswordAuthenticationToken(user, "", user.getAuthorities());
        OAuth2Request request = new OAuth2Request(null, accessToken, null, true, null,
                null, null, null, null);
        return new OAuth2Authentication(request, authentication);
    }

    // default impl to bypass private access of getMap()
    private Map<String, Object> getMap(String path, String accessToken) {
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Getting user info from: " + path);
        }
        try {
            OAuth2RestOperations restTemplate = this.restTemplate;
            if (restTemplate == null) {
                BaseOAuth2ProtectedResourceDetails resource = new BaseOAuth2ProtectedResourceDetails();
                resource.setClientId(this.clientId);
                restTemplate = new OAuth2RestTemplate(resource);
            }
            OAuth2AccessToken existingToken = restTemplate.getOAuth2ClientContext().getAccessToken();
            if (existingToken == null || !accessToken.equals(existingToken.getValue())) {
                DefaultOAuth2AccessToken token = new DefaultOAuth2AccessToken(accessToken);
                String tokenType = DefaultOAuth2AccessToken.BEARER_TYPE;
                token.setTokenType(tokenType);
                restTemplate.getOAuth2ClientContext().setAccessToken(token);
            }
            return restTemplate.getForEntity(path, Map.class).getBody();
        }
        catch (Exception ex) {
            this.logger.warn("Could not fetch user details: " + ex.getClass() + ", " + ex.getMessage());
            return Collections.<String, Object>singletonMap("error", "Could not fetch user details");
        }
    }
}

