package com.rag.demo.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rag.demo.domain.ChatHistory;
import com.rag.demo.domain.Document;
import com.rag.demo.dto.Answer;
import com.rag.demo.dto.ChunkProjection;
import com.rag.demo.dto.HistoryProjection;
import com.rag.demo.repository.ChatHistoryRepository;
import com.rag.demo.repository.DocumentChunkRepository;
import com.rag.demo.repository.DocumentRepository;
import com.rag.demo.util.ChunkUtils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class AnalyzeDocumentService {

    private final EmbeddingModel embeddingModel;
    private final ChatModelService chatModelService;
    private final DocumentChunkRepository documentChunkRepository;
    private final DocumentRepository documentRepository;
    private final ChatHistoryRepository chatHistoryRepository;
    private final ObjectMapper objectMapper;

    @SneakyThrows
    public Answer analyze(String question) {

        final float[] questionEmbedded = embeddingModel.embed(question);

        final String embedding = ChunkUtils.chunkToStr(questionEmbedded);

        Optional<HistoryProjection> similarEmbedding = chatHistoryRepository.findSimilarEmbedding(embedding);

        if (similarEmbedding.isPresent() && similarEmbedding.get().getSimilarity() > 0.9) {

            final Answer answer = objectMapper.readValue(similarEmbedding.get().getAnswer(), Answer.class);

            chatHistoryRepository.save(ChatHistory.builder()
                    .embeddingUserMessage(questionEmbedded)
                    .botMessage(answer)
                    .userMessage(question)
                    .build());

            return answer;
        }

        final List<ChunkProjection> similarChunks = documentChunkRepository
                .findSimilarChunks(embedding, 5);

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

        final Answer answerObj = new Answer(answer, documents, avgConfidence);

        chatHistoryRepository.save(ChatHistory.builder()
                .embeddingUserMessage(questionEmbedded)
                .botMessage(answerObj)
                .userMessage(question)
                .build());

        return answerObj;
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
