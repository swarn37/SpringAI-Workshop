package dev.swaran.spring_ai_workshop.Multimodal;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.core.io.Resource;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class ImageExplaination {

    ChatClient chatclient ;
    /**
     * Constructor to initialize the ImageExplaination controller.
     *
     * @param builder ChatClient builder to create the chat client instance
     */
    ImageExplaination(ChatClient.Builder builder){
        this.chatclient = builder.build();
    }
    /** Image resource loaded from classpath */
    @Value("classpath:/images/marvels-spider-man-miles-morales.jpg")
    Resource image;



    /**
     * Explains what is happening in the loaded image using AI analysis.
     *
     * @return String description of the image contents
     */
    @GetMapping("/image-explain")
    public String explainImage(){
        var prompt = """
                Explain what is happening in this image
                """;
        return chatclient.prompt()
                .user(u -> {
                    u.text(prompt);
                    u.media(MimeTypeUtils.IMAGE_JPEG, image);
                        }
                )
                .call()
                .content();
    }
}