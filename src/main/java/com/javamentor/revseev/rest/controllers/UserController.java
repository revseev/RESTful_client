package com.javamentor.revseev.rest.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/")
public class UserController {

    @GetMapping("/login")
    public ModelAndView showLoginPage() {
        return new ModelAndView("login");
    }

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
