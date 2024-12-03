package project.side.blogai.infrastructure.webclient.openai;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import project.side.blogai.dto.BlogRequest;
import project.side.blogai.infrastructure.feign.openai.dto.OpenAiChatRequest;
import reactor.core.publisher.Flux;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.List;
import java.util.Map;

@Service
public class OpenAiWebClientBlogGenerateService {
    private WebClient webClient;
    private final ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE );
    private final String openAiApiKey;
    private final String model;

    public OpenAiWebClientBlogGenerateService(
            @Value("${OPEN_AI_API_KEY}") String openAiApiKey,
            @Value("${feign.openai.model}") String model
    ) {
        this.openAiApiKey = openAiApiKey;
        this.model = model;
    }

    @PostConstruct
    public void init(
    ) {
        var client = HttpClient.create().responseTimeout(Duration.ofSeconds(45));

        webClient = WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(client))
                .baseUrl("https://api.openai.com/v1")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public Flux<String> generate(BlogRequest blogRequest) throws JsonProcessingException {
        return webClient.post()
                .uri("/chat/completions")
                .header(HttpHeaders.ACCEPT, MediaType.TEXT_EVENT_STREAM_VALUE)
                .header("Authorization", "Bearer " + openAiApiKey)
                .bodyValue(Map.of(
                        "model", "gpt-4o",
                        "messages", List.of(Map.of(
                                "role", "user",
                                "content", "이 내용을 바탕으로 블로그를 자세하게 작성해줘." + blogRequest.draft()
                        )),
                        "stream", true
                ))
                .accept(MediaType.TEXT_EVENT_STREAM)
                .retrieve()
                .bodyToFlux(String.class)
                .onErrorResume(error -> {
                    if (error.getMessage().contains("JsonToken.START_ARRAY")) {
                        return Flux.empty();
                    }
                    else {
                        return Flux.error(error);
                    }
                })
                .map(chunk -> {
                    if (chunk.startsWith("data: ")) {
                        chunk = chunk.substring(6);
                    }
                    if ("[DONE]".equals(chunk)) {
                        return "";
                    }
                    try {
                        JsonNode node = new ObjectMapper().readTree(chunk);
                        return node.at("/choices/0/delta/content").asText("");
                    } catch (Exception e) {
                        return "";
                    }
                })
                .filter(text -> !text.isEmpty());
    }
}
