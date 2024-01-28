package org.example.service;

import org.example.accessor.post.PostAccessor;
import org.example.accessor.user.UserAccessorImpl;
import org.example.dto.CommentRequestDTO;
import org.example.dto.CommentResponseDTO;
import org.example.dto.ListCommentsResponseDTO;
import org.example.exceptions.ValidationException;
import org.example.service.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CommentsServiceImpl implements CommentService {
    private final Map<String, Comment> commentMap;

    private final Map<String, Map<String, Set<Comment>>> postCommentMap;

    private final UserAccessorImpl userAccessor;

    private final ReactionService reactionService;

    private final PostAccessor postAccessor;


    @Autowired
    public CommentsServiceImpl(final UserAccessorImpl userAccessor,
                               final ReactionService reactionService,
                               final PostAccessor postAccessor) {
        this.commentMap = new HashMap<>();
        this.userAccessor = userAccessor;
        this.postCommentMap = new HashMap<>();
        this.reactionService = reactionService;
        this.postAccessor = postAccessor;
    }

    @Override
    public Comment createPostComment(final String postId, final CommentRequestDTO commentRequestDTO) {
        userAccessor.validateUser(commentRequestDTO.getUserId());
        postAccessor.validatePost(postId);

        return createComment(postId, postId, commentRequestDTO);
    }

    @Override
    public Comment createCommentReply(final String postId, final String parentId, final CommentRequestDTO commentRequestDTO) {

        userAccessor.validateUser(commentRequestDTO.getUserId());
        postAccessor.validatePost(postId);
        this.validateComment(parentId);

        return createComment(postId, parentId, commentRequestDTO);
    }

    public ListCommentsResponseDTO getPostComments(final String postId,  final long timestamp, final Integer size) {
        // validation
        postAccessor.validatePost(postId);

        return getComments(postId, postId, timestamp, size);
    }

    public ListCommentsResponseDTO getCommentReplies(final String postId, final String parentId,final long timestamp, final Integer size) {
        // validation
        postAccessor.validatePost(postId);
        this.validateComment(parentId);

        return getComments(postId, parentId, timestamp, size);
    }

    private ListCommentsResponseDTO getComments(final String postId, final String parentId, final long timestamp, final Integer size) {
        Map<String, Set<Comment>> allRepliesMap = postCommentMap.getOrDefault(postId, new HashMap<>());
        Set<Comment> allReplies = allRepliesMap.getOrDefault(parentId, new HashSet<>());
        List<Comment> comments = new ArrayList<>(allReplies);
        System.out.println(comments.get(0).getCreatedAt()+ "  "+ timestamp);
        int cursorIndex = timestamp >= 0
                ? findIndexByTimestamp(timestamp, comments)
                : -1;
        List<Comment> paginatedComments = new ArrayList<>();
        System.out.println(cursorIndex);

        if (cursorIndex >= 0 && cursorIndex < allReplies.size()) {
            paginatedComments = comments.subList(cursorIndex , Math.min(cursorIndex + size, comments.size()));
        }
        for(Comment comment: comments) {
            comment.setCommentReactionMap(reactionService.getCommentReactionCount(comment.getId()));
        }

        long timestampCursor = 0;
        if(!paginatedComments.isEmpty()) {
            timestampCursor = paginatedComments.get(paginatedComments.size()-1).getCreatedAt();
        }
        return ListCommentsResponseDTO.builder()
                .comments(paginatedComments.stream()
                        .map(CommentResponseDTO::getEntityResponseDtoFromComment)
                        .collect(Collectors.toList()))
                .cursor(timestampCursor)
                .totalItems(allReplies.size())
                .build();
    }


    private int findIndexByTimestamp(long timestamp, List<Comment> comments) {
        for (int i = 0; i < comments.size(); i++) {
            if (comments.get(i).getCreatedAt() < timestamp) {
                return i;
            }
        }
        return -1;
    }

    private Comment createComment(final String postId, final String parentId, final CommentRequestDTO commentRequestDTO) {
        Comment comment = Comment.builder()
                .id(UUID.randomUUID().toString())
                .content(commentRequestDTO.getContent())
                .addedBy(commentRequestDTO.getUserId())
                .postId(postId)
                .createdAt(System.currentTimeMillis())
                .updatedAt(System.currentTimeMillis())
                .parentId(parentId)
                .upVoteCount(0)
                .downVoteCount(0)
                .commentsCount(0)
                .build();
        commentMap.put(comment.getId(), comment);
        if(commentMap.containsKey(parentId)) {
            commentMap.get(parentId)
                    .setCommentsCount(commentMap.get(parentId).getCommentsCount()+1);
        }

        Map<String, Set<Comment>> commentCommentsMap = postCommentMap.getOrDefault(comment.getPostId(), new HashMap<>());

        Set<Comment> parentComments = commentCommentsMap.getOrDefault(parentId,
                new TreeSet<>(Comparator.comparing(Comment::getCreatedAt, Collections.reverseOrder())));
        parentComments.add(comment);
        commentCommentsMap.put(parentId, parentComments);
        postCommentMap.put(comment.getPostId(), commentCommentsMap);
        return comment;

    }

    private void validateComment(final String commentId) {
        if (!commentMap.containsKey(commentId)) {
            throw new ValidationException(String.format("Post with postId %s is not present.", commentId));
        }
    }
}
