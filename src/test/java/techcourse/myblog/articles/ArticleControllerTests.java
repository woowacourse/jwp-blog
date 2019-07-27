package techcourse.myblog.articles;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ArticleControllerTests {

    @Autowired
    WebTestClient webTestClient;

    private String id;
    private String title = "title";
    private String coverUrl = "coverUrl";
    private String contents = "contents";

    @BeforeEach
    void setUp() {
        id = webTestClient.post().uri("/articles")
                .body(BodyInserters
                        .fromFormData("title", title)
                        .with("coverUrl", coverUrl)
                        .with("contents", contents))
                .exchange()
                .returnResult(String.class)
                .getResponseHeaders().get("Location").get(0).split(".*/articles/")[1];
    }

    @Test
    void writeForm() {
        webTestClient.get().uri("/articles/new")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void writeTest() {
        webTestClient.post().uri("/articles")
                .body(BodyInserters
                        .fromFormData("title", title)
                        .with("coverUrl", coverUrl)
                        .with("contents", contents))
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueMatches("location", ".*/articles/.*");
    }

    @Test
    void showTest() {
        webTestClient.get().uri("/articles/" + id)
                .exchange()
                .expectStatus().isOk()
                .expectBody().consumeWith(response -> {
            String body = new String(response.getResponseBody());
            assertThat(body.contains(title)).isTrue();
            assertThat(body.contains(coverUrl)).isTrue();
            assertThat(body.contains(contents)).isTrue();
        });
    }

    @Test
    void editFormTest() {
        webTestClient.get().uri("/articles/" + id + "/edit")
                .exchange()
                .expectStatus().isOk()
                .expectBody().consumeWith(response -> {
            String body = new String(response.getResponseBody());
            assertThat(body.contains(title)).isTrue();
            assertThat(body.contains(coverUrl)).isTrue();
            assertThat(body.contains(contents)).isTrue();
        });
    }

    @Test
    void editTest() {
        String title = "1";
        String coverUrl = "2";
        String contents = "3";

        webTestClient.put().uri("/articles/" + id)
                .body(BodyInserters.fromFormData("title", title)
                        .with("coverUrl", coverUrl)
                        .with("contents", contents))
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueMatches("location", ".*/articles/" + id);
    }

    @AfterEach
    void tearDown() {
        webTestClient.delete().uri("/articles/" + id)
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueMatches("location", ".*/");
    }
}