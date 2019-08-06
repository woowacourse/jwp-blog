package techcourse.myblog.domain.article;

import org.junit.jupiter.api.Test;
import techcourse.myblog.domain.user.User;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ArticleTest {
    static final User user = new User("TEST", "test@test.com", "test1234");
    static final Article articleA = new Article(user, "Title", "Background", "Content");
    static final Article articleB = new Article(user, "afhasr", "asfhsgtAEHs", "");

    @Test
    void isSameAuthorTestA() {
        assertThat(articleA.isSameAuthor(articleB)).isTrue();

    }

    @Test
    void isSameAuthorTestB() {
        assertThat(articleA.isSameAuthor(user)).isTrue();
    }
}