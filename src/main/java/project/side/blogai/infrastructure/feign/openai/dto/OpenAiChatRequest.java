package project.side.blogai.infrastructure.feign.openai.dto;

import java.util.List;

public record OpenAiChatRequest(String model, List<OpenAiChatRequestMessage> messages) {
    public static OpenAiChatRequest of(String model, List<OpenAiChatRequestMessage> messages) {
        return new OpenAiChatRequest(model, messages);
    }

    public record OpenAiChatRequestMessage(
            String role,
            String content
    ) {
    }
}
