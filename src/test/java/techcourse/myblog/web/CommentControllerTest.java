package techcourse.myblog.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;
import techcourse.myblog.dto.comment.CommentRequest;

public class CommentControllerTest extends LoggedInTemplate {

    private static final Logger log = LoggerFactory.getLogger(CommentControllerTest.class);

    private static final String CONTENTS = "comment";

    private static final String OTHER_USER_NAME = "bob";
    private static final String OTHER_USER_PASSWORD = "Password1!";
    private static final String OTHER_USER_EMAIL = "bob@gmail.com";

    private static final String COMMENT_ID = "1";

    private CommentRequest commentRequest;
    private String articleId;

    @BeforeEach
    void setUp() {
        signUpUser();
        commentRequest = new CommentRequest(CONTENTS);
        articleId = getArticleId();
        댓글_작성(articleId);
    }

    @Test
    void 댓글_작성_성공() {
        댓글_작성(articleId).expectStatus()
                .isOk();
    }

    @Test
    void 댓글_수정_성공_테스트() {
        loggedInPutRequest("/comments/" + articleId + "/" + COMMENT_ID)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(commentRequest), CommentRequest.class)
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    void 작성자가_아닌_경우_댓글_수정_살패_테스트() {
        signUpUser(OTHER_USER_NAME, OTHER_USER_PASSWORD, OTHER_USER_EMAIL);

        loggedInPutRequest("/comments/" + articleId + "/" + COMMENT_ID, OTHER_USER_EMAIL, OTHER_USER_PASSWORD)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(commentRequest), CommentRequest.class)
                .exchange()
                .expectStatus()
                .is4xxClientError()
                .expectHeader()
                .valueMatches("location", ".+/");
    }

    @Test
    void 댓글_삭제_성공_테스트() {
        loggedInDeleteRequest("/comments/" + articleId + "/" + COMMENT_ID)
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    void 작성자가_아닌_경우_댓글_삭제_실패_테스트() {
        signUpUser(OTHER_USER_NAME, OTHER_USER_PASSWORD, OTHER_USER_EMAIL);

        loggedInDeleteRequest("/comments/" + articleId + "/" + COMMENT_ID, OTHER_USER_EMAIL, OTHER_USER_PASSWORD)
                .exchange()
                .expectStatus()
                .is4xxClientError()
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
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("title", title)
                        .with("contents", contents)
                        .with("coverUrl", coverUrl))
                .exchange();
    }

    private WebTestClient.ResponseSpec 댓글_작성(String articleId) {
        return loggedInPostRequest("/comments/" + articleId + "/" + COMMENT_ID)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(commentRequest), CommentRequest.class)
                .exchange();
    }
}
