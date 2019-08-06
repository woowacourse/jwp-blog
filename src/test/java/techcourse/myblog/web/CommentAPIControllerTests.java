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
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;
import techcourse.myblog.application.dto.CommentRequest;
import techcourse.myblog.application.dto.CommentResponse;
import techcourse.myblog.application.dto.ErrorResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class CommentAPIControllerTests {

    private static final String name = "bmo";
    private static final String email = "bmo@bmo.com";
    private static final String password = "Password123!";
    private static final String title = "googler bmo";
    private static final String coverUrl = "bmo.jpg";
    private static final String contents = "why bmo so great?";
    private static final String BLANK = " ";
    private static final String name2 = "cmo";
    private static final String email2 = "cmo@cmo.com";

    private String cookie;

    @Autowired
    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {

        // 회원가입
        signup(name, email, password);
        signup(name2, email2, password);

        // 로그인
        cookie = login(email, password);

        // 게시글 작성
        webTestClient.post()
                .uri("/articles")
                .header("Cookie", cookie)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("title", title)
                        .with("coverUrl", coverUrl)
                        .with("contents", contents))
                .exchange()
        ;
    }

    private String login(String email, String password) {
        return webTestClient.post().uri("/login")
                .body(BodyInserters.fromFormData("email", email)
                        .with("password", password))
                .exchange()
                .expectStatus()
                .isFound()
                .returnResult(String.class)
                .getResponseHeaders()
                .getFirst("Set-Cookie");
    }

    private void signup(String name, String email, String password) {
        webTestClient.post().uri("/users")
                .body(BodyInserters.fromFormData("name", name)
                        .with("email", email)
                        .with("password", password))
                .exchange()
        ;
    }

    @Test
    void 댓글_작성_성공_테스트() {
        String commentContents = "comment contents";
        requestSaveCommentJson(commentContents)
                .expectStatus()
                .isCreated()
                .expectBody()
                .jsonPath("$.result").isEqualTo("ok")
                .jsonPath("$.comment.contents").isEqualTo(commentContents)
        ;
    }

    @Test
    void 빈칸_입력_댓글_작성_실패_테스트() {
        String commentContents = BLANK;
        requestSaveCommentJson(commentContents)
                .expectStatus()
                .is4xxClientError()
                .expectBody()
                .jsonPath("$.result").isEqualTo("fail")
                .jsonPath("$.message").isEqualTo("댓글 내용을 입력하세요.")
        ;
    }

    @Test
    void 작성자가_아닌_사람이_댓글_수정_시도_실패() {
        // 작성자가 아닌 사용자로 로그인
        String anotherUserCookie = login(email2, password);

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

        assertThat(response.getResponseBody().getResult()).isEqualTo("fail");
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

        assertThat(response.getResponseBody().getComment().getContents()).isEqualTo(request.getContents());

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
                .expectBody()
                .jsonPath("$.result").isEqualTo("ok")
        ;
    }

    @Test
    void 존재하지_않는_댓글_삭제() {
        // 댓글 작성
        String commentContents = "comment contents";
        requestSaveCommentJson(commentContents);

        // 댓글 삭제
        webTestClient.delete()
                .uri("/articles/1/comments/999")
                .header("Cookie", cookie)
                .exchange()
                .expectBody()
                .jsonPath("$.result").isEqualTo("fail")
                .jsonPath("$.message").isEqualTo("존재하지 않는 댓글입니다.")
        ;
    }

    @Test
    void 작성자가_아닌_유저가_댓글_삭제시_예외_발생() {
        // 작성자가 아닌 사용자로 로그인
        String anotherUserCookie = login(email2, password);

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

