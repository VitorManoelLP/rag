package com.rag.demo.dto;

import java.util.UUID;

public interface ChunkProjection {
    UUID getId();
    float[] getChunk();
    String getContent();
    UUID getDocumentId();
    Double getSimilarity();
}
