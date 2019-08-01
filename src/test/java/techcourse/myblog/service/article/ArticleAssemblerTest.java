package techcourse.myblog.service.article;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import techcourse.myblog.domain.article.Article;
import techcourse.myblog.domain.user.User;
import techcourse.myblog.service.dto.article.ArticleRequest;
import techcourse.myblog.service.dto.article.ArticleResponse;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static techcourse.myblog.service.article.ArticleAssembler.convertToDto;
import static techcourse.myblog.service.article.ArticleAssembler.convertToEntity;

public class ArticleAssemblerTest {
    private static final User DEFAULT_AUTHOR = new User("user@example.com", "john", "p@ssW0rd");

    private ArticleRequest articleRequest;
    private ArticleResponse articleResponse;
    private Article article;

    @BeforeEach
    void setUp() {
        articleRequest = new ArticleRequest("title", "", "contents");
        articleResponse = new ArticleResponse(1L, "title", "", "contents", Collections.emptyList());
        article = new Article("title", "", "contents", DEFAULT_AUTHOR);
    }

    @Test
    void dto를_entity로_변환하는지_확인() {
        Article convertedArticle = convertToEntity(articleRequest, DEFAULT_AUTHOR);
        assertThat(convertedArticle.getTitle()).isEqualTo(article.getTitle());
        assertThat(convertedArticle.getCoverUrl()).isEqualTo(article.getCoverUrl());
        assertThat(convertedArticle.getContents()).isEqualTo(article.getContents());
        assertThat(convertedArticle.getAuthor()).isEqualTo(article.getAuthor());
    }

    @Test
    void entity를_dto로_변환하는지_확인() {
        assertThat(convertToDto(article)).isEqualTo(articleResponse);
    }
}
