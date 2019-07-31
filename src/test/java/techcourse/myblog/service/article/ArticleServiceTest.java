package techcourse.myblog.service.article;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.exception.ArticleNotFoundException;
import techcourse.myblog.service.dto.article.ArticleRequestDto;
import techcourse.myblog.service.dto.article.ArticleResponseDto;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class ArticleServiceTest {
    private static final Long DEFAULT_USER_ID = 999L;
    private static final Long DEFAULT_ARTICLE_ID = 999L;
    private static final int AUTO_INCREMENT_ID = 1;

    @Autowired
    private ArticleService articleService;

    @Test
    void 게시글_생성_확인() {
        ArticleRequestDto article = new ArticleRequestDto("some title", "", "some contents");
        Long articleId = articleService.save(article, DEFAULT_USER_ID);
        assertThat(articleService.findById(articleId))
                .isEqualTo(new ArticleResponseDto(
                        DEFAULT_ARTICLE_ID + AUTO_INCREMENT_ID,
                        "some title",
                        "",
                        "some contents",
                        Collections.emptyList()));
    }

    @Test
    void 게시글_조회_확인() {
        ArticleResponseDto retrieveArticleDto = articleService.findById(DEFAULT_ARTICLE_ID);
        assertThat(retrieveArticleDto).isEqualTo(articleService.findById(DEFAULT_ARTICLE_ID));
    }

    @Test
    void 모든_게시글_조회_확인() {
        List<ArticleResponseDto> articleDtos = articleService.findAll();
        assertThat(articleDtos).isEqualTo(Arrays.asList(articleService.findById(DEFAULT_ARTICLE_ID)));
    }

    @Test
    void 게시글_수정_확인() {
        ArticleRequestDto updatedArticleDto = new ArticleRequestDto("title", "", "contents");
        articleService.update(DEFAULT_ARTICLE_ID, updatedArticleDto);
        ArticleResponseDto retrievedArticleDto = articleService.findById(DEFAULT_ARTICLE_ID);
        assertThat(retrievedArticleDto.getId()).isEqualTo(DEFAULT_ARTICLE_ID);
        assertThat(retrievedArticleDto.getTitle()).isEqualTo(updatedArticleDto.getTitle());
        assertThat(retrievedArticleDto.getCoverUrl()).isEqualTo(updatedArticleDto.getCoverUrl());
        assertThat(retrievedArticleDto.getContents()).isEqualTo(updatedArticleDto.getContents());
    }

    @Test
    void 게시글_수정_오류확인_게시글이_null일_경우() {
        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> articleService.update(DEFAULT_ARTICLE_ID, null));
    }

    @Test
    void 게시글_삭제_확인() {
        articleService.delete(DEFAULT_ARTICLE_ID);
        assertThatExceptionOfType(ArticleNotFoundException.class)
                .isThrownBy(() -> articleService.findById(DEFAULT_ARTICLE_ID));
    }

    @Test
    void 게시글_삭제_오류확인_없는_게시글_삭제요청할_경우() {
        assertThatExceptionOfType(EmptyResultDataAccessException.class)
                .isThrownBy(() -> articleService.delete(DEFAULT_ARTICLE_ID + 1));
    }
}
