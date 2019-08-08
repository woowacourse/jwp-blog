package techcourse.myblog.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import static org.assertj.core.api.Assertions.assertThat;
import static techcourse.myblog.web.ControllerTestUtil.*;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ArticleControllerTests {

    private String cookie;

    @Autowired
    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        // 회원가입
        signUp(webTestClient, NAME, EMAIL, PASSWORD);

        // 로그인
        cookie = login(webTestClient, EMAIL, PASSWORD);

        // 게시글 작성
        writeArticle(webTestClient, TITLE, COVER_URL, CONTENTS, cookie);
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
        webTestClient.get()
            .uri("/articles/" + "1")
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .consumeWith(response -> {
                String body = new String(response.getResponseBody());
                assertThat(body.contains(TITLE)).isTrue();
                assertThat(body.contains(COVER_URL)).isTrue();
                assertThat(body.contains(CONTENTS)).isTrue();
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
            .body(BodyInserters.fromFormData("title", "updated title")
                .with("coverUrl", "updated coverUrl")
                .with("contents", "updated Contents"))
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
}
