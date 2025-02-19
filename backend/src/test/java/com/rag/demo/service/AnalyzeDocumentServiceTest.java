package com.rag.demo.service;

import com.rag.demo.dto.Answer;
import com.rag.demo.extension.TestContainerExtension;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

@Sql(scripts = {
        "classpath:db/document.sql",
        "classpath:db/document_chunk.sql"
})
public class AnalyzeDocumentServiceTest extends TestContainerExtension {

    @Autowired
    private AnalyzeDocumentService analyzeDocumentService;

    @Test
    public void analyze() {

        final Answer answer = analyzeDocumentService.analyze("Qual o objetivo do artigo?");

        Assertions.assertThat(answer.usedDocuments()).hasSize(1);
        Assertions.assertThat(answer.value()).isEqualTo("Answer");
        Assertions.assertThat(answer.confidence()).isEqualTo(0.8031185986207525);

    }

}