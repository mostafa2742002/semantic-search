package com.mostafa.semantic.search.service;

import java.util.List;

import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

@Service
public class DocumentService {

    private final VectorStore vectorStore;

    public DocumentService(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    public void addDocument( String content) {
        Document document = new Document(content);
        vectorStore.add(List.of(document));
    }

    public List<Document> searchDocuments(String query) {
        return vectorStore.similaritySearch(query);
    }
}
