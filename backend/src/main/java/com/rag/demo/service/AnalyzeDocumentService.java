package com.rag.demo.service;

import com.rag.demo.domain.Document;
import com.rag.demo.dto.Answer;
import com.rag.demo.dto.ChunkProjection;
import com.rag.demo.repository.DocumentChunkRepository;
import com.rag.demo.repository.DocumentRepository;
import com.rag.demo.util.ChunkUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class AnalyzeDocumentService {

    private final EmbeddingModel embeddingModel;
    private final ChatModelService chatModelService;
    private final DocumentChunkRepository documentChunkRepository;
    private final DocumentRepository documentRepository;

    public Answer analyze(String question) {

        final float[] questionEmbedded = embeddingModel.embed(question);

        final List<ChunkProjection> similarChunks = documentChunkRepository
                .findSimilarChunks(ChunkUtils.chunkToStr(questionEmbedded), 5);

        if (similarChunks.isEmpty()) {
            throw new IllegalArgumentException("No documents found to analyze");
        }

        final Set<UUID> documentsAnalyzed = similarChunks.stream()
                .map(ChunkProjection::getDocumentId)
                .collect(Collectors.toSet());

        final List<String> documents = documentRepository.findAllById(documentsAnalyzed)
                .stream()
                .map(Document::getName)
                .toList();

        final double avgConfidence = similarChunks.stream()
                .mapToDouble(ChunkProjection::getSimilarity)
                .average()
                .orElse(0.0);

        final String answer = chatModelService.getAnswer(Map.of("documents", buildPromptContext(similarChunks), "question", question), "classpath:prompt.st");

        return new Answer(answer, documents, avgConfidence);
    }

    private String buildPromptContext(List<ChunkProjection> chunks) {

        StringBuilder context = new StringBuilder();

        for (int i = 0; i < chunks.size(); i++) {
            ChunkProjection chunk = chunks.get(i);
            context.append("Documento ").append(i + 1).append(": ");
            context.append(chunk.getContent()).append("\n\n");
        }

        return context.toString();
    }

}
