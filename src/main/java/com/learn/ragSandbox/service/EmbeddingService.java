package com.learn.ragSandbox.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.stereotype.Service;

@Service
public class EmbeddingService {

    private static final Logger log = LoggerFactory.getLogger(EmbeddingService.class);

    private final EmbeddingModel embeddingModel;

    public EmbeddingService(EmbeddingModel embeddingModel) {
        this.embeddingModel = embeddingModel;
    }

    public float[] embed(String text) {
        EmbeddingResponse response = embeddingModel.embedForResponse(java.util.List.of(text));
        float[] vector = response.getResult().getOutput();

        log.info("Embedded text ({} chars) into vector of dimension {}", text.length(), vector.length);
        log.info("First 5 values: {}", java.util.Arrays.toString(java.util.Arrays.copyOfRange(vector, 0, 5)));

        return vector;
    }
}
