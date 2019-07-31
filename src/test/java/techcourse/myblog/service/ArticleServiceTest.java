package techcourse.myblog.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.ArticleSaveRequestDto;
import techcourse.myblog.exception.ArticleNotFoundException;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class ArticleServiceTest {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private UserService userService;

    private User author;
    private Article article;

    @BeforeEach
    void setUp_article_save() {
        author = User.builder()
                .name("이름")
                .email("test123@test.com")
                .password("asdfas1!")
                .build();
        userService.save(author);

        ArticleSaveRequestDto articleSaveRequestDto = new ArticleSaveRequestDto("title", "coverUrl", "contents");
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
    void delete() {
        articleService.deleteById(article.getId());
        assertThrows(ArticleNotFoundException.class, () -> articleService.findById(article.getId()));

        userService.deleteUser(author.getId());
    }
}