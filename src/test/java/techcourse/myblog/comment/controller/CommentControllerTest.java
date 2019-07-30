package techcourse.myblog.comment.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import techcourse.myblog.comment.CommentDataForTest;
import techcourse.myblog.util.ArticleUtilForTest;
import techcourse.myblog.util.UserUtilForTest;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CommentControllerTest {
    private static long commentId = 1;

    @Autowired
    private WebTestClient webTestClient;

    private String cookie;
    private String path;

    @BeforeEach
    void setUp() {
        UserUtilForTest.signUp(webTestClient);
        cookie = UserUtilForTest.loginAndGetCookie(webTestClient);
        path = ArticleUtilForTest.createArticle(webTestClient, cookie);

        webTestClient.post()
                .uri(path + "/comments")
                .header("Cookie", cookie)
                .body(BodyInserters.fromFormData("contents", CommentDataForTest.COMMENT_CONTENTS))
                .exchange();
    }

    @Test
    void 댓글_수정_페이지_이동_테스트() {
        webTestClient.get()
                .uri("/comments/" + commentId + "/edit")
                .header("Cookie", cookie)
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    void 댓글_업데이트_테스트() {
        webTestClient.put()
                .uri(path + "/comments/" + commentId)
                .header("Cookie", cookie)
                .body(BodyInserters.fromFormData("contents", CommentDataForTest.UPDATED_CONTENTS))
                .exchange()
                .expectStatus()
                .isFound()
                .expectBody()
                .consumeWith(res -> {
                    URI location = res.getResponseHeaders().getLocation();
                    webTestClient.get()
                            .uri(location)
                            .header("Cookie", cookie)
                            .exchange()
                            .expectStatus()
                            .isOk()
                            .expectBody()
                            .consumeWith(response -> {
                                String body = new String(response.getResponseBody());
                                assertThat(body.contains(CommentDataForTest.UPDATED_CONTENTS)).isTrue();
                            });
                });
    }

    @Test
    void 댓글_삭제_테스트() {
        webTestClient.delete()
                .uri(path + "/comments/" + commentId)
                .header("Cookie", cookie)
                .exchange()
                .expectStatus()
                .isFound()
                .expectBody()
                .consumeWith(res -> {
                    URI location = res.getResponseHeaders().getLocation();
                    webTestClient.get()
                            .uri(location)
                            .header("Cookie", cookie)
                            .exchange()
                            .expectStatus()
                            .isOk()
                            .expectBody()
                            .consumeWith(response -> {
                                String body = new String(response.getResponseBody());
                                assertThat(body.contains(CommentDataForTest.COMMENT_CONTENTS)).isFalse();
                            });
                });
    }

    @AfterEach
    void tearDown() {
        commentId++;
    }
}