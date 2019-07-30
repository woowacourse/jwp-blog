package techcourse.myblog.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.BodyInserters;
import techcourse.myblog.domain.ArticleRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class IndexControllerTest {
    @Autowired
    WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        articleRepository.deleteAll();
    }

    @Test
    @DisplayName("새로운글을_쓸때_테스트")
    public void showWritingPage() {
        webTestClient.get()
                .uri(WRITING_URL)
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    @DisplayName("Article을_추가할때_redirect하는지_테스트")
    public void addArticleTest() {
        webTestClient.post()
                .uri(ARTICLE_URL)
                .body(BodyInserters
                        .fromFormData(TITLE_NAME, TITLE_VALUE)
                        .with(COVER_URL_NAME, COVER_URL_VALUE)
                        .with(CONTENTS_NAME, CONTENTS_VALUE))
                .exchange()
                .expectStatus()
                .isFound();
    }

    @Test
    @DisplayName("Article의_목록을_조회하는지_테스트")
    public void indexTest() {
        final int count = 3;
        addArticleTest();
        addArticleTest();
        addArticleTest();

        webTestClient.get()
                .uri(ROOT_URL)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response -> {
                    String body = new String(response.getResponseBody());
                    assertEquals(count, StringUtils.countOccurrencesOf(body, ARTICLE_DELIMITER));
                });
    }

    @Test
    @DisplayName("ArticleId에_맞는_Article을_조회할때_테스트")
    public void showArticleById() {
        addArticleTest();

        webTestClient.get()
                .uri(SPECIFIC_ARTICLE_URL)
                .exchange()
                .expectStatus()
                .isOk();
    }
}