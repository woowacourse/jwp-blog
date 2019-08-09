package techcourse.myblog.web.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;
import techcourse.myblog.service.dto.CommentRequestDto;
import techcourse.myblog.template.LoginTemplate;

import static org.assertj.core.api.Assertions.assertThat;

public class CommentRestControllerTest extends LoginTemplate {
    private static final String CONTENTS = "댓글썼당";
    private static final String SECOND_USER_NAME = "두번째유저";
    private static final String SECOND_USER_EMAIL = "second@user.com";
    private static final String SECOND_USER_PASSWORD = "Second22@";

    private static int commentId = 1;

    @BeforeEach
    void setUp() {
        registeredWebTestClient();
    }

    @Test
    void 댓글이_정상적으로_등록되는지_테스트() {
        String articleId = getNewArticleId();
        CommentRequestDto commentRequestDto = new CommentRequestDto(CONTENTS);

        loggedInPostRequest("/article/" + articleId + "/comment")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(commentRequestDto), CommentRequestDto.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(CommentRequestDto.class)
                .consumeWith(res -> assertThat(res.getResponseBody().getContents()).isEqualTo(CONTENTS));
    }

    @Test
    void 댓글이_정상적으로_수정되는지_테스트() {
        String articleId = getNewArticleId();
        getNewCommentId(articleId);

        CommentRequestDto commentRequestDto = new CommentRequestDto(CONTENTS + 2);

        loggedInPutRequest("/article/" + articleId + "/comment/" + commentId)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(commentRequestDto), CommentRequestDto.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(CommentRequestDto.class)
                .consumeWith(res -> {
                    assertThat(res.getResponseBody().getContents()).isEqualTo(CONTENTS + 2);
                });
    }

    @Test
    void 수정하려는_사용자가_댓글_작성자가_아닌_경우_예외처리() {
        CommentRequestDto commentRequestDto = new CommentRequestDto(CONTENTS);
        registeredWebTestClient(SECOND_USER_NAME, SECOND_USER_EMAIL, SECOND_USER_PASSWORD);
        String articleId = getNewArticleId();
        getNewCommentId(articleId);

        loggedInPutRequest("/article/" + articleId + "/comment/" + commentId, SECOND_USER_EMAIL, SECOND_USER_PASSWORD)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(commentRequestDto), CommentRequestDto.class)
                .exchange()
                .expectStatus().isFound();
    }

    @Test
    void 댓글이_정상적으로_삭제되는지_테스트() {
        String articleId = getNewArticleId();
        getNewCommentId(articleId);

        loggedInDeleteRequest("/article/" + articleId + "/comment/" + commentId)
                .exchange()
                .expectStatus().isFound()
                .expectHeader().valueMatches("Location", "http://localhost:[0-9]+/articles/" + articleId + ".*");

    }

    @Test
    void 삭제하려는_사용자가_댓글_작성자가_아닌_경우_예외처리() {
        registeredWebTestClient(SECOND_USER_NAME, SECOND_USER_EMAIL, SECOND_USER_PASSWORD);
        String articleId = getNewArticleId();
        getNewCommentId(articleId);

        loggedInDeleteRequest("/article/" + articleId + "/comment/" + commentId, SECOND_USER_EMAIL, SECOND_USER_PASSWORD)
                .exchange()
                .expectStatus().isFound()
                .expectHeader().valueMatches("Location", "http://localhost:[0-9]+/");
    }

    @AfterEach
    void tearDown() {
        commentId++;
    }

    private String getNewArticleId() {
        return articlePostRequest()
                .getResponseHeaders()
                .getLocation()
                .getPath().split("/")[2];
    }

    private void getNewCommentId(String articleId) {
        CommentRequestDto commentRequestDto = new CommentRequestDto(CONTENTS);

        loggedInPostRequest("/article/" + articleId + "/comment")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(commentRequestDto), CommentRequestDto.class)
                .exchange()
                .expectStatus().isOk();
    }

    public EntityExchangeResult<byte[]> articlePostRequest() {
        String title = "제목";
        String contents = "본문";
        String coverUrl = "배경";

        return loggedInPostRequest("/articles")
                .body(BodyInserters
                        .fromFormData("title", title)
                        .with("contents", contents)
                        .with("coverUrl", coverUrl))
                .exchange()
                .expectBody()
                .returnResult();
    }
}
