package project.side.blogai.dto;

import project.side.blogai.model.ContentType;

public record BlogRequest(
        String draft,
        ContentType contentType
) {
}
