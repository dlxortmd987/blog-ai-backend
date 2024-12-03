package project.side.blogai.infrastructure.feign.openai;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import project.side.blogai.infrastructure.feign.openai.dto.OpenAiChatRequest;
import project.side.blogai.infrastructure.feign.openai.dto.OpenAiChatResponse;

@FeignClient(value = "OpenAiFeignClient", url = "${feign.openai.url}")
public interface OpenAiFeignClient {
    @PostMapping("/chat/completions")
    OpenAiChatResponse chat(
            @RequestHeader("Authorization") String authorization,
            @RequestBody OpenAiChatRequest request
    );
}
