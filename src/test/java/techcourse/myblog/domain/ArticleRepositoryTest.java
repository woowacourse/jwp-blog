package techcourse.myblog.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ArticleRepositoryTest {

    @Autowired
    private ArticleRepository articleRepository;

    @Test
    void addArticle() {
        Article newArticle = articleRepository.save(Article.of("title", "http://background.com", "가나다라마바사"));
        assertThat(newArticle).isNotNull();
    }

    @Test
    void findById() {
        Article newArticle = articleRepository.save(Article.of("title", "http://background.com", "가나다라마바사"));

        Optional<Article> maybeArticle = articleRepository.findById(newArticle.getId());
        assertThat(maybeArticle.isPresent()).isTrue();
        assertThat(maybeArticle.get()).isEqualTo(newArticle);
    }

    @Test
    void update() {
        Article newArticle = articleRepository.save(Article.of("title", "http://background.com", "가나다라마바사"));

        Article articleFound = articleRepository.findById(newArticle.getId())
            .orElseThrow(IllegalStateException::new);
        articleFound.update(Article.of("changed title", articleFound.getCoverUrl(), articleFound.getContents()));
        articleRepository.save(articleFound);

        Article articleToAssert = articleRepository.findById(newArticle.getId())
            .orElseThrow(IllegalStateException::new);

        assertThat(articleToAssert.getTitle()).isEqualTo("changed title");
    }

    @Test
    void delete() {
        Article newArticle = articleRepository.save(Article.of("title", "http://background.com", "가나다라마바사"));
        articleRepository.deleteById(newArticle.getId());

        assertThat(articleRepository.findById(newArticle.getId()).isPresent()).isFalse();
    }
}
