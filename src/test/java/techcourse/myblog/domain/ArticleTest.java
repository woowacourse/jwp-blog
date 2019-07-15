package techcourse.myblog.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import techcourse.myblog.web.dto.ArticleDto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ArticleTest {
    private static final int TEST_ARTICLE_ID = 1;
    private Article article;

    @BeforeEach
    void setUp() {
        article = new Article(
                TEST_ARTICLE_ID,
                "test title",
                "test coverUrl",
                "test contents"
        );
    }

    @Test
    @DisplayName("같은 아이디 찾는 테스트")
    void isSameIdTest() {
        assertTrue(article.isSameId(TEST_ARTICLE_ID));
    }

    @Test
    @DisplayName("업데이트하는 테스트")
    void updateArticleTest() {
        ArticleDto articleDto = new ArticleDto();
        articleDto.setTitle("update title");
        articleDto.setCoverUrl("update coverUrl");
        articleDto.setContents("update contents");
        article.updateArticle(articleDto);

        assertThat(article.getTitle()).isEqualTo("update title");
        assertThat(article.getCoverUrl()).isEqualTo("update coverUrl");
        assertThat(article.getContents()).isEqualTo("update contents");

    }
}