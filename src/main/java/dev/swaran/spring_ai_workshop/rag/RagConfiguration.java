package dev.swaran.spring_ai_workshop.rag;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.transformers.TransformersEmbeddingModel;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;


/**
 * Configuration class for Retrieval-Augmented Generation (RAG) setup.
 * This class configures a vector store that can be used to store and retrieve
 * document embeddings for AI-powered search and retrieval operations.
 */
@Configuration
public class RagConfiguration {

    // Logger instance for logging operations and debugging
    private static final Logger log = LoggerFactory.getLogger(RagConfiguration.class);

    // Injects the value "vectorstore.json" as the vector store file name
    @Value("vectorstore.json")
    private String vectorStoreName;

    // Injects a classpath resource pointing to the models.json file containing document data
    @Value("classpath:/data/models.json")
    private Resource models;

/**
 * Creates and configures an EmbeddingModel bean for converting text into vector embeddings.
 * This model is used to transform document text and user queries into numerical vectors
 * that can be compared for semantic similarity in the RAG system.
 *
 * @return A TransformersEmbeddingModel instance that handles text-to-vector conversion
 */
@Bean
EmbeddingModel embeddingModel() {
    return new TransformersEmbeddingModel();
}
    /**
     Here's a step-by-step breakdown of how the vector store gets created:

     1. First time (no file exists)
     models.json (raw text)
     ↓
     TextReader          → reads the file into List<Document>
     ↓
     TokenTextSplitter     → breaks large docs into smaller chunks (tokens)
     ↓
     EmbeddingModel       → converts each chunk into a float[] vector (numbers)
     ↓
     SimpleVectorStore     → stores {text + vector} pairs in memory
     ↓
     save to disk         → writes to vectorstore.json

     Copy
     2. Second time onwards (file exists)
     vectorstore.json
     ↓
     simpleVectorStore.load()  → reads vectors directly from file, skips all above steps

     Copy
     Why split the documents?

     LLMs and embedding models have token limits. A large JSON file can't be embedded as one chunk. TokenTextSplitter breaks it into smaller overlapping pieces so each chunk fits within the model's limit.

     What is an embedding?

     It converts text like "gemini-2.5-flash is a multimodal model" into a vector like [0.23, -0.87, 0.45, ...] — a list of numbers that represents the semantic meaning of the text. Similar meanings produce similar vectors.

     Why save to disk?

     Generating embeddings via API costs money and takes time. Saving to vectorstore.json means you only do it once — subsequent restarts just load from file instantly.

     At query time, your question also gets converted to a vector, and the store finds the chunks whose vectors are closest (most similar meaning) to your question vector — that's the "retrieval" part of RAG.
     */


    @Bean
    SimpleVectorStore simpleVectorStore(EmbeddingModel embeddingModel) throws IOException {
        // Creates a new SimpleVectorStore instance using the provided embedding model
        var simpleVectorStore = SimpleVectorStore.builder(embeddingModel).build();

        var vectorStoreFile = getVectorStoreFile();

        if (vectorStoreFile.exists()) {
            log.info("Vector Store File Exists,");
            simpleVectorStore.load(vectorStoreFile);
        }
        else {
            log.info("Vector Store File Does Not Exist, loading documents");

            // Creates a TextReader to read the models.json resource file
            TextReader textReader = new TextReader(models);

            // Adds custom metadata to identify the source filename
            textReader.getCustomMetadata().put("filename", "models.txt");

            // Reads and parses the documents from the resource file
            List<Document> documents = textReader.get();

            // Creates a TokenTextSplitter to break documents into smaller chunks
            TextSplitter textSplitter = new TokenTextSplitter();

            // Applies the text splitter to break documents into manageable pieces
            List<Document> splitDocuments = textSplitter.apply(documents);

            // Adds the split documents to the vector store (this creates embeddings)
            simpleVectorStore.add(splitDocuments);

            // Persists the vector store to disk for future use
            simpleVectorStore.save(vectorStoreFile);
        }
        // Returns the configured vector store instance
        return simpleVectorStore;
    }

    /**
     * Helper method to construct the file path for the vector store persistence file.
     *
     * @return A File object pointing to the vector store location
     */
    private File getVectorStoreFile() {
        // Creates a path pointing to the src/main/resources/data directory
        Path path = Paths.get("src", "main", "resources", "data");
        // Constructs the absolute file path by combining the directory path with the vector store filename
        String absolutePath = path.toFile().getAbsolutePath() + "/" + vectorStoreName;
        // Returns a File object representing the vector store file location
        return new File(absolutePath);
    }
}