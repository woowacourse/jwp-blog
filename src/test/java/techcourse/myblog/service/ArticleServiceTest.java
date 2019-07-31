package techcourse.myblog.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.domain.article.Article;
import techcourse.myblog.domain.user.User;
import techcourse.myblog.repository.ArticleRepository;
import techcourse.myblog.repository.UserRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ArticleServiceTest {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private UserRepository userRepository;

    private Article article;
    private User author;

    @BeforeEach
    @Transactional
    void setUp() {
        userRepository.deleteAll();
        author = userRepository.save(new User("andole", "A!1bcdefg", "andole@gmail.com"));
        article = new Article("t1", "c1", "c1", author);
    }

    @Test
    void findTest() {
        long id = articleService.save(article);
        assertThat(articleService.findArticle(id).getTitle()).isEqualTo(article.getTitle());
    }

    @Test
    void updateTest() {
        long id = articleService.save(article);
        Article editArticle = new Article("edit", "edit", "edit", author);
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
        articleService.delete(id, author);
        List<Article> articles = articleService.findAll();
        assertThat(articles.size()).isEqualTo(0);
    }
}