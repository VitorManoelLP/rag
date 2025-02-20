package com.rag.demo.service;

import com.rag.demo.domain.Document;
import com.rag.demo.domain.DocumentChunk;
import com.rag.demo.dto.Chunk;
import com.rag.demo.dto.Notification;
import com.rag.demo.repository.DocumentRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@Validated
@Transactional
@RequiredArgsConstructor
public class DocumentService {

    private final PdfEmbeddingProcessorService embeddingProcessorService;
    private final DocumentRepository documentRepository;
    private final NotificationService notificationService;
    private final ApplicationEventPublisher applicationEventPublisher;

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

        final List<Chunk> chunks = embeddingProcessorService.getChunksTextFromDocument(content);

        chunks.forEach(chunk -> document.getChunks().add(DocumentChunk.builder()
                .document(document)
                .chunk(chunk.chunk())
                .content(chunk.originalContent())
                .build()));

        notificationService.send("Salvando documento...", "upload");

        documentRepository.save(document);

        applicationEventPublisher.publishEvent(new Notification("Documento salvos com sucesso...", false, true, "upload"));
    }

}
