package project.side.blogai.infrastructure.feign.openai;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import project.side.blogai.dto.BlogRequest;
import project.side.blogai.dto.BlogResponse;
import project.side.blogai.infrastructure.feign.openai.dto.OpenAiChatRequest;
import project.side.blogai.infrastructure.feign.openai.dto.OpenAiChatResponse;
import project.side.blogai.service.BlogGenerateService;

@Service
public class OpenAiBlogGenerateService implements BlogGenerateService {
    private final String openAiApiKey;
    private final String model;
    private final OpenAiFeignClient openAiFeignClient;

    public OpenAiBlogGenerateService(
            @Value("${OPEN_AI_API_KEY}") String openAiApiKey,
            @Value("${feign.openai.model}") String model,
            OpenAiFeignClient openAiFeignClient
    ) {
        this.openAiApiKey = openAiApiKey;
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
        String template = "이 내용을 바탕으로 블로그를 자세하게 작성해줘." + blogRequest.draft();
        return OpenAiChatRequest.from(model, template);
    }
}
