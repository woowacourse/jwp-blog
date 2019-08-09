package techcourse.myblog.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.reactive.function.BodyInserters;

import static org.assertj.core.api.Assertions.assertThat;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class ArticleControllerTests extends ControllerTemplate {

    private static final String TITLE = "ThisIsTitle";
    private static final String COVER_URL = "ThisIsCoverUrl";
    private static final String CONTENTS = "ThisIsContents";

    private String cookie;

    @BeforeEach
    void setUp() {
        // 회원가입
        requestSignUp(NAME, EMAIL, PASSWORD);

        // 로그인
        requestLogin(EMAIL, PASSWORD);
        cookie = getCookie(EMAIL, PASSWORD);

        // 게시글 작성
        requestWriteArticle(cookie, TITLE, COVER_URL, CONTENTS)
                .expectStatus()
                .isFound()
        ;
    }

    @Test
    void index() {
        webTestClient.get().uri("/")
                .exchange()
                .expectStatus()
                .isOk()
        ;
    }

    @Test
    void articleForm() {
        webTestClient.get().uri("/writing")
                .exchange()
                .expectStatus()
                .isOk()
        ;
    }

    @Test
    void 게시글조회() {
        webTestClient.get()
                .uri("/articles/" + "1")
                .header("Cookie", cookie)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .consumeWith(response2 -> {
                    String body = new String(response2.getResponseBody());
                    assertThat(body.contains(TITLE)).isTrue();
                    assertThat(body.contains(COVER_URL)).isTrue();
                    assertThat(body.contains(CONTENTS)).isTrue();
                })
        ;
    }

    @Test
    void 존재하지_않는_게시글_조회_에러() {
        webTestClient.get()
                .uri("/articles/" + "2")
                .header("Cookie", cookie)
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
                .expectStatus()
                .isOk()
        ;
    }

    @Test
    void 게시글삭제() {
        webTestClient.delete().uri("/articles/1")
                .header("Cookie", cookie)
                .exchange()
                .expectHeader()
                .valueMatches("location", ".*/")
                .expectStatus()
                .isFound()
        ;
    }

    @Test
    void article_Duplicate_Fail() {
        webTestClient.post().uri("/articles")
                .body(BodyInserters.fromFormData("title", TITLE)
                        .with("coverUrl", "커버")
                        .with("contents", "중복"))
                .exchange()
                .expectStatus()
                .is4xxClientError()
        ;
    }
}
