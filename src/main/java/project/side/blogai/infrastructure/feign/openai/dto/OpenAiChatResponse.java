package project.side.blogai.infrastructure.feign.openai.dto;

import java.util.List;

public record OpenAiChatResponse(
        List<OpenAiChatChoiceResponse> choices
) {
    public String getAnswer() {
        if (choices == null || choices.size() == 0) {
            return null;
        }

        return choices.getFirst().message().content();
    }
}
