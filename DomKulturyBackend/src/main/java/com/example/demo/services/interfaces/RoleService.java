package com.example.demo.services.interfaces;

import com.example.demo.models.ERole;
import com.example.demo.models.Role;

import java.util.List;

public interface RoleService {
    List<Role> findAll();

    Role findByName(ERole eRole);
}
