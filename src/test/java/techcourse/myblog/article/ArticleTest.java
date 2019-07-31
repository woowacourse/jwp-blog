package techcourse.myblog.article;

import org.junit.jupiter.api.Test;
import techcourse.myblog.exception.InvalidAuthorException;
import techcourse.myblog.user.User;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ArticleTest {
    private static final User user = User.builder()
            .userName("buddy")
            .email("buddy@gmail.com")
            .password("Aa12345!")
            .build();

    private static final Article article = Article.builder()
            .title("title")
            .coverUrl("coverUrl")
            .contents("contents")
            .author(user)
            .build();

    @Test
    void 아티클_작성자_확인() {
        assertDoesNotThrow(() -> article.checkCorrespondingAuthor(user));
    }

    @Test
    void 아티클을_다른사람이_변경시도() {
        assertThrows(InvalidAuthorException.class, () -> article.checkCorrespondingAuthor(new User()));
    }
}
