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

    @Test
    void 기본_Url_테스트() {
        Article article = new Article("title", "contents", "");
        assertThat(article.getCoverUrl()).isEqualTo("/images/default/bg.jpg");
    }
}