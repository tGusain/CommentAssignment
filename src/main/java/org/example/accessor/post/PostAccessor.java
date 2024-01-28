package org.example.accessor.post;

import org.example.accessor.post.model.Post;
import org.example.exceptions.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class PostAccessor {
    private final Map<String, Post> postMap;

    @Autowired
    public PostAccessor() {
        this.postMap = new HashMap<>();
    }
    public Post create(Post post)  {
        postMap.put(post.getId(), post);
        return post;
    }

    public void validatePost(String postId) {
        if (!postMap.containsKey(postId)) {
            throw new ValidationException(String.format("Post with postId %s is not present.", postId));
        }
    }
}
