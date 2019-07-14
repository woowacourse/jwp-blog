package techcourse.myblog.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ArticleTest {
    @Test
    void create() {
        Article newArticle = new Article("Title", "CoverUrl", "Contents");
        assertThat(newArticle.getTitle()).isEqualTo("Title");
        assertThat(newArticle.getCoverUrl()).isEqualTo("CoverUrl");
        assertThat(newArticle.getContents()).isEqualTo("Contents");
    }

    @Test
    void checkSameId() {
        Article newArticle = new Article("Title", "CoverUrl", "Contents");
        assertTrue(newArticle.isSameId(newArticle.getId()));
    }

    @Test
    void updateArticle() {
        Article article = new Article("Title", "CoverUrl", "Contents");
        Article after = new Article("AfterTitle", "AfterCoverUrl", "AfterContents");
        article.updateArticle(after);
        assertThat(article.getTitle()).isEqualTo("AfterTitle");
        assertThat(article.getCoverUrl()).isEqualTo("AfterCoverUrl");
        assertThat(article.getContents()).isEqualTo("AfterContents");
    }
}
