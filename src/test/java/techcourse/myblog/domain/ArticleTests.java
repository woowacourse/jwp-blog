package techcourse.myblog.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ArticleTests {
    private Article article;

    @BeforeEach
    void setUp() {
        Article.initializeNextId();
        article = new Article("title", "url", "contents");
    }

    @Test
    public void ID_증가_확인() {
        Article newArticle = new Article("title1", "url1", "contents1");

        assertThat(newArticle.getId()).isEqualTo(1);
    }

    @Test
    void matchId() {
        assertTrue(article.matchId(0));
    }

    @Test
    void updateArticle() {
        String newTitle = "new Title";
        String newCoverUrl = "new CoverUrl";
        String newContents = "new Contents";

        article.updateArticle(newTitle, newCoverUrl, newContents);

        assertThat(article.getTitle()).isEqualTo(newTitle);
        assertThat(article.getCoverUrl()).isEqualTo(newCoverUrl);
        assertThat(article.getContents()).isEqualTo(newContents);
    }
}
