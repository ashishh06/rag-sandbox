package com.learn.ragSandbox.service;

import com.learn.ragSandbox.util.PdfExtractionException;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class PdfExtractionService {

    private static final Logger log = LoggerFactory.getLogger(PdfExtractionService.class);

    public String extractText(MultipartFile file) {
        if (file.isEmpty()) {
            throw new PdfExtractionException("Uploaded file is empty");
        }

        try (PDDocument document = Loader.loadPDF(file.getBytes())) {
            if (document.isEncrypted()) {
                throw new PdfExtractionException("Cannot process encrypted PDF");
            }

            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document);

            log.info("Extracted {} characters from {} ({} pages)",
                    text.length(), file.getOriginalFilename(), document.getNumberOfPages());

            return text;

        } catch (IOException e) {
            log.error("Failed to extract text from PDF: {}", file.getOriginalFilename(), e);
            throw new PdfExtractionException("Failed to read PDF file: " + e.getMessage());
        }
    }
}
