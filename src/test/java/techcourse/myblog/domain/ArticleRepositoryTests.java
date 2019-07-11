package techcourse.myblog.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ArticleRepositoryTests {
    @Autowired
    private ArticleRepository articleRepository;

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

        Article foundArticle = articleRepository.findById(article.getId());
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
        assertThrows(IllegalArgumentException.class, () -> articleRepository.findById(3));
    }

    @AfterEach
    void tearDown() {
        articleRepository.clear();
    }
}
