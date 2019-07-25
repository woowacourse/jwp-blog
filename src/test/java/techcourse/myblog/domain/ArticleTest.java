package techcourse.myblog.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ArticleTest {
    private Article article;

    @BeforeEach
    void setUp() {
        article = Article.of("test title", "test coverUrl", "test contents");
    }

    @Test
    @DisplayName("게시물을 업데이트한다.")
    void updateTest() {
        article.updateArticle(Article.of("update title",
                "update coverUrl",
                "update contents")
        );

        assertThat(article.getTitle()).isEqualTo("update title");
        assertThat(article.getCoverUrl()).isEqualTo("update coverUrl");
        assertThat(article.getContents()).isEqualTo("update contents");
    }
}