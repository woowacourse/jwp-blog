package techcourse.myblog.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import techcourse.myblog.service.exception.InvalidAuthorException;

import static org.assertj.core.api.Assertions.*;

public class ArticleTest {

    private User user;
    private Article article;

    @BeforeEach
    void setUp() {
        user = new User("pobi", "pobi@woowa.com", "PobiPobi1!");
        article = new Article("test title", "coverUrl test", "content test", user);
    }

    @Test
    void 게시글_정상_생성() {
        assertThatCode(() -> new Article("test title", "coverUrl test", "content test"))
                .doesNotThrowAnyException();
    }

    @Test
    void 일치하지_않는_작성자() {
        User anotherUser = new User("bopi", "bopi@woowa.com", "BopiBopi1!");
        assertThatThrownBy(() -> article.checkAuthor(anotherUser)).isInstanceOf(InvalidAuthorException.class);
    }

    @Test
    void 작성자_일치() {
        assertThatCode(() -> article.checkAuthor(user)).doesNotThrowAnyException();
    }
}
