package techcourse.myblog.web;

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
    private WebTestClient.ResponseSpec responseSpec;

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
        insertArticle();

        responseSpec
                .expectStatus()
                .isOk();
    }

    @Test
    void update_test() {
        insertArticle();

        webTestClient.put().uri("/articles/1")
                .body(BodyInserters.fromFormData("title", "title")
                        .with("coverUrl", "coverUrl").with("contents", "contents"))
                .exchange()
                .expectHeader().valueMatches("Location","http://localhost:[0-9]+/$")
                .expectStatus().is3xxRedirection();
    }

    @Test
    void delete_test() {
        insertArticle();

        webTestClient.delete().uri("/articles/1")
                .exchange()
                .expectStatus()
                .is3xxRedirection();
    }

    private void insertArticle() {
        responseSpec = webTestClient.post().uri("/articles")
                .body(BodyInserters.fromFormData("title", "title")
                        .with("coverUrl", "coverUrl").with("contents", "contents"))
                .exchange();
    }
}
