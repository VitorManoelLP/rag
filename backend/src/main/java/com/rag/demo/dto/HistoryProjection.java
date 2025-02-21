package com.rag.demo.dto;

import java.util.UUID;

public interface HistoryProjection {
    UUID getId();
    String getAnswer();
    Double getSimilarity();
}
