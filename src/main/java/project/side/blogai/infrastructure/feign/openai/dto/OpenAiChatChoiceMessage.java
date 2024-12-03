package project.side.blogai.infrastructure.feign.openai.dto;

public record OpenAiChatChoiceMessage(
        String role,
        String content
) {
}
