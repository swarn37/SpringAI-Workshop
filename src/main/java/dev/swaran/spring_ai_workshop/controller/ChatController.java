package dev.swaran.spring_ai_workshop.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class ChatController {

    private ChatClient chatClient;

    /**
     *    client to communicate with LLM using default DefaultChatClientBuilder in case of single LLM model
     */
    public ChatController(ChatClient.Builder builder){
        this.chatClient = builder.build();
    }

    /**
     * Simple chat endpoint that returns only the content string from the LLM response.
     * Use this method when you only need the text content and don't require metadata like tokens or model info.
     * @return String content from the LLM response
     */
    @GetMapping("/chat")
    public String chat(){

        return chatClient.prompt()
                .user("Tell me an interesting insight about java")
                .call()
                .content();
    }

    /**
     * Streaming chat endpoint that returns content as a reactive stream.
     * Use this method for real-time streaming responses, especially useful for long responses
     * where you want to display content as it's being generated rather than waiting for completion.
     * @return Flux<String> stream of content chunks from the LLM
     */
    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamedChat()
    {
        return chatClient.prompt()
                .user("Write a java code to print fibonacci series")
                .stream()
                .content();
    }

    /**
     * Complete response endpoint that returns the full ChatResponse object.
     * Use this method when you need access to metadata like token usage, model information,
     * finish reason, or other response details beyond just the content.
     * @return ChatResponse complete response object with metadata
     */
    @GetMapping("/complete")
    public ChatResponse completeResponse()
    {
        return chatClient.prompt()
                .user("Tell me a dad joke")
                .call()
                .chatResponse();
    }
}