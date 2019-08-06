package techcourse.myblog.web;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.reactive.function.BodyInserters;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
class CommentControllerTest extends ControllerTest {

    @BeforeEach
    void setUp() {
        init();
    }

    @Test
    void 비로그인_댓글_작성_테스트() {
        webTestClient.post().uri("/comments")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(BodyInserters
                .fromFormData("contents", "datgeul").with("articleId", articleId))
            .exchange().expectStatus().isFound()
            .expectHeader().valueMatches("location", ".*/login");
    }

    @Test
    void 비로그인_댓글_수정_테스트() {
        webTestClient.put().uri("/comments/" + commentId)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(BodyInserters
                .fromFormData("contents", "update").with("articleId", articleId))
            .exchange().expectStatus().isFound()
            .expectHeader().valueMatches("location", ".*/login");
    }

    @Test
    void 댓글_수정_테스트() {
        webTestClient.put().uri("/comments/" + commentId)
            .header("Cookie", cookie)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(BodyInserters
                .fromFormData("contents", "update").with("articleId", articleId))
            .exchange().expectStatus().isFound()
            .expectHeader().valueMatches("location", ".*/articles/.*");
    }

    @Test
    void 비로그인_댓글_삭제_테스트() {
        webTestClient.delete().uri("/comments/" + commentId)
            .exchange().expectStatus().isFound()
            .expectHeader().valueMatches("location", ".*/login");
    }

    //TODO :
    @AfterEach
    void 댓글_삭제_테스트() {
        webTestClient.delete().uri("/comments/" + commentId)
            .header("Cookie", cookie)
            .exchange().expectStatus().isFound()
            .expectHeader().valueMatches("location", ".*/articles/.*");
    }
}