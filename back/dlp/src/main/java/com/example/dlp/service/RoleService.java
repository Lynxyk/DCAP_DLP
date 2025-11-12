package com.example.dlp.service;

import com.example.dlp.model.Role;
import com.example.dlp.repository.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {
    private final RoleRepository repo;
    public RoleService(RoleRepository repo) { this.repo = repo; }
    public List<Role> findAll() { return repo.findAll(); }
    public Role findById(Long id) { return repo.findById(id).orElseThrow(); }
    public Role save(Role r) { return repo.save(r); }
    public Role update(Long id, Role r) { r.setId(id); return repo.save(r); }
    public void delete(Long id) { repo.deleteById(id); }
}