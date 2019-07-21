package techcourse.myblog.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import techcourse.myblog.domain.Article.Article;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ArticleServiceTest {

    @Autowired
    private ArticleService articleService;

    private Article article;

    @BeforeEach
    void setUp() {
        article = new Article("t1", "c1", "c1");
    }

    @Test
    void findTest() {
        long id = articleService.save(article);
        assertThat(articleService.findArticle(id).getTitle()).isEqualTo(article.getTitle());
    }

    @Test
    void updateTest() {
        long id = articleService.save(article);
        Article editArticle = new Article("edit", "edit", "edit");
        article.update(editArticle);
        articleService.save(article);
        assertThat(articleService.findArticle(id).getTitle()).isEqualTo(editArticle.getTitle());
    }

    @Test
    void findAllTest() {
        articleService.save(article);
        List<Article> articles = articleService.findAll();
        assertThat(articles.size()).isEqualTo(1);
    }

    @Test
    void deleteTest() {
        long id = articleService.save(article);
        articleService.delete(id);
        List<Article> articles = articleService.findAll();
        assertThat(articles.size()).isEqualTo(0);
    }
}