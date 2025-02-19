package com.rag.demo.repository;

import com.rag.demo.dto.ChunkProjection;
import com.rag.demo.extension.TestContainerExtension;
import com.rag.demo.fixture.ChunkFixture;
import com.rag.demo.util.ChunkUtils;
import org.assertj.core.api.Assertions;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.UUID;

@Sql(scripts = {
        "classpath:db/document.sql",
        "classpath:db/document_chunk.sql"
})
public class DocumentChunkRepositoryTest extends TestContainerExtension {

    @Autowired
    private DocumentChunkRepository documentChunkRepository;

    @Test
    public void findSimilarChunks() {

        final var embeddingArray = ChunkFixture.getBaseChunk();

        final List<ChunkProjection> similarChunks = documentChunkRepository.findSimilarChunks(ChunkUtils.chunkToStr(embeddingArray), 2);

        Assertions.assertThat(similarChunks)
                .extracting(ChunkProjection::getId, ChunkProjection::getSimilarity)
                .containsExactlyInAnyOrder(
                        Tuple.tuple(UUID.fromString("73fc3b74-ea00-4cde-a784-2e304dc2417d"), 0.8177383677129482d),
                        Tuple.tuple(UUID.fromString("457a587c-d7ed-453e-9666-522b8046c3a9"), 0.8026133414968073d)
                );

    }


}
