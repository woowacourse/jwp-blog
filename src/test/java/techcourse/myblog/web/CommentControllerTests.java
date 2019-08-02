package techcourse.myblog.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class CommentControllerTests {

    private static final String name = "bmo";
    private static final String email = "bmo@bmo.com";
    private static final String password = "Password123!";
    private static final String title = "googler bmo";
    private static final String coverUrl = "bmo.jpg";
    private static final String contents = "why bmo so great?";

    private String cookie;

    @Autowired
    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {

        // 회원가입
        webTestClient.post().uri("/users")
                .body(BodyInserters.fromFormData("name", name)
                        .with("email", email)
                        .with("password", password))
                .exchange()
        ;

        // 로그인
        cookie = webTestClient.post().uri("/login")
                .body(BodyInserters.fromFormData("email", email)
                        .with("password", password))
                .exchange()
                .expectStatus()
                .isFound()
                .returnResult(String.class)
                .getResponseHeaders()
                .getFirst("Set-Cookie");

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

    @Test
    void 댓글_작성_성공_테스트() {
        String commentContents = "comment contents";
        requestSaveComment(commentContents)
                .expectStatus()
                .isFound()
                .expectHeader()
                .valueMatches("location", ".*/articles/1")
        ;
    }

    @Test
    void 댓글_수정_성공_테스트() {
        // 댓글 작성
        String commentContents = "comment contents";
        requestSaveComment(commentContents);

        // 댓글 수정
        String updatedContentes = "updated comment contents";
        webTestClient.put()
                .uri("/articles/1/comments/1")
                .header("Cookie", cookie)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("contents", updatedContentes))
                .exchange()
                .expectStatus()
                .isFound()
                .expectHeader()
                .valueMatches("location", ".*/articles/1")
        ;
    }

    @Test
    void 댓글_삭제_성공_테스트() {
        // 댓글 작성
        String commentContents = "comment contents";
        requestSaveComment(commentContents);

        // 댓글 삭제
        webTestClient.delete()
                .uri("/articles/1/comments/1")
                .header("Cookie", cookie)
                .exchange()
                .expectStatus()
                .isFound()
                .expectHeader()
                .valueMatches("location", ".*/articles/1");
    }

    private WebTestClient.ResponseSpec requestSaveComment(String commentContents) {
        return webTestClient.post()
                .uri("/articles/1/comments")
                .header("Cookie", cookie)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("contents", commentContents))
                .exchange()
                ;
    }

    @Test
    void 댓글_조회_성공_테스트() {
        // 댓글 작성
        String commentContents = "댓글 본문";
        requestSaveComment(commentContents);

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

