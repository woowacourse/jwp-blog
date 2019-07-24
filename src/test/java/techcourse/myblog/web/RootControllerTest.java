package techcourse.myblog.web;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.BodyInserters;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@TestPropertySource("classpath:application_test.properties")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RootControllerTest {
    private static final String ARTICLE_DELIMITER
            = "<div role=\"tabpanel\" class=\"tab-pane fade in active\" id=\"tab-centered-1\">";
    private static String title = "TEST";
    private static String coverUrl = "https://img.com";
    private static String contents = "testtest";
    private static String categoryId = "1";

    @Autowired
    private WebTestClient webTestClient;

    private static long articleId;

    @BeforeEach
    void setUp() {
        webTestClient.post()
                .uri("/articles/new")
                .body(BodyInserters
                        .fromFormData("title", title)
                        .with("coverUrl", coverUrl)
                        .with("contents", contents)
                        .with("categoryId", categoryId))
                .exchange()
                .expectHeader()
                .value("location", s -> {
                    articleId = Long.parseLong(s.split("/")[4]);
                });
    }

    @Test
    public void index() {
        int count = 1;

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
    public void indexTestByCategory() {
        int count = 1;

        webTestClient.get()
                .uri("/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response -> {
                    String body = new String(response.getResponseBody());
                    assertEquals(count, StringUtils.countOccurrencesOf(body, ARTICLE_DELIMITER));
                });
    }

    @AfterEach
    void tearDown() {
        //delete
        webTestClient.delete()
                .uri("/articles/" + articleId)
                .exchange()
                .expectStatus().isFound();
    }

}