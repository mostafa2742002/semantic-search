package com.mostafa.semantic.search.service;


import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

@Service
public class RagService {

    private final ChatClient chatClient;
    private final VectorStore vectorStore;

    public RagService(ChatClient chatClient, VectorStore vectorStore) {
        this.chatClient = chatClient;
        this.vectorStore = vectorStore;
    }

    public String ask(String question) {
        return chatClient.prompt()
                .system("""
                    You are a helpful backend engineering assistant.
                    Answer only from the retrieved context when possible.
                    If the context is insufficient, say clearly that the answer
                    is not fully supported by the provided documents.
                    Keep the answer clear and practical.
                """)
                .user(question)
                .advisors(QuestionAnswerAdvisor.builder(vectorStore)
                        .searchRequest(SearchRequest.builder()
                                .topK(3)
                                .similarityThreshold(0.5)
                                .build())
                        .build())
                .call()
                .content();
    }
}
