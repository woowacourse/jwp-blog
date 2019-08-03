package techcourse.myblog.article;

import org.junit.jupiter.api.Test;
import techcourse.myblog.exception.InvalidAuthorException;
import techcourse.myblog.user.User;
import techcourse.myblog.user.UserTest;

import static org.junit.jupiter.api.Assertions.*;
import static techcourse.myblog.user.UserTest.user;


public class ArticleTest {
    public static final Article article = Article.builder()
            .title("title")
            .coverUrl("coverUrl")
            .contents("contents")
            .author(UserTest.user)
            .build();

    @Test
    void 아티클_작성자_확인() {
        assertDoesNotThrow(() -> article.checkCorrespondingAuthor(user));
    }

    @Test
    void 아티클을_다른사람이_변경시도() {
        assertThrows(InvalidAuthorException.class, () -> article.checkCorrespondingAuthor(new User()));
    }

    @Test
    void 자신의_아티클_수정() {
        Article article2 = Article.builder()
                .title("update title")
                .coverUrl("update coverUrl")
                .contents("update contents")
                .author(UserTest.user)
                .build();

        article.update(article2);

        assertEquals(article.getTitle(), "update title");
        assertEquals(article.getCoverUrl(), "update coverUrl");
        assertEquals(article.getContents(), "update contents");
    }
}
