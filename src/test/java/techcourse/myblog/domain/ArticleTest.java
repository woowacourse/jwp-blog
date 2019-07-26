package techcourse.myblog.domain;

import org.junit.jupiter.api.Test;
import techcourse.myblog.domain.article.Article;

import static org.assertj.core.api.Assertions.assertThat;

public class ArticleTest {
    @Test
    void updateTest() {
        Article origin = new Article("title", "contents", "coverUrl");
        Article updated = new Article("updateTitle", "updateContents", "updateCoverUrl");
        origin.update(updated);
        assertThat(origin).isEqualTo(updated);
    }
}