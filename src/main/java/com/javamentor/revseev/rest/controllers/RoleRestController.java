package com.javamentor.revseev.rest.controllers;

import com.javamentor.revseev.rest.model.Role;
import com.javamentor.revseev.rest.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController()
public class RoleRestController {

    @Autowired
    private RoleService roleService;

    @GetMapping("/api/roles")
    public List<Role> getAllRoles() {
        return roleService.getAllRoles();
    }
}
