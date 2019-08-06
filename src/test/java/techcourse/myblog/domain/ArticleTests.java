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
import static techcourse.myblog.Utils.TestConstants.BASE_USER_ID;
import static techcourse.myblog.Utils.TestConstants.MISMATCH_USER_ID;

public class ArticleTests {
    private User user;
    private ArticleVo articleVo;

    @BeforeEach
    void setUp() {
        user = mock(User.class);
        articleVo = new ArticleVo("title", "url", "contents");

        when(user.getId()).thenReturn(BASE_USER_ID);
        when(user.matchId(BASE_USER_ID)).thenReturn(true);
        when(user.matchId(MISMATCH_USER_ID)).thenReturn(false);
    }

    @Test
    void 자신이_작성한_글인지_확인() {
        Article article = new Article(user, articleVo);

        assertThat(article.getAuthorId()).isEqualTo(BASE_USER_ID);
    }

    @Test
    void 자신이_작성한_게시글_수정() {
        ArticleVo newArticleVo = new ArticleVo("new Title", "new CoverUrl", "new Contents");
        Article article = new Article(user, articleVo);

        article.updateArticle(newArticleVo, BASE_USER_ID);

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
