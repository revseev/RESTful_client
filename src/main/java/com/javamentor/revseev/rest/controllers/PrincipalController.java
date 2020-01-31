/*
package com.javamentor.revseev.rest.controllers;

import com.javamentor.revseev.rest.model.User;
import com.javamentor.revseev.rest.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Collections;
import java.util.Map;

@RestController
public class PrincipalController {

    @Autowired
    UserService userService;

    @GetMapping("/user")
    public Map<String, Object> extractUser(@AuthenticationPrincipal OAuth2User principal) {
//        if (principal != null) {
//            return Collections.singletonMap("name", principal.getAttribute("email"));
//        }
//        return null;
        if (principal != null) {
            String principalName = principal.getAttribute("email");
            User extractedUser = userService.findByUsername(principalName);
            if ( extractedUser == null) {
                String principalPassword = principal.getAttribute("at_hash");
                long principalMoney = Long.parseLong(principal.getAttribute("sub"));

                extractedUser = new User(principalName, principalPassword, principalMoney);
                userService.saveUser(extractedUser);
//                principal = new Principal()
            }
            return Collections.singletonMap("name", principal.getAttribute("email"));
        }
        return null;
    }
}
*/
