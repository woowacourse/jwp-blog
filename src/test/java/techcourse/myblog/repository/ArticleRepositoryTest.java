package techcourse.myblog.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import techcourse.myblog.domain.Article;

import static org.assertj.core.api.Assertions.assertThat;

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
    void findById() {
        Article savedArticle = articleRepository.save(article);
        assertThat(articleRepository.findById(article.getId())).isEqualTo(savedArticle);
    }
}