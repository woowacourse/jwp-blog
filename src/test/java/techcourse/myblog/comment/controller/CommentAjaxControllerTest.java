package techcourse.myblog.comment.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import reactor.core.publisher.Mono;
import techcourse.myblog.comment.dto.CommentUpdateDto;
import techcourse.myblog.template.RequestTemplate;

class CommentAjaxControllerTest extends RequestTemplate {

    @Test
    void name() {
        CommentUpdateDto commentUpdateDto = new CommentUpdateDto();
        commentUpdateDto.setContents("update");
        loggedInPutAjaxRequest("/ajax/comments/1")
                .body(Mono.just(commentUpdateDto), CommentUpdateDto.class)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody()
                .jsonPath("$.contents").isEqualTo("update");
    }
}