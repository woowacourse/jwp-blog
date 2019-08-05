package techcourse.myblog.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.domain.article.Article;
import techcourse.myblog.domain.user.User;
import techcourse.myblog.domain.user.UserEmail;
import techcourse.myblog.dto.ArticleDto;
import techcourse.myblog.repository.ArticleRepository;
import techcourse.myblog.repository.UserRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ArticleServiceTest {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private UserRepository userRepository;

    private ArticleDto articleDto;
    private User author;

    @BeforeEach
    void setUp() {
        author = userRepository.findByEmail(UserEmail.of("test@test.com")).get();
        articleDto = new ArticleDto("t1", "c1", "c1");
    }

    @Test
    void findTest() {
        long id = articleService.save(articleDto, author).getId();
        assertThat(articleService.findArticle(id).getTitle()).isEqualTo(articleDto.getTitle());
    }

    @Test
    void updateTest() {
        Article article = articleService.save(articleDto, author);
        Article editArticle = new Article("z", "x", "c", author);
        article.update(editArticle);
        assertThat(articleService.findArticle(article.getId()).getTitle()).isEqualTo(editArticle.getTitle());
    }

    @Test
    void findAllTest() {
        int before = articleService.findAll().size();
        articleService.save(new ArticleDto("1", "2", "3"), author);
        List<Article> articles = articleService.findAll();
        assertThat(articles.size()).isNotEqualTo(before);
    }

    @Test
    void deleteTest() {
        long id = articleService.save(articleDto, author).getId();
        int before = articleService.findAll().size();
        articleService.delete(id, author);
        List<Article> articles = articleService.findAll();
        assertThat(articles.size()).isNotEqualTo(before);
    }
}