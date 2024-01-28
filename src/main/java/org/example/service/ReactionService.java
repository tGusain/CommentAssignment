package org.example.service;

import org.example.dto.EntityReactRequestDTO;

import java.util.List;
import java.util.Map;

public interface ReactionService {
    public boolean reactOnPost(String postID, EntityReactRequestDTO reactRequestDTO);
    public List<String> getUsersForPostReactType(String postId, String reactType);
    public boolean reactOnComment(String postId, String commentId, EntityReactRequestDTO reactRequestDTO);
    public Map<String, Integer> getCommentReactionCount(String commentId);
    public List<String> getUsersForCommentReactType(String commentId, String reactType);

}
