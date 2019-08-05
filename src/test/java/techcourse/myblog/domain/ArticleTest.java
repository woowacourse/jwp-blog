package techcourse.myblog.domain;

import org.junit.jupiter.api.Test;
import techcourse.myblog.domain.article.Article;
import techcourse.myblog.domain.user.User;

import static org.assertj.core.api.Assertions.assertThat;

public class ArticleTest {

    private User user = new User("andole", "A!1bcdefg", "andole@gmail.com");

    @Test
    void updateTest() {
        Article origin = new Article("title", "contents", "coverUrl", user);
        Article updated = new Article("updateTitle", "updateContents", "updateCoverUrl", user);
        origin.update(updated);
        assertThat(origin).isEqualTo(updated);
    }

    @Test
    void defaultCoverUrlTest() {
        Article article = new Article("title", "contents", "", user);
        assertThat(article.getCoverUrl()).isNotEqualTo("");
    }
}