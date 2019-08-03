package techcourse.myblog.web;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static techcourse.myblog.user.UserTest.user;
import static techcourse.myblog.user.UserTest.user2;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CommentControllerTest extends AbstractControllerTest {

    @Test
    void 로그인_전_댓글_생성() {
        webTestClient.post().uri("/articles/1/comment")
                .body(BodyInserters
                        .fromFormData("contents", "댓글입니다."))
                .exchange()
                .expectStatus().is3xxRedirection();
    }

    @Test
    void 로그인_후_댓글_생성() {
        String jSessionId = extractJSessionId(login(user));
        webTestClient.post().uri("/articles/1/comment")
                .cookie("JSESSIONID", jSessionId)
                .body(BodyInserters
                        .fromFormData("contents", "댓글입니다."))
                .exchange()
                .expectStatus().isFound()
                .expectHeader().valueMatches("location", ".*/articles.*");
    }

    @Test
    void 로그인_후_댓글_수정() {
        String jSessionId = extractJSessionId(login(user));
        String updateComment = "수정된 댓글입니다.";
        WebTestClient.ResponseSpec responseSpec = webTestClient.put().uri("/articles/1/comment/1")
                .cookie("JSESSIONID", jSessionId)
                .body(BodyInserters
                        .fromFormData("contents", updateComment))
                .exchange()
                .expectStatus().isFound()
                .expectHeader().valueMatches("location", ".*/articles.*");

        responseSpec.expectBody()
                .consumeWith(response -> {
                    webTestClient.get().uri(Objects.requireNonNull(response.getResponseHeaders().get("Location")).get(0))
                            .exchange()
                            .expectStatus().isOk()
                            .expectBody()
                            .consumeWith(res -> {
                                String body = new String(Objects.requireNonNull(res.getResponseBody()));
                                assertThat(body.contains(updateComment)).isTrue();
                            });
                });


    }

    @Test
    void 로그인_후_댓글_삭제() {
        String jSessionId = extractJSessionId(login(user));
        webTestClient.delete().uri("/articles/1/comment/2")
                .cookie("JSESSIONID", jSessionId)
                .exchange()
                .expectStatus().isFound()
                .expectHeader().valueMatches("location", ".*/articles.*");
    }

    @Test
    void 자신이_쓰지_않은_댓글_수정_시도() {
        String jSessionId = extractJSessionId(login(user2));
        String updateComment = "수정된 댓글입니다.";

        webTestClient.put().uri("/articles/1/comment/1")
                .cookie("JSESSIONID", jSessionId)
                .body(BodyInserters
                        .fromFormData("contents", updateComment))
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueMatches("location", ".*/");
    }

    @Test
    void 자신이_쓰지_않은_댓글_삭제_시도() {
        String jSessionId = extractJSessionId(login(user2));
        webTestClient.delete().uri("/articles/1/comment/1")
                .cookie("JSESSIONID", jSessionId)
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueMatches("location", ".*/");
    }

}
