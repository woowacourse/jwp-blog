package techcourse.myblog.article;

import org.junit.jupiter.api.Test;
import techcourse.myblog.exception.UserHasNotAuthorityException;
import techcourse.myblog.user.User;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class ArticleTest {
    private static final User DEFAULT_AUTHOR = new User(999L, "john123@example.com", "john", "p@ssW0rd");

    @Test
    void 게시글_수정_확인() {
        Article oldArticle = new Article("title", "", "content", DEFAULT_AUTHOR);
        Article newArticle = new Article("newTitle", "", "newContent", DEFAULT_AUTHOR);
        oldArticle.update(newArticle);
        assertThat(oldArticle.getTitle()).isEqualTo(newArticle.getTitle());
        assertThat(oldArticle.getCoverUrl()).isEqualTo(newArticle.getCoverUrl());
        assertThat(oldArticle.getContents()).isEqualTo(newArticle.getContents());
        assertThat(oldArticle.getAuthor()).isEqualTo(newArticle.getAuthor());
    }

    @Test
    void 게시글_수정_오류확인_로그인한_회원이_게시글_작성자가_아닌_경우() {
        Article oldArticle = new Article("title", "", "content", DEFAULT_AUTHOR);
        Article newArticle = new Article(
                "newTitle",
                "",
                "newContent",
                new User(1000L, "paul123@example.com", "paul", "p@ssW0rd"));
        assertThatExceptionOfType(UserHasNotAuthorityException.class)
                .isThrownBy(() -> oldArticle.update(newArticle));
    }

    @Test
    void 게시글_작성자인지_확인() {
        Article article = new Article("title", "", "content", DEFAULT_AUTHOR);
        assertThat(article.matchAuthor(DEFAULT_AUTHOR)).isTrue();
    }

    @Test
    void 게시글_작성자인지_확인_게시글_작성자가_아닐_경우() {
        Article article = new Article("title", "", "content", DEFAULT_AUTHOR);
        assertThat(article.matchAuthor(new User(1000L, "paul123,@example.com", "paul", "p@ssW0rd"))).isFalse();
    }
}
