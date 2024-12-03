package project.side.blogai.infrastructure.feign.openai.dto;

import java.util.List;

public record OpenAiChatRequest(String model, List<OpenAiChatRequestMessage> messages) {

    public static OpenAiChatRequest from(String model, String content) {
        return new OpenAiChatRequest(model, List.of(new OpenAiChatRequestMessage(
                "user",
                content
        )));
    }

    public record OpenAiChatRequestMessage(
            String role,
            String content
    ) {
    }
}
