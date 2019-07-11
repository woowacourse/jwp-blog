package techcourse.myblog.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ArticleRepositoryTest {
    private Article article;

    @BeforeEach
    void setUp() {
        article = new Article("test title", "test contents", "test Url", 1);
    }

    @Test
    @DisplayName("게시물 등록하는 테스트")
    void save() {
        // Given
        ArticleRepository articleRepository = new ArticleRepository();

        // When
        articleRepository.save(article);

        // Then
        assertThat(articleRepository.findAll()).contains(article);
    }

    @Test
    @DisplayName("게시물 id로 게시물을 조회한다.")
    void findTest() {
        ArticleRepository articleRepository = new ArticleRepository();
        articleRepository.save(article);

        assertThat(articleRepository.find(1)).isEqualTo(article);
    }

    @Test
    @DisplayName("게시물 id가 없는 경우 예외를 던져준다.")
    void notFindTest() {
        ArticleRepository articleRepository = new ArticleRepository();
        articleRepository.save(article);

        assertThrows(IllegalArgumentException.class, () -> articleRepository.find(2));
    }
}