package project.side.blogai.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.side.blogai.dto.BlogRequest;
import project.side.blogai.infrastructure.webclient.openai.OpenAiWebClientBlogGenerateService;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/blog")
public class BlogController {

    private final OpenAiWebClientBlogGenerateService blogGenerateService;

    public BlogController(OpenAiWebClientBlogGenerateService blogGenerateService) {
        this.blogGenerateService = blogGenerateService;
    }

    @PostMapping(value = "/generate/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> generateBlog(@RequestBody BlogRequest request) throws JsonProcessingException {
        return blogGenerateService.generate(request);
    }
}
