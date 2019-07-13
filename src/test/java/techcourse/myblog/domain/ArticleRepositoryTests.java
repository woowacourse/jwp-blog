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
        Article article = new Article();
        article.setContents("Contents");
        article.setCoverUrl("https://www.woowa.com");
        article.setTitle("Hi");
        articleRepository.add(article);

        Article addedArticle = articleRepository.findAll().get(0);
        assertThat(addedArticle.getContents()).isEqualTo("Contents");
        assertThat(addedArticle.getCoverUrl()).isEqualTo("https://www.woowa.com");
        assertThat(addedArticle.getTitle()).isEqualTo("Hi");
    }

    @Test
    void findById() {
        Article article = new Article();
        article.setContents("Contents");
        article.setCoverUrl("https://www.woowa.com");
        article.setTitle("Hi");
        articleRepository.add(new Article());
        articleRepository.add(new Article());
        articleRepository.add(article);

        Article foundArticle = articleRepository.findById(article.getId()).orElseThrow(ArticleNotFoundException::new);
        assertThat(foundArticle.getContents()).isEqualTo("Contents");
        assertThat(foundArticle.getCoverUrl()).isEqualTo("https://www.woowa.com");
        assertThat(foundArticle.getTitle()).isEqualTo("Hi");
    }

    @Test
    void deleteById() {
        Article article = new Article();
        article.setContents("Contents");
        article.setCoverUrl("https://www.woowa.com");
        article.setTitle("Hi");
        articleRepository.add(new Article());
        articleRepository.add(new Article());
        articleRepository.add(article);

        articleRepository.deleteById(article.getId());
        assertFalse(articleRepository.findById(3).isPresent());
    }

    @AfterEach
    void tearDown() {
        articleRepository.clear();
    }
}
