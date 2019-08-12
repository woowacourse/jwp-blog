package techcourse.myblog.web;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import reactor.core.publisher.Mono;
import techcourse.myblog.application.dto.CommentRequest;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test")
public class CommentControllerTests extends ControllerTemplate {

    private static final String ANOTHER_NAME = "kim";
    private static final String ANOTHER_EMAIL = "kim@gmail.com";
    private static final String ANOTHER_PASSWORD = "Password1234!";
    private static final String TITLE = "ThisIsTitle";
    private static final String COVER_URL = "ThisIsCoverUrl";
    private static final String CONTENTS = "ThisIsContents";
    private static final String COMMENT_CONTENTS = "ThisIsComment";

    private String cookie1;
    private String cookie2;

    private static int commentId = 1;

    @BeforeEach
    void setUp() {
        // 회원가입
        requestSignUp(NAME, EMAIL, PASSWORD);

        // 회원가입2
        requestSignUp(ANOTHER_NAME, ANOTHER_EMAIL, ANOTHER_PASSWORD);

        // 로그인
        cookie1 = getCookie(EMAIL, PASSWORD);

        // 로그인2
        cookie2 = getCookie(ANOTHER_EMAIL, ANOTHER_PASSWORD);
    }

    @Test
    void 댓글_수정_성공_테스트() {
        // 게시글 작성 및 해당 ID 가져오기
        String newArticleId = writeArticleAndGetId();

        // 해당 게시글에 댓글 작성하기
        requestWriteCommentAjax(cookie1, COMMENT_CONTENTS, newArticleId);

        String updatedContents = "ThisIsUpdatedCommentContents";
        CommentRequest commentRequest = new CommentRequest(updatedContents);

        webTestClient.put()
                .uri("/articles/" + newArticleId + "/comments/" + commentId)
                .header("Cookie", cookie1)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(commentRequest), CommentRequest.class)
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody()
                .jsonPath("$.contents").isNotEmpty()
                .jsonPath("$.contents").isEqualTo(updatedContents)
        ;
    }

    @Test
    void 다른_유저가_댓글_수정하는_오류() {
        // 게시글 작성 및 해당 ID 가져오기
        String newArticleId = writeArticleAndGetId();

        // 해당 게시글에 댓글 작성하기
        requestWriteCommentAjax(cookie1, COMMENT_CONTENTS, newArticleId);

        String updatedContents = "ThisIsUpdatedCommentContents";
        CommentRequest commentRequest = new CommentRequest(updatedContents);
        webTestClient.put()
                .uri("/articles/" + newArticleId + "/comments/" + commentId)
                .header("Cookie", cookie2)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(commentRequest), CommentRequest.class)
                .exchange()
                .expectStatus()
                .is4xxClientError()
                .expectHeader()
                .valueMatches("location", ".*/")
        ;
    }

    @Test
    void 댓글_삭제_성공_테스트() {
        // 게시글 작성 및 해당 ID 가져오기
        String newArticleId = writeArticleAndGetId();

        // 해당 게시글에 댓글 작성하기
        requestWriteCommentAjax(cookie1, COMMENT_CONTENTS, newArticleId);

        webTestClient.delete()
                .uri("/articles/" + newArticleId + "/comments/" + commentId)
                .header("Cookie", cookie1)
                .exchange()
                .expectStatus()
                .isOk()
        ;
    }

    @Test
    void 다른_유저가_댓글_삭제_오류() {
        // 게시글 작성 및 해당 ID 가져오기
        String newArticleId = writeArticleAndGetId();

        // 해당 게시글에 댓글 작성하기
        requestWriteCommentAjax(cookie1, COMMENT_CONTENTS, newArticleId);

        webTestClient.delete()
                .uri("/articles/" + newArticleId + "/comments/" + commentId)
                .header("Cookie", cookie2)
                .exchange()
                .expectStatus()
                .is4xxClientError()
                .expectHeader()
                .valueMatches("location", ".*/")
        ;
    }

    @Test
    void 댓글_조회_성공_테스트() {
        // 게시글 작성 및 해당 ID 가져오기
        String newArticleId = writeArticleAndGetId();

        // 해당 게시글에 댓글 작성하기
        requestWriteCommentAjax(cookie1, COMMENT_CONTENTS, newArticleId);

        webTestClient.get()
                .uri("/articles/" + newArticleId)
                .header("Cookie", cookie1)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .consumeWith(response -> {
                    String body = new String(response.getResponseBody());
                    assertTrue(body.contains(COMMENT_CONTENTS));
                })
        ;
    }

    @AfterEach
    void increaseCommentId() {
        ++commentId;
    }

    public String writeArticleAndGetId() {
        EntityExchangeResult<byte[]> result = requestWriteArticle(cookie1, TITLE, COVER_URL, CONTENTS)
                .expectStatus()
                .isFound()
                .expectBody()
                .returnResult();
        return result.getResponseHeaders()
                .getLocation()
                .getPath()
                .split("/")[2]
                ;
    }
}
