package com.example.les17.controller;

import com.example.les17.dto.RoleDto;
import com.example.les17.model.Role;
import com.example.les17.repository.RoleRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    private final RoleRepository repos;

    public RoleController(RoleRepository repos) {
        this.repos = repos;
    }

    @PostMapping("/")
    public String createRole(@RequestBody RoleDto role) {
        Role newRole = new Role();
        newRole.setRolename(role.getRolename());
        repos.save(newRole);

        return "Done";
    }
}
