package com.example.dlp.service;

import com.example.dlp.model.StopWord;
import com.example.dlp.repository.StopWordRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StopWordService {
    private final StopWordRepository repo;
    public StopWordService(StopWordRepository repo) { this.repo = repo; }
    public List<StopWord> findAll() { return repo.findAll(); }
    public StopWord findById(Long id) { return repo.findById(id).orElseThrow(); }
    public StopWord save(StopWord s) { return repo.save(s); }
    public StopWord update(Long id, StopWord s) { s.setId(id); return repo.save(s); }
    public void delete(Long id) { repo.deleteById(id); }
}
