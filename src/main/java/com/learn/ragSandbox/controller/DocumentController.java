package com.learn.ragSandbox.controller;

import com.learn.ragSandbox.service.PdfExtractionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    private final PdfExtractionService pdfExtractionService;

    public DocumentController(PdfExtractionService pdfExtractionService){
        this.pdfExtractionService = pdfExtractionService;
    }

    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file){
        String extractedText = pdfExtractionService.extractText(file);
        return ResponseEntity.ok(extractedText);
    }
}
