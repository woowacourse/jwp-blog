package techcourse.myblog.comment.controller;

import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.BodyInserters;
import techcourse.myblog.data.CommentDataForTest;
import techcourse.myblog.template.RequestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

public class CommentControllerTest extends RequestTemplate {
    @Test
    void 댓글_작성() {
        loggedInPostRequest("/articles/1/comments")
                .body(BodyInserters.fromFormData("contents", CommentDataForTest.COMMENT_CONTENTS))
                .exchange()
                .expectStatus()
                .isFound();

        checkCommentContents("/articles/1", CommentDataForTest.COMMENT_CONTENTS);
    }

    @Test
    void 댓글_수정_페이지_이동() {
        loggedInGetRequest("/comments/1/edit")
                .expectStatus()
                .isOk();
    }

    @Test
    void 댓글_업데이트() {
        loggedInPutRequest("/articles/1/comments/1")
                .body(BodyInserters.fromFormData("contents", CommentDataForTest.UPDATED_CONTENTS))
                .exchange()
                .expectStatus()
                .isFound();

        checkCommentContents("/articles/1", CommentDataForTest.UPDATED_CONTENTS);
    }

    @Test
    void 댓글_삭제() {
        loggedInDeleteRequest("/articles/1/comments/2")
                .expectStatus()
                .isFound();

        loggedInGetRequest("/comments/2/edit")
                .expectStatus()
                .isFound();
    }

    private void checkCommentContents(String path, String contents) {
        loggedInGetRequest(path)
                .expectStatus()
                .isOk()
                .expectBody()
                .consumeWith(res -> {
                    String body = new String(res.getResponseBody());
                    assertThat(body.contains(contents)).isTrue();
                });
    }
}
