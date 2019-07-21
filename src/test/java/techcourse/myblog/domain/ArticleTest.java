package techcourse.myblog.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import techcourse.myblog.dto.ArticleDto;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ArticleTest {
    private Article article;

    @BeforeEach
    void setUp() {
        ArticleDto articleDto = new ArticleDto();
        articleDto.setTitle("test title");
        articleDto.setCoverUrl("test coverUrl");
        articleDto.setContents("test contents");

        article = Article.of(articleDto);
    }

    @Test
    @DisplayName("게시물을 업데이트한다.")
    void updateTest() {
        // Given
        ArticleDto articleDto = new ArticleDto();
        articleDto.setTitle("update title");
        articleDto.setCoverUrl("update coverUrl");
        articleDto.setContents("update contents");

        // When
        article.updateArticle(articleDto);

        // Then
        assertThat(article.getTitle()).isEqualTo("update title");
        assertThat(article.getCoverUrl()).isEqualTo("update coverUrl");
        assertThat(article.getContents()).isEqualTo("update contents");
    }
}