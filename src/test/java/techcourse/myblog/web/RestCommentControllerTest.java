package techcourse.myblog.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import techcourse.myblog.service.dto.CommentRequestDto;
import techcourse.myblog.service.dto.CommentResponseDto;

import static techcourse.myblog.Utils.TestConstants.*;
import static techcourse.myblog.Utils.TestUtils.logInAsBaseUser;
import static techcourse.myblog.Utils.TestUtils.logInAsMismatchUser;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RestCommentControllerTest {

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

    @Test
    void 비동기_댓글_생성() {
        CommentRequestDto commentRequestDto = new CommentRequestDto(1L, "comment");

        webTestClient.post().uri("/api/comments")
                .cookie("JSESSIONID", logInAsBaseUser(webTestClient))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(commentRequestDto), CommentRequestDto.class)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void 비동기_댓글_생성_비로그인() {
        CommentRequestDto commentRequestDto = new CommentRequestDto(1L, "comment");

        webTestClient.post().uri("/api/comments")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(commentRequestDto), CommentRequestDto.class)
                .exchange()
                .expectStatus().isFound()
                .expectHeader()
                .valueMatches("location", ".*/login.*");
    }

    @Test
    void 비동기_댓글_수정() {
        CommentRequestDto commentRequestDto = new CommentRequestDto(1L, "edit comment");

        webTestClient.put().uri("/api/comments/" + SAMPLE_COMMENT_ID)
                .cookie("JSESSIONID", logInAsBaseUser(webTestClient))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(commentRequestDto), CommentRequestDto.class)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void 비동기_댓글_수정_다른_유저() {
        CommentRequestDto commentRequestDto = new CommentRequestDto(1L, "edit comment");

        webTestClient.put().uri("/api/comments/" + SAMPLE_COMMENT_ID)
                .cookie("JSESSIONID", logInAsMismatchUser(webTestClient))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(commentRequestDto), CommentRequestDto.class)
                .exchange()
                .expectStatus().is5xxServerError();
    }

    @Test
    void 비동기_댓글_삭제_비로그인() {
        webTestClient.delete().uri("/api/comments/" + SAMPLE_DELETE_COMMENT_ID)
                .exchange()
                .expectStatus().isFound()
                .expectHeader()
                .valueMatches("location", ".*/login.*");
    }

    @Test
    void 비동기_댓글_삭제() {
        webTestClient.delete().uri("/api/comments/" + SAMPLE_DELETE_COMMENT_ID)
                .cookie("JSESSIONID", logInAsBaseUser(webTestClient))
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void 비동기_댓글_삭제_다른_유저() {
        webTestClient.delete().uri("/api/comments/" + SAMPLE_DELETE_COMMENT_ID)
                .cookie("JSESSIONID", logInAsMismatchUser(webTestClient))
                .exchange()
                .expectStatus().is5xxServerError();
    }
}