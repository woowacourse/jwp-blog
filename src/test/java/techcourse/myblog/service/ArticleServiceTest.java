package techcourse.myblog.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.domain.article.Article;
import techcourse.myblog.domain.user.User;
import techcourse.myblog.dto.ArticleDto;
import techcourse.myblog.dto.ArticleResponseDto;
import techcourse.myblog.repository.ArticleRepository;
import techcourse.myblog.repository.UserRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ArticleServiceTest {

    @Autowired
    private ArticleGenericService articleGenericService;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private UserRepository userRepository;

    private ArticleDto articleDto;
    private User author;

    @BeforeEach
    void setUp() {
        author = userRepository.findByEmailEmail("test@test.com").get();
        articleDto = new ArticleDto("t1", "c1", "c1");
    }

    @Test
    void findTest() {
        long id = articleGenericService.add(articleDto, author, Article.class).getId();
        assertThat(articleGenericService.findArticle(id, ArticleResponseDto.class).getTitle()).isEqualTo(articleDto.getTitle());
        assertThat(articleGenericService.findArticle(id, Article.class).getTitle()).isEqualTo(articleDto.getTitle());
    }

    @Test
    void updateTest() {
        Article article = articleGenericService.add(articleDto, author, Article.class);
        Article editArticle = new Article("z", "x", "c", author);
        article.update(editArticle);
        assertThat(articleGenericService.findArticle(article.getId(), Article.class).getTitle()).isEqualTo(editArticle.getTitle());
        assertThat(articleGenericService.findArticle(article.getId(), ArticleResponseDto.class).getTitle()).isEqualTo(editArticle.getTitle());
    }

    @Test
    void findAllTest() {
        int before = articleGenericService.findAll(Article.class).size();
        articleGenericService.add(new ArticleDto("1", "2", "3"), author, Article.class);
        List<Article> articles = articleGenericService.findAll(Article.class);
        assertThat(articles.size()).isNotEqualTo(before);
    }

    @Test
    void deleteTest() {
        long id = articleGenericService.add(articleDto, author, Article.class).getId();
        int before = articleGenericService.findAll(Article.class).size();
        articleGenericService.delete(id, author);
        List<Article> articles = articleGenericService.findAll(Article.class);
        assertThat(articles.size()).isNotEqualTo(before);
    }
}