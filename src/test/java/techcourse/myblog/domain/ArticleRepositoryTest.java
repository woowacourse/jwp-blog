package techcourse.myblog.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ArticleRepositoryTest {
    private ArticleRepository articleRepository;

    @BeforeEach
    void setup() {
        articleRepository = new ArticleRepository();
    }

    @Test
    public void findAll_모든_article을_잘_가져오는지() {
        List<Article> articles = Arrays.asList(
                new Article("t1", "u1", "c1")
                , new Article("t2", "u2", "c2")
                , new Article("t3", "u3", "c3")
        );
        articles.forEach(article -> articleRepository.insertArticle(article));

        assertThat(articleRepository.findAll()).isEqualTo(articles);
    }

    @Test
    public void findById_id에_해당하는_article을_잘_가져오는지() {
        int id = 1;

        List<Article> articles = Arrays.asList(
                new Article("t1", "u1", "c1")
                , new Article("t2", "u2", "c2")
        );
        articles.forEach(article -> articleRepository.insertArticle(article));

        assertThat(articleRepository.findById(id)).isEqualTo(articles.get(id));
    }

    @Test
    public void findById_존재하지않는_id의_article을_가져올경우() {
        int notExistId = 5;

        List<Article> articles = Arrays.asList(
                new Article("t1", "u1", "c1")
                , new Article("t2", "u2", "c2")
        );
        articles.forEach(article -> articleRepository.insertArticle(article));

        assertThat(articleRepository.findById(notExistId)).isNull();
    }

    @Test
    void insertArticle_insert테스트() {
        Article article = new Article("t", "u", "c");
        int id = articleRepository.insertArticle(article);

        assertThat(articleRepository.findById(id)).isEqualTo(article);
    }

    @Test
    void updateArticle_update테스트() {
        Article previousArticle = new Article("t1", "u1", "c1");
        int id = articleRepository.insertArticle(previousArticle);

        Article newArticle = new Article("t2", "u2", "c2");
        articleRepository.updateArticle(id, newArticle);

        assertThat(articleRepository.findById(id)).isEqualTo(newArticle);
    }

    @Test
    void deleteArticle_delete테스트() {
        Article article = new Article("t", "u", "c");
        int id = articleRepository.insertArticle(article);

        articleRepository.deleteArticle(id);
        assertThat(articleRepository.findById(id)).isNull();
    }

    @AfterEach
    void tearDown() {
        articleRepository = null;
    }
}