package techcourse.myblog.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import techcourse.myblog.web.exception.NotExistEntityException;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ArticleRepositoryTest {
    private ArticleRepository articleRepository;

    @BeforeEach
    void setup() {
        articleRepository = new ArticleRepository();
    }

    @Test
    public void findAll_모든_article을_잘_가져오는지() {
        List<Article> articles = Arrays.asList(
                new Article(1, "t1", "u1", "c1")
                , new Article(1, "t2", "u2", "c2")
                , new Article(1, "t3", "u3", "c3")
        );
        articles.forEach(article -> articleRepository.saveArticle(article));

        assertThat(articleRepository.findAll()).isEqualTo(articles);
    }

    @Test
    public void findById_id에_해당하는_article을_잘_가져오는지() {
        List<Article> articles = Arrays.asList(
                new Article(1, "t1", "u1", "c1")
                , new Article(1, "t2", "u2", "c2")
        );
        articles.forEach(article -> articleRepository.saveArticle(article));
        int idx = 1;
        Article article = articles.get(idx);
        int id = article.getId();

        assertThat(articleRepository.findById(id)).isEqualTo(article);
    }

    @Test
    public void findById_존재하지않는_id의_article을_가져올경우() {
        int notExistId = 5;

        List<Article> articles = Arrays.asList(
                new Article(1, "t1", "u1", "c1")
                , new Article(1, "t2", "u2", "c2")
        );
        articles.forEach(article -> articleRepository.saveArticle(article));

        assertThrows(NotExistEntityException.class, () -> articleRepository.findById(notExistId));
    }

    @Test
    void insertArticle_insert테스트() {
        Article article = new Article(1, "t", "u", "c");
        int id = articleRepository.saveArticle(article);
        article.changeId(id);

        assertThat(articleRepository.findById(id)).isEqualTo(article);
    }

    @Test
    void updateArticle_update테스트() {
        Article previousArticle = new Article(1, "t1", "u1", "c1");
        int id = articleRepository.saveArticle(previousArticle);

        Article newArticle = new Article(1, "t2", "u2", "c2");
        newArticle.changeId(id);

        articleRepository.modifyArticle(newArticle);

        assertThat(articleRepository.findById(id)).isEqualTo(newArticle);
    }

    @Test
    void deleteArticle_delete테스트() {
        Article article = new Article(1, "t", "u", "c");
        int id = articleRepository.saveArticle(article);

        articleRepository.removeArticle(id);
        assertThrows(NotExistEntityException.class, () -> articleRepository.findById(id));
    }

    @AfterEach
    void tearDown() {
        articleRepository = null;
    }
}