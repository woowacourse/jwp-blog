package techcourse.myblog.articles.comments;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import techcourse.myblog.exception.AuthException;
import techcourse.myblog.users.User;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

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
        assertThat(comment.isWrittenBy(user)).isTrue();
    }

    @Test
    void isWrittenBy_다른_작성자() {
        User anotherUser = User.builder()
                .id(2L)
                .email("email1@email.com")
                .password("P@ssw0rd")
                .name("name")
                .build();

        assertThrows(AuthException.class, () -> comment.isWrittenBy(anotherUser));
    }
}