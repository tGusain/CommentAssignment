package org.example.service;

import org.example.accessor.post.PostAccessor;
import org.example.accessor.user.UserAccessorImpl;
import org.example.dto.EntityReactRequestDTO;
import org.example.service.model.enums.ReactType;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class ReactionServiceImpl implements ReactionService {

    Map<String, Set<String>> postReactTypeUserMap = new HashMap<>();

    Map<String, Set<String>> commentReactTypeUserMap = new HashMap<>();

    UserAccessorImpl userAccessor;
    PostAccessor postAccessor;

    public ReactionServiceImpl(UserAccessorImpl userAccessor, PostAccessor postAccessor) {
        this.postAccessor = postAccessor;
        this.userAccessor = userAccessor;
    }
    @Override
    public boolean reactOnPost(String postID, EntityReactRequestDTO reactRequestDTO) {
        ReactType.validateReactType(reactRequestDTO.getReactType());
        userAccessor.validateUser(reactRequestDTO.getUserId());
        postAccessor.validatePost(postID);
        Set<String> commentReactUserList = postReactTypeUserMap.getOrDefault(
                getReactKey(reactRequestDTO.getReactType(), postID), new HashSet<>());
        commentReactUserList.add(reactRequestDTO.getUserId());
        postReactTypeUserMap.put(getReactKey(reactRequestDTO.getReactType(), postID), commentReactUserList);

        for(String reactKey : postReactTypeUserMap.keySet()) {
            if(reactKey.equals(getReactKey(reactRequestDTO.getReactType(), postID))) {
                continue;
            }
            Set<String> userSet = postReactTypeUserMap.get(reactKey);
            if(!CollectionUtils.isEmpty(userSet)) {
                userSet.remove(reactRequestDTO.getUserId());
            }
        }

        return true;
    }

    @Override
    public List<String> getUsersForPostReactType(String postId, String reactType) {
        ReactType.validateReactType(reactType);
        postAccessor.validatePost(postId);
        String reactKey = getReactKey(reactType, postId);
        if(!postReactTypeUserMap.containsKey(reactKey)) {
            return new ArrayList<>();
        }
        return new ArrayList<>(postReactTypeUserMap.get(reactKey));
    }

    public Map<String, Integer> getCommentReactionCount(String commentId) {
        Map<String, Integer> reactionMap = new HashMap<>();
        for(ReactType type : ReactType.values()) {
            String reactKey = getReactKey(type.getVal(), commentId);
            if(commentReactTypeUserMap.containsKey(reactKey)) {
                reactionMap.put(type.getVal(), commentReactTypeUserMap.get(reactKey).size());
            }
        }
        return reactionMap;
    }
    @Override
    public boolean reactOnComment(String postId, String commentId, EntityReactRequestDTO reactRequestDTO) {
        ReactType.validateReactType(reactRequestDTO.getReactType());
        postAccessor.validatePost(postId);
        userAccessor.validateUser(reactRequestDTO.getUserId());
        Set<String> commentReactUserList = commentReactTypeUserMap.getOrDefault(getReactKey(reactRequestDTO.getReactType(), commentId), new HashSet<>());
        commentReactUserList.add(reactRequestDTO.getUserId());
        commentReactTypeUserMap.put(getReactKey(reactRequestDTO.getReactType(), commentId), commentReactUserList);
        for(String reactKey : commentReactTypeUserMap.keySet()) {
            if(reactKey.equals(getReactKey(reactRequestDTO.getReactType(), commentId))) {
                continue;
            }
            Set<String> userSet = commentReactTypeUserMap.get(reactKey);
            if(!CollectionUtils.isEmpty(userSet)) {
                userSet.remove(reactRequestDTO.getUserId());
            }
        }

        return true;
    }

    @Override
    public List<String> getUsersForCommentReactType(String commentId, String reactType) {
        ReactType.validateReactType(reactType);
        String reactKey = getReactKey(reactType, commentId);
        if(!commentReactTypeUserMap.containsKey(reactKey)) {
            return new ArrayList<>();
        }
        return new ArrayList<>(commentReactTypeUserMap.get(getReactKey(reactType, commentId)));
    }

    private String getReactKey(String reactType, String entityId) {
        String sb = reactType +
                "_" +
                entityId;
        return sb;
    }
}
