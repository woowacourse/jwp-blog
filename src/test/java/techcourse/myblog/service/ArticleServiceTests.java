package techcourse.myblog.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import techcourse.myblog.MyblogApplicationTests;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.ArticleRepository;
import techcourse.myblog.domain.User;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

public class ArticleServiceTests extends MyblogApplicationTests {
    User user;
    Article article;

    @MockBean
    private ArticleRepository articleRepository;

    @Autowired
    private ArticleService articleService;


    @BeforeEach
    public void setup() {
        user = new User(1L, USER_NAME, USER_PASSWORD, USER_EMAIL);
        article = new Article(ARTICLE_TITLE, ARTICLE_CONTENTS, ARTICLE_COVER_URL, user);
    }

    @Test
    @DisplayName("아티클 작성자와 현재 유저가 불일치할 때")
    void is_not_author_test() {
        given(articleRepository.findById(1L)).willReturn(Optional.of(article));
        assertThat(articleService.isNotAuthor(1L, 2L)).isTrue();
    }

    @Test
    @DisplayName("아티클 작성자와 현재 유저가 일치할 때")
    void is_author_test() {
        given(articleService.getArticleOrElseThrow(1L)).willReturn(article);
        //System.out.println();articleService.isNotMatchAuthor(1L);
        assertThat(articleService.isNotAuthor(1L, 1L)).isFalse();
    }
}
