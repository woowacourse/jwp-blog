package techcourse.myblog.presentation.controller;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.web.reactive.function.BodyInserters.fromFormData;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CommentControllerTests {

    private static final Logger log = LoggerFactory.getLogger(CommentControllerTests.class);

    private static String EMAIL = "hard@gmail.com";
    private static String NAME = "hard";
    private static String PASSWORD = "qwerasdf";

    private static Long commentId = 3L;

    @Autowired
    WebTestClient webTestClient;

    @Test
    void new_comment_test() {
        register();
        String sessionId = logInAndGetSessionId();
        String articleUri = postArticle(sessionId);

        // 댓글 작성
        webTestClient.post().uri(articleUri + "/comments")
                .header("Cookie", sessionId)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("contents", "444"))
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectBody().consumeWith(body -> {
            assertThat(body.getResponseHeaders().getLocation().getPath()).isEqualTo(articleUri);
        });

        // 댓글 조회 (생성 확인)
        webTestClient.get().uri(articleUri)
                .exchange()
                .expectBody().consumeWith(response -> {
            String body = new String(response.getResponseBody());
            assertThat(body.contains("444")).isTrue();
            assertThat(body.contains("445")).isFalse();
        });


        // 댓글 수정
        webTestClient.put().uri(articleUri + "/comments/" + commentId)
                .header("Cookie", sessionId)
                .body(BodyInserters
                        .fromFormData("contents", "445"))
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectBody().consumeWith(body -> {
            assertThat(body.getResponseHeaders().getLocation().getPath()).isEqualTo(articleUri);
        });

        // 댓글 조회 (수정 확인)
        webTestClient.get().uri(articleUri)
                .exchange()
                .expectBody().consumeWith(response -> {
            String body = new String(response.getResponseBody());
            assertThat(body.contains("444")).isFalse();
            assertThat(body.contains("445")).isTrue();
        });

        // 댓글 삭제
        webTestClient.delete().uri(articleUri + "/comments/" + commentId)
                .header("Cookie", sessionId)
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectBody().consumeWith(body -> {
            assertThat(body.getResponseHeaders().getLocation().getPath()).isEqualTo(articleUri);
        });


        // 댓글 조회 (삭제 확인)
        webTestClient.get().uri(articleUri)
                .exchange()
                .expectBody().consumeWith(response -> {
            String body = new String(response.getResponseBody());
            assertThat(body.contains("444")).isFalse();
            assertThat(body.contains("445")).isFalse();
        });
    }

    private String postArticle(String sessionId) {
        EntityExchangeResult result = webTestClient.post().uri("/articles")
                .header("Cookie", sessionId)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("title", "111")
                        .with("coverUrl", "222")
                        .with("contents", "333"))
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectBody().returnResult();
        return result.getResponseHeaders().getLocation().getPath();
    }

    private void register() {
        webTestClient.post()
                .uri("/users")
                .body(fromFormData("name", NAME)
                        .with("email", EMAIL)
                        .with("password", PASSWORD))
                .exchange()
                .expectStatus().isFound();
    }

    private String logInAndGetSessionId() {
        return webTestClient.post()
                .uri("/login").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(fromFormData("email", EMAIL)
                        .with("password", PASSWORD))
                .exchange()
                .returnResult(String.class).getResponseHeaders().getFirst("Set-Cookie");
    }
}