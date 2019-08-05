package techcourse.myblog.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import techcourse.myblog.service.dto.CommentResponseDto;

import static techcourse.myblog.Utils.TestConstants.SAMPLE_ARTICLE_ID;
import static techcourse.myblog.Utils.TestUtils.logInAsBaseUser;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RestCommentControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @Test
    void 비동기_댓글_조회() {
        webTestClient.get().uri("/comments/" + SAMPLE_ARTICLE_ID)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBodyList(CommentResponseDto.class);
    }

    @Test
    void 비동기_댓글_생성() {
        webTestClient.post().uri("/comments")
                .cookie("JSESSIONID", logInAsBaseUser(webTestClient))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .body(BodyInserters
                        .fromFormData("articleId", "1")
                        .with("comment", "comment"))
                .exchange()
                .expectStatus().isOk();
    }
}