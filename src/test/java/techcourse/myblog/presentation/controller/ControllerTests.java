package techcourse.myblog.presentation.controller;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import techcourse.myblog.application.dto.CommentDto;

import static org.springframework.web.reactive.function.BodyInserters.fromFormData;
import static org.springframework.web.reactive.function.BodyInserters.fromObject;

public abstract class ControllerTests {
    private static final Logger log = LoggerFactory.getLogger(ControllerTests.class);

    private static Long userId = 2L;
    private static Long articleId = 0L;
    private static Long commentId = 0L;

    @Autowired
    WebTestClient webTestClient;

    @LocalServerPort
    public int portNo;

    Gson gson = new Gson();

    Long registerUser(String name, String email, String password) {
        userId += 1;
        log.info("[ADD USER] - " + userId);
        log.info("[ARTICLE] - " + getArticleId());
        log.info("[COMMENT] - " + getCommentId());

        webTestClient.post()
                .uri("/users")
                .body(fromFormData("name", name)
                        .with("email", email)
                        .with("password", password))
                .exchange()
                .expectStatus().isFound();

        return userId;
    }

    void deleteUser(String sessionId) {
        webTestClient.delete().uri("/users")
                .header("Cookie", sessionId)
                .exchange();
    }


    String logInAndGetSessionId(String email, String password) {
        return webTestClient.post()
                .uri("/login").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(fromFormData("email", email)
                        .with("password", password))
                .exchange()
                .returnResult(String.class).getResponseHeaders().getFirst("Set-Cookie");
    }

    Long createArticle(String title, String coverUrl, String contents, String sessionId) {
        articleId += 1;
        log.info("[USER] - " + getUserId());
        log.info("[ADD ARTICLE] - " + articleId);
        log.info("[COMMENT] - " + getCommentId());

        webTestClient.post().uri("/articles")
                .header("Cookie", sessionId)
                .body(fromFormData("title", title)
                        .with("coverUrl", coverUrl)
                        .with("contents", contents))
                .exchange()
                .expectBody().returnResult();

        return articleId;
    }

    Long createComments(String contents, Long articleId, String sessionId) {
        commentId += 1;
        log.info("[USER] - " + getUserId());
        log.info("[ARTICLE] - " + getArticleId());
        log.info("[ADD COMMENT] - " + commentId);

        webTestClient.post().uri("/articles/" + articleId + "/comments")
                .header("Cookie", sessionId)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(fromObject(gson.toJson(new CommentDto(null, contents, null))))
                .exchange();

        return commentId;
    }

    Long countUser() {
        userId += 1;
        log.info("[ADD USER] - " + userId);
        log.info("[ARTICLE] - " + getArticleId());
        log.info("[COMMENT] - " + getCommentId());
        return userId;
    }

    Long countArticle() {
        articleId += 1;
        log.info("[USER] - " + getUserId());
        log.info("[ADD ARTICLE] - " + articleId);
        log.info("[COMMENT] - " + getCommentId());
        return articleId;
    }

    Long countComment() {
        commentId += 1;
        log.info("[USER] - " + getUserId());
        log.info("[ARTICLE] - " + getArticleId());
        log.info("[ADD COMMENT] - " + commentId);
        return commentId;
    }

    Long getUserId() {
        return userId;
    }

    Long getCommentId() {
        return commentId;
    }

    public static Long getArticleId() {
        return articleId;
    }
}
