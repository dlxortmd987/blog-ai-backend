package project.side.blogai.infrastructure.feign.openai;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import project.side.blogai.dto.BlogRequest;
import project.side.blogai.dto.BlogResponse;
import project.side.blogai.infrastructure.feign.openai.dto.OpenAiChatRequest;
import project.side.blogai.infrastructure.feign.openai.dto.OpenAiChatResponse;
import project.side.blogai.model.ContentType;
import project.side.blogai.service.BlogGenerateService;

import java.util.List;

@Service
public class OpenAiBlogGenerateService implements BlogGenerateService {
    private static final String SYSTEM_ROLE = "system";
    private static final String USER_ROLE = "user";
    private final String openAiApiKey;
    private final String model;
    private final OpenAiFeignClient openAiFeignClient;

    public OpenAiBlogGenerateService(
            @Value("${OPEN_AI_API_KEY}") String openAiApiKey,
            @Value("${feign.openai.model}") String model,
            OpenAiFeignClient openAiFeignClient
    ) {
        this.openAiApiKey = "Bearer " + openAiApiKey;
        this.model = model;
        this.openAiFeignClient = openAiFeignClient;
    }

    @Override
    public BlogResponse generate(BlogRequest blogRequest) {
        OpenAiChatResponse response = openAiFeignClient.chat(
                openAiApiKey,
                makePrompt(blogRequest)
        );
        return mapFrom(response);
    }

    private BlogResponse mapFrom(OpenAiChatResponse response) {
        return new BlogResponse(response.getAnswer());
    }

    private OpenAiChatRequest makePrompt(BlogRequest blogRequest) {
        OpenAiChatRequest.OpenAiChatRequestMessage systemMessage = new OpenAiChatRequest.OpenAiChatRequestMessage(
                SYSTEM_ROLE,
                makeSystemMessage(blogRequest.contentType())
        );
        OpenAiChatRequest.OpenAiChatRequestMessage userMessage = new OpenAiChatRequest.OpenAiChatRequestMessage(
                USER_ROLE,
                makeUserMessage(blogRequest.draft())
        );
        return OpenAiChatRequest.of(
                model,
                List.of(systemMessage, userMessage)
        );
    }

    private String makeUserMessage(String draft) {
        return """
                    Write a blog post based on given draft message.
                    Write it richly and in detail.
                    Write in Korean.
                    [Draft Message]: %s
                """.formatted(draft);
    }

    private String makeSystemMessage(ContentType contentType) {
        return switch (contentType) {
            case TRAVEL -> """
                        You are a power blogger who runs travel blog.
                        You can use phrases, emoticons, and hashtags that fit trend.
                    """;
            case RESTAURANT -> """
                        You are a power blogger who runs restaurant blog.
                        You can use phrases, emoticons, and hashtags that fit trend.
                    """;
            case PROGRAMMING -> """
                        You are a power blogger who runs programming blog.
                        You can use phrases, emoticons, and hashtags that fit trend.
                    """;
            default -> throw new IllegalStateException("Unexpected value: " + contentType);
        };
    }
}
