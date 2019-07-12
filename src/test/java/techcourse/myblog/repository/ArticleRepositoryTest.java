package techcourse.myblog.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import techcourse.myblog.domain.Article;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ArticleRepositoryTest {
    private ArticleRepository articleRepository;
    private Article article = new Article("title", "contents", "coverUrl");

    @BeforeEach
    void setUp() {
        articleRepository = new ArticleRepository();
    }

    @Test
    void saveTest() {
        Article savedArticle = articleRepository.save(article);
        assertThat(articleRepository.findAll().get(0)).isEqualTo(savedArticle);
    }

    @Test
    void findByIdTest() {
        Article savedArticle = articleRepository.save(article);
        assertThat(articleRepository.findById(article.getId())).isEqualTo(savedArticle);
    }

    @Test
    void deleteTest() {
        Article savedArticle = articleRepository.save(article);
        articleRepository.deleteById(savedArticle.getId());
        assertThrows(IllegalArgumentException.class, () -> articleRepository.findById(savedArticle.getId()));
    }
}