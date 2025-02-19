package com.rag.demo.service;

import com.rag.demo.domain.Document;
import com.rag.demo.domain.DocumentChunk;
import com.rag.demo.dto.Chunk;
import com.rag.demo.repository.DocumentRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Validated
@Transactional
@RequiredArgsConstructor
public class DocumentService {

    private final PdfEmbeddingProcessorService embeddingProcessorService;
    private final DocumentRepository documentRepository;

    @SneakyThrows
    public void processDocument(MultipartFile multipartFile) {

        final byte[] content = multipartFile.getInputStream().readAllBytes();

        final Document document = Document.builder()
                .hash(DigestUtils.sha256Hex(content))
                .name(multipartFile.getOriginalFilename())
                .build();

//        if (documentRepository.existsByHash(document.getHash())) {
//            throw new IllegalArgumentException("This document has already been uploaded");
//        }

        final List<Chunk> chunks = embeddingProcessorService.getChunksTextFromDocument(content);

        chunks.forEach(chunk -> document.getChunks().add(DocumentChunk.builder()
                        .document(document)
                        .chunk(chunk.chunk())
                        .content(chunk.originalContent())
                        .build()));

        documentRepository.save(document);

    }

}
