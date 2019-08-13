package techcourse.myblog.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import static org.assertj.core.api.Assertions.assertThat;

public class ArticleControllerTests extends LoggedInTemplate {

    private static final Logger log = LoggerFactory.getLogger(ArticleControllerTests.class);

    private static final String TITLE = "title";
    private static final String CONTENTS = "contents";
    private static final String COVER_URL = "/images/pages/index/bg.jpg";

    private static final String UPDATED_TITLE = "updatedTitle";
    private static final String UPDATED_COVER_URL = "updatedCoverUrl";
    private static final String UPDATED_CONTENTS = "updatedContents";

    @BeforeEach
    void setUp() {
        signUpUser();
    }

    @Test
    void 메인_페이지_접근() {
        webTestClient.get()
                .uri("/")
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    void 게시글_작성__페이지_접근() {
        loggedInGetRequest("/writing")
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    void 게시글_작성_성공_테스트() {
        게시글_작성().expectStatus()
                .is3xxRedirection()
                .expectHeader()
                .valueMatches("location", ".+/articles/.+");
    }

    @Test
    void 작성된_게시글_목록_조회() {
        게시글_작성().expectStatus()
                .is3xxRedirection();

        loggedInGetRequest("/")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .consumeWith(response -> {
                    String body = new String(response.getResponseBody());
                    assertThat(body.contains(TITLE)).isTrue();
                    assertThat(body.contains(CONTENTS)).isTrue();
                });
    }

    @Test
    void 게시글_수정_성공_테스트() {
        String articleId = getArticleID();

        loggedInPutRequest("/articles/" + articleId)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(BodyInserters
                        .fromFormData("title", UPDATED_TITLE)
                        .with("contents", UPDATED_CONTENTS)
                        .with("coverUrl", UPDATED_COVER_URL))
                .exchange()
                .expectStatus()
                .is3xxRedirection()
                .expectHeader()
                .valueMatches("location", ".+/articles/" + articleId);
    }

    @Test
    void 게시글_삭제_성공_테스트() {
        String articleId = getArticleID();

        loggedInDeleteRequest("/articles/" + articleId)
                .exchange()
                .expectStatus()
                .is3xxRedirection()
                .expectHeader()
                .valueMatches("location", ".+/");
    }

    WebTestClient.ResponseSpec 게시글_작성() {
        return loggedInPostRequest("/articles")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("title", TITLE)
                        .with("contents", CONTENTS)
                        .with("coverUrl", COVER_URL))
                .exchange();
    }

    private String getArticleID() {
        String path = 게시글_작성().expectBody()
                .returnResult()
                .getResponseHeaders()
                .getLocation()
                .getPath();

        log.debug("article path : {} ", path);
        return path.split("/")[2];
    }
}