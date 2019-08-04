package techcourse.myblog.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import java.util.Objects;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CommentControllerTests {
    private static final Long DEFAULT_ARTICLE_ID = 999L;
    private static final Long DEFAULT_COMMENT_ID = 999L;
    private static final int AUTO_INCREMENT_ID = 1;

    @Autowired
    private WebTestClient webTestClient;
    private String jSessionId;

    @BeforeEach
    void setUp() {
        jSessionId = getJSessionId("paul123@example.com", "p@ssW0rd");
    }

    private String getJSessionId(String email, String password) {
        EntityExchangeResult<byte[]> loginResult = webTestClient.post().uri("/users/login")
                .body(BodyInserters
                        .fromFormData("email", email)
                        .with("password", password))
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueMatches("Location", ".*/.*")
                .expectBody()
                .returnResult();

        return extractJSessionId(loginResult);
    }

    private String extractJSessionId(EntityExchangeResult<byte[]> loginResult) {
        String[] cookies = loginResult.getResponseHeaders().get("Set-Cookie").stream()
                .filter(it -> it.contains("JSESSIONID"))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("JSESSIONID가 없습니다."))
                .split(";");
        return Stream.of(cookies)
                .filter(it -> it.contains("JSESSIONID"))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("JSESSIONID가 없습니다."))
                .split("=")[1];
    }

    @Test
    void 로그인한_상태로_댓글_작성() {
        webTestClient.post().uri("/articles/" + DEFAULT_ARTICLE_ID + "/comments")
                .cookie("JSESSIONID", jSessionId)
                .body(BodyInserters.fromFormData("comment", "hello"))
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueMatches("Location", ".*/articles/\\d*");

        webTestClient.get().uri("/articles/" + DEFAULT_ARTICLE_ID)
                .exchange()
                .expectBody()
                .consumeWith(response -> {
                    String body = new String(Objects.requireNonNull(response.getResponseBody()));
                    assertThat(body).contains("hello");
                });
    }

    @Test
    void 로그인하지_않은_상태로_댓글_작성() {
        webTestClient.post().uri("/articles/" + DEFAULT_ARTICLE_ID + "/comments")
                .body(BodyInserters.fromFormData("comment", "hello"))
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueMatches("Location", ".*/login");
    }

    @Test
    void 로그인하지_않은_상태로_댓글_수정() {
        webTestClient.put().uri("/articles/" + DEFAULT_ARTICLE_ID + "/comments/" + DEFAULT_COMMENT_ID)
                .body(BodyInserters.fromFormData("comment", "newHello"))
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueMatches("Location", ".*/login");
    }

    @Test
    void 댓글작성자가_댓글_수정() {
        webTestClient.put().uri("/articles/" + DEFAULT_ARTICLE_ID + "/comments/" + DEFAULT_COMMENT_ID)
                .cookie("JSESSIONID", jSessionId)
                .body(BodyInserters.fromFormData("comment", "newHello"))
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueMatches("Location", ".*/articles/\\d*");
    }

    @Test
    void 댓글작성자가_아닌_회원이_댓글_수정() {
        String outsiderJSessionId = getJSessionId("john123@example.com", "p@ssW0rd");

        webTestClient.put().uri("/articles/" + DEFAULT_ARTICLE_ID + "/comments/" + DEFAULT_COMMENT_ID)
                .cookie("JSESSIONID", outsiderJSessionId)
                .body(BodyInserters.fromFormData("comment", "newHello"))
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueMatches("Location", ".*/");
    }

    @Test
    void 로그인하지_않은_상태로_댓글_삭제() {
        webTestClient.delete().uri("/articles/" + DEFAULT_ARTICLE_ID + "/comments/" + DEFAULT_COMMENT_ID)
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueMatches("Location", ".*/login");
    }

    @Test
    void 댓글작성자가_댓글_삭제() {
        webTestClient.post().uri("/articles/" + DEFAULT_ARTICLE_ID + "/comments")
                .cookie("JSESSIONID", jSessionId)
                .body(BodyInserters.fromFormData("comment", "hello"))
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueMatches("Location", ".*/articles/\\d*");

        webTestClient.delete().uri("/articles/" + DEFAULT_ARTICLE_ID + "/comments/" + (DEFAULT_COMMENT_ID + AUTO_INCREMENT_ID))
                .cookie("JSESSIONID", jSessionId)
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueMatches("Location", ".*/articles/\\d*");
    }

    @Test
    void 댓글작성자가_아닌_회원이_댓글_삭제() {
        String outsiderJSessionId = getJSessionId("john123@example.com", "p@ssW0rd");

        webTestClient.delete().uri("/articles/" + DEFAULT_ARTICLE_ID + "/comments/" + DEFAULT_COMMENT_ID)
                .cookie("JSESSIONID", outsiderJSessionId)
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueMatches("Location", ".*/");
    }
}
