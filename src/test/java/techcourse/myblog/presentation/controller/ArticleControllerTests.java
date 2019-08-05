package techcourse.myblog.presentation.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import static org.springframework.web.reactive.function.BodyInserters.fromFormData;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ArticleControllerTests {
    private static final String EMAIL = "hard@gmail.com";
    private static final String NAME = "hard";
    private static final String PASSWORD = "qwerasdf";

    WebTestClient webTestClient;

    private static Long id = 1L;

    private static String cookie;

    @Autowired
    public ArticleControllerTests(WebTestClient webTestClient) {
        this.webTestClient = webTestClient;
        registerUser();
        logInAndGetSessionId();
    }

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
                .header("Cookie", cookie)
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
                .header("Cookie", cookie)
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
                .header("Cookie", cookie)
                .exchange()
                .expectStatus()
                .isFound();
    }

    private void deleteArticle() {
        webTestClient.delete().uri("/articles/" + id)
                .exchange();
    }

    private void registerUser() {
        webTestClient.post().uri("/users")
                .body(BodyInserters.fromFormData("email", EMAIL)
                        .with("name", NAME)
                        .with("password", PASSWORD))
                .exchange();
    }

    private void insertArticle() {
        ++id;

        webTestClient.post().uri("/articles")
                .header("Cookie", cookie)
                .body(BodyInserters.fromFormData("title", "title")
                        .with("coverUrl", "coverUrl")
                        .with("contents", "contents"))
                .exchange();
    }


    private void logInAndGetSessionId() {
        cookie = webTestClient.post()
                .uri("/login").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(fromFormData("email", EMAIL)
                        .with("password", PASSWORD))
                .exchange()
                .returnResult(String.class).getResponseHeaders().getFirst("Set-Cookie");
    }
}
