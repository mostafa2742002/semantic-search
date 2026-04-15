package com.mostafa.semantic.search.controller;

import java.util.List;

import org.springframework.ai.document.Document;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mostafa.semantic.search.dto.AddDocumentRequest;
import com.mostafa.semantic.search.service.DocumentService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @PostMapping
    public String add(@RequestBody @Valid AddDocumentRequest request) {
        documentService.addDocument(request.content());
        return "Document added successfully";
    }
    

    @GetMapping("/search")
    public List<Document> search( @RequestParam String search){
        return documentService.searchDocuments(search);
    }

}
