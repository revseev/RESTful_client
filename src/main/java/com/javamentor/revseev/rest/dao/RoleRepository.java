package com.javamentor.revseev.rest.dao;

import com.javamentor.revseev.rest.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByType(String type);
}
