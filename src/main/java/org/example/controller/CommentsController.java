package org.example.controller;

import lombok.extern.log4j.Log4j2;
import org.example.dto.CommentRequestDTO;
import org.example.dto.CommentResponseDTO;
import org.example.dto.ListCommentsResponseDTO;
import org.example.dto.ResponseDTO;
import org.example.exceptions.ValidationException;
import org.example.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/posts/{postId}")
@Log4j2
public class CommentsController {

    private final CommentService commentService;

    @Autowired
    public CommentsController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/comment")
    public ResponseDTO<CommentResponseDTO> commentOnPost(@PathVariable String postId,
            @RequestBody CommentRequestDTO commentRequestDto) {

        return new ResponseDTO<>(
                CommentResponseDTO.getEntityResponseDtoFromComment(
                        commentService.createPostComment(postId, commentRequestDto)
                )
        );
    }

    @GetMapping("/comments")
    public ResponseDTO<ListCommentsResponseDTO> getPostComments(@PathVariable String postId,
                                                                                @RequestParam(value = "size", defaultValue = "10", required = false) int size,
                                                                                @RequestParam(value = "cursor", defaultValue = "0", required = false) long cursor) {

        if(cursor == 0) {
            cursor = System.currentTimeMillis();
        }

        return new ResponseDTO<>(
                commentService.getPostComments(postId, cursor, size)
               );
    }

    @PostMapping("/comments/{commentId}/reply")
    public ResponseDTO<CommentResponseDTO> replyOnComment(@PathVariable String postId,
                                                          @PathVariable String commentId,
                                                          @RequestBody CommentRequestDTO commentRequestDto) {

        return new ResponseDTO<>(
                CommentResponseDTO.getEntityResponseDtoFromComment(
                        commentService.createCommentReply(postId, commentId, commentRequestDto)
                )
        );
    }

    @GetMapping("/comments/{commentId}/replies")
    public ResponseDTO<ListCommentsResponseDTO> getCommentReply(@PathVariable String postId,
                                                            @PathVariable String commentId,
                                                            @RequestParam(value = "size", defaultValue = "10", required = false) int size,
                                                            @RequestParam(value = "cursor", defaultValue = "0", required = false) long cursor) {

        if(cursor == 0) {
            cursor = System.currentTimeMillis();
        }

        return new ResponseDTO<>(commentService.getCommentReplies(postId, commentId, cursor, size));
        /*
        try {
            return new ResponseDTO<>(commentService.getCommentReplies(postId, commentId, cursor, size);
            return ResponseEntity.ok(new ResponseDTO<>(commentService.getCommentReplies(postId, commentId, cursor, size)));
            // return new ResponseDTO<>(reactionService.getUsersForPostReactType(postId, reactType));

        } catch (ValidationException e) {
            log.error("Error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(new ResponseDTO<>(false,)));
        }

         */

    }
}
