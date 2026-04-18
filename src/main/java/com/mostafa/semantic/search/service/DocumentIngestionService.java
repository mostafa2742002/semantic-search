package com.mostafa.semantic.search.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.ai.document.Document;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

@Service
public class DocumentIngestionService {


    private final VectorStore vectorStore;

    public DocumentIngestionService(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    public int ingestTextFile(String classPathLocation){
        TextReader textReader =  new TextReader( new ClassPathResource(classPathLocation) );
        List<Document> rawDocuments = textReader.get();

        List<Document> splitDocuments = TokenTextSplitter.builder()
        .withChunkSize(800)
        .withMinChunkSizeChars(350)
        .withMinChunkLengthToEmbed(10)
        .withMaxNumChunks(1000)
        .withKeepSeparator(true)
        .build()
        .apply(rawDocuments);

        vectorStore.add(splitDocuments);

        return splitDocuments.size();

    }

     public int ingestPdfFile(String classpathLocation) {
        PagePdfDocumentReader pdfReader = new PagePdfDocumentReader(
                new ClassPathResource(classpathLocation),
                PdfDocumentReaderConfig.builder()
                        .withPagesPerDocument(1)
                        .build()
        );

        List<Document> rawDocuments = pdfReader.read();

        List<Document> splitDocuments = TokenTextSplitter.builder()
                .withChunkSize(800)
                .withMinChunkSizeChars(350)
                .withMinChunkLengthToEmbed(10)
                .withMaxNumChunks(2000)
                .withKeepSeparator(true)
                .build()
                .apply(rawDocuments);

        vectorStore.add(splitDocuments);
        return splitDocuments.size();
    }


    private List<Document> enrichMetadata(List<Document> documents,
                                        String source,
                                        String category,
                                        String docId) {
        return documents.stream()
                .map(doc -> {
                    Map<String, Object> metadata = new HashMap<>(doc.getMetadata());
                    metadata.put("source", source);
                    metadata.put("category", category);
                    metadata.put("docId", docId);
                    return new Document(doc.getText(), metadata);
                })
                .toList();
    }
}
