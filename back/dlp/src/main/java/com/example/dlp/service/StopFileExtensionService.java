package com.example.dlp.service;

import com.example.dlp.model.StopFileExtension;
import com.example.dlp.repository.StopFileExtensionRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class StopFileExtensionService {
    private final StopFileExtensionRepository repo;
    public StopFileExtensionService(StopFileExtensionRepository repo) { this.repo = repo; }
    public List<StopFileExtension> findAll() { return repo.findAll(); }
    public StopFileExtension findById(Long id) { return repo.findById(id).orElseThrow(); }
    public StopFileExtension save(StopFileExtension s) { return repo.save(s); }
    public StopFileExtension update(Long id, StopFileExtension s) { s.setId(id); return repo.save(s); }
    public void delete(Long id) { repo.deleteById(id); }
}