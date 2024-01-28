package service;

import org.example.accessor.post.PostAccessor;
import org.example.accessor.post.model.Post;
import org.example.accessor.user.UserAccessorImpl;
import org.example.accessor.user.model.User;
import org.example.dto.CommentRequestDTO;
import org.example.dto.ListCommentsResponseDTO;
import org.example.exceptions.ValidationException;
import org.example.service.CommentService;
import org.example.service.CommentsServiceImpl;
import org.example.service.ReactionService;
import org.example.service.ReactionServiceImpl;
import org.example.service.model.Comment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CommentServiceTest {
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
    public void successPostCommentTest() throws Exception {
        User user = userAccessor.createUser(User.builder()
                .emailId("god@gmail.com")
                .userId("god")
                .userName("god")
                .build());
        Post post = postAccessor.create(Post.builder().id("b95e9ecf-cf36-45f3-88d9-5304251eed77").addedBy(user.getUserId()).content("My first post").build());
        Comment comment  = commentService.createPostComment(post.getId(), CommentRequestDTO.builder().content("new comment").userId(user.getUserId()).build());
        Thread.sleep(100);
        ListCommentsResponseDTO commentsResponseDTO = commentService.getPostComments(post.getId(),  System.currentTimeMillis()
, 10);
        assertEquals(1, commentsResponseDTO.getTotalItems());
        assertEquals(comment.getId(), commentsResponseDTO.getComments().get(0).getId());
    }

    @Test
    public void successCommentReplyTest() throws Exception {
        User user = userAccessor.createUser(User.builder()
                .emailId("god@gmail.com")
                .userId("god")
                .userName("god")
                .build());
        Post post = postAccessor.create(Post.builder().id("b95e9ecf-cf36-45f3-88d9-5304251eed77").addedBy(user.getUserId()).content("My first post").build());
        Comment parentComment  = commentService.createPostComment(post.getId(), CommentRequestDTO.builder().content("new comment").userId(user.getUserId()).build());
        Thread.sleep(100);

        Comment commentReply = commentService.createCommentReply(post.getId(), parentComment.getId(),
                CommentRequestDTO.builder().content("new comment reply").userId(user.getUserId()).build());
        Thread.sleep(100);
        ListCommentsResponseDTO commentsResponseDTO = commentService.getCommentReplies(post.getId(), parentComment.getId(),  System.currentTimeMillis()
                , 10);
        assertEquals(1, commentsResponseDTO.getTotalItems());
        assertEquals(commentReply.getId(), commentsResponseDTO.getComments().get(0).getId());
    }

    @Test
    public void invalidPostFailureTest()  {
        User user = userAccessor.createUser(User.builder()
                .emailId("god@gmail.com")
                .userId("god")
                .userName("god")
                .build());

        assertThrows(ValidationException.class,()->{
            commentService.createPostComment(
                    "SDS",
                    CommentRequestDTO.builder().content("new comment").userId(user.getUserId()).build());
        });
    }

    @Test
    public void invalidUserFailureTest()  {
        User user = userAccessor.createUser(User.builder()
                .emailId("god@gmail.com")
                .userId("god")
                .userName("god")
                .build());
        Post post = postAccessor.create(Post.builder().id("b95e9ecf-cf36-45f3-88d9-5304251eed77").addedBy(user.getUserId()).content("My first post").build());

        assertThrows(ValidationException.class,()->{
            commentService.createPostComment(
                    post.getId(),
                    CommentRequestDTO.builder().content("new comment").userId("SD").build());
        });
    }

    @Test
    public void invalidParentCommentFailureTest()  {
        User user = userAccessor.createUser(User.builder()
                .emailId("god@gmail.com")
                .userId("god")
                .userName("god")
                .build());
        Post post = postAccessor.create(Post.builder().id("b95e9ecf-cf36-45f3-88d9-5304251eed77").addedBy(user.getUserId()).content("My first post").build());
        assertThrows(ValidationException.class,()->{
            commentService.createCommentReply(
                    post.getId(),
                    "SD",
                    CommentRequestDTO.builder().content("new comment").userId(user.getUserId()).build());
        });
    }
}
