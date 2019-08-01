package techcourse.myblog.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static techcourse.myblog.web.ControllerTestUtil.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CommentControllerTests {
    private static final Long DEFAULT_ARTICLE_ID = 999L;
    private static final Long DEFAULT_COMMENT_ID = 999L;

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void 로그인한_상태로_댓글_작성() {
        // Given
        String sid = postLoginSync(webTestClient, "paul123@example.com", "p@ssW0rd")
            .getResponseCookies().getFirst(KEY_JSESSIONID).getValue();

        // When
        webTestClient.post().uri("/articles/" + DEFAULT_ARTICLE_ID + "/comments")
            .cookie(KEY_JSESSIONID, sid)
            .body(BodyInserters.fromFormData("contents", "hello9"))
            .exchange()
            .expectStatus().is3xxRedirection()
            .expectHeader().valueMatches("Location", ".*/articles/\\d*");

        // Then
        webTestClient.get().uri("/articles/" + DEFAULT_ARTICLE_ID)
            .exchange()
            .expectBody()
            .consumeWith(response -> {
                assertThat(new String(response.getResponseBody()))
                    .contains("hello9");
            });
    }

    @Test
    void 로그인하지_않은_상태로_댓글_작성() {
        webTestClient.post().uri("/articles/" + DEFAULT_ARTICLE_ID + "/comments")
            .body(BodyInserters.fromFormData("contents", "hello"))
            .exchange()
            .expectStatus().is3xxRedirection()
            .expectHeader().valueMatches("Location", ".*/login");
    }

    @Test
    void 로그인하지_않은_상태로_댓글_수정() {
        webTestClient.put().uri("/articles/" + DEFAULT_ARTICLE_ID + "/comments/" + DEFAULT_COMMENT_ID)
            .body(BodyInserters.fromFormData("contents", "newHello"))
            .exchange()
            .expectStatus().is3xxRedirection()
            .expectHeader().valueMatches("Location", ".*/login");
    }

    @Test
    void 댓글작성자가_댓글_수정() {
        String sid = postLoginSync(webTestClient, "paul123@example.com", "p@ssW0rd")
            .getResponseCookies().getFirst(KEY_JSESSIONID).getValue();

        webTestClient.put().uri("/articles/" + DEFAULT_ARTICLE_ID + "/comments/" + 1000)
            .cookie(KEY_JSESSIONID, sid)
            .body(BodyInserters.fromFormData("contents", "newHello"))
            .exchange()
            .expectStatus().is3xxRedirection()
            .expectHeader().valueMatches("Location", ".*/articles/\\d*");

        assertThat(new String(getSync(webTestClient, "/articles/" + DEFAULT_ARTICLE_ID, sid)
            .getResponseBody()))
            .contains("newHello");
    }

    @Test
    void 댓글작성자가_아닌_회원이_댓글_수정() {
        String sid = postLoginSync(webTestClient, "john123@example.com", "p@ssW0rd")
            .getResponseCookies()
            .getFirst(KEY_JSESSIONID).getValue();

        webTestClient.put().uri("/articles/" + DEFAULT_ARTICLE_ID + "/comments/" + DEFAULT_COMMENT_ID)
            .cookie(KEY_JSESSIONID, sid)
            .body(BodyInserters.fromFormData("contents", "newHello"))
            .exchange()
            .expectStatus().is3xxRedirection()
            .expectHeader().valueMatches("Location", ".*/articles/\\d*");
    }

    @Test
    void 로그인하지_않은_상태로_댓글_삭제() {
        webTestClient.delete().uri("/articles/" + DEFAULT_ARTICLE_ID + "/comments/" + DEFAULT_COMMENT_ID)
            .exchange()
            .expectStatus().is3xxRedirection()
            .expectHeader().valueMatches("Location", ".*/login");

        assertThat(new String(getSync(webTestClient, "/articles/" + DEFAULT_ARTICLE_ID, null)
            .getResponseBody()))
            .contains("hello1");
    }

    @Test
    void 댓글작성자가_댓글_삭제() {
        // Given
        String sid = postLoginSync(webTestClient, "paul123@example.com", "p@ssW0rd")
            .getResponseCookies().getFirst(KEY_JSESSIONID).getValue();

        // When
        webTestClient.delete().uri("/articles/" + DEFAULT_ARTICLE_ID + "/comments/" + 1001)
            .cookie(KEY_JSESSIONID, sid)
            .exchange()
            .expectStatus().is3xxRedirection()
            .expectHeader().valueMatches("Location", ".*/articles/.*")
            .expectBody()
            .returnResult();

        // Then
        assertThat(new String(getSync(webTestClient, "/articles/" + DEFAULT_ARTICLE_ID, null)
            .getResponseBody()))
            .doesNotContain("hello3");
    }

    @Test
    void 댓글작성자가_아닌_회원이_댓글_삭제() {
        // Given
        String sid = postLoginSync(webTestClient, "john123@example.com", "p@ssW0rd")
            .getResponseCookies().getFirst(KEY_JSESSIONID).getValue();

        // When
        webTestClient.delete().uri("/articles/" + DEFAULT_ARTICLE_ID + "/comments/" + DEFAULT_COMMENT_ID)
            .cookie(KEY_JSESSIONID, sid)
            .exchange()
            .expectStatus().is3xxRedirection()
            .expectHeader().valueMatches("Location", ".*/articles/\\d*");

        // Then
        assertThat(new String(getSync(webTestClient, "/articles/" + DEFAULT_ARTICLE_ID, sid)
            .getResponseBody()))
            .contains("hello1");
    }
}
