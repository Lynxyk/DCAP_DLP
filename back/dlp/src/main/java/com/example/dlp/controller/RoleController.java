package com.example.dlp.controller;

import com.example.dlp.model.Role;
import com.example.dlp.service.RoleService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
public class RoleController {
    private final RoleService service;
    public RoleController(RoleService service) { this.service = service; }
    @GetMapping
    public List<Role> all() { return service.findAll(); }
    @GetMapping("/{id}") public Role get(@PathVariable Long id) { return service.findById(id); }
    @PostMapping
    public Role create(@RequestBody Role r) { return service.save(r); }
    @PutMapping("/{id}") public Role update(@PathVariable Long id, @RequestBody Role r) { return service.update(id, r); }
    @DeleteMapping("/{id}") public void delete(@PathVariable Long id) { service.delete(id); }
}