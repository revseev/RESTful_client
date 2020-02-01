package com.javamentor.revseev.rest.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/")
public class ViewController {

    @GetMapping("/login")
    public ModelAndView showLoginPage() {
        return new ModelAndView("login");
    }

    @GetMapping("/access-denied")
    public ModelAndView showDeniedPage() {
        return new ModelAndView("denied");
    }

    @GetMapping(value = "/list")
    public ModelAndView toList() {
        return new ModelAndView("user-list");
    }

    @GetMapping("/")
    public String index() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication.isAuthenticated()) {

            Set<String> roleNAme = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());

            if (roleNAme.contains("ADMIN")) {
                return "redirect:/list";
            } else
                return "index";
        }
        return "redirect:/login";
    }
}
