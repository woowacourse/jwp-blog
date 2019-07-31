package techcourse.myblog.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.domain.article.Article;
import techcourse.myblog.domain.user.Email;
import techcourse.myblog.domain.user.User;
import techcourse.myblog.repository.ArticleRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
class ArticleServiceTest {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private ArticleRepository articleRepository;

    private Article article;
    private User user = new User("andole", "aA1231!!", "andole@gmail.com");
    private Email email = Email.of("andole@gmail.com");

    @BeforeEach
    @Transactional
    @Rollback
    void setUp() {
        articleRepository.deleteAll();
        article = new Article("t1", "c1", "c1");
        article.setAuthor(user);
    }

    @Test
    void findTest() {
        long id = articleService.save(article, email);
        assertThat(articleService.findArticle(id).getTitle()).isEqualTo(article.getTitle());
    }

    @Test
    void updateTest() {
        long id = articleService.save(article, email);
        Article editArticle = new Article("edit", "edit", "edit");
        article.update(editArticle);
        articleService.save(article, email);
        assertThat(articleService.findArticle(id).getTitle()).isEqualTo(editArticle.getTitle());
    }

    @Test
    void findAllTest() {
        articleService.save(article, email);
        List<Article> articles = articleService.findAll();
        assertThat(articles.size()).isEqualTo(1);
    }

    @Test
    void deleteTest() {
        long id = articleService.save(article, email);
        articleService.delete(id, "andole@gmail.com");
        List<Article> articles = articleService.findAll();
        assertThat(articles.size()).isEqualTo(0);
    }
}