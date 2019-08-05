package techcourse.myblog.service.article;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.exception.ArticleNotFoundException;
import techcourse.myblog.exception.UserHasNotAuthorityException;
import techcourse.myblog.service.dto.article.ArticleRequest;
import techcourse.myblog.service.dto.article.ArticleResponse;
import techcourse.myblog.service.dto.user.UserResponse;

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
        ArticleRequest article = new ArticleRequest("some title", "", "some contents");
        Long articleId = articleService.save(article, DEFAULT_USER_ID);
        assertThat(articleService.findById(articleId))
                .isEqualTo(new ArticleResponse(
                        DEFAULT_ARTICLE_ID + AUTO_INCREMENT_ID,
                        "some title",
                        "",
                        "some contents",
                        Collections.emptyList()));
    }

    @Test
    void 게시글_조회_확인() {
        ArticleResponse retrieveArticleDto = articleService.findById(DEFAULT_ARTICLE_ID);
        assertThat(retrieveArticleDto).isEqualTo(articleService.findById(DEFAULT_ARTICLE_ID));
    }

    @Test
    void 모든_게시글_조회_확인() {
        List<ArticleResponse> articleDtos = articleService.findAll();
        assertThat(articleDtos).isEqualTo(Arrays.asList(articleService.findById(DEFAULT_ARTICLE_ID)));
    }

    @Test
    void 게시글_수정_확인() {
        ArticleRequest updatedArticleDto = new ArticleRequest("title", "", "contents");
        articleService.update(DEFAULT_ARTICLE_ID, updatedArticleDto, new UserResponse(999L, "john123@example.com", "john"));
        ArticleResponse retrievedArticleDto = articleService.findById(DEFAULT_ARTICLE_ID);
        assertThat(retrievedArticleDto.getId()).isEqualTo(DEFAULT_ARTICLE_ID);
        assertThat(retrievedArticleDto.getTitle()).isEqualTo(updatedArticleDto.getTitle());
        assertThat(retrievedArticleDto.getCoverUrl()).isEqualTo(updatedArticleDto.getCoverUrl());
        assertThat(retrievedArticleDto.getContents()).isEqualTo(updatedArticleDto.getContents());
    }

    @Test
    void 게시글_작성자가_게시글_삭제() {
        articleService.delete(DEFAULT_ARTICLE_ID, new UserResponse(DEFAULT_USER_ID, "john123@example.com", "john"));
        assertThatExceptionOfType(ArticleNotFoundException.class)
                .isThrownBy(() -> articleService.findById(DEFAULT_ARTICLE_ID));
    }

    @Test
    void 게시글_작성자가_아닌_회원이_게시글_삭제() {
        assertThatExceptionOfType(UserHasNotAuthorityException.class)
                .isThrownBy(() -> articleService.delete(
                        DEFAULT_ARTICLE_ID,
                        new UserResponse(DEFAULT_USER_ID + 1L, "paul123@example.com", "paul")));
    }
}
