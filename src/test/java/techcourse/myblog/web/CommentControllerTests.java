package techcourse.myblog.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.reactive.function.BodyInserters;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
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

        // 게시글 작성
        requestWriteArticle(cookie1, TITLE, COVER_URL, CONTENTS);

        // 댓글 작성 성공 테스트
        requestWriteComment(cookie1, COMMENT_CONTENTS)
                .expectStatus()
                .isFound()
                .expectHeader()
                .valueMatches("location", ".*/articles/1")
        ;
    }

    @Test
    void 댓글_수정_성공_테스트() {
        String updatedContents = "ThisIsUpdatedCommentContents";
        webTestClient.put()
                .uri("/articles/1/comments/1")
                .header("Cookie", cookie1)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("contents", updatedContents))
                .exchange()
                .expectStatus()
                .isFound()
                .expectHeader()
                .valueMatches("location", ".*/articles/1")
        ;
    }

    @Test
    void 다른_유저가_댓글_수정하는_오류() {
        String updatedContents = "ThisIsUpdatedCommentContents";
        webTestClient.put()
                .uri("/articles/1/comments/1")
                .header("Cookie", cookie2)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("contents", updatedContents))
                .exchange()
                .expectStatus()
                .isFound()
                .expectHeader()
                .valueMatches("location", ".*/")
        ;
    }

    @Test
    void 댓글_삭제_성공_테스트() {
        webTestClient.delete()
                .uri("/articles/1/comments/1")
                .header("Cookie", cookie2)
                .exchange()
                .expectStatus()
                .isFound()
                .expectHeader()
                .valueMatches("location", ".*/")
        ;
    }

    @Test
    void 다른_유저가_댓글_삭제_오류() {
        webTestClient.delete()
                .uri("/articles/1/comments/1")
                .header("Cookie", cookie2)
                .exchange()
                .expectStatus()
                .isFound()
                .expectHeader()
                .valueMatches("location", ".*/")
        ;
    }

    @Test
    void 댓글_조회_성공_테스트() {
        webTestClient.get()
                .uri("/articles/1")
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
}
