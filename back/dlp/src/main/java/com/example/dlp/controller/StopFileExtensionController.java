package com.example.dlp.controller;
import com.example.dlp.model.StopFileExtension;
import com.example.dlp.service.StopFileExtensionService;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/extensions")
public class StopFileExtensionController {
    private final StopFileExtensionService service;
    public StopFileExtensionController(StopFileExtensionService service) { this.service = service; }
    @GetMapping public List<StopFileExtension> all() { return service.findAll(); }
    @GetMapping("/{id}") public StopFileExtension get(@PathVariable Long id) { return service.findById(id); }
    @PostMapping public StopFileExtension create(@RequestBody StopFileExtension s) { return service.save(s); }
    @PutMapping("/{id}") public StopFileExtension update(@PathVariable Long id, @RequestBody StopFileExtension s) { return service.update(id, s); }
    @DeleteMapping("/{id}") public void delete(@PathVariable Long id) { service.delete(id); }
}