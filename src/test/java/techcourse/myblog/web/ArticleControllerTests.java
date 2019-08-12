package techcourse.myblog.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.web.reactive.function.BodyInserters;

import static org.assertj.core.api.Assertions.assertThat;

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
        String newArticleId = writeArticleAndGetId();
        webTestClient.get()
                .uri("/articles/" + newArticleId)
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
        String newArticleId = writeArticleAndGetId();
        newArticleId = String.format("%d", (Integer.parseInt(newArticleId) + 1));
        System.out.println("#####-Id: " + newArticleId);
        webTestClient.get()
                .uri("/articles/" + newArticleId)
                .header("Cookie", cookie)
                .exchange()
                .expectStatus()
                .isOk()
        ;
    }

    @Test
    void 게시글수정페이지() {
        String newArticleId = writeArticleAndGetId();
        webTestClient.get().uri("/articles/" + newArticleId + "/edit")
                .header("Cookie", cookie)
                .exchange()
                .expectStatus()
                .isOk()
        ;
    }

    @Test
    void 게시글수정() {
        String newArticleId = writeArticleAndGetId();
        webTestClient.put().uri("/articles/" + newArticleId)
                .header("Cookie", cookie)
                .body(BodyInserters.fromFormData("title", "수정")
                        .with("coverUrl", "수정")
                        .with("contents", "수정"))
                .exchange()
                .expectHeader()
                .valueMatches("location", ".*/articles/" + newArticleId)
                .expectStatus()
                .isFound()
        ;
    }

    @Test
    void 게시글삭제() {
        String newArticleId = writeArticleAndGetId();
        webTestClient.delete().uri("/articles/" + newArticleId)
                .header("Cookie", cookie)
                .exchange()
                .expectHeader()
                .valueMatches("location", ".*/")
                .expectStatus()
                .isFound()
        ;
    }

    public String writeArticleAndGetId() {
        EntityExchangeResult<byte[]> result = requestWriteArticle(cookie, TITLE, COVER_URL, CONTENTS)
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
