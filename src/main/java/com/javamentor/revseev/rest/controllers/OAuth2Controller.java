package com.javamentor.revseev.rest.controllers;

import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.javamentor.revseev.rest.service.GoogleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.GsonJsonParser;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.Random;

@RestController
public class OAuth2Controller {

    private final String secretState = "secret" + new Random().nextInt(999_999);
    @Autowired
    private GoogleService service;


    @GetMapping(value ="/login/scribe-google")
    public void redirectToGoogle(HttpServletResponse response){
        String auth = service.getService().getAuthorizationUrl();
//        String auth = service.getMyAuthUrl(secretState);

        response.setHeader("Location", auth);
        response.setStatus(302);

    }

    @GetMapping(value = "/auth/google", produces = MediaType.APPLICATION_JSON_VALUE)
    public String google(@RequestParam String code, HttpServletResponse servletResponse){

//        if (secretState.equals(state)) {
            try {
                OAuth2AccessToken token = service.getService().getAccessToken(code);

                OAuthRequest request = new OAuthRequest(Verb.GET, "https://www.googleapis.com/oauth2/v3/userinfo");
                service.getService().signRequest(token, request);
                Response response = service.getService().execute(request);
                String bodyString = response.getBody();

                System.out.println("/////////");
                System.out.println(bodyString);



            }catch (Exception e){
                servletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
//        } else {
//            System.out.println("State in request does not match!");
//        }
        return null;
    }



}
