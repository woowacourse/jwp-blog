package techcourse.myblog.comment.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import techcourse.myblog.comment.dto.CommentUpdateDto;
import techcourse.myblog.template.RequestTemplate;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;

class CommentAjaxControllerTest extends RequestTemplate {

    @Test
    @DisplayName(value = "댓글 추가 테스트")
    void addTest() {
        CommentUpdateDto commentUpdateDto = new CommentUpdateDto();
        commentUpdateDto.setContents("add");
        loggedInPostAjaxRequest("/ajax/articles/3/comments")
                .body(Mono.just(commentUpdateDto), CommentUpdateDto.class)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody()
                .jsonPath("$..contents").value(hasItem("add"));
    }

    @Test
    @DisplayName(value = "업데이트 테스트")
    void updateTest() {
        CommentUpdateDto commentUpdateDto = new CommentUpdateDto();
        commentUpdateDto.setContents("update");
        loggedInPutJsonRequest("/ajax/articles/3/comments/3")
                .body(Mono.just(commentUpdateDto), CommentUpdateDto.class)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody()
                .jsonPath("$..contents").value(hasItem("update"));
    }

    @Test
    @DisplayName(value = "삭제 테스트")
    void deleteTest() {
        WebTestClient.BodyContentSpec map = loggedInDeleteJsonRequest("/ajax/articles/3/comments/4")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody()
                .jsonPath("$..id").value(not(hasItem("4")));
    }
}