package com.javamentor.revseev.rest.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/")
public class UserController {

    @GetMapping("/login")
    public ModelAndView showLoginPage() {
        return new ModelAndView("login");
    }

//    private static String authorizationRequestBaseUri = "oauth2/authorization";
//
//    Map<String, String> oauth2AuthenticationUrls
//            = new HashMap<>();
//
//    @Autowired
//    private ClientRegistrationRepository clientRegistrationRepository;

//    @GetMapping("/oauth2")
//    public ModelAndView getOauthLoginPage() {
//        return new ModelAndView("oauth");
//    }


    @GetMapping("/access-denied")
    public ModelAndView showDeniedPage() {
        return new ModelAndView("denied");
    }

    @GetMapping
    public ModelAndView toIndex() {
        return new ModelAndView("index");
    }

    @GetMapping(value = "/list")
    public ModelAndView toList() {
        return new ModelAndView("user-list");
    }
}
