package techcourse.myblog.article.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import techcourse.myblog.article.ArticleDataForTest;
import techcourse.myblog.user.UserDataForTest;
import techcourse.myblog.util.ArticleUtilForTest;
import techcourse.myblog.util.UserUtilForTest;
import techcourse.myblog.util.WebTest;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ArticleControllerTests {

    @Autowired
    private WebTestClient webTestClient;

    private String cookie;
    private String path;

    @BeforeEach
    void setUp() {
        UserUtilForTest.signUp(webTestClient);
        cookie = UserUtilForTest.loginAndGetCookie(webTestClient);
        path = ArticleUtilForTest.createArticle(webTestClient, cookie);
    }

    @Test
    void index() {
        WebTest.executeGetTest(webTestClient, "/", UserDataForTest.EMPTY_COOKIE)
                .expectStatus().isOk();
    }

    @Test
    void 게시물_작성_페이지_이동_테스트() {
        WebTest.executeGetTest(webTestClient, "/writing", cookie)
                .expectStatus().isOk();
    }

    @Test
    void 게시물_조회_테스트() {

        WebTest.executeGetTest(webTestClient, path, cookie)
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

        WebTest.executePostTest(webTestClient, "/articles", cookie,
                ArticleDataForTest.NEW_ARTICLE_BODY)
                .expectStatus().isFound()
                .expectBody()
                .consumeWith(response -> {
                    String uri = String.valueOf(response.getResponseHeaders().getLocation());
                    WebTest.executeGetTest(webTestClient, uri, cookie)
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
        WebTest.executeGetTest(webTestClient, path + "/edit", cookie)
                .expectStatus().isOk();
    }

    @Test
    void 게시물_수정_요청_테스트() {

        WebTest.executePutTest(webTestClient, path, cookie,
                ArticleDataForTest.UPDATE_BODY_INSERTER)
                .expectStatus().isFound()
                .expectBody()
                .consumeWith(response -> {
                    String uri = String.valueOf(response.getResponseHeaders().getLocation());
                    WebTest.executeGetTest(webTestClient, uri, cookie)
                            .expectBody()
                            .consumeWith(res -> {
                                String body = new String(res.getResponseBody());
                                assertThat(body.contains(ArticleDataForTest.UPDATE_TITLE)).isTrue();
                                //assertThat(body.contains(ArticleDataForTest.UPDATE_COVER_URL)).isTrue();
                                assertThat(body.contains(ArticleDataForTest.UPDATE_CONTENTS)).isTrue();
                            });
                });
    }

    @AfterEach
    void 게시물_삭제_요청_테스트() {
        WebTest.executeDeleteTest(webTestClient, path, cookie);
    }
}