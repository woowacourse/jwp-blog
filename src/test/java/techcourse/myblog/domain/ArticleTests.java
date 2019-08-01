package techcourse.myblog.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import techcourse.myblog.domain.article.Article;
import techcourse.myblog.domain.article.ArticleVo;
import techcourse.myblog.domain.exception.UserMismatchException;
import techcourse.myblog.domain.user.User;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ArticleTests {
    private static final Long MATCH_USER_ID = 1L;
    private static final Long MISMATCH_USER_ID = 2L;

    private User user;
    private ArticleVo articleVo;

    @BeforeEach
    void setUp() {
        user = mock(User.class);
        articleVo = new ArticleVo("title", "url", "contents");

        when(user.getId()).thenReturn(MATCH_USER_ID);
        when(user.matchId(MATCH_USER_ID)).thenReturn(true);
        when(user.matchId(MISMATCH_USER_ID)).thenReturn(false);
    }

    @Test
    void 자신이_작성한_글인지_확인() {
        Article article = new Article(user, articleVo);

        assertThat(article.getAuthorId()).isEqualTo(MATCH_USER_ID);
    }

    @Test
    void 자신이_작성한_게시글_수정() {
        ArticleVo newArticleVo = new ArticleVo("new Title", "new CoverUrl", "new Contents");
        Article article = new Article(user, articleVo);

        article.updateArticle(newArticleVo, MATCH_USER_ID);

        assertThat(article.getTitle()).isEqualTo("new Title");
        assertThat(article.getCoverUrl()).isEqualTo("new CoverUrl");
        assertThat(article.getContents()).isEqualTo("new Contents");
    }

    @Test
    void 자신이_작성한_게시글이_아니면_수정되지_않는다() {
        ArticleVo newArticleVo = new ArticleVo("new Title", "new CoverUrl", "new Contents");
        Article article = new Article(user, articleVo);

        assertThatThrownBy(() -> article.updateArticle(newArticleVo, MISMATCH_USER_ID))
                .isInstanceOf(UserMismatchException.class);
    }

}
