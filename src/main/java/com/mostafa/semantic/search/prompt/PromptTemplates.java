package com.mostafa.semantic.search.prompt;


public final class PromptTemplates {

    private PromptTemplates() {}

    public static String ragSystemPrompt() {
        return """
                You are a helpful backend engineering assistant.
                Use the provided context when answering.
                If the context is insufficient, say so clearly.
                Keep answers practical, concise, and correct.
                """;
    }

    public static String ragUserPrompt(String question, String context) {
        return """
                Question:
                %s

                Context:
                %s
                """.formatted(question, context);
    }
}