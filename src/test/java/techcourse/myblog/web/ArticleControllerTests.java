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

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureWebTestClient
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ArticleControllerTests {

    private String cookie;

    @Autowired
    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        String name = "bmo";
        String email = "bmo@bmo.com";
        String password = "Password123!";

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
        String title = "titleTest";
        String coverUrl = "coverUrlTest";
        String contents = "contentsTest";
        webTestClient.post()
                .uri("/articles")
                .header("Cookie", cookie)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("title", title)
                        .with("coverUrl", coverUrl)
                        .with("contents", contents))
                .exchange()
                .expectStatus().isFound();
    }

    @Test
    void index() {
        webTestClient.get().uri("/")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void articleForm() {
        webTestClient.get().uri("/writing")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void 게시글조회() {
        String title = "titleTest";
        String coverUrl = "coverUrlTest";
        String contents = "contentsTest";
        webTestClient.get()
                .uri("/articles/" + "1")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response2 -> {
                    String body = new String(response2.getResponseBody());
                    assertThat(body.contains(title)).isTrue();
                    assertThat(body.contains(coverUrl)).isTrue();
                    assertThat(body.contains(contents)).isTrue();
                });
    }

    @Test
    void 존재하지_않는_게시글_조회_에러() {
        webTestClient.get()
                .uri("/articles/" + "2")
                .exchange()
                .expectStatus()
                .isOk()
        ;
    }

    @Test
    void 게시글수정페이지() {
        webTestClient.get().uri("/articles/1/edit")
                .header("Cookie", cookie)
                .exchange()
                .expectStatus()
                .isOk()
        ;
    }

    @Test
    void 게시글수정() {
        webTestClient.put().uri("/articles/1")
                .header("Cookie", cookie)
                .body(BodyInserters.fromFormData("title", "수정")
                        .with("coverUrl", "수정")
                        .with("contents", "수정"))
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void 게시글삭제() {
        webTestClient.delete().uri("/articles/1")
                .header("Cookie", cookie)
                .exchange()
                .expectHeader()
                .valueMatches("location", ".*/")
                .expectStatus()
                .is3xxRedirection();
    }

    @Test
    void article_Duplicate_Fail() {
        webTestClient.post().uri("/articles")
                .body(BodyInserters.fromFormData("title", "titleTest")
                        .with("coverUrl", "커버")
                        .with("contents", "중복"))
                .exchange()
                .expectStatus().is4xxClientError();
    }
}
