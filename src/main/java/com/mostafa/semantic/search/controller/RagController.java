package com.mostafa.semantic.search.controller;



import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mostafa.semantic.search.dto.RagAnswerResponse;
import com.mostafa.semantic.search.service.RagService;

@RestController
@RequestMapping("/api/rag")
public class RagController {

    private final RagService ragService;

    public RagController(RagService ragService) {
        this.ragService = ragService;
    }

    @GetMapping("/ask")
    public RagAnswerResponse ask(@RequestParam String q) {
        return ragService.ask(q);
    }
}