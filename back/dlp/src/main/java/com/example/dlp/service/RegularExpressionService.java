package com.example.dlp.service;

import com.example.dlp.model.RegularExpression;
import com.example.dlp.repository.RegularExpressionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RegularExpressionService {
    private final RegularExpressionRepository repo;
    public RegularExpressionService(RegularExpressionRepository repo) { this.repo = repo; }
    public List<RegularExpression> findAll() { return repo.findAll(); }
    public RegularExpression findById(Long id) { return repo.findById(id).orElseThrow(); }
    public RegularExpression save(RegularExpression r) { return repo.save(r); }
    public RegularExpression update(Long id, RegularExpression r) { r.setId(id); return repo.save(r); }
    public void delete(Long id) { repo.deleteById(id); }
}
