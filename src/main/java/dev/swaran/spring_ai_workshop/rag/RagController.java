package dev.swaran.spring_ai_workshop.rag;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RagController {

    private final ChatClient chatClient;

/**
 * Constructor for RagController that initializes the ChatClient with RAG capabilities.
 *
 * The QuestionAnswerAdvisor is a key component that enables Retrieval-Augmented Generation (RAG):
 * - It automatically retrieves relevant documents from the vector store based on the user's question
 * - The retrieved context is then provided to the language model along with the original question
 * - This allows the model to generate more accurate and contextually relevant answers
 * - The advisor acts as a middleware that enhances the chat client's responses with domain-specific knowledge
 *
 * @param builder The ChatClient builder used to configure the chat client
 * @param vectorStore The vector store containing embedded documents for retrieval
 */
public RagController(ChatClient.Builder builder, VectorStore vectorStore){
        this.chatClient = builder
                .defaultAdvisors(QuestionAnswerAdvisor.builder(vectorStore).build())
                .build();
    }
    @GetMapping("/rag/models")
    public Models ragModels(@RequestParam(value="q", defaultValue="Give me all openAI models with their context window size") String question){
        return chatClient.prompt()
                .user(question)
                .call()
                .entity(Models.class);
    }
}