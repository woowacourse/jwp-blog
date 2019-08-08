package techcourse.myblog.domain.article;

import org.junit.jupiter.api.Test;
import techcourse.myblog.domain.InvalidAuthorException;
import techcourse.myblog.domain.user.User;

import static org.junit.jupiter.api.Assertions.*;
import static techcourse.myblog.domain.user.UserTest.user;


public class ArticleTest {
    public static final Contents contents = new Contents("title", "coverUrl", "contents");
    public static final Article article = new Article(contents, user);

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
        Contents contents = new Contents("update title", "update coverUrl", "update contents");
        Article article2 = new Article(contents, user);

        article.update(article2);

        assertEquals(article.contents, contents);
    }
}
