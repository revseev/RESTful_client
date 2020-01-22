package com.javamentor.revseev.rest.service;


import com.javamentor.revseev.rest.dao.RoleRepository;
import com.javamentor.revseev.rest.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class RoleServiceImpl implements RoleService {

    @Autowired
    RoleRepository roleRepository;

    @Override
    public Role findById(long id) { //TODO maybe change everywhere to Long
        return roleRepository.findById(id).get();
    }

    @Override
    public Role findByType(String type) {
        return roleRepository.findByType(type);
    }

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }
}
