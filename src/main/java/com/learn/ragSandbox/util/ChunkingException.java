package com.learn.ragSandbox.util;

import com.learn.ragSandbox.service.PdfExtractionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

public class ChunkingException extends RuntimeException {
    private static final Logger log = LoggerFactory.getLogger(PdfExtractionService.class);

    public ChunkingException(String message) {
        super(message);
    }

    @ExceptionHandler(ChunkingException.class)
    public ResponseEntity<Map<String, Object>> handleChunkingException(ChunkingException ex) {
        log.error("Chunking failed: {}", ex.getMessage());
        Map<String, Object> body = Map.of(
                "error", "CHUNKING_FAILED",
                "message", ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }
}
