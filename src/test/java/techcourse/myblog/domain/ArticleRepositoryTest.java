package techcourse.myblog.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ArticleRepositoryTest {
    ArticleRepository articleRepository;

    @BeforeEach
    void setUp() {
        articleRepository = new ArticleRepository();
    }

    @Test
    void 저장_확인() {
        Article article = new Article("title", "Url", "contents");
        articleRepository.save(article);
        assertThat(articleRepository.findArticleById(0)).isEqualTo(article);
    }

    @Test
    void 삭제_확인() {
        Article article = new Article("title", "Url", "contents");
        articleRepository.save(article);
        articleRepository.removeById(0);
        assertThrows(IllegalArgumentException.class, () -> articleRepository.findArticleById(0));
    }
}