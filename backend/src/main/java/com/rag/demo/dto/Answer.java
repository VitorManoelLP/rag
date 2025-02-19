package com.rag.demo.dto;

import java.util.List;

public record Answer(String value, List<String> usedDocuments, double confidence) {
}
