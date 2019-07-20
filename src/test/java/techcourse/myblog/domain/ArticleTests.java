package techcourse.myblog.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import techcourse.myblog.dto.ArticleDto;

import static org.assertj.core.api.Assertions.assertThat;

public class ArticleTests {
    private Article article;

    @BeforeEach
    void setUp() {
        article = new Article(1L,"title", "url", "contents");
    }

    @Test
    void updateArticle() {
        String newTitle = "new Title";
        String newCoverUrl = "new CoverUrl";
        String newContents = "new Contents";
        ArticleDto newArticleDto = new ArticleDto(1L, newTitle, newCoverUrl, newContents);

        article.updateArticle(newArticleDto);

        assertThat(article.getTitle()).isEqualTo(newTitle);
        assertThat(article.getCoverUrl()).isEqualTo(newCoverUrl);
        assertThat(article.getContents()).isEqualTo(newContents);
    }
}
