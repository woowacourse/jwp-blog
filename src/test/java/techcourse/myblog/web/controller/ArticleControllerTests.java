package techcourse.myblog.web.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ArticleControllerTests extends BaseControllerTests {

    @Autowired
    WebTestClient webTestClient;

    private String articleId;
    private String title = "title";
    private String coverUrl = "coverUrl";
    private String contents = "contents";

    private final String userEmail = "emailArticle@gamil.com";
    private final String userPassword = "P@ssw0rd";
    private String jSessionId;

    @BeforeEach
    void setUp() {
        addUser("name", userEmail, userPassword);
        jSessionId = getJSessionId(userEmail, userPassword);

        articleId = webTestClient.post().uri("/articles")
                .cookie(JSESSIONID, jSessionId)
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
                .cookie(JSESSIONID, jSessionId)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void writeTest() {
        webTestClient.post().uri("/articles")
                .cookie(JSESSIONID, jSessionId)
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
        webTestClient.get().uri("/articles/" + articleId)
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
        webTestClient.get().uri("/articles/" + articleId + "/edit")
                .cookie(JSESSIONID, jSessionId)
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

        webTestClient.put().uri("/articles/" + articleId)
                .cookie(JSESSIONID, jSessionId)
                .body(BodyInserters.fromFormData("title", title)
                        .with("coverUrl", coverUrl)
                        .with("contents", contents))
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueMatches("location", ".*/articles/" + articleId);
    }

    @Test
    void 다른_사용자가_edit_시도_예외처리() {
        String title = "1";
        String coverUrl = "2";
        String contents = "3";

        webTestClient.put().uri("/articles/" + articleId)
                .cookie(JSESSIONID, getJSessionId())
                .body(BodyInserters.fromFormData("title", title)
                        .with("coverUrl", coverUrl)
                        .with("contents", contents))
                .exchange()
                .expectStatus().isForbidden();
    }

    @AfterEach
    void tearDown() {
        webTestClient.delete().uri("/articles/" + articleId)
                .cookie(JSESSIONID, jSessionId)
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueMatches("location", ".*/");
    }
}