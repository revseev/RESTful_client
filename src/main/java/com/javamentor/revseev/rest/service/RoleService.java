package com.javamentor.revseev.rest.service;


import com.javamentor.revseev.rest.model.Role;

import java.util.List;

public interface RoleService {

    Role findById(long id);

    Role findByType(String type);

    List<Role> getAllRoles();

}
