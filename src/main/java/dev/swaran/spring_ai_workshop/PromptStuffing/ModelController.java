package dev.swaran.spring_ai_workshop.PromptStuffing;


import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ModelController {

    public ChatClient chatClient;

    public ModelController(ChatClient.Builder builder){
        this.chatClient = builder.build();
    }

    /**
     * Retrieves information about the top 5 LLM models with their token limitations.
     * This method demonstrates a basic chat client interaction without any prompt stuffing.
     * The AI model will respond based on its training data without additional context.
     *
     * @return A string containing information about top 5 LLM models and their context window token limits
     */
    @GetMapping("/model")
        public String getModelData()
        {
            return chatClient
                    .prompt()
                    .user("give top 5 best LLM models with theirt token limitation in a context window")
                    .call()
                    .content();
        }

/**
 * Retrieves information about the top 5 LLM models using prompt stuffing technique.
 * This method demonstrates prompt stuffing by providing reference data in the system prompt
 * to guide the AI model's response with specific, predefined information about LLM models.
 * The system prompt contains structured data about model capabilities and performance metrics
 * which ensures more consistent and accurate responses compared to relying solely on training data.
 *
 * @return A string containing information about top 5 LLM models based on the stuffed prompt data
 */
@GetMapping("/model/stuffing-prompt")
public String getModelDataAfterStuffing()
{

    var promptStuffing = """
        Reference Data for LLM Models:
        
        1. GPT-4 Turbo: 128,000 tokens context window, response time ~2-5 seconds
        2. Claude-3 Opus: 200,000 tokens context window, response time ~3-6 seconds
        3. Gemini Pro: 32,000 tokens context window, response time ~1-3 seconds
        4. GPT-3.5 Turbo: 16,385 tokens context window, response time ~1-2 seconds
        5. LLaMA 2 70B: 4,096 tokens context window, response time ~2-4 seconds
        
        """;


    return chatClient
            .prompt()
            .system(promptStuffing)
            .user("give top 5 best LLM models with their token limitation in a context window")
            .call()
            .content();
}
}