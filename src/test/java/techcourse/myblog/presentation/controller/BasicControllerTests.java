package techcourse.myblog.presentation.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.web.reactive.function.BodyInserters.fromFormData;

@AutoConfigureWebClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BasicControllerTests {
    protected static final Logger log = LoggerFactory.getLogger(CommentControllerTests.class);
    protected static String EMAIL = "hard@gmail.com";
    protected static String NAME = "hard";
    protected static String PASSWORD = "qwerasdf";
    protected static String sessionId;
    protected static EntityExchangeResult result;
    protected static String articleUri;
    protected static Consumer<EntityExchangeResult<byte[]>> consumer;

    protected static int ID = 3;

    @Autowired
    protected WebTestClient webTestClient;

    protected void checkTestPass(String checkCondition, Boolean result) {
        makeConsumer(checkCondition, result);
        webTestClient.get().uri(articleUri)
                .exchange()
                .expectBody().consumeWith(consumer);
    }


    private void makeConsumer(String checkCondition, Boolean result) {
        consumer = (response) -> {
            String body = new String(response.getResponseBody());
            assertThat(body.contains(checkCondition)).isEqualTo(result);
        };
    }

    protected void prepareTestForComment() {
        registerUser();

        sessionId = logInAndGetSessionId();
        result = writeArticle(sessionId);

        articleUri = result.getResponseHeaders().getLocation().getPath();
        writeComment(articleUri, sessionId);
    }

    protected void finishTestForComment() {
        deleteArticle(articleUri);
        ID++;
    }

    protected String logInAndGetSessionIdDiff() {
        return webTestClient.post()
                .uri("/login").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(fromFormData("email", "easy@gmail.com")
                        .with("password", PASSWORD))
                .exchange()
                .returnResult(String.class).getResponseHeaders().getFirst("Set-Cookie");
    }

    protected void writeComment(String articleUri, String sessionId) {
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

    protected void registerUser() {
        webTestClient.post()
                .uri("/users")
                .body(fromFormData("name", NAME)
                        .with("email", EMAIL)
                        .with("password", PASSWORD))
                .exchange()
                .expectStatus().isFound();
    }

    protected EntityExchangeResult writeArticle(String sessionId) {
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

    protected String logInAndGetSessionId() {
        return webTestClient.post()
                .uri("/login").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(fromFormData("email", EMAIL)
                        .with("password", PASSWORD))
                .exchange()
                .returnResult(String.class).getResponseHeaders().getFirst("Set-Cookie");
    }

    protected void deleteArticle(String articleUri) {
        webTestClient.delete().uri(articleUri)
                .exchange();
    }

}
