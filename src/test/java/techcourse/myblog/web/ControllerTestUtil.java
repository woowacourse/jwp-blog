package techcourse.myblog.web;

import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

public class ControllerTestUtil {
    public static final String KEY_JSESSIONID = "JSESSIONID";

    public static final String DEFAULT_USER_EMAIL = "john123@example.com";
    public static final String DEFAULT_USER_PASSWORD = "p@ssW0rd";

    public static EntityExchangeResult<byte[]> postLoginSync(WebTestClient webTestClient, String email, String password) {
        return webTestClient.post()
            .uri("/users/login")
            .body(BodyInserters.fromFormData("email", email)
                .with("password", password))
            .exchange()
            .expectBody()
            .returnResult();
    }

    public static EntityExchangeResult<byte[]> postArticleSync(WebTestClient webTestClient, String title, String coverUrl, String contents, String sid) {
        return webTestClient.post().uri("/articles")
            .cookie(KEY_JSESSIONID, sid)
            .body(BodyInserters.fromFormData("title", title)
                .with("coverUrl", coverUrl)
                .with("contents", contents))
            .exchange()
            .expectBody()
            .returnResult();
    }

    public static EntityExchangeResult<byte[]> postCommentSync(WebTestClient webTestClient, Long articleId, String contents) {
        return webTestClient.post()
            .uri("/articles/" + articleId + "/comments")
            .body(BodyInserters.fromFormData("contents", contents))
            .exchange()
            .expectBody()
            .returnResult();
    }

    public static EntityExchangeResult<byte[]> getSync(WebTestClient webTestClient, String uri, String sid) {
        return webTestClient.get()
            .uri(uri)
            .cookie(KEY_JSESSIONID, sid)
            .exchange()
            .expectBody()
            .returnResult();
    }
}
