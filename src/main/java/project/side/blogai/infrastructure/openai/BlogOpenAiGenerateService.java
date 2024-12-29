package project.side.blogai.infrastructure.openai;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import project.side.blogai.dto.BlogRequest;
import project.side.blogai.dto.BlogResponse;
import project.side.blogai.model.ContentType;
import project.side.blogai.service.BlogGenerateService;

@Service
public class BlogOpenAiGenerateService implements BlogGenerateService {
    private final ChatClient chatClient;

    public BlogOpenAiGenerateService(ChatClient.Builder clientBuilder) {
        this.chatClient = clientBuilder.build();
    }

    @Override
    public BlogResponse generate(BlogRequest blogRequest) {
        String content = chatClient.prompt()
                .system(getSystemMessage(blogRequest.contentType()))
                .user(getUserMessage(blogRequest.draft()))
                .call()
                .content();

        return new BlogResponse(content);
    }

    private String getUserMessage(String draft) {
        return """
                    Write a blog post based on given draft message.
                    Write it richly and in detail.
                    Write in Korean.
                    [Draft Message]: %s
                """.formatted(draft);
    }

    private String getSystemMessage(ContentType contentType) {
        return switch (contentType) {
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
