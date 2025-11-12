package com.example.dlp.controller;
import com.example.dlp.model.RegularExpression;
import com.example.dlp.service.RegularExpressionService;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/regexps")
public class RegularExpressionController {
    private final RegularExpressionService service;
    public RegularExpressionController(RegularExpressionService service) { this.service = service; }
    @GetMapping public List<RegularExpression> all() { return service.findAll(); }
    @GetMapping("/{id}") public RegularExpression get(@PathVariable Long id) { return service.findById(id); }
    @PostMapping public RegularExpression create(@RequestBody RegularExpression r) { return service.save(r); }
    @PutMapping("/{id}") public RegularExpression update(@PathVariable Long id, @RequestBody RegularExpression r) { return service.update(id, r); }
    @DeleteMapping("/{id}") public void delete(@PathVariable Long id) { service.delete(id); }
}