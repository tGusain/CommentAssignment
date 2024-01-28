package org.example.controller;

import org.example.dto.EntityReactRequestDTO;
import org.example.dto.ResponseDTO;
import org.example.service.ReactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/posts/{postId}")
public class ReactionController {

    ReactionService reactionService;


    @Autowired
    public ReactionController(ReactionService reactionService) {
        this.reactionService = reactionService;
    }

    // React on a comment
    @PostMapping("/react")
    public ResponseDTO<String> reactOnPost(@PathVariable String postId,
                                           @RequestBody EntityReactRequestDTO entityReactRequestDTO) {
        reactionService.reactOnPost(postId, entityReactRequestDTO);
        return new ResponseDTO<>("Succesfully Reacted");
    }

    // Get user reaction on a post
    @GetMapping("/react/{reactType}/users")
    public ResponseDTO<List<String>> getUsersForPostReactType(@PathVariable String postId, @PathVariable String reactType) {
        return new ResponseDTO<>(reactionService.getUsersForPostReactType(postId, reactType));
    }


    // React on a comment
    @PostMapping("/comment/{commentId}/react")
    public ResponseDTO<String> reactOnComment(@PathVariable String postId,
                                           @PathVariable String commentId,
                                           @RequestBody EntityReactRequestDTO entityReactRequestDTO) {
        reactionService.reactOnComment(postId, commentId, entityReactRequestDTO);
        return new ResponseDTO<>("Succesfully Reacted");
    }


    // Get user reaction on a comment
    @GetMapping("comment/{commentId}/react/{reactType}/users")
    public ResponseDTO<List<String>> getUsersForReactType(@PathVariable String commentId, @PathVariable String reactType) {
        return new ResponseDTO<>(reactionService.getUsersForCommentReactType(commentId, reactType));
    }
}
