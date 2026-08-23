package com.learn.ragSandbox.service;

import com.learn.ragSandbox.model.TextChunk;
import com.learn.ragSandbox.util.ChunkingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TextChunkingService {

    private static final Logger log = LoggerFactory.getLogger(TextChunkingService.class);

    private static final int CHUNK_SIZE = 200;
    private static final int CHUNK_OVERLAP = 50;
    private static final int BOUNDARY_SEARCH_WINDOW = 20;

    private static final String PARAGRAPH_BREAK = "\n\n";
    private static final String SENTENCE_BREAK = ". ";

    public TextChunkingService() {
        if (CHUNK_OVERLAP >= CHUNK_SIZE) {
            throw new IllegalStateException(
                    "CHUNK_OVERLAP (" + CHUNK_OVERLAP + ") must be smaller than CHUNK_SIZE (" + CHUNK_SIZE + ")");
        }
    }

    public List<TextChunk> chunk(String text) {
        if (text == null || text.isBlank()) {
            throw new ChunkingException("Cannot chunk empty or blank text");
        }

        List<TextChunk> chunks = new ArrayList<>();

        // Trivial case: whole text fits in one chunk
        if (text.length() <= CHUNK_SIZE) {
            chunks.add(new TextChunk(0, text.trim()));
            log.info("Text fit in a single chunk ({} chars)", text.length());
            return chunks;
        }

        int start = 0;
        int index = 0;

        while (start < text.length()) {
            int targetEnd = Math.min(start + CHUNK_SIZE, text.length());

            // If we're already at the end, just take the rest and stop
            if (targetEnd == text.length()) {
                chunks.add(new TextChunk(index, text.substring(start).trim()));
                break;
            }

            int breakPoint = findBreakPoint(text, start, targetEnd);

            String content = text.substring(start, breakPoint).trim();
            if (!content.isEmpty()) {
                chunks.add(new TextChunk(index, content));
                index++;
            }

            int nextStart = breakPoint - CHUNK_OVERLAP;

            // Guard against infinite loop: force forward progress if overlap
            // math would put us at or before the current start
            if (nextStart <= start) {
                nextStart = start + CHUNK_SIZE;
            }

            start = nextStart;
        }

        log.info("Split text ({} chars) into {} chunks", text.length(), chunks.size());
        return chunks;
    }

    /**
     * Looks backward from targetEnd for a clean break point (paragraph, then
     * sentence). Falls back to the hard targetEnd cutoff if nothing suitable
     * is found within the search window.
     */
    private int findBreakPoint(String text, int searchStart, int targetEnd) {
        int windowStart = Math.max(searchStart, targetEnd - BOUNDARY_SEARCH_WINDOW);

        // Prefer a paragraph break
        int paragraphBreak = text.lastIndexOf(PARAGRAPH_BREAK, targetEnd);
        if (paragraphBreak >= windowStart) {
            return paragraphBreak + PARAGRAPH_BREAK.length();
        }

        // Fall back to a sentence break
        int sentenceBreak = text.lastIndexOf(SENTENCE_BREAK, targetEnd);
        if (sentenceBreak >= windowStart) {
            return sentenceBreak + SENTENCE_BREAK.length();
        }

        // No clean boundary nearby — hard cutoff
        return targetEnd;
    }
}
