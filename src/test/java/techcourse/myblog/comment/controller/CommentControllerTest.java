package techcourse.myblog.comment.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import techcourse.myblog.comment.dto.CommentCreateDto;
import techcourse.myblog.util.UserUtilForTest;
import techcourse.myblog.util.WebTest;

import static org.assertj.core.api.Assertions.assertThat;
import static techcourse.myblog.comment.CommentDataForTest.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CommentControllerTest {
    private static final long COMMENT_ID = 1;
    private static final long COMMENT_DELETE_ID = 2;
    private static final String ARTICLE_PATH = "/articles/1";

    @Autowired
    private WebTestClient webTestClient;

    private String cookie;

    @BeforeEach
    void setUp() {
        cookie = UserUtilForTest.loginAndGetCookie(webTestClient);
    }

    @Test
    void 댓글_생성_테스트() {
        CommentCreateDto commentCreateDto = new CommentCreateDto();
        commentCreateDto.setContents(COMMENT_CONTENTS);

        webTestClient.post().uri(ARTICLE_PATH + "/comments")
                .header("Cookie", cookie)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(commentCreateDto), CommentCreateDto.class)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody()
                .jsonPath("$.contents").isNotEmpty()
                .jsonPath("$.contents").isEqualTo(COMMENT_CONTENTS);
    }

    @Test
    void 댓글_수정_페이지_이동_테스트() {
            WebTest.executeGetTest(webTestClient, "/comments/" + COMMENT_ID + "/edit", cookie)
                    .expectStatus().isOk();
    }

    @Test
    void 댓글_업데이트_테스트() {
        EntityExchangeResult<byte[]> entityExchangeResult =
                WebTest.executePutTest(webTestClient, ARTICLE_PATH + "/comments/" + COMMENT_ID, cookie,
                        UPDATED_COMMENT_BODY)
                        .expectStatus().isFound()
                        .expectBody()
                        .returnResult();

        String location = String.valueOf(entityExchangeResult.getResponseHeaders().getLocation());

        WebTest.executeGetTest(webTestClient, location, cookie)
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response -> {
                    String body = new String(response.getResponseBody());
                    assertThat(body.contains(UPDATED_COMMENT_CONTENTS)).isTrue();
                });
    }

    @Test
    void 댓글_삭제_테스트() {
        EntityExchangeResult<byte[]> entityExchangeResult =
                WebTest.executeDeleteTest(webTestClient, ARTICLE_PATH + "/comments/" + COMMENT_DELETE_ID, cookie)
                        .expectStatus().isFound()
                        .expectBody()
                        .returnResult();

        String location = String.valueOf(entityExchangeResult.getResponseHeaders().getLocation());

        WebTest.executeGetTest(webTestClient, location, cookie)
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response -> {
                    String body = new String(response.getResponseBody());
                    assertThat(body.contains(COMMENT_CONTENTS)).isFalse();
                });
    }
}