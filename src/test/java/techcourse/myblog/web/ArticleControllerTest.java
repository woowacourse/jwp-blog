package techcourse.myblog.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.BodyInserters;
import techcourse.myblog.domain.article.ArticleRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class ArticleControllerTest {
    private static final String ARTICLE_DELIMITER
            = "<div role=\"tabpanel\" class=\"tab-pane fade in active\" id=\"tab-centered-1\">";

    private static final String TITLE_NAME = "title";
    private static final String COVER_URL_NAME = "coverUrl";
    private static final String CONTENTS_NAME = "contents";

    private static final String TITLE_VALUE = "TEST";
    private static final String COVER_URL_VALUE = "https://img.com";
    private static final String CONTENTS_VALUE = "TEST_CONTENTS";

    private static long testId = 0;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private WebTestClient webTestClient;

    @BeforeEach
    public void setUp() {
        articleRepository.deleteAll();
    }

    @Test
    @DisplayName("새로운_Article을_쓸때_테스트")
    public void showWritingPage() {
        webTestClient.get()
                .uri("/writing")
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    @DisplayName("Article_목록_조회_테스트")
    public void indexTest() {
        final int count = 3;
        addArticleTest();
        addArticleTest();
        addArticleTest();

        webTestClient.get()
                .uri("/")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response -> {
                    String body = new String(response.getResponseBody());
                    assertEquals(count, StringUtils.countOccurrencesOf(body, ARTICLE_DELIMITER));
                });
    }

    @Test
    @DisplayName("Article_추가_테스트")
    public void addArticleTest() {
        testId++;
        webTestClient.post()
                .uri("/articles/")
                .body(BodyInserters
                        .fromFormData(TITLE_NAME, TITLE_VALUE)
                        .with(COVER_URL_NAME, COVER_URL_VALUE)
                        .with(CONTENTS_NAME, CONTENTS_VALUE))
                .exchange()
                .expectStatus()
                .isFound();
    }

    @Test
    @DisplayName("Article_개별조회_테스트")
    public void showArticleById() {
        addArticleTest();

        webTestClient.get()
                .uri("/articles/" + testId)
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    @DisplayName("Article_변경_테스트")
    public void updateArticleById() {
        addArticleTest();

        webTestClient.get()
                .uri("/articles/" + testId + "/edit")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response -> {
                    String body = new String(response.getResponseBody());
                    assertThat(body.contains(TITLE_VALUE)).isTrue();
                    assertThat(body.contains(COVER_URL_VALUE)).isTrue();
                    assertThat(body.contains(CONTENTS_VALUE)).isTrue();
                });
    }

    @Test
    @DisplayName("Article_삭제_테스트")
    public void deleteArticle() {
        addArticleTest();

        webTestClient.delete()
                .uri("/articles/" + testId)
                .exchange()
                .expectStatus()
                .isFound();
    }
}
