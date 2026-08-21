package com.learn.ragSandbox.controller;

import com.learn.ragSandbox.model.TextChunk;
import com.learn.ragSandbox.service.PdfExtractionService;
import com.learn.ragSandbox.service.TextChunkingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    private final PdfExtractionService pdfExtractionService;
    private final TextChunkingService textChunkingService;

    public DocumentController(PdfExtractionService pdfExtractionService, TextChunkingService textChunkingService){
        this.pdfExtractionService = pdfExtractionService;
        this.textChunkingService = textChunkingService;
    }

    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file){
        String extractedText = pdfExtractionService.extractText(file);
        return ResponseEntity.ok(extractedText);
    }

    // Temporary inspection endpoint for Milestone 1 — verify chunking quality
    @PostMapping(value = "/upload/chunks", consumes = "multipart/form-data")
    public ResponseEntity<List<Map<String, Object>>> uploadAndChunk(@RequestParam("file") MultipartFile file) {
        String extractedText = pdfExtractionService.extractText(file);
        List<TextChunk> chunks = textChunkingService.chunk(extractedText);

        List<Map<String, Object>> preview = chunks.stream()
                .map(c -> Map.<String, Object>of(
                        "index", c.index(),
                        "length", c.content().length(),
                        "preview", c.content().substring(0, Math.min(150, c.content().length())) + "..."
                ))
                .toList();

        return ResponseEntity.ok(preview);
    }
}
