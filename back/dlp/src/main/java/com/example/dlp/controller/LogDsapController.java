package com.example.dlp.controller;

import com.example.dlp.model.LogDsap;
import com.example.dlp.service.LogDsapService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/logs")
public class LogDsapController {
    private final LogDsapService service;

    public LogDsapController(LogDsapService service) {
        this.service = service;
    }

    @GetMapping
    public List<LogDsap> all() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public LogDsap get(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    public LogDsap create(@RequestBody LogDsap entry) {
        return service.save(entry);
    }

    @PutMapping("/{id}")
    public LogDsap update(@PathVariable Long id, @RequestBody LogDsap entry) {
        return service.update(id, entry);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
