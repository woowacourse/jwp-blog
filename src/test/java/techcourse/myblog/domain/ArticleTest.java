package techcourse.myblog.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import techcourse.myblog.dto.ArticleDto;

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
        ArticleDto articleDto = new ArticleDto("new title", "new coverUrl", "new contents");
        article.update(articleDto);
        assertThat(article.getTitle().equals(articleDto.getTitle()));
        assertThat(article.getCoverUrl().equals(articleDto.getCoverUrl()));
        assertThat(article.getContents().equals(articleDto.getContents()));
    }

    @Test
    void increase_id_test() {
        Article secondArticle = new Article(title, coverUrl, contents);
        assertThat(secondArticle.getId()).isEqualTo(secondArticle.getId());
    }
}
