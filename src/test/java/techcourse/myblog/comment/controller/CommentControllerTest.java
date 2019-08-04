package techcourse.myblog.comment.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.web.reactive.function.BodyInserters;
import techcourse.myblog.template.LoginTemplate;

public class CommentControllerTest extends LoginTemplate {
    private static final String CONTENTS = "댓글썼당";
    private static final String SECOND_USER_NAME = "두번째유저";
    private static final String SECOND_USER_EMAIL = "second@user.com";
    private static final String SECOND_USER_PASSWORD = "Second22@";

    @BeforeEach
    void setUp() {
        registeredWebTestClient();
    }

    @Test
    void 댓글이_정상적으로_등록되는지_테스트() {
        String articleId = getNewArticleId();

        loggedInPostRequest("/comment/" + articleId)
                .body(BodyInserters.fromFormData("contents", CONTENTS))
                .exchange()
                .expectStatus().isFound()
                .expectHeader().valueMatches("Location", "http://localhost:[0-9]+/articles/" + articleId + ".*");
    }

    @Test
    void 댓글이_정상적으로_수정되는지_테스트() {
        String articleId = getNewArticleId();
        String commentId = getNewCommentId(articleId);

        loggedInPutRequest("/comment/" + articleId + "/" + commentId)
                .body(BodyInserters.fromFormData("contents", CONTENTS))
                .exchange()
                .expectStatus().isFound()
                .expectHeader().valueMatches("Location", "http://localhost:[0-9]+/articles/" + articleId + ".*");
    }

    @Test
    void 수정하려는_사용자가_댓글_작성자가_아닌_경우_예외처리() {
        registeredWebTestClient(SECOND_USER_NAME, SECOND_USER_EMAIL, SECOND_USER_PASSWORD);
        String articleId = getNewArticleId();
        String commentId = getNewCommentId(articleId);

        loggedInPutRequest("/comment/" + articleId + "/" + commentId, SECOND_USER_EMAIL, SECOND_USER_PASSWORD)
                .body(BodyInserters.fromFormData("contents", CONTENTS))
                .exchange()
                .expectStatus().isFound()
                .expectHeader().valueMatches("Location", "http://localhost:[0-9]+/");
    }

    @Test
    void 댓글이_정상적으로_삭제되는지_테스트() {
        String articleId = getNewArticleId();
        String commentId = getNewCommentId(articleId);

        loggedInDeleteRequest("/comment/" + articleId + "/" + commentId)
                .exchange()
                .expectStatus().isFound()
                .expectHeader().valueMatches("Location", "http://localhost:[0-9]+/articles/" + articleId + ".*");

    }

    @Test
    void 삭제하려는_사용자가_댓글_작성자가_아닌_경우_예외처리() {
        registeredWebTestClient(SECOND_USER_NAME, SECOND_USER_EMAIL, SECOND_USER_PASSWORD);
        String articleId = getNewArticleId();
        String commentId = getNewCommentId(articleId);

        loggedInDeleteRequest("/comment/" + articleId + "/" + commentId, SECOND_USER_EMAIL, SECOND_USER_PASSWORD)
                .exchange()
                .expectStatus().isFound()
                .expectHeader().valueMatches("Location", "http://localhost:[0-9]+/");
    }

    private String getNewArticleId() {
        return articlePostRequest()
                .getResponseHeaders()
                .getLocation()
                .getPath().split("/")[2];
    }

    private String getNewCommentId(String articleId) {
        return loggedInPostRequest("/comment/" + articleId)
                .body(BodyInserters.fromFormData("contents", CONTENTS))
                .exchange()
                .expectStatus().isFound()
                .expectBody().returnResult()
                .getResponseHeaders()
                .getLocation()
                .getPath().split("/")[2];
    }

    public EntityExchangeResult<byte[]> articlePostRequest() {
        String title = "제목";
        String contents = "본문";
        String coverUrl = "배경";

        return loggedInPostRequest("/articles")
                .body(BodyInserters
                        .fromFormData("title", title)
                        .with("contents", contents)
                        .with("coverUrl", coverUrl))
                .exchange()
                .expectBody()
                .returnResult();
    }
}
