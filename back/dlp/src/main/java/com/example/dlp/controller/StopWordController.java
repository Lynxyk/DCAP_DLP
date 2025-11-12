package com.example.dlp.controller;
import com.example.dlp.model.StopWord;
import com.example.dlp.service.StopWordService;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/stopwords")
public class StopWordController {
    private final StopWordService service;
    public StopWordController(StopWordService service) { this.service = service; }
    @GetMapping public List<StopWord> all() { return service.findAll(); }
    @GetMapping("/{id}") public StopWord get(@PathVariable Long id) { return service.findById(id); }
    @PostMapping public StopWord create(@RequestBody StopWord s) { return service.save(s); }
    @PutMapping("/{id}") public StopWord update(@PathVariable Long id, @RequestBody StopWord s) { return service.update(id, s); }
    @DeleteMapping("/{id}") public void delete(@PathVariable Long id) { service.delete(id); }
}