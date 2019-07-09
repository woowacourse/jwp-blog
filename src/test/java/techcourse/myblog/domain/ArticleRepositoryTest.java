package techcourse.myblog.domain;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class ArticleRepositoryTest {

    @Test
    void addArticle() {
        ArticleRepository articleRepository = new ArticleRepository();
        articleRepository.addArticle(Article.of("title", "http://background.com", "가나다라마바사"));
        assertThat(articleRepository.findAll()).hasSize(1);
    }

    @Test
    void findById() {
        ArticleRepository articleRepository = new ArticleRepository();
        Article newArticle = Article.of("title", "http://background.com", "가나다라마바사");
        articleRepository.addArticle(newArticle);
        Optional<Article> maybeArticle = articleRepository.findById(newArticle.getId());
        assertThat(maybeArticle.isPresent()).isTrue();
        assertThat(maybeArticle.get()).isEqualTo(newArticle);
    }
}
