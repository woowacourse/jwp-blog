package techcourse.myblog.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.User;
import techcourse.myblog.web.dto.CommentDto;
import techcourse.myblog.web.dto.UserDto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CommentServiceTest {
    private static final String EMAIL = "aiden1@naver.com";

    @Autowired
    private CommentService commentService;
    private Comment beforeComment;
    private User user;
    @Autowired
    private UserService userService;

    @BeforeEach
    void setUp() {
        UserDto userDto = new UserDto("aiden",
                EMAIL,
                "aiden3");
        user = userService.save(userDto);
        beforeComment = commentService.save(new CommentDto(user, "brand new comment"));
    }

    @Test
    void 저장_조회_테스트() {
        Comment comment = commentService.save(
                new CommentDto(user, "abc")
        );
        assertThat(commentService.findById(comment.getId()))
                .isEqualTo(comment);
    }

    @Test
    void 수정_테스트() {
        CommentDto newCommentDto = new CommentDto(user, "asdf");
        Comment newComment = commentService.update(beforeComment.getId(), newCommentDto);
        assertThat(newComment.getContents()).isEqualTo("asdf");
    }

    @Test
    void 삭제_테스트() {
        Long beforeCommentId = beforeComment.getId();
        commentService.delete(beforeCommentId);
        assertThatThrownBy(() -> {
            commentService.findById(beforeCommentId);
        }).isInstanceOf(IllegalArgumentException.class);
    }
}