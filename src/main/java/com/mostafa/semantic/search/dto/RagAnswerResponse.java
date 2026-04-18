package com.mostafa.semantic.search.dto;


import java.util.List;
import java.util.Map;

public record RagAnswerResponse(
        String answer,
        List<SourceItem> sources
) {
    public record SourceItem(
            String documentId,
            String source,
            String category,
            Map<String, Object> metadata
    ) {}
}