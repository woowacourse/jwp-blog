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

@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CommentServiceTest {

    @Autowired
    private CommentService commentService;
    private Comment beforeComment;
    private User user;
    private UserDto userDto;
    @Autowired
    private UserService userService;

    @BeforeEach
    void setUp() {
        userDto = new UserDto("aiden",
                "aiden1@naver.com",
                "aiden3");
        user = userService.save(userDto);
    }

    @Test
    void 저장_조회_테스트() {
        Comment comment = commentService.save(
                new CommentDto(user, "abc")
        );
        assertThat(commentService.findById(comment.getId()))
                .isEqualTo(comment);
    }
}