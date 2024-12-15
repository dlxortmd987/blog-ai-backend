package project.side.blogai.infrastructure.openai;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import project.side.blogai.dto.BlogRequest;
import project.side.blogai.dto.BlogResponse;
import project.side.blogai.service.BlogGenerateService;

@Primary
@Service
public class BlogOpenAiGenerateService implements BlogGenerateService {
    private final ChatClient chatClient;

    public BlogOpenAiGenerateService(ChatClient.Builder clientBuilder) {
        this.chatClient = clientBuilder.build();
    }

    @Override
    public BlogResponse generate(BlogRequest blogRequest) {

        return null;
    }
}
