package techcourse.myblog.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import techcourse.myblog.service.exception.AuthException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CommentTest {
    User user;
    Comment comment;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .email("email@email.com")
                .password("P@ssw0rd")
                .name("name")
                .build();

        comment = Comment.builder()
                .contents("contents")
                .user(user)
                .build();
    }

    @Test
    void isWrittenBy_본인() {
        assertThat(comment.isWrittenBy(user.getId())).isTrue();
    }

    @Test
    void isWrittenBy_다른_작성자() {
        assertThrows(AuthException.class, () -> comment.isWrittenBy(100L));
    }
}