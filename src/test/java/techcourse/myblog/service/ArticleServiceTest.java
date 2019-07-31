package techcourse.myblog.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.ArticleSaveRequestDto;
import techcourse.myblog.exception.ArticleNotFoundException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class ArticleServiceTest {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private UserService userService;

    private User author;
    private Article article;

    @BeforeEach
    void setUp() {
        author = User.builder()
                .name("이름")
                .email("test123@test.com")
                .password("asdfas1!")
                .build();
        userService.save(author);

        article = articleService.save(Article.builder()
                .title("title")
                .coverUrl("coverUrl")
                .contents("contents")
                .build(), author);
    }

    @Test
    void findAllArticles() {
        Iterable<Article> articles = articleService.findAllArticles();

        assertThat(articles.iterator().next()).isEqualTo(article);
    }

    @Test
    void save() {
        Article newArticle = Article.builder()
                .title("newTitle")
                .coverUrl("newCoverUrl")
                .contents("newContents")
                .build();
        Article savedArticle = articleService.save(newArticle, author);

        assertThat(savedArticle.getTitle()).isEqualTo(newArticle.getTitle());
        assertThat(savedArticle.getCoverUrl()).isEqualTo(newArticle.getCoverUrl());
        assertThat(savedArticle.getContents()).isEqualTo(newArticle.getContents());

        articleService.deleteById(savedArticle.getId());
    }

    @Test
    void findById() {
        Long id = article.getId();
        assertThat(articleService.findById(id)).isEqualTo(article);
    }

    @Test
    void update() {
        ArticleSaveRequestDto articleSaveRequestDto = new ArticleSaveRequestDto();
        articleSaveRequestDto.setTitle("newTitle");
        articleSaveRequestDto.setCoverUrl("newCoverUrl");
        articleSaveRequestDto.setContents("newContents");
        Long id = article.getId();

        articleService.update(articleSaveRequestDto, id);

        Article updatedArticle = articleService.findById(id);
        assertThat(updatedArticle.getTitle()).isEqualTo(articleSaveRequestDto.getTitle());
        assertThat(updatedArticle.getCoverUrl()).isEqualTo(articleSaveRequestDto.getCoverUrl());
        assertThat(updatedArticle.getContents()).isEqualTo(articleSaveRequestDto.getContents());
    }

    @Test
    void deleteById() {
        Article newArticle = Article.builder()
                .title("newTitle")
                .coverUrl("newCoverUrl")
                .contents("newContents")
                .build();
        Article savedArticle = articleService.save(newArticle, author);
        Long id = savedArticle.getId();

        articleService.deleteById(id);

        assertThrows(ArticleNotFoundException.class, () -> articleService.findById(id));
    }

    @AfterEach
    void tearDown() {
        articleService.deleteById(article.getId());
        userService.deleteUser(author.getId());
    }
}