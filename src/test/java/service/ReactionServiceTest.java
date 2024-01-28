package service;

import org.example.accessor.post.PostAccessor;
import org.example.accessor.post.model.Post;
import org.example.accessor.user.UserAccessorImpl;
import org.example.accessor.user.model.User;
import org.example.dto.CommentRequestDTO;
import org.example.dto.EntityReactRequestDTO;
import org.example.dto.ListCommentsResponseDTO;
import org.example.service.CommentService;
import org.example.service.CommentsServiceImpl;
import org.example.service.ReactionService;
import org.example.service.ReactionServiceImpl;
import org.example.service.model.Comment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReactionServiceTest {
    CommentService commentService;
    PostAccessor postAccessor;
    UserAccessorImpl userAccessor;

    ReactionService reactionService;

    @BeforeEach
    public void setup(){
        this.userAccessor = new UserAccessorImpl();
        this.postAccessor = new PostAccessor();
        this.reactionService = new ReactionServiceImpl(userAccessor, postAccessor);
        this.commentService = new CommentsServiceImpl(userAccessor, reactionService, postAccessor);

    }


    @Test
    public void successCommentReactionTest() {
        User user = userAccessor.createUser(User.builder()
                .userId("god")
                .userName("god")
                .build());
        User user2 = userAccessor.createUser(User.builder()
                .userId("god2")
                .userName("god2")
                .build());
        Post post = postAccessor.create(Post.builder().id("b95e9ecf-cf36-45f3-88d9-5304251eed77").addedBy(user.getUserId()).content("My first post").build());
        Comment comment  = commentService.createPostComment(post.getId(), CommentRequestDTO.builder().content("new comment").userId(user.getUserId()).build());
        reactionService.reactOnComment(post.getId(), comment.getId(),
                EntityReactRequestDTO.builder().reactType("upVote").userId(user.getUserId()).build());
        reactionService.reactOnComment(post.getId(), comment.getId(),
                EntityReactRequestDTO.builder().reactType("downVote").userId(user.getUserId()).build());

        reactionService.reactOnComment(post.getId(), comment.getId(),
                EntityReactRequestDTO.builder().reactType("downVote").userId(user2.getUserId()).build());

        ListCommentsResponseDTO commentsResponseDTO = commentService.getPostComments(post.getId(),  System.currentTimeMillis()
                , 10);

        List<String> upVoteReactions = reactionService.getUsersForCommentReactType(comment.getId(), "upVote");

        List<String> downVoteReactions = reactionService.getUsersForCommentReactType(comment.getId(), "downVote");
        assertEquals(user.getUserId(), downVoteReactions.get(1));
        assertEquals(0, upVoteReactions.size());

        assertEquals(0, commentsResponseDTO.getComments().get(0).getCommentReactionMap().get("upVote"));
        assertEquals(2, commentsResponseDTO.getComments().get(0).getCommentReactionMap().get("downVote"));


    }

    @Test
    public void successPostReactionTest() {
        User user = userAccessor.createUser(User.builder()
                .userId("god")
                .userName("god")
                .build());
        User user2 = userAccessor.createUser(User.builder()
                .userId("god2")
                .userName("god2")
                .build());
        Post post = postAccessor.create(Post.builder().id("b95e9ecf-cf36-45f3-88d9-5304251eed77").addedBy(user.getUserId()).content("My first post").build());
        reactionService.reactOnPost(post.getId(),
                EntityReactRequestDTO.builder().reactType("upVote").userId(user.getUserId()).build());
        reactionService.reactOnPost(post.getId(),
                EntityReactRequestDTO.builder().reactType("upVote").userId(user2.getUserId()).build());
        List<String> reactions = reactionService.getUsersForPostReactType(post.getId(), "upVote");
        assertEquals(user.getUserId(), reactions.get(1));
        assertEquals(2, reactions.size());
    }
}
