package techcourse.myblog.web;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import java.net.URI;

import static io.restassured.RestAssured.delete;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static techcourse.myblog.Utils.TestUtils.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ArticleControllerTest {
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
                .cookie("JSESSIONID", logInAsBaseUser(webTestClient))
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
    void showCreatePageWhenUserLogIn() {
        webTestClient.get().uri("/articles/new")
                .cookie("JSESSIONID", logInAsBaseUser(webTestClient))
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void showCreatePageWhenUserLogOut() {
        webTestClient.get().uri("/articles/new")
                .exchange()
                .expectStatus().isFound();
    }

    @Test
    void createArticleWhenLogin() {
        String newTitle = "New Title";
        String newCoverUrl = "New Cover Url";
        String newContents = "New Contents";

        webTestClient.post()
                .uri("/articles")
                .cookie("JSESSIONID", logInAsBaseUser(webTestClient))
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
    void createArticleWhenLogout() {
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
                .expectHeader().valueMatches("location", ".*/login.*");
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
    void showEditPageWhenUserMatch() {
        webTestClient.get()
                .uri(setUpArticleUrl + "/edit")
                .cookie("JSESSIONID", logInAsBaseUser(webTestClient))
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
    void showEditPageWhenUserMismatch() {
        webTestClient.get()
                .uri(setUpArticleUrl + "/edit")
                .cookie("JSESSIONID", logInAsMismatchUser(webTestClient))
                .exchange()
                .expectStatus().isFound()
                .expectHeader().valueMatches("location", ".*/articles/[0-9]+.*");
    }

    @Test
    void editArticleWhenUserMatch() {
        String newTitle = "test";
        String newCoverUrl = "newCorverUrl";
        String newContents = "newContents";

        webTestClient.put()
                .uri(setUpArticleUrl)
                .cookie("JSESSIONID", logInAsBaseUser(webTestClient))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("title", newTitle)
                        .with("coverUrl", newCoverUrl)
                        .with("contents", newContents))
                .exchange()
                .expectStatus().is3xxRedirection();

        WebTestClient.ResponseSpec responseSpec = webTestClient.get()
                .uri(setUpArticleUrl)
                .exchange()
                .expectStatus().isOk();

        assertThat(getBody(responseSpec)).contains(newTitle, newCoverUrl, newContents);
    }

    @Test
    void editArticleWhenUserMismatch() {
        String newTitle = "test";
        String newCoverUrl = "newCorverUrl";
        String newContents = "newContents";

        webTestClient.put()
                .uri(setUpArticleUrl)
                .cookie("JSESSIONID", logInAsMismatchUser(webTestClient))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("title", newTitle)
                        .with("coverUrl", newCoverUrl)
                        .with("contents", newContents))
                .exchange()
                .expectStatus().is5xxServerError();
    }

    @Test
    void deleteArticleWithMatchUser() {
        webTestClient.delete()
                .uri(setUpArticleUrl)
                .cookie("JSESSIONID", logInAsBaseUser(webTestClient))
                .exchange()
                .expectStatus().isFound();

        webTestClient.get()
                .uri(setUpArticleUrl)
                .exchange()
                .expectStatus().isFound();
    }

    @Test
    void deleteArticleWithMismatchUser() {
        webTestClient.delete()
                .uri(setUpArticleUrl)
                .cookie("JSESSIONID", logInAsMismatchUser(webTestClient))
                .exchange()
                .expectStatus().is5xxServerError();
    }

    @AfterEach
    void tearDown() {
        delete(setUpArticleUrl);
    }
}
