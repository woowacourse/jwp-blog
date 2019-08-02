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

import javax.persistence.EntityResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.web.reactive.function.BodyInserters.fromFormData;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CommentControllerTests {

    private static final Logger log = LoggerFactory.getLogger(CommentControllerTests.class);
    private static String EMAIL = "hard@gmail.com";
    private static String NAME = "hard";
    private static String PASSWORD = "qwerasdf";

    private static int ID = 3;

    private WebTestClient webTestClient;

    @Autowired
    public CommentControllerTests(WebTestClient webTestClient) {
        this.webTestClient = webTestClient;
    }

    @Test
    void new_comment_test() {
        //회원가입
        registerUser();
        //로그인
        String sessionId = logInAndGetSessionId();
        //글작성
        EntityExchangeResult result = writeArticle(sessionId);
        //댓글작성
        String articleUri = result.getResponseHeaders().getLocation().getPath();
        writeComment(articleUri, sessionId);
        log.info("url: " + articleUri);

        //댓글 작성 확인
        webTestClient.get().uri(articleUri)
                .exchange()
                .expectBody().consumeWith(response -> {
            String body = new String(response.getResponseBody());
            assertThat(body.contains("444")).isTrue();
            assertThat(body.contains("445")).isFalse();
        });

        deleteArticle(articleUri);
        ID ++;
    }

    @Test
    void delete_comment_test() {
        //회원가입
        registerUser();
        //로그인
        String sessionId = logInAndGetSessionId();
        //글작성
        EntityExchangeResult result = writeArticle(sessionId);
        //댓글작성
        String articleUri = result.getResponseHeaders().getLocation().getPath();
        writeComment(articleUri, sessionId);

        webTestClient.delete().uri(articleUri+"/comments/" + ID)
                .header("Cookie", sessionId)
                .exchange()
                .expectStatus().is3xxRedirection();

        webTestClient.get().uri(articleUri)
                .exchange()
                .expectBody().consumeWith(response -> {
            String body = new String(response.getResponseBody());
            assertThat(body.contains("444")).isFalse();
        });

        deleteArticle(articleUri);
        ID++;
    }

    @Test
    void update_comment_test() {
        //회원가입
        registerUser();
        //로그인
        String sessionId = logInAndGetSessionId();
        //글작성
        EntityExchangeResult result = writeArticle(sessionId);
        //댓글작성
        String articleUri = result.getResponseHeaders().getLocation().getPath();
        writeComment(articleUri, sessionId);

        webTestClient.put().uri(articleUri+"/comments/" + ID)
                .header("Cookie", sessionId)
                .body(BodyInserters.fromFormData("contents", "7788"))
                .exchange()
                .expectStatus()
                .is3xxRedirection();

        webTestClient.get().uri(articleUri)
                .exchange()
                .expectBody().consumeWith(response -> {
            String body = new String(response.getResponseBody());
            assertThat(body.contains("7788")).isTrue();
        });

        deleteArticle(articleUri);
        ID ++;
    }

    @Test
    void update_comment_test_when_not_logged_in() {
        //회원가입
        registerUser();
        //로그인
        String sessionId = logInAndGetSessionId();
        //글작성
        EntityExchangeResult result = writeArticle(sessionId);
        //댓글작성
        String articleUri = result.getResponseHeaders().getLocation().getPath();
        writeComment(articleUri, sessionId);
        //로그아웃


    }

    private void writeComment(String articleUri, String sessionId) {
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
    }

    private void registerUser() {
        webTestClient.post()
                .uri("/users")
                .body(fromFormData("name", NAME)
                        .with("email", EMAIL)
                        .with("password", PASSWORD))
                .exchange()
                .expectStatus().isFound();
    }

    private EntityExchangeResult writeArticle(String sessionId) {
        return webTestClient.post().uri("/articles")
                .header("Cookie", sessionId)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("title", "111")
                        .with("coverUrl", "222")
                        .with("contents", "333"))
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectBody().returnResult();
    }

    private String logInAndGetSessionId() {
        return webTestClient.post()
                .uri("/login").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(fromFormData("email", EMAIL)
                        .with("password", PASSWORD))
                .exchange()
                .returnResult(String.class).getResponseHeaders().getFirst("Set-Cookie");
    }

    private void deleteArticle(String articleUri) {
        webTestClient.delete().uri(articleUri)
                .exchange();
    }
}