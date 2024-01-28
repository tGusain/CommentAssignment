package org.example.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.service.model.Comment;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentResponseDTO {
    private String id;
    private String content;
    private Long createdAt;
    private Long updatedAt;
    private String userId;
    private long commentsCount;
    private long upvoteCount;
    private long downVoteCount;
    private String postId;
    private String parentId;
    private Map<String, Integer> commentReactionMap;

    public static CommentResponseDTO getEntityResponseDtoFromComment(Comment comment) {
        return CommentResponseDTO.builder()
                .id(comment.getId())
                .postId(comment.getPostId())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .content(comment.getContent())
                .userId(comment.getAddedBy())
                .commentsCount(comment.getCommentsCount())
                .commentReactionMap(comment.getCommentReactionMap())
                .parentId(comment.getParentId() == null ? comment.getPostId() : comment.getParentId())
                .build();
    }
}