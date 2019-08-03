package techcourse.myblog.article.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import techcourse.myblog.util.UserUtilForTest;
import techcourse.myblog.util.WebTest;

import static org.assertj.core.api.Assertions.assertThat;
import static techcourse.myblog.article.ArticleDataForTest.*;
import static techcourse.myblog.user.UserDataForTest.EMPTY_COOKIE;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ArticleControllerTests {
    private static final long ARTICLE_ID = 1;
    private static final long ARTICLE_DELETE_ID = 2;

    @Autowired
    private WebTestClient webTestClient;

    private String cookie;

    @BeforeEach
    void setUp() {
        cookie = UserUtilForTest.loginAndGetCookie(webTestClient);
    }

    @Test
    void index() {
        WebTest.executeGetTest(webTestClient, "/", EMPTY_COOKIE)
                .expectStatus().isOk();
    }

    @Test
    void 게시물_작성_페이지_이동_테스트() {
        WebTest.executeGetTest(webTestClient, "/writing", cookie)
                .expectStatus().isOk();
    }

    @Test
    void 게시물_조회_테스트() {

        WebTest.executeGetTest(webTestClient, "/", cookie)
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response -> {
                    String body = new String(response.getResponseBody());
                    assertThat(body.contains(ARTICLE_TITLE)).isTrue();
                    //assertThat(body.contains(ARTICLE_COVER_URL)).isTrue();
                    assertThat(body.contains(ARTICLE_CONTENTS)).isTrue();
                });
    }

    @Test
    void 게시물_추가_요청_테스트() {

        EntityExchangeResult<byte[]> entityExchangeResult =
                WebTest.executePostTest(webTestClient, "/articles", cookie,
                        NEW_ARTICLE_BODY)
                        .expectStatus().isFound()
                        .expectBody()
                        .returnResult();

        String location = String.valueOf(entityExchangeResult.getResponseHeaders().getLocation());
        WebTest.executeGetTest(webTestClient, location, cookie)
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response -> {
                    String body = new String(response.getResponseBody());
                    assertThat(body.contains(NEW_TITLE)).isTrue();
                    //assertThat(body.contains(NEW_COVER_URL)).isTrue();
                    assertThat(body.contains(NEW_CONTENTS)).isTrue();
                });
    }

    @Test
    void 게시물_수정_페이지_이동_테스트() {
        WebTest.executeGetTest(webTestClient, "/articles/" + ARTICLE_ID + "/edit", cookie)
                .expectStatus().isOk();
    }

    @Test
    void 게시물_수정_요청_테스트() {

        EntityExchangeResult<byte[]> entityExchangeResult =
                WebTest.executePutTest(webTestClient, "/articles/" + ARTICLE_ID, cookie,
                        UPDATE_BODY_INSERTER)
                        .expectStatus().isFound()
                        .expectBody()
                        .returnResult();

        String location = String.valueOf(entityExchangeResult.getResponseHeaders().getLocation());
        WebTest.executeGetTest(webTestClient, location, cookie)
                .expectBody()
                .consumeWith(response -> {
                    String body = new String(response.getResponseBody());
                    assertThat(body.contains(UPDATE_TITLE)).isTrue();
                    //assertThat(body.contains(UPDATE_COVER_URL)).isTrue();
                    assertThat(body.contains(UPDATE_CONTENTS)).isTrue();
                });
    }

    @Test
    void 게시물_삭제_요청_테스트() {
        WebTest.executeDeleteTest(webTestClient, "/articles/" + ARTICLE_DELETE_ID, cookie);
    }
}