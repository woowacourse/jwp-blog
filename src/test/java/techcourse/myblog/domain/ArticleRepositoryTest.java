package techcourse.myblog.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class ArticleRepositoryTest {
    private ArticleRepository articleRepository;
    private Article newArticle;

    @BeforeEach
    void setUp() {
        articleRepository = new ArticleRepository();
        newArticle = Article.of("title", "http://background.com", "가나다라마바사");
    }

    @Test
    void addArticle() {
        articleRepository.addArticle(newArticle);

        assertThat(articleRepository.findAll()).hasSize(1);
    }

    @Test
    void findById() {
        articleRepository.addArticle(newArticle);
        Optional<Article> maybeArticle = articleRepository.findById(newArticle.getId());

        assertThat(maybeArticle.isPresent()).isTrue();
        assertThat(maybeArticle.get()).isEqualTo(newArticle);
    }

    @Test
    void 수정_테스트() {
        articleRepository.addArticle(newArticle);
        Article articleFound = articleRepository.findById(newArticle.getId())
                .orElseThrow(IllegalStateException::new);

        articleFound.setTitle("changed title");
        articleFound = articleRepository.findById(newArticle.getId())
                .orElseThrow(IllegalStateException::new);

        assertThat(articleFound.getTitle()).isEqualTo("changed title");
    }

    @Test
    void delete() {
        articleRepository.addArticle(newArticle);
        articleRepository.deleteById(newArticle.getId());

        assertThat(articleRepository.findAll()).hasSize(0);
    }
}
