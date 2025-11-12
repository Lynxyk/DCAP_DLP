package com.example.dlp.controller;

import com.example.dlp.model.*;
import com.example.dlp.model.text.TextCheckRequest;
import com.example.dlp.model.text.TextCheckResponse;
import com.example.dlp.service.RegularExpressionService;
import com.example.dlp.service.StopFileExtensionService;
import com.example.dlp.service.StopWordService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/check")
public class TextCheckController {
    private final StopWordService stopWordService;
    private final RegularExpressionService regexpService;
    private final StopFileExtensionService extensionService;

    public TextCheckController(StopWordService stopWordService,
                               RegularExpressionService regexpService,
                               StopFileExtensionService extensionService) {
        this.stopWordService = stopWordService;
        this.regexpService = regexpService;
        this.extensionService = extensionService;
    }

    @PostMapping("/text")
    public TextCheckResponse checkText(@RequestBody TextCheckRequest request) {
        String text = request.getText();
        Long roleId = request.getRoleId();
        String extension = request.getExtension();

        List<StopFileExtension> extensions = extensionService.findAll();

        boolean extensionAllowed = extensions.stream().anyMatch(ext -> {
            Long id = ext.getRole().getId();
            return (id == 1 || id.equals(roleId)) && ext.getExtension().equalsIgnoreCase(extension);
        });

        if (!extensionAllowed) {
            return new TextCheckResponse(false); // ❌ расширение не разрешено
        }

        List<StopWord> stopWords = stopWordService.findAll();
        List<RegularExpression> regexps = regexpService.findAll();

        boolean failed = stopWords.stream().anyMatch(w -> {
            Long id = w.getRole().getId();
            return (id == 1 || id.equals(roleId)) && text.contains(w.getWord());
        });

        if (!failed) {
            failed = regexps.stream().anyMatch(r -> {
                Long id = r.getRole().getId();
                return (id == 1 || id.equals(roleId)) && Pattern.compile(r.getPattern()).matcher(text).find();
            });
        }

        return new TextCheckResponse(!failed); // ✅ true — проверка пройдена, false — не пройдена
    }
}
