package techcourse.myblog.domain.article;

import org.junit.jupiter.api.Test;
import techcourse.myblog.domain.InvalidAuthorException;
import techcourse.myblog.domain.user.User;
import techcourse.myblog.domain.user.UserTest;

import static org.junit.jupiter.api.Assertions.*;
import static techcourse.myblog.domain.user.UserTest.user;


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
