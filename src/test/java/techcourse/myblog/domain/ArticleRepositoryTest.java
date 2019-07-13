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

    @Test
    void update() {
        ArticleRepository articleRepository = new ArticleRepository();

        Article newArticle = Article.of("title", "http://background.com", "가나다라마바사");
        articleRepository.addArticle(newArticle);

        Article articleFound = articleRepository.findById(newArticle.getId())
            .orElseThrow(IllegalStateException::new);
        articleFound.setTitle("changed title");
        Article articleToAssert = articleRepository.findById(newArticle.getId())
            .orElseThrow(IllegalStateException::new);

        assertThat(articleToAssert.getTitle()).isEqualTo("changed title");
    }

    @Test
    void delete() {
        ArticleRepository articleRepository = new ArticleRepository();

        Article newArticle = Article.of("title", "http://background.com", "가나다라마바사");
        articleRepository.addArticle(newArticle);
        articleRepository.deleteById(newArticle.getId());

        assertThat(articleRepository.findAll()).hasSize(0);
    }
}
