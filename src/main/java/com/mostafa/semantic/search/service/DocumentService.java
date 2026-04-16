package com.mostafa.semantic.search.service;

import java.util.List;

import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import com.mostafa.semantic.search.dto.SearchResponse;

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
        return vectorStore.similaritySearch(
            SearchRequest.builder()
                .query(query)
                .topK(1)
                .build()
        );
    }

    public List<SearchResponse> search(String query) {
        List<Document> results = vectorStore.similaritySearch(
                SearchRequest.builder()
                        .query(query)
                        .topK(3)
                        .similarityThreshold(0.5)
                        .build()
        );

        return results.stream()
                .map(doc -> new SearchResponse(doc.getText(), doc.getMetadata()))
                .toList();
    }
}
