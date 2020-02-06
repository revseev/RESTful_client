package com.javamentor.revseev.rest.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.javamentor.revseev.rest.model.Role;
import com.javamentor.revseev.rest.model.User;
import com.javamentor.revseev.rest.service.GoogleService;
import com.javamentor.revseev.rest.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;

@RestController
public class OAuth2Controller {

    private final String secretState = "secret" + new Random().nextInt(999_999);

    @Autowired
    private GoogleService service;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    @Qualifier("noOpPasswordEncoder")
    private PasswordEncoder passwordEncoder;


    @GetMapping(value ="/login/scribe-google")
    public void redirectToGoogle(HttpServletResponse response){
//        String auth = service.getService().getAuthorizationUrl();
        String auth = service.getMyAuthUrl(secretState);

        response.setHeader("Location", auth);
        response.setStatus(302);

    }

    @GetMapping(value = "/auth/google"/*, produces = MediaType.APPLICATION_JSON_VALUE*/)
    public RedirectView google(@RequestParam String code, @RequestParam String state, HttpServletResponse servletResponse){

        if (secretState.equals(state)) {
            try {
                OAuth2AccessToken token = service.getService().getAccessToken(code);

                OAuthRequest request = new OAuthRequest(Verb.GET, "https://www.googleapis.com/oauth2/v3/userinfo");
                service.getService().signRequest(token, request);
                Response response = service.getService().execute(request);
                String bodyString = response.getBody();

                System.out.println(bodyString);

                // getting response body with User info as Map
                ObjectMapper mapper = new ObjectMapper();
                Map<String, String> bodyMap = mapper.readValue(bodyString, new TypeReference<Map<String, String>>() {});
                // extracting parameters to create a User
                String principalName = bodyMap.get("email");
                User user = userService.findByUsername(principalName);
                // adding User to DB (if new)
                if (user == null) {
                    String principalPassword = passwordEncoder.encode(bodyMap.get("sub"));
                    Long principalMoney = Long.parseLong(bodyMap.get("sub").substring(0,6));

                    user = new User(principalName,
                            principalPassword,
                            principalMoney,
                            new HashSet<>(Arrays.asList(new Role(1L,"USER"))));
                    userService.saveUser(user);
                }

                // authenticating this user
                UsernamePasswordAuthenticationToken authReq = new UsernamePasswordAuthenticationToken(user, user.getPassword());
                Authentication authentication = authenticationManager.authenticate(authReq);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                /*PreAuthenticatedAuthenticationToken authentication = new PreAuthenticatedAuthenticationToken(user, "", user.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);*/

            }catch (Exception e){
                servletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        } else {
        System.out.println("State in request does not match!");
    }
        //redirecting to IndexController ( "/user" if USER, "/list" if ADMIN )
        return new RedirectView("/");
    }
}
