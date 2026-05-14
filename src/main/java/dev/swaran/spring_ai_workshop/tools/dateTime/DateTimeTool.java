package dev.swaran.spring_ai_workshop.tools.dateTime;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

@Component
public class DateTimeTool {

    @Tool(description="Get the time of user's timezone in 12 hr format")
    public String getCurrentTime() {
        return "The current time is " + java.time.LocalTime.now();
    }
}
