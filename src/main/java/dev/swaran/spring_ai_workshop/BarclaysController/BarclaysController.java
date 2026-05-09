package dev.swaran.spring_ai_workshop.BarclaysController;


import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BarclaysController {


    ChatClient chatClient;

    public BarclaysController(ChatClient.Builder builder){
        this.chatClient = builder.build();
    }

    @GetMapping("/barclays")
    public String getBarclaysInfo(@RequestParam String question)
    {
        //system Instructions are the guardrails to avoid answer non-related topics
        String systemInstructions = "You are a Barclays banking assistant." +
                " Only answer questions related to banking, finance, loans, accounts, credit cards, and related financial topics." +
                " If the user asks anything unrelated to banking, respond with: 'Sorry, I can only answer banking-related questions.'";


        return chatClient.prompt()
                .system(systemInstructions)
                .user(question)
                .call()
                .content();
    };

@GetMapping("/writeArticle")
    public String writeArticle(@RequestParam(value = "topic", defaultValue = "why barclays") String topic)
    {
        String systemInstructions = "You are a professional content writer." +
                " Write in a conversational yet professional tone — like you are explaining to a smart friend over coffee, not lecturing in a boardroom." +
                " Keep the article to approximately 250 words." +
                " Support your points with real-world evidence, statistics, or examples where relevant." +
                " Structure the article with a compelling opening, clear body points, and a strong closing thought." +
                " Avoid jargon — keep it simple, engaging, and easy to read.";

        // This method uses the ChatClient's prompt() method to build a structured AI request
        // The .user() method with lambda expression allows for parameterized prompts
        // u.text("Write an article about {topic}") creates a template with a placeholder {topic}
        // .param("topic", topic) replaces the {topic} placeholder with the actual topic value send by user as request param
        // This approach enables dynamic prompt generation while maintaining clean separation
        // between the prompt template and the variable content
        return chatClient.prompt()
                .system(systemInstructions)
                .user(u -> u.text("Write an article about {topic}").param("topic", topic))
                .call()
                .content();

    };

}
