# Semantic Search and RAG API

A production-style Spring Boot backend that turns raw text and PDF content into searchable knowledge using embeddings, `pgvector`, and Retrieval-Augmented Generation (RAG).

This project demonstrates how to build a modern AI-enabled backend service with clean layering, vector search, local LLM integration through Ollama, and API endpoints for ingestion, retrieval, and grounded question answering.

## Why This Project Stands Out

Most demo projects stop at "call an AI model and return text." This one goes further:

- Ingests both plain text and PDF documents
- Splits documents into embedding-friendly chunks
- Stores semantic vectors in PostgreSQL with `pgvector`
- Supports similarity search over indexed content
- Produces grounded answers with source attribution
- Runs on local models via Ollama, reducing external API dependency
- Uses modern Spring AI components with Spring Boot 3 and Java 21

This is the kind of project that shows practical backend engineering, not just framework familiarity.

## What It Does

The application exposes three core capabilities:

1. Document ingestion
   Load text or PDF files from the classpath, split them into chunks, and store them in the vector database.

2. Semantic search
   Search by meaning instead of exact keyword matching using embeddings and nearest-neighbor retrieval.

3. Retrieval-Augmented Generation
   Ask natural-language questions and get responses grounded in the most relevant retrieved content.

## Architecture

```text
Client
  |
  v
Spring Boot REST API
  |
  +-- Document Ingestion Layer
  |     |- TextReader
  |     |- PagePdfDocumentReader
  |     |- TokenTextSplitter
  |
  +-- Retrieval Layer
  |     |- VectorStore
  |     |- PGVector / PostgreSQL
  |
  +-- Generation Layer
        |- ChatClient
        |- Ollama
        |- Llama 3.1 8B
```

## Tech Stack

- Java 21
- Spring Boot 3.4.5
- Spring AI 1.1.4
- PostgreSQL
- `pgvector`
- Ollama
- Llama 3.1 8B for chat
- `nomic-embed-text` for embeddings
- Maven
- Docker Compose

## Key Engineering Highlights

- Clear separation of concerns across controllers, services, DTOs, prompts, and configuration
- Vector-native search backed by PostgreSQL instead of in-memory shortcuts
- Local-first AI architecture through Ollama for privacy, cost control, and reproducibility
- PDF ingestion pipeline using Spring AI document readers
- Prompt templating for maintainable RAG behavior
- Source-aware answer responses to improve trust and explainability
- Dockerized database setup for fast local onboarding

## Project Structure

```text
src/main/java/com/mostafa/semantic/search
├── config
│   └── ChatConfig.java
├── controller
│   ├── DocumentController.java
│   ├── IngestionController.java
│   └── RagController.java
├── dto
│   ├── AddDocumentRequest.java
│   ├── RagAnswerResponse.java
│   └── SearchResponse.java
├── prompt
│   └── PromptTemplates.java
├── service
│   ├── DocumentIngestionService.java
│   ├── DocumentService.java
│   └── RagService.java
└── SemanticSearchApplication.java
```

## API Endpoints

### 1. Add a document manually

`POST /api/documents`

```json
{
  "content": "PostgreSQL supports ACID transactions."
}
```

### 2. Search documents semantically

`GET /api/documents/search?search=how to secure backend api`

### 3. Ingest a text file

`POST /api/ingestion/text?path=docs/security.txt`

### 4. Ingest a PDF file

`POST /api/ingestion/pdf?path=docs/your-file.pdf`

### 5. Ask a grounded RAG question

`GET /api/rag/ask?q=how does jwt authentication work in spring boot`

## Example Workflow

1. Start PostgreSQL with `pgvector`
2. Run Ollama locally
3. Pull the required chat and embedding models
4. Start the Spring Boot app
5. Ingest source documents
6. Perform semantic search or ask grounded questions

This flow mirrors how real internal knowledge assistants, support copilots, and document intelligence systems are built.

## Local Setup

### Prerequisites

- Java 21
- Docker and Docker Compose
- Ollama installed locally

### 1. Start PostgreSQL with pgvector

```bash
docker-compose up -d
```

This starts PostgreSQL on port `5415`.

### 2. Start Ollama

Make sure Ollama is running on:

```text
http://localhost:11434
```

### 3. Pull the models

```bash
ollama pull llama3.1:8b
ollama pull nomic-embed-text
```

### 4. Run the application

```bash
./mvnw spring-boot:run
```

The API runs on:

```text
http://localhost:8081
```

## Configuration

Current application configuration is defined in [`application.yml`](src/main/resources/application.yml).

```yaml
server:
  port: 8081

spring:
  datasource:
    url: jdbc:postgresql://localhost:5415/semantic_db
    username: postgres
    password: postgres

  ai:
    ollama:
      base-url: http://localhost:11434
      chat:
        model: llama3.1:8b
      embedding:
        model: nomic-embed-text

    vectorstore:
      pgvector:
        initialize-schema: true
```

## Example Requests

The project includes a ready-to-use [`requests.http`](requests.http) file.

You can also use `curl`:

```bash
curl -X POST http://localhost:8081/api/documents \
  -H "Content-Type: application/json" \
  -d '{"content":"JWT secures stateless APIs by sending signed tokens with each request."}'
```

```bash
curl "http://localhost:8081/api/documents/search?search=how to secure stateless apis with token"
```

```bash
curl "http://localhost:8081/api/rag/ask?q=how does jwt authentication work in spring boot"
```

## Sample Use Cases

- Internal knowledge base search
- Engineering documentation assistant
- Security policy Q and A
- Support knowledge retrieval
- Technical onboarding assistant
- Domain-specific chatbot backends

## What This Project Demonstrates About the Developer

- Strong understanding of modern backend architecture
- Ability to combine AI infrastructure with traditional software engineering
- Experience with vector databases and semantic retrieval
- Comfort integrating local LLM tooling into Java systems
- Practical API design using Spring Boot
- Attention to maintainability through DTOs, service layers, and prompt abstraction

## Roadmap

Planned improvements that would move this from strong prototype to production-ready platform:

- Metadata-aware filtering during retrieval
- Upload-based ingestion instead of classpath-only ingestion
- Authentication and authorization
- Observability with metrics and tracing
- Swagger or OpenAPI documentation
- Better source ranking and citation formatting
- Integration tests with Testcontainers
- Hybrid retrieval combining lexical and vector search
- Multi-tenant document collections

## Why Recruiters and Engineers Notice Projects Like This

This project sits at the intersection of backend engineering, applied AI, and real-world data systems. It shows the ability to:

- design service boundaries
- work with infrastructure
- build on top of emerging AI tooling
- keep implementation grounded in maintainable Java code

That combination is valuable because it maps directly to the kinds of systems many teams are building right now: internal copilots, knowledge assistants, semantic search tools, and retrieval-backed AI workflows.

## Running Tests

```bash
./mvnw test
```

## Author

Built by Mostafa as a focused backend and AI engineering project to demonstrate practical skills in:

- Spring Boot
- Spring AI
- Java 21
- PostgreSQL and `pgvector`
- semantic search
- RAG system design

## License

This project is open for learning, experimentation, and portfolio demonstration.
