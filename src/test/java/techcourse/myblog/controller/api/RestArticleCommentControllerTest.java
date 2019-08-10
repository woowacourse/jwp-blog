package techcourse.myblog.controller.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import techcourse.myblog.service.dto.CommentResponseDto;

import static techcourse.myblog.Utils.TestConstants.SAMPLE_ARTICLE_ID;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RestArticleCommentControllerTest {
    @Autowired
    WebTestClient webTestClient;

    @LocalServerPort
    int localServerPort;

    @Test
    void 비동기_댓글_조회() {
        webTestClient.get().uri("/api/articles/" + SAMPLE_ARTICLE_ID + "/comments")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBodyList(CommentResponseDto.class);
    }
}
