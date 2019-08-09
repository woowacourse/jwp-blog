package techcourse.myblog.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import techcourse.myblog.application.dto.CommentRequest;
import techcourse.myblog.web.dto.CommentResponse;
import techcourse.myblog.web.dto.ErrorResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static techcourse.myblog.web.ControllerTestUtil.*;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class CommentAPIControllerTests {


    private static final String BLANK = " ";
    private static final String NAME2 = "cmo";
    private static final String EMAIL2 = "cmo@cmo.com";

    private String cookie;

    @Autowired
    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {

        // 회원가입
        signUp(webTestClient, NAME, EMAIL, PASSWORD);
        signUp(webTestClient, NAME2, EMAIL2, PASSWORD);

        // 로그인
        cookie = login(webTestClient, EMAIL, PASSWORD);

        // 게시글 작성
        writeArticle(webTestClient, TITLE, COVER_URL, CONTENTS, cookie);
    }

    @Test
    void 댓글_작성_성공_테스트() {
        String commentContents = "comment contents";
        requestSaveCommentJson(commentContents)
            .expectStatus()
            .isCreated()
            .expectBody()
            .jsonPath("$.contents").isEqualTo(commentContents)
        ;
    }

    @Test
    void 빈칸_입력_댓글_작성_실패_테스트() {
        String commentContents = BLANK;
        ErrorResponse res = requestSaveCommentJson(commentContents)
            .expectStatus()
            .is4xxClientError()
            .expectBody(ErrorResponse.class)
            .returnResult().getResponseBody();
        assertThat(res.getResult()).isEqualTo(ErrorResponse.ErrorResult.fail);
        assertThat(res.getMessage()).isEqualTo("댓글 내용을 입력하세요.");
    }

    @Test
    void 작성자가_아닌_사람이_댓글_수정_시도_실패() {
        // 작성자가 아닌 사용자로 로그인
        String anotherUserCookie = login(webTestClient, EMAIL2, PASSWORD);

        // 댓글 작성
        String commentContents = "comment contents";
        requestSaveCommentJson(commentContents);

        CommentRequest request = new CommentRequest("changed comment");
        EntityExchangeResult<ErrorResponse> response = webTestClient.put()
            .uri("/articles/1/comments/1")
            .header("Cookie", anotherUserCookie)
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .body(Mono.just(request), CommentRequest.class)
            .exchange()
            .expectStatus().is4xxClientError()
            .expectBody(ErrorResponse.class)
            .returnResult();

        assertThat(response.getResponseBody().getResult()).isEqualTo(ErrorResponse.ErrorResult.fail);
        assertThat(response.getResponseBody().getMessage()).isEqualTo("해당 작성자만 댓글을 수정할 수 있습니다.");
    }

    @Test
    void 댓글_수정_성공_테스트() {
        // 댓글 작성
        String commentContents = "comment contents";
        requestSaveCommentJson(commentContents);

        CommentRequest request = new CommentRequest("changed comment");
        EntityExchangeResult<CommentResponse> response = webTestClient.put()
            .uri("/articles/1/comments/1")
            .header("Cookie", cookie)
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .body(Mono.just(request), CommentRequest.class)
            .exchange()
            .expectStatus()
            .isOk()
            .expectBody(CommentResponse.class)
            .returnResult();

        assertThat(response.getResponseBody().getContents()).isEqualTo(request.getContents());

    }

    @Test
    void 댓글_삭제_성공_테스트() {
        // 댓글 작성
        String commentContents = "comment contents";
        requestSaveCommentJson(commentContents);

        // 댓글 삭제
        webTestClient.delete()
            .uri("/articles/1/comments/1")
            .header("Cookie", cookie)
            .exchange()
            .expectStatus()
            .isNoContent();
    }

    @Test
    void 존재하지_않는_댓글_삭제() {
        // 댓글 작성
        String commentContents = "comment contents";
        requestSaveCommentJson(commentContents);

        // 댓글 삭제
        ErrorResponse res = webTestClient.delete()
            .uri("/articles/1/comments/999")
            .header("Cookie", cookie)
            .exchange()
            .expectStatus()
            .is4xxClientError()
            .expectBody(ErrorResponse.class)
            .returnResult()
            .getResponseBody();

        assertThat(res.getResult()).isEqualTo(ErrorResponse.ErrorResult.fail);
        assertThat(res.getMessage()).isEqualTo("존재하지 않는 댓글입니다.");
    }

    @Test
    void 작성자가_아닌_유저가_댓글_삭제시_예외_발생() {
        // 작성자가 아닌 사용자로 로그인
        String anotherUserCookie = login(webTestClient, EMAIL2, PASSWORD);

        // 댓글 작성
        String commentContents = "comment contents";
        requestSaveCommentJson(commentContents);

        // 댓글 삭제
        webTestClient.delete()
            .uri("/articles/1/comments/1")
            .header("Cookie", anotherUserCookie)
            .exchange()
            .expectBody()
            .jsonPath("$.result").isEqualTo("fail")
        ;
    }

    private WebTestClient.ResponseSpec requestSaveCommentJson(String commentContents) {
        CommentRequest request = new CommentRequest(commentContents);
        return webTestClient.post()
            .uri("/articles/1/comments")
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .header("Cookie", cookie)
            .body(Mono.just(request), CommentRequest.class)
            .exchange();
    }

    @Test
    void 댓글_조회_성공_테스트() {
        // 댓글 작성
        String commentContents = "댓글 본문";
        requestSaveCommentJson(commentContents);

        // 댓글 조회
        webTestClient.get()
            .uri("/articles/1")
            .exchange()
            .expectStatus()
            .isOk()
            .expectBody()
            .consumeWith(response -> {
                String body = new String(response.getResponseBody());
                assertTrue(body.contains(commentContents));
            });
    }
}

