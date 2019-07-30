package techcourse.myblog.article.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import techcourse.myblog.article.ArticleDataForTest;
import techcourse.myblog.util.ArticleUtilForTest;
import techcourse.myblog.util.UserUtilForTest;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ArticleControllerTests {
    private static long currentArticleId = 1;

    @Autowired
    private WebTestClient webTestClient;

    private String cookie;

    @BeforeEach
    void setUp() {
        UserUtilForTest.signUp(webTestClient);
        cookie = UserUtilForTest.loginAndGetCookie(webTestClient);
        ArticleUtilForTest.createArticle(webTestClient, cookie);
    }

    @Test
    void index() {
        webTestClient.get().uri("/")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void 게시물_작성_페이지_이동_테스트() {
        webTestClient.get().uri("/writing")
                .header("Cookie", cookie)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void 게시물_작성_요청_후_리다이렉팅_테스트() {
        webTestClient.post().uri("/articles")
                .exchange()
                .expectStatus().is3xxRedirection();
    }

    @Test
    void 게시물_조회_테스트() {

        webTestClient.get().uri("/articles/" + currentArticleId)
                .header("Cookie", cookie)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response -> {
                    String body = new String(response.getResponseBody());
                    assertThat(body.contains(ArticleDataForTest.ARTICLE_TITLE)).isTrue();
                    //assertThat(body.contains(ArticleDataForTest.ARTICLE_COVER_URL)).isTrue();
                    assertThat(body.contains(ArticleDataForTest.ARTICLE_CONTENTS)).isTrue();

                });
    }

    @Test
    void 게시물_추가_요청_테스트() {

        webTestClient.post()
                .uri("/articles")
                .header("Cookie", cookie)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("title", ArticleDataForTest.NEW_TITLE)
                        .with("coverUrl", ArticleDataForTest.NEW_COVER_URL)
                        .with("contents", ArticleDataForTest.NEW_CONTENTS))
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectBody()
                .consumeWith(response -> {
                    webTestClient.get().uri(response.getResponseHeaders().getLocation())
                            .header("Cookie", cookie)
                            .exchange()
                            .expectBody()
                            .consumeWith(res -> {
                                String body = new String(res.getResponseBody());
                                assertThat(body.contains(ArticleDataForTest.NEW_TITLE)).isTrue();
                                //assertThat(body.contains(ArticleDataForTest.NEW_COVER_URL)).isTrue();
                                assertThat(body.contains(ArticleDataForTest.NEW_CONTENTS)).isTrue();
                            });
                });
    }

    @Test
    void 게시물_수정_페이지_이동_테스트() {
        webTestClient.get().uri("/articles/" + currentArticleId + "/edit")
                .header("Cookie", cookie)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void 게시물_수정_요청_테스트() {

        webTestClient.put().uri("/articles/" + currentArticleId)
                .header("Cookie", cookie)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("title", ArticleDataForTest.UPDATE_TITLE)
                        .with("coverUrl", ArticleDataForTest.UPDATE_COVER_URL)
                        .with("contents", ArticleDataForTest.UPDATE_CONTENTS))
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectBody()
                .consumeWith(response -> webTestClient.get().uri(response.getResponseHeaders().getLocation())
                        .header("Cookie", cookie)
                        .exchange()
                        .expectBody()
                        .consumeWith(res -> {
                            String body = new String(res.getResponseBody());
                            assertThat(body.contains(ArticleDataForTest.UPDATE_TITLE)).isTrue();
                            //assertThat(body.contains(ArticleDataForTest.UPDATE_COVER_URL)).isTrue();
                            assertThat(body.contains(ArticleDataForTest.UPDATE_CONTENTS)).isTrue();
                        }));
    }

    @AfterEach
    void 게시물_삭제_요청_테스트() {
        webTestClient.delete().uri("/articles/" + currentArticleId++)
                .exchange()
                .expectStatus().is3xxRedirection();
    }
}
