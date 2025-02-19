package com.rag.demo.service;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;
import com.rag.demo.dto.Chunk;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

@Service
@Validated
@RequiredArgsConstructor
public class PdfEmbeddingProcessorService {

    private final EmbeddingModel embeddingModel;

    @SneakyThrows
    public List<Chunk> getChunksTextFromDocument(@NotNull byte[] content) {

        final String fullText = extractTextFromPdf(content);

        final List<String> textChunks = createTextChunks(fullText);

        final List<Chunk> embeddings = new ArrayList<>();

        for (String chunk : textChunks) {
            embeddings.add(new Chunk(embeddingModel.embed(chunk), chunk));
        }

        return embeddings;
    }

    @SneakyThrows
    private String extractTextFromPdf(byte[] content) {
        StringBuilder fullText = new StringBuilder();
        try (PdfDocument pdfDocument = new PdfDocument(new PdfReader(new ByteArrayInputStream(content)))) {
            for (int pageIndex = 1; pageIndex <= pdfDocument.getNumberOfPages(); pageIndex++) {
                fullText.append(PdfTextExtractor.getTextFromPage(pdfDocument.getPage(pageIndex)));
                fullText.append("\n\n");
            }
        }
        return fullText.toString();
    }

    private List<String> createTextChunks(String text) {

        final List<String> chunks = new ArrayList<>();

        final int CHUNK_SIZE = 1000;
        final int OVERLAP = 200;

        int textLength = text.length();

        for (int i = 0; i < textLength; i += (CHUNK_SIZE - OVERLAP)) {

            int end = Math.min(i + CHUNK_SIZE, textLength);

            if (end < textLength && !Character.isWhitespace(text.charAt(end))) {

                while (end < textLength && !Character.isWhitespace(text.charAt(end))) {
                    end++;
                }

            }

            String chunk = text.substring(i, end).trim();

            if (!chunk.isEmpty()) {
                chunks.add(chunk);
            }

            if (end >= textLength) break;
        }

        return chunks;
    }

}
