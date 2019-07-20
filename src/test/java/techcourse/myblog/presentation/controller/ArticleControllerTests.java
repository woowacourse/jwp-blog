package techcourse.myblog.presentation.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ArticleControllerTests {
    @Autowired
    private WebTestClient webTestClient;

    private Long id = 0L;

    @Test
    void index() {
        webTestClient.get().uri("/")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void writeForm_test() {
        webTestClient.get().uri("/writing")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void save_test() {
        webTestClient.post().uri("/articles")
                .body(BodyInserters.fromFormData("title", "title")
                        .with("coverUrl", "coverUrl")
                        .with("contents", "contents"))
                .exchange()
                .expectStatus()
                .isFound();

        ++id;
    }

    @Test
    void update_test() {
        insertArticle();

        webTestClient.put().uri("/articles/" + id)
                .body(BodyInserters.fromFormData("title", "title")
                        .with("coverUrl", "coverUrl")
                        .with("contents", "contents"))
                .exchange()
                .expectHeader().valueMatches("location", "(http://localhost:)(.*)(/articles/" + id + ")")
                .expectStatus()
                .isFound();

        deleteArticle();
    }

    @Test
    void delete_test() {
        insertArticle();

        webTestClient.delete().uri("/articles/" + id)
                .exchange()
                .expectStatus()
                .isFound();
    }

    private void deleteArticle() {
        webTestClient.delete().uri("/articles/" + id)
                .exchange();
    }

    private void insertArticle() {
        ++id;

        webTestClient.post().uri("/articles")
                .body(BodyInserters.fromFormData("title", "title")
                        .with("coverUrl", "coverUrl")
                        .with("contents", "contents"))
                .exchange();
    }
}
