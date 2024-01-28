package org.example.service.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Builder
@Setter
public class Comment {

    private String id;
    private String content;
    private Long createdAt;
    private Long updatedAt;
    private String addedBy;
    private Integer commentsCount;
    private String parentId;
    private String postId;
    private Integer upVoteCount;
    private Integer downVoteCount;
    private Map<String, Integer> commentReactionMap;

}
