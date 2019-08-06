package techcourse.myblog.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import techcourse.myblog.controller.dto.LoginDto;
import techcourse.myblog.controller.dto.RequestCommentDto;
import techcourse.myblog.utils.Utils;

@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CommentControllerTest {
    private static final String EMAIL = "aiden@woowa.com";
    private static final String PASSWORD = "12Woowa@@";

    @Autowired
    private WebTestClient webTestClient;

    private String cookie;
    private Long articleId;


    @BeforeEach
    void setUp() {
        cookie = Utils.getLoginCookie(webTestClient, new LoginDto(EMAIL, PASSWORD));
        articleId = 9l;
    }

    @Test
    @DisplayName("comment를 저장한다.")
    public void saveComment() {
        RequestCommentDto requestCommentDto = new RequestCommentDto();
        requestCommentDto.setContents("contents");
        requestCommentDto.setArticleId(articleId);

        webTestClient.post().uri("/comments")
                .header("Cookie", cookie)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(requestCommentDto), RequestCommentDto.class)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody()
                .jsonPath("$.contents").isNotEmpty()
                .jsonPath("$.contents").isEqualTo("contents");
    }

    @Test
    @DisplayName("comment를 수정한다.")
    void updateComment() {
        RequestCommentDto updateRequestCommentDto = new RequestCommentDto();
        updateRequestCommentDto.setContents("update contents");

        webTestClient.put().uri("/comments/" + 20)
                .header("Cookie", cookie)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(updateRequestCommentDto), RequestCommentDto.class)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody()
                .jsonPath("$.contents").isNotEmpty()
                .jsonPath("$.contents").isEqualTo("update contents");
    }

    @Test
    @DisplayName("comment를 삭제한다.")
    void deleteComment() {
        webTestClient.delete().uri("/comments/" + 19)
                .header("Cookie", cookie)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk();
    }
}