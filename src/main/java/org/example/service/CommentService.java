package org.example.service;

import org.example.dto.CommentRequestDTO;
import org.example.dto.ListCommentsResponseDTO;
import org.example.service.model.Comment;

import java.util.List;

public interface CommentService {
    Comment createPostComment(String postId, CommentRequestDTO commentRequestDTO);

    Comment createCommentReply(String postId, String commentID, CommentRequestDTO commentRequestDTO);

    ListCommentsResponseDTO getPostComments(final String postId, final long cursor, final Integer size);

    ListCommentsResponseDTO getCommentReplies(final String postId, final String parentId, final long cursor, final Integer size);

}