package com.mostafa.semantic.search.dto;

import jakarta.validation.constraints.NotBlank;

public record AddDocumentRequest (
    @NotBlank String content
){}
