package dev.swaran.spring_ai_workshop.ChatMemory;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MemoryController {

    private ChatClient chatClient;
/**
 * Constructor for MemoryController that initializes the ChatClient with memory capabilities.
 * The MessageChatMemoryAdvisor is configured as a default advisor to enable conversation
 * memory functionality, allowing the chat client to remember previous interactions and
 * maintain context across multiple chat exchanges within the same conversation session.
 *
 * @param builder The ChatClient.Builder used to construct the chat client
 * @param memory The ChatMemory instance that stores and retrieves conversation history
 */
public MemoryController(ChatClient.Builder builder, ChatMemory memory){
    this.chatClient =
            builder
                    .defaultAdvisors(MessageChatMemoryAdvisor.builder(memory).build())
                    .build();
}

    @GetMapping("/memory")
    public String chatWithMemory(String message){
        return chatClient.prompt()
                .user(message)
                .call()
                .content();
    }
}
