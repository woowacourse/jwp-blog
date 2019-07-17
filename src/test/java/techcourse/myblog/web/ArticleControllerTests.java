package techcourse.myblog.web;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import java.net.URI;

import static io.restassured.RestAssured.delete;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ArticleControllerTests {
    private static final String SAMPLE_TITLE = "SAMPLE_TITLE";
    private static final String SAMPLE_COVER_URL = "SAMPLE_COVER_URL";
    private static final String SAMPLE_CONTENTS = "SAMPLE_CONTENTS";

    private String baseUrl;
    private String setUpArticleUrl;

    @LocalServerPort
    int randomServerPort;

    @Autowired
    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + randomServerPort;

        setUpArticleUrl = given()
                .param("title", SAMPLE_TITLE)
                .param("coverUrl", SAMPLE_COVER_URL)
                .param("contents", SAMPLE_CONTENTS)
                .post(baseUrl + "/articles")
                .getHeader("Location");
    }

    @Test
    void index() {
        webTestClient.get().uri("/")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void showArticles() {
        webTestClient.get().uri("/articles")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void showCreatePage() {
        webTestClient.get().uri("/articles/new")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void createArticle() {
        String newTitle = "New Title";
        String newCoverUrl = "New Cover Url";
        String newContents = "New Contents";

        webTestClient.post()
                .uri("/articles")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("title", newTitle)
                        .with("coverUrl", newCoverUrl)
                        .with("contents", newContents))
                .exchange()
                .expectStatus().isFound()
                .expectBody()
                .consumeWith(response -> {
                    URI location = response.getResponseHeaders().getLocation();
                    webTestClient.get()
                            .uri(location)
                            .exchange()
                            .expectBody()
                            .consumeWith(res -> {
                                String body = new String(res.getResponseBody());
                                assertThat(body.contains(newTitle)).isTrue();
                                assertThat(body.contains(newCoverUrl)).isTrue();
                                assertThat(body.contains(newContents)).isTrue();
                            });
                });
    }

    @Test
    void showArticle() {
        webTestClient.get()
                .uri(setUpArticleUrl)
                .exchange()
                .expectBody()
                .consumeWith(res -> {
                    String body = new String(res.getResponseBody());
                    assertThat(body.contains(SAMPLE_TITLE)).isTrue();
                    assertThat(body.contains(SAMPLE_COVER_URL)).isTrue();
                    assertThat(body.contains(SAMPLE_CONTENTS)).isTrue();
                });
    }

    @Test
    void showEditPage() {
        webTestClient.get()
                .uri(setUpArticleUrl + "/edit")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response -> {
                    String body = new String(response.getResponseBody());
                    assertThat(body.contains(SAMPLE_TITLE)).isTrue();
                    assertThat(body.contains(SAMPLE_COVER_URL)).isTrue();
                    assertThat(body.contains(SAMPLE_CONTENTS)).isTrue();
                });
    }

    @Test
    void editArticle() {
        String newTitle = "test";
        String newCoverUrl = "newCorverUrl";
        String newContents = "newContents";

        webTestClient.put()
                .uri(setUpArticleUrl)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("title", newTitle)
                        .with("coverUrl", newCoverUrl)
                        .with("contents", newContents))
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectBody()
                .consumeWith(response -> {
                    URI location = response.getResponseHeaders().getLocation();
                    webTestClient.get()
                            .uri(location)
                            .exchange()
                            .expectBody()
                            .consumeWith(res -> {
                                String body = new String(res.getResponseBody());
                                assertThat(body.contains(newTitle)).isTrue();
                                assertThat(body.contains(newCoverUrl)).isTrue();
                                assertThat(body.contains(newContents)).isTrue();
                            });
                });
    }

    @Test
    void deleteArticle() {
        webTestClient.delete()
                .uri(setUpArticleUrl)
                .exchange()
                .expectStatus().is3xxRedirection();

        webTestClient.get()
                .uri(setUpArticleUrl)
                .exchange()
                .expectStatus().is5xxServerError();
    }

    @AfterEach
    void tearDown() {
        delete(setUpArticleUrl);
    }
}
