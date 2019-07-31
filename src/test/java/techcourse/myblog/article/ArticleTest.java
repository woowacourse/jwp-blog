package techcourse.myblog.article;

import org.junit.jupiter.api.Test;
import techcourse.myblog.exception.InvalidAuthorException;
import techcourse.myblog.user.User;

import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    void 자신의_아티클_수정() {
        Article article2 = Article.builder()
                .title("update title")
                .coverUrl("update coverUrl")
                .contents("update contents")
                .author(user)
                .build();

        article.update(article2);
        
        assertEquals(article2.getTitle(), "update title");
        assertEquals(article2.getCoverUrl(), "update coverUrl");
        assertEquals(article2.getContents(), "update contents");
    }
}
