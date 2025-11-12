package com.example.dlp.service;

import com.example.dlp.model.LogDsap;
import com.example.dlp.repository.LogDsapRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LogDsapService {
    private final LogDsapRepository repo;

    public LogDsapService(LogDsapRepository repo) {
        this.repo = repo;
    }

    public List<LogDsap> findAll() {
        return repo.findAll();
    }

    public LogDsap findById(Long id) {
        return repo.findById(id).orElseThrow();
    }

    public LogDsap save(LogDsap entry) {
        return repo.save(entry);
    }

    public LogDsap update(Long id, LogDsap entry) {
        entry.setId(id);
        return repo.save(entry);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}
