package com.rag.demo.repository;

import com.rag.demo.domain.DocumentChunk;
import com.rag.demo.dto.ChunkProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DocumentChunkRepository extends JpaRepository<DocumentChunk, UUID> {

    @Query(value =
            "SELECT dc.id as id, dc.chunk as chunk, dc.content as content, dc.id_document as documentId, " +
                    "(1 - (dc.chunk <=> cast(:embedding as vector(1536)))) as similarity " +
                    "FROM document_chunk dc " +
                    "ORDER BY dc.chunk <=> cast(:embedding as vector(1536)) " +
                    "LIMIT :limit",
            nativeQuery = true)
    List<ChunkProjection> findSimilarChunks(
            @Param("embedding") String embedding,
            @Param("limit") int limit);

}
