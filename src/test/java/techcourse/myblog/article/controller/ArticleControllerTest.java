package techcourse.myblog.article.controller;

import org.junit.jupiter.api.Test;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.web.reactive.function.BodyInserters;
import techcourse.myblog.data.ArticleDataForTest;
import techcourse.myblog.template.RequestTemplate;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;

public class ArticleControllerTest extends RequestTemplate {
    @Test
    void 메인페이지_이동() {
        loggedOutGetRequest("/")
                .expectStatus()
                .isOk();
    }

    @Test
    void 게시글작성_페이지_이동() {
        loggedInGetRequest("/articles/new")
                .expectStatus()
                .isOk();
    }

    @Test
    void 게시글작성() {
        EntityExchangeResult<byte[]> entityExchangeResult = loggedInPostRequest("/articles")
                .body(BodyInserters.fromFormData("title", ArticleDataForTest.ARTICLE_TITLE)
                        .with("coverUrl", ArticleDataForTest.ARTICLE_COVER_URL)
                        .with("contents", ArticleDataForTest.ARTICLE_CONTENTS))
                .exchange()
                .expectStatus()
                .isFound()
                .expectBody()
                .returnResult();

        URI location = entityExchangeResult.getRequestHeaders().getLocation();
        webTestClient.get()
                .uri(location)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .consumeWith(res -> {
                    String body = new String(res.getResponseBody());
                    assertThat(body.contains(ArticleDataForTest.ARTICLE_TITLE)).isTrue();
                    assertThat(body.contains(ArticleDataForTest.ARTICLE_CONTENTS)).isTrue();
                });
    }

    @Test
    void 게시글_페이지_이동() {
        loggedInGetRequest("/articles/1")
                .expectStatus()
                .isOk()
                .expectBody()
                .consumeWith(res -> {
                    String body = new String(res.getResponseBody());
                    assertThat(body.contains(ArticleDataForTest.ARTICLE_TITLE)).isTrue();
                    assertThat(body.contains(ArticleDataForTest.ARTICLE_CONTENTS)).isTrue();
                });
    }

    @Test
    void 게시글_수정_페이지_이동() {
        loggedInGetRequest("/articles/1/edit")
                .expectStatus()
                .isOk();
    }

    @Test
    void 게시글_업데이트() {
        EntityExchangeResult<byte[]> entityExchangeResult = loggedInPutRequest("/articles/1")
                .body(BodyInserters.fromFormData("title", ArticleDataForTest.ARTICLE_TITLE_TWO)
                        .with("coverUrl", ArticleDataForTest.ARTICLE_COVER_URL_TWO)
                        .with("contents", ArticleDataForTest.ARTICLE_CONTENTS_TWO))
                .exchange()
                .expectStatus()
                .isFound()
                .expectBody()
                .returnResult();

        URI location = entityExchangeResult.getRequestHeaders().getLocation();
        loggedInGetRequest(location)
                .expectStatus()
                .isOk()
                .expectBody()
                .consumeWith(res -> {
                    String body = new String(res.getResponseBody());
                    assertThat(body.contains(ArticleDataForTest.ARTICLE_TITLE_TWO)).isTrue();
                    assertThat(body.contains(ArticleDataForTest.ARTICLE_CONTENTS_TWO)).isTrue();
                });
    }

    @Test
    void 게시글_삭제() {
        loggedInDeleteRequest("/articles/2")
                .expectStatus()
                .isFound();

        EntityExchangeResult<byte[]> entityExchangeResult = loggedInGetRequest("/articles/2")
                .expectStatus()
                .isFound()
                .expectBody()
                .returnResult();

        URI location = entityExchangeResult.getRequestHeaders().getLocation();
        loggedInGetRequest(location)
                .expectStatus()
                .isOk()
                .expectBody()
                .consumeWith(res -> {
                    String body = new String(res.getResponseBody());
                    assertThat(body.contains("찾을 수 없는 게시물입니다."));
                });
    }
}
