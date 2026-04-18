package com.mostafa.semantic.search.service;


import java.util.List;
import java.util.stream.Collectors;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import com.mostafa.semantic.search.dto.RagAnswerResponse;
import com.mostafa.semantic.search.prompt.PromptTemplates;

@Service
public class RagService {

    private final ChatClient chatClient;
    private final VectorStore vectorStore;

    public RagService(ChatClient chatClient, VectorStore vectorStore) {
        this.chatClient = chatClient;
        this.vectorStore = vectorStore;
    }

   public RagAnswerResponse ask(String question) {
        List<Document> docs = vectorStore.similaritySearch(
                SearchRequest.builder()
                        .query(question)
                        .topK(4)
                        .similarityThreshold(0.5)
                        .build()
        );

        String context = docs.stream()
                .map(Document::getText)
                .collect(Collectors.joining("\n\n---\n\n"));

        String answer = chatClient.prompt()
                        .system(PromptTemplates.ragSystemPrompt())
                        .user(PromptTemplates.ragUserPrompt(question, context))
                        .call()
                        .content();

        List<RagAnswerResponse.SourceItem> sources = docs.stream()
                .map(doc -> new RagAnswerResponse.SourceItem(
                        doc.getId(),
                        String.valueOf(doc.getMetadata().get("source")),
                        String.valueOf(doc.getMetadata().get("category")),
                        doc.getMetadata()
                ))
                .toList();

        return new RagAnswerResponse(answer, sources);
    }
}
