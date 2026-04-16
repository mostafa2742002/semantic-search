package com.mostafa.semantic.search.dto;


import java.util.Map;

public record SearchResponse(
        String content,
        Map<String, Object> metadata
) {}