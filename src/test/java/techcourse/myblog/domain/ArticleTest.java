package techcourse.myblog.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import techcourse.myblog.exception.AuthException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ArticleTest {
    User user;
    Article article;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .password("P@ssw0rd")
                .email("email@gamil.com")
                .name("name")
                .build();

        article = Article.builder()
                .author(user)
                .title("title")
                .contents("contents")
                .coverUrl("coverUrl")
                .build();
    }

    @Test
    void isWrittenBy_본인() {
        assertThat(article.isWrittenBy(user.getId())).isTrue();
    }

    @Test
    void isWrittenBy_다른_작성자() {
        User anotherUser = User.builder()
                .id(2L)
                .email("email1@email.com")
                .password("P@ssw0rd")
                .name("name")
                .build();

        assertThrows(AuthException.class, () -> article.isWrittenBy(anotherUser.getId()));
    }
}
