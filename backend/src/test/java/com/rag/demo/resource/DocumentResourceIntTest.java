package com.rag.demo.resource;

import com.rag.demo.domain.Document;
import com.rag.demo.domain.DocumentChunk;
import com.rag.demo.extension.TestContainerExtension;
import com.rag.demo.repository.DocumentRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.ResourceUtils;

import java.nio.file.Files;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class DocumentResourceIntTest extends TestContainerExtension {

    private MockMvc mockMvc;

    @Autowired
    private DocumentResource resource;

    @Autowired
    private DocumentRepository documentRepository;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(resource)
                .build();
    }

    @Test
    public void processDocument() throws Exception {

        final MockMultipartFile file = new MockMultipartFile(
                "file",
                "test-document.pdf",
                "application/pdf",
                Files.readAllBytes(ResourceUtils.getFile("classpath:pdf_sample_2.pdf").toPath())
        );

        mockMvc.perform(multipart("/api/documents")
                        .file(file))
                .andExpect(status().isCreated());

        final Document document = documentRepository.findAll().stream().findFirst().get();

        Assertions.assertThat(document.getChunks())
                .isNotEmpty()
                .extracting(DocumentChunk::getContent)
                .contains("PDF de ExPDF de Exemploemplo\n" +
                        "PDF de ExPDF de Exemploemplo\n" +
                        "PDF de ExPDF de Exemploemplo\n" +
                        "PDF de ExPDF de Exemploemplo\n" +
                        "PDF de ExPDF de Exemploemplo\n" +
                        "PDF de ExPDF de Exemploemplo\n" +
                        "PDF de ExPDF de Exemploemplo");

        Assertions.assertThat(document.getHash()).isEqualTo("6f088873b84cb8c176d012969977430e3f3a692222d6e26f596644a46e392dc7");
        Assertions.assertThat(document.getName()).isEqualTo("test-document.pdf");

    }


}
