package techcourse.myblog.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import techcourse.myblog.domain.TestDomainFactory;
import techcourse.myblog.domain.User;
import techcourse.myblog.service.dto.ArticleRequest;
import techcourse.myblog.service.dto.CommentRequest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CommentControllerTest {
    private static final Logger log = LoggerFactory.getLogger(CommentControllerTest.class);

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private TestDomainFactory testDomainFactory;

    private String cookie;

    private Long articleId;
    private Long commentId;

    private ArticleRequest articleRequest;
    private CommentRequest commentRequest;

    @BeforeEach
    void setUp() {
        User user = testDomainFactory.newUser();
        TestRequestHelper.signup(webTestClient, testDomainFactory.toUserRequestFromUser(user));

        cookie = TestRequestHelper.getCookie(webTestClient, testDomainFactory.toUserLoginRequestFromUser(user));

        articleRequest = newArticleRequest();
        articleId = TestRequestHelper.createArticle(webTestClient, cookie, articleRequest);

        commentRequest = newCommentRequest(articleId);
        commentId = TestRequestHelper.createComment(webTestClient, cookie, commentRequest);

        log.debug("SetUp finished..!!+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
    }

    @Test
    void 댓글조회_존재하는_댓글id사용() {
        webTestClient.get().uri("/api/comments/" + commentId)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.contents").isEqualTo(commentRequest.getContents());
    }

    @Test
    void 댓글조회_존재하지않는_댓글id사용() {
        long notExistsCommentId = -1l;
        webTestClient.get().uri("/api/comments/" + notExistsCommentId)
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void 댓글생성() {
        // Setup 에서 확인 중
    }

    @Test
    void 댓글생성_로그인되지않은상태_isForbidden() {
        webTestClient.post().uri("/api/comments")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(commentRequest), CommentRequest.class)
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    void 댓글작성자_댓글수정() {
        CommentRequest commentRequest = new CommentRequest(articleId, "미스타꼬");

        webTestClient.put().uri("/api/comments/" + commentId)
                .header("Cookie", cookie)
                .body(Mono.just(commentRequest), CommentRequest.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.contents").isEqualTo(commentRequest.getContents());
    }

    @Test
    void 댓글수정_작성자가_아닐_때_isForbidden() {
        CommentRequest commentRequest = new CommentRequest(articleId, "updated comment");

        webTestClient.put().uri("/api/comments/" + commentId)
                .body(Mono.just(commentRequest), CommentRequest.class)
                .exchange()
                .expectStatus()
                .isForbidden();
    }

    @Test
    void 댓글작성자_댓글삭제() {
        webTestClient.delete().uri("/api/comments/" + commentId)
                .header("Cookie", cookie)
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void 존재하지않는_댓글삭제_isNotFound() {
        Long notExistId = -1L;
        webTestClient.delete().uri("/api/comments/" + notExistId)
                .header("Cookie", cookie)
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    void 작성자가_아닐_때_isBadRequest() {
        User notCommenter = testDomainFactory.newUser();
        TestRequestHelper.signup(webTestClient, testDomainFactory.toUserRequestFromUser(notCommenter));

        String cookieOfNotCommenter = TestRequestHelper.getCookie(webTestClient, testDomainFactory.toUserLoginRequestFromUser(notCommenter));

        webTestClient.delete().uri("/api/comments/" + commentId)
                .header("Cookie", cookieOfNotCommenter)
                .exchange()
                .expectStatus()
                .isBadRequest();
    }

    @Test
    void 비로그인_isForbidden() {
        webTestClient.delete().uri("/api/comments/" + commentId)
                .exchange()
                .expectStatus()
                .isForbidden();
    }

    private ArticleRequest newArticleRequest() {
        String title = "title";
        String coverUrl = "/hello/world";
        String contents = "오늘도 참 좋은 날 입니다..!";

        return new ArticleRequest(title, coverUrl, contents);
    }

    private CommentRequest newCommentRequest(Long articleId) {
        return new CommentRequest(articleId, "글이 참 좋네요..!");
    }
}
