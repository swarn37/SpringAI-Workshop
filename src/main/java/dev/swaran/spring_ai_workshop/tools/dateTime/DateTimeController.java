package dev.swaran.spring_ai_workshop.tools.dateTime;


import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DateTimeController {

    private final ChatClient chatClient;
    private final DateTimeTool dateTimeTool;

    DateTimeController(ChatClient.Builder builder, DateTimeTool dateTimeTool){
        this.chatClient = builder.build();
        this.dateTimeTool= dateTimeTool;
    }

    @GetMapping("/tools/time")
    public String getTime()
    {
        return chatClient.prompt()
                .user("what time is after 6 hours?")
                .tools(dateTimeTool)
                .call()
                .content();
    }

}
