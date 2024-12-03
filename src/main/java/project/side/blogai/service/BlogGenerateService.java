package project.side.blogai.service;

import project.side.blogai.dto.BlogRequest;
import project.side.blogai.dto.BlogResponse;

public interface BlogGenerateService {
    BlogResponse generate(BlogRequest blogRequest);
}
