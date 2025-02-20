package com.rag.demo.resource;

import com.rag.demo.domain.Document;
import com.rag.demo.dto.Answer;
import com.rag.demo.repository.DocumentRepository;
import com.rag.demo.service.AnalyzeDocumentService;
import com.rag.demo.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/documents")
public class DocumentResource {

    private final DocumentService documentService;
    private final AnalyzeDocumentService analyzeDocumentService;
    private final DocumentRepository documentRepository;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void processDocument(@RequestParam("file") MultipartFile file) {
        documentService.processDocument(file);
    }

    @PostMapping("/answer")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Answer getAnswer(@RequestBody String question) {
        return analyzeDocumentService.analyze(question);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<Document> findAll(Pageable pageable) {
        return documentRepository.findAll(pageable);
    }

}
