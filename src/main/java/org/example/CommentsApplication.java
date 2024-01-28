package org.example;

import org.example.accessor.post.PostAccessor;
import org.example.accessor.post.model.Post;
import org.example.accessor.user.UserAccessorImpl;
import org.example.accessor.user.model.User;
import org.example.dto.CommentRequestDTO;
import org.example.dto.EntityReactRequestDTO;
import org.example.service.CommentService;
import org.example.service.ReactionService;
import org.example.service.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class CommentsApplication {

    private final UserAccessorImpl userAccessor;
    private final PostAccessor postAccessor;
    private final CommentService commentService;

    private final ReactionService reactionService;

    public static void main(String[] args) {
        SpringApplication.run(CommentsApplication.class, args);
        System.out.println("Hello world!");
    }

    @Autowired
    public CommentsApplication(UserAccessorImpl userAccessor, PostAccessor postAccessor,
                               CommentService commentService,
                               ReactionService reactionService) {
        this.userAccessor = userAccessor;
        this.postAccessor = postAccessor;
        this.commentService = commentService;
        this.reactionService = reactionService;

    }

    @Bean
    public void sampleTestCase() throws Exception {

        List<User> userList = userAccessor.bulkCreateUser();

        String userId = userList.get(0).getUserId();
        System.out.println("\nCreating a post by userId " + userId);
        Post post = postAccessor.create(Post.builder().id("b95e9ecf-cf36-45f3-88d9-5304251eed77").addedBy(userId).content("My first post").build());
        System.out.println("Post created is " + post.getId());

        System.out.println("\nUpvoting post: " + post.getId() +" by user: " +userId);
        reactionService.reactOnPost(post.getId(), EntityReactRequestDTO.builder()
                .userId(userList.get(1).getUserId()).reactType("upVote")
                .build());

        System.out.println("\nPut comments and react");

        for (int i = 0; i < userList.size(); i++) {
            Comment comment = commentService.createPostComment(post.getId(), CommentRequestDTO.builder()
                    .userId(userList.get(i).getUserId()).content("Awesome comment on post: " + userList.get(i).getUserId())
                    .build());
            reactionService.reactOnComment(post.getId(), comment.getId(), EntityReactRequestDTO.builder()
                    .userId(userList.get(userList.size()-i-1).getUserId()).reactType("upVote")
                    .build());

            commentService.createCommentReply(post.getId(),comment.getId(), CommentRequestDTO.builder()
                    .userId(userList.get(i).getUserId()).content("Reply comment: " + userList.get(i).getUserId())
                    .build());
        }
    }
}