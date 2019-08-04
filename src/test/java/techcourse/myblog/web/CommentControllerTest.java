package techcourse.myblog.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

public class CommentControllerTest extends LoggedInTemplate {

    private static final Logger log = LoggerFactory.getLogger(CommentControllerTest.class);

    private static final String CONTENTS = "comment";
    private static final String NEW_CONTENTS = "new comment";

    private static final String OTHER_USER_NAME = "bob";
    private static final String OTHER_USER_PASSWORD = "Password1!";
    private static final String OTHER_USER_EMAIL = "bob@gmail.com";

    @BeforeEach
    void setUp() {
        signUpUser();
    }

    @Test
    void 댓글_작성_성공_테스트() {
        String articleId = getArticleId();
        댓글_작성(articleId)
                .expectStatus()
                .is3xxRedirection()
                .expectHeader()
                .valueMatches("location", ".+/articles/" + articleId);
    }

    @Test
    void 댓글_수정_성공_테스트() {
        String articleId = getArticleId();
        String commentId = getCommentId(articleId);

        loggedInPutRequest("/comment/" + articleId + "/" + commentId)
                .body(BodyInserters
                        .fromFormData("contents", NEW_CONTENTS))
                .exchange()
                .expectStatus()
                .is3xxRedirection()
                .expectHeader()
                .valueMatches("location", ".+/articles/" + articleId);
    }

    @Test
    void 작성자가_아닌_경우_댓글_수정_살패_테스트() {
        signUpUser(OTHER_USER_NAME, OTHER_USER_PASSWORD, OTHER_USER_EMAIL);

        String articleId = getArticleId();
        String commentId = getCommentId(articleId);
        loggedInPutRequest("/comment/" + articleId + "/" + commentId, OTHER_USER_EMAIL, OTHER_USER_PASSWORD)
                .body(BodyInserters
                        .fromFormData("contents", NEW_CONTENTS))
                .exchange()
                .expectStatus()
                .is3xxRedirection()
                .expectHeader()
                .valueMatches("location", ".+/");
    }

    @Test
    void 댓글_삭제_성공_테스트() {
        String articleId = getArticleId();
        String commentId = getCommentId(articleId);

        loggedInDeleteRequest("/comment/" + articleId + "/" + commentId)
                .exchange()
                .expectStatus()
                .is3xxRedirection()
                .expectHeader()
                .valueMatches("location", ".+/articles/" + articleId);
    }

    @Test
    void 작성자가_아닌_경우_댓글_삭제_실패_테스트() {
        signUpUser(OTHER_USER_NAME, OTHER_USER_PASSWORD, OTHER_USER_EMAIL);

        String articleId = getArticleId();
        String commentId = getCommentId(articleId);
        loggedInDeleteRequest("/comment/" + articleId + "/" + commentId, OTHER_USER_EMAIL, OTHER_USER_PASSWORD)
                .exchange()
                .expectStatus()
                .is3xxRedirection()
                .expectHeader()
                .valueMatches("location", ".+/");
    }

    private String getArticleId() {
        String path = 게시글_작성()
                .expectBody()
                .returnResult()
                .getResponseHeaders()
                .getLocation()
                .getPath();

        log.debug("article path : {} ", path);
        return path.split("/")[2];
    }

    private WebTestClient.ResponseSpec 게시글_작성() {
        String title = "title";
        String contents = "contents";
        String coverUrl = "coverUrl";

        return loggedInPostRequest("/articles")
                .body(BodyInserters
                        .fromFormData("title", title)
                        .with("contents", contents)
                        .with("coverUrl", coverUrl))
                .exchange();
    }

    private WebTestClient.ResponseSpec 댓글_작성(String articleId) {
        return loggedInPostRequest("/comment/" + articleId)
                .body(BodyInserters
                        .fromFormData("contents", CONTENTS))
                .exchange();
    }

    private String getCommentId(String articleId) {
        String path = 댓글_작성(articleId)
                .expectBody()
                .returnResult()
                .getResponseHeaders()
                .getLocation()
                .getPath();

        log.debug("comment path : {} ", path);
        return path.split("/")[2];
    }
}
