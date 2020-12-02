package com.example.demo.services.implementations;

import com.example.demo.exceptions.exception.NotFoundGlobalException;
import com.example.demo.models.ERole;
import com.example.demo.models.Role;
import com.example.demo.repository.RoleRepository;
import com.example.demo.services.interfaces.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    @Override
    public Role findByName(ERole eRole) {
        return roleRepository.findByName(eRole).orElseThrow(() -> new NotFoundGlobalException("Nie znaleziono roli " + eRole));
    }
}
