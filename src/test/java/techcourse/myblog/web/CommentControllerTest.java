package techcourse.myblog.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

class CommentControllerTest extends ControllerTest {
    @Autowired
    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        jSessionId = login(webTestClient);
    }

    @Test
    void 코멘트_수정_폼_테스트() {
        String commentId = "19";

        webTestClient.get().uri("/comment/" + commentId + "/edit")
                .cookie(JSESSIONID, jSessionId)
                .exchange()
                .expectStatus().isOk()
        ;
    }

    @Test
    void 코멘트_작성() {
        String articleId = "9";

        webTestClient.post().uri("/articles/" + articleId + "/comment")
                .cookie(JSESSIONID, jSessionId)
                .body(BodyInserters
                        .fromFormData("contents", "test comment contents"))
                .exchange()
                .expectStatus().is3xxRedirection()
        ;
    }

    @Test
    void 코멘트_수정() {
        String commentId = "19";

        webTestClient.put().uri("/comment/" + commentId)
                .cookie(JSESSIONID, jSessionId)
                .body(BodyInserters
                        .fromFormData("contents", "update test comment contents"))
                .exchange()
                .expectStatus().is3xxRedirection()
        ;
    }

    @Test
    void 코멘트_수정_권한없음() {
        String commentId = "19";

        webTestClient.put().uri("/comment/" + commentId)
                .cookie(JSESSIONID, login(webTestClient, "woowa@woowa.com", "12Woowa@@"))
                .body(BodyInserters
                        .fromFormData("contents", "update test comment contents"))
                .exchange()
                .expectBody()
                .returnResult().getStatus().is4xxClientError()
        ;
    }

    @Test
    void 코멘트_삭제() {
        String commentId = "19";

        webTestClient.delete().uri("/comment/" + commentId)
                .cookie(JSESSIONID, jSessionId)
                .exchange()
                .expectStatus().is3xxRedirection()
        ;
    }

    @Test
    void 코멘트_삭제_권한없음() {
        String commentId = "19";

        webTestClient.delete().uri("/comment/" + commentId)
                .cookie(JSESSIONID, login(webTestClient, "woowa@woowa.com", "12Woowa@@"))
                .exchange()
                .expectBody()
                .returnResult().getStatus().is4xxClientError()
        ;
    }
}