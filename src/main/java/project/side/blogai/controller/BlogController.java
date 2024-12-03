package project.side.blogai.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.side.blogai.dto.BlogRequest;
import project.side.blogai.dto.BlogResponse;
import project.side.blogai.service.BlogGenerateService;

@RestController
@RequestMapping("/api/blog")
public class BlogController {

    private final BlogGenerateService blogGenerateService;

    public BlogController(BlogGenerateService blogGenerateService) {
        this.blogGenerateService = blogGenerateService;
    }

    @PostMapping("/generate")
    public BlogResponse generateBlog(@RequestBody BlogRequest request) {
        return blogGenerateService.generate(request);
    }
}
