package techcourse.myblog.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ArticleTest {
    private String title = "title";
    private String coverUrl = "coverUrl";
    private String contents = "contents";
    private Article article;

    @BeforeEach
    void setUp() {
        article = new Article(title, coverUrl, contents);
    }

    @Test
    void update() {
        ArticleVO articleVO = new ArticleVO("new title", "new coverUrl", "new contents");
        article.update(articleVO);
        assertThat(article.getTitle().equals(articleVO.getTitle()));
        assertThat(article.getCoverUrl().equals(articleVO.getCoverUrl()));
        assertThat(article.getContents().equals(articleVO.getContents()));
    }

    @Test
    void increase_articleId_test() {
        Article secondArticle = new Article(title, coverUrl, contents);
        assertThat(secondArticle.getArticleId()).isEqualTo(secondArticle.getArticleId());
    }
}
