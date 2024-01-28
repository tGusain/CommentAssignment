package org.example.accessor.post.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
public class Post {
    private String id;
    private String content;
    private Long createdAt;
    private Long updatedAt;
    private String addedBy;
    @Setter
    private long commentsCount;
}
