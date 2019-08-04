package techcourse.myblog.web.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.User;
import techcourse.myblog.service.dto.ArticleRequestDto;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static techcourse.myblog.web.dto.ArticleAssembler.convertToDto;
import static techcourse.myblog.web.dto.ArticleAssembler.convertToEntity;

public class ArticleAssemblerTest {
    private static final User DEFAULT_AUTHOR = new User("user@example.com", "john", "p@ssW0rd");

    private ArticleRequestDto requestDto;
    private ArticleDto articleDto;
    private Article article;

    @BeforeEach
    void setUp() {
        requestDto = new ArticleRequestDto("title", "", "contents");
        articleDto = new ArticleDto(0L, "title", "", "contenst", Collections.emptyList());
        article = new Article("title", "", "contents", DEFAULT_AUTHOR);
    }

    @Test
    void dto를_entity로_변환하는지_확인() {
        Article convertedArticle = convertToEntity(requestDto, DEFAULT_AUTHOR);
        assertThat(convertedArticle.getTitle()).isEqualTo(article.getTitle());
        assertThat(convertedArticle.getCoverUrl()).isEqualTo(article.getCoverUrl());
        assertThat(convertedArticle.getContents()).isEqualTo(article.getContents());
        assertThat(convertedArticle.getAuthor()).isEqualTo(article.getAuthor());
    }

    @Test
    void entity를_dto로_변환하는지_확인() {
        ArticleDto convertedDto = convertToDto(article);
        assertThat(convertedDto.getTitle()).isEqualTo(requestDto.getTitle());
        assertThat(convertedDto.getContents()).isEqualTo(requestDto.getContents());
        assertThat(convertedDto.getCoverUrl()).isEqualTo(requestDto.getCoverUrl());
    }
}
