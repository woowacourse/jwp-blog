package techcourse.myblog.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ArticleRepositoryTest {
    @Autowired
    ArticleRepository articleRepository;

    @BeforeEach
    void setUp() {
        Article article = new Article(null, "title", "coverUrl", "contents");
        articleRepository.save(article);
    }

    @Test
    void saveTest() {
        Article article = new Article(null, "title", "coverUrl", "contents");
        assertNotNull(articleRepository.save(article));
    }

    @Test
    void findAllTest() {
        assertFalse(articleRepository.findAll().isEmpty());
    }

    @Test
    void 존재하지_않는_ID_삭제할때_예외처리() {
        assertThrows(IllegalArgumentException.class, () -> articleRepository.deleteById(100L));
    }

    @Test
    void deleteByIdTest() {
        List<Article> articles = articleRepository.findAll();
        int actual = articles.size() - 1;

        Article deleteArticle = articles.get(0);
        articleRepository.deleteById(deleteArticle.getId());
        int expected = articleRepository.findAll().size();

        assertEquals(expected, actual);
    }

    @Test
    void updateTest() {
        Article article = new Article(null, "update foo", "update foo", "update foo");
        Long id = articleRepository.save(article).get();

        Article editedArticle = new Article(id, "new foo", "foo new", "new foo contents");
        id = articleRepository.update(editedArticle);
        Article expected = articleRepository.findById(id).get();

        assertEquals(editedArticle, expected);
    }
}