package com.rag.demo.service;

import com.rag.demo.domain.Document;
import com.rag.demo.dto.Notification;
import com.rag.demo.repository.DocumentRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Map;

@Service
@Validated
@Transactional
@RequiredArgsConstructor
public class DocumentService {

    private final PdfEmbeddingProcessorService embeddingProcessorService;
    private final DocumentRepository documentRepository;
    private final NotificationService notificationService;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final VectorStore vectorStore;

    @Async
    @SneakyThrows
    public void processDocument(String filename, byte[] content) {

        final Document document = Document.builder()
                .hash(DigestUtils.sha256Hex(content))
                .size(content.length)
                .name(filename)
                .build();

        if (documentRepository.existsByHash(document.getHash())) {

            notificationService.sendError("Este documento já foi enviado antes...", "upload");

            throw new IllegalArgumentException("This document has already been uploaded");
        }

        notificationService.send("Processando documento: criando segmentos para análise pela IA...", "upload");

        final List<String> chunks = embeddingProcessorService.getChunksTextFromDocument(content);

        notificationService.send("Salvando documento...", "upload");

        documentRepository.save(document);

        vectorStore.add(chunks.stream().map(c -> org.springframework.ai.document.Document.builder()
                        .text(c)
                        .metadata(Map.of("document", document.getName()))
                        .build())
                .toList());

        applicationEventPublisher.publishEvent(new Notification("Documento salvos com sucesso...", false, true, "upload"));
    }

}
