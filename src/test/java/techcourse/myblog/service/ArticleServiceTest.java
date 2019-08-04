package techcourse.myblog.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import techcourse.myblog.domain.article.Article;
import techcourse.myblog.dto.ArticleDto;
import techcourse.myblog.exception.NotMatchAuthorException;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ArticleServiceTest extends ServiceTest {
    @BeforeEach
    void setUp() {
        init();
    }

    @AfterEach
    void tearDown() {
        terminate();
    }

    @Test
    void 게시글_전체_조회_테스트() {
        assertThat(articleService.findAll()).isEqualTo(Arrays.asList(articleDto));
    }

    @Test
    void 게시글_단건_조회_테스트() {
        assertThat(articleService.findById(articleId)).isEqualTo(articleDto);
    }

    @Test
    void 작석자_확인_게시글_단건_조회_테스트() {
        assertThat(articleService.findById(userDto, articleId)).isEqualTo(articleDto);
    }

    @Test
    void 다른_작석자_확인_게시글_단건_조회_테스트() {
        assertThrows(NotMatchAuthorException.class, () ->
            articleService.findById(otherUserDto, articleId));
    }

    @Test
    void 게시글_수정_테스트() {
        Article updatedArticle = Article.builder()
            .id(articleId)
            .title("updatedTitle")
            .coverUrl("updatedCoverUrl")
            .contents("updatedContents")
            .author(user)
            .build();

        ArticleDto.Response updatedArticleDto =
            articleService.update(userDto, articleId, modelMapper.map(updatedArticle, ArticleDto.Update.class));

        assertThat(updatedArticleDto)
            .isEqualTo(modelMapper.map(updatedArticle, ArticleDto.Response.class));
    }

    @Test
    void 다른_작성자_게시글_수정_테스트() {
        Article updatedArticle = Article.builder()
            .id(articleId)
            .title("updatedTitle")
            .coverUrl("updatedCoverUrl")
            .contents("updatedContents")
            .author(user)
            .build();

        assertThrows(NotMatchAuthorException.class, () ->
            articleService.update(otherUserDto, articleId, modelMapper.map(updatedArticle, ArticleDto.Update.class)));
    }

    @Test
    void 다른_작성자_게시글_삭제_테스트() {
        assertThrows(NotMatchAuthorException.class, () ->
            articleService.deleteById(otherUserDto, articleId));
    }
}