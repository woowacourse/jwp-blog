package techcourse.myblog.domain.article;

import org.junit.jupiter.api.Test;
import techcourse.myblog.domain.user.User;
import techcourse.myblog.exception.ArticleToUpdateNotFoundException;
import techcourse.myblog.exception.UserHasNotAuthorityException;
import techcourse.myblog.exception.UserNotLogInException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class ArticleTest {
    private static final User DEFAULT_AUTHOR = new User("user@example.com", "john", "p@ssW0rd");

    @Test
    void 생성자_오류확인_title이_null일_경우() {
        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> new Article(null, "", "content", DEFAULT_AUTHOR));
    }

    @Test
    void 생성자_오류확인_coverUrl이_null일_경우() {
        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> new Article("title", null, "content", DEFAULT_AUTHOR));
    }

    @Test
    void 생성자_오류확인_contents가_null일_경우() {
        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> new Article("title", "", null, DEFAULT_AUTHOR));
    }

    @Test
    void 게시글_수정_확인() {
        Article oldArticle = new Article("title", "", "content", DEFAULT_AUTHOR);
        Article newArticle = new Article("newTitle", "", "newContent", DEFAULT_AUTHOR);
        oldArticle.update(newArticle, "user@example.com");
        assertThat(oldArticle.getTitle()).isEqualTo(newArticle.getTitle());
        assertThat(oldArticle.getCoverUrl()).isEqualTo(newArticle.getCoverUrl());
        assertThat(oldArticle.getContents()).isEqualTo(newArticle.getContents());
        assertThat(oldArticle.getAuthor()).isEqualTo(newArticle.getAuthor());
    }

    @Test
    void 게시글_수정_오류확인_인자가_null인_경우() {
        Article article = new Article("title", "", "content", DEFAULT_AUTHOR);
        assertThatExceptionOfType(ArticleToUpdateNotFoundException.class)
                .isThrownBy(() -> article.update(null, "user@example.com"));
    }

    @Test
    void 게시글_수정_오류확인_로그인상태가_아닐_경우() {
        Article oldArticle = new Article("title", "", "content", DEFAULT_AUTHOR);
        Article newArticle = new Article("newTitle", "", "newContent", DEFAULT_AUTHOR);
        assertThatExceptionOfType(UserNotLogInException.class)
                .isThrownBy(() -> oldArticle.update(newArticle, null));
    }

    @Test
    void 게시글_수정_오류확인_로그인한_회원이_게시글_작성자가_아닌_경우() {
        Article oldArticle = new Article("title", "", "content", DEFAULT_AUTHOR);
        Article newArticle = new Article("newTitle", "", "newContent", DEFAULT_AUTHOR);
        assertThatExceptionOfType(UserHasNotAuthorityException.class)
                .isThrownBy(() -> oldArticle.update(newArticle, "paul123@example.com"));
    }
}
