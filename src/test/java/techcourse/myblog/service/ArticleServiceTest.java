package techcourse.myblog.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.ArticleSaveRequestDto;
import techcourse.myblog.exception.ArticleNotFoundException;
import techcourse.myblog.exception.IllegalArticleDeleteRequestException;
import techcourse.myblog.exception.IllegalArticleUpdateRequestException;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class ArticleServiceTest {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private UserService userService;

    private User author;
    private ArticleSaveRequestDto articleSaveRequestDto;
    private Article article;

    @BeforeEach
    void setUp_article_save() {
        author = User.builder()
                .name("이름")
                .email("test123@test.com")
                .password("asdfas1!")
                .build();
        userService.save(author);

        articleSaveRequestDto = new ArticleSaveRequestDto("title", "coverUrl", "contents");
        article = articleService.save(articleSaveRequestDto, author);
    }

    @Test
    void findAllArticles() {
        List<Article> articles = new ArrayList<>();
        for (Article foundArticle : articleService.findAllArticles()) {
            articles.add(foundArticle);
        }

        assertThat(articles.contains(article)).isTrue();
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

        articleService.update(articleSaveRequestDto, id, author);

        Article updatedArticle = articleService.findById(id);
        assertThat(updatedArticle.getTitle()).isEqualTo(articleSaveRequestDto.getTitle());
        assertThat(updatedArticle.getCoverUrl()).isEqualTo(articleSaveRequestDto.getCoverUrl());
        assertThat(updatedArticle.getContents()).isEqualTo(articleSaveRequestDto.getContents());
    }

    @Test
    void update_작성자가_아닌_경우() {
        ArticleSaveRequestDto anotherArticleSaveRequestDto = new ArticleSaveRequestDto();
        anotherArticleSaveRequestDto.setTitle("newTitle");
        anotherArticleSaveRequestDto.setCoverUrl("newCoverUrl");
        anotherArticleSaveRequestDto.setContents("newContents");
        Long id = article.getId();

        User anotherAuthor = User.builder()
                .name("이름")
                .email("anotherAuthor@test.com")
                .password("password1!")
                .build();
        userService.save(anotherAuthor);

        assertThrows(IllegalArticleUpdateRequestException.class
                , () -> articleService.update(anotherArticleSaveRequestDto, id, anotherAuthor));

        Article updatedArticle = articleService.findById(id);
        assertThat(updatedArticle.getTitle()).isEqualTo(articleSaveRequestDto.getTitle());
        assertThat(updatedArticle.getCoverUrl()).isEqualTo(articleSaveRequestDto.getCoverUrl());
        assertThat(updatedArticle.getContents()).isEqualTo(articleSaveRequestDto.getContents());
    }

    @Test
    void delete() {
        articleService.deleteById(article.getId(), author);
        assertThrows(ArticleNotFoundException.class, () -> articleService.findById(article.getId()));
    }

    @Test
    void delete_작성자가_아닌_경우() {
        User anotherAuthor = User.builder()
                .name("이름")
                .email("anotherAuthor@test.com")
                .password("password1!")
                .build();
        userService.save(anotherAuthor);

        assertThrows(IllegalArticleDeleteRequestException.class
                , () -> articleService.deleteById(article.getId(), anotherAuthor));

        assertDoesNotThrow(() -> articleService.findById(article.getId()));
    }
}