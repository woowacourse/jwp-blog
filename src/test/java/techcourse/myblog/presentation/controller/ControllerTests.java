package techcourse.myblog.presentation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.springframework.web.reactive.function.BodyInserters.fromFormData;

public abstract class ControllerTests {
    private static Long userId = 2L;
    private static Long articleId = 1L;
    private static Long commentId = 2L;

    @Autowired
    WebTestClient webTestClient;

    @LocalServerPort
    public int portNo;

    Long registerUser(String name, String email, String password) {
        userId++;

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
        articleId++;

        webTestClient.post().uri("/articles")
                .header("Cookie", sessionId)
                .body(fromFormData("title", title)
                        .with("coverUrl", coverUrl)
                        .with("contents", contents))
                .exchange();

        return articleId;
    }

    private void deleteArticle(Long articleId, String sessionId) {
        webTestClient.delete().uri("/articles/" + articleId)
                .header("Cookie", sessionId)
                .exchange();
    }

    Long countUser() {
        return ++userId;
    }

    Long countArticle() {
        return ++articleId;
    }

    Long countComment() {
        return ++commentId;
    }

    public static Long getUserId() {
        return userId;
    }

    public static Long getArticleId() {
        return articleId;
    }

    public static Long getCommentId() {
        return commentId;
    }
}
