package dev.swaran.spring_ai_workshop.StructuredOutput;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VacationPlan {

    ChatClient chatClient ;

/**
 * Constructor for VacationPlan class.
 * Initializes the ChatClient using the provided builder.
 * @param builder ChatClient.Builder instance used to build the ChatClient
 */
public VacationPlan(ChatClient.Builder builder){
    this.chatClient = builder.build();
}

/**
 * Generates an unstructured vacation plan for Spain as a plain text string.
 * Uses ChatClient to send a prompt requesting vacation planning information.
 * @return String containing the vacation plan in unstructured text format
 */
@GetMapping("/vacation/unstructured")
public String unstructuredOutput() {
    return chatClient.prompt()
            .user("Plan a vacation for me to Spain.Include the things to Do")
            .call()
            .content();
}

/**
 * Generates a structured vacation plan for Spain as an Itinerary object.
 * Uses ChatClient to send a prompt and converts the response to a structured Itinerary entity.
 * @return Itinerary object containing the structured vacation plan data
 */
@GetMapping("/vacation/structured")
public Itinerary structuredOutput() {
    return chatClient.prompt()
            .user("Plan a vacation for me to Spain.Include the things to Do")
            .call()
            .entity(Itinerary.class);
}
}
