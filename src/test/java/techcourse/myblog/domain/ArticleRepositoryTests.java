package techcourse.myblog.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class ArticleRepositoryTests {
    private ArticleRepository articleRepository;

    @BeforeEach
    void setUp() {
        articleRepository = new ArticleRepository();
    }

    @Test
    void add() {
        Article article = new Article("Title", "https://www.woowa.com", "Contents");
        articleRepository.add(article);

        Article addedArticle = articleRepository.findById(article.getId()).orElseThrow(ArticleNotFoundException::new);
        assertThat(addedArticle.getTitle()).isEqualTo("Title");
        assertThat(addedArticle.getCoverUrl()).isEqualTo("https://www.woowa.com");
        assertThat(addedArticle.getContents()).isEqualTo("Contents");
    }

    @Test
    void findById() {
        Article article = new Article("Title", "https://www.woowa.com", "Contents");

        articleRepository.add(new Article("", "", ""));
        articleRepository.add(new Article("", "", ""));
        articleRepository.add(article);

        Article foundArticle = articleRepository.findById(article.getId()).orElseThrow(ArticleNotFoundException::new);
        assertThat(foundArticle.getTitle()).isEqualTo("Title");
        assertThat(foundArticle.getCoverUrl()).isEqualTo("https://www.woowa.com");
        assertThat(foundArticle.getContents()).isEqualTo("Contents");
    }

    @Test
    void deleteById() {
        Article article = new Article("Title", "https://www.woowa.com", "Contents");

        articleRepository.add(new Article("", "", ""));
        articleRepository.add(new Article("", "", ""));
        articleRepository.add(article);

        articleRepository.deleteById(article.getId());
        assertFalse(articleRepository.findById(3).isPresent());
    }

    @AfterEach
    void tearDown() {
        articleRepository.clear();
    }
}
