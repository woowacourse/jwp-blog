package techcourse.myblog.controller.web;

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
    private static final String NEW_TITLE = "New Title";
    private static final String NEW_COVER_URL = "New Cover Url";
    private static final String NEW_CONTENTS = "New Contents";

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
        URI location = webTestClient.post()
                .uri("/articles")
                .cookie("JSESSIONID", logInAsBaseUser(webTestClient))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("title", NEW_TITLE)
                        .with("coverUrl", NEW_COVER_URL)
                        .with("contents", NEW_CONTENTS))
                .exchange()
                .expectStatus().isFound()
                .expectBody()
                .returnResult()
                .getUrl();

        WebTestClient.ResponseSpec responseSpec = webTestClient.get()
                .uri(location)
                .exchange()
                .expectStatus().isOk();

        assertThat(getBody(responseSpec)).contains(NEW_TITLE, NEW_COVER_URL, NEW_CONTENTS);
    }

    @Test
    void createArticleWhenLogout() {
        webTestClient.post()
                .uri("/articles")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("title", NEW_TITLE)
                        .with("coverUrl", NEW_CONTENTS)
                        .with("contents", NEW_COVER_URL))
                .exchange()
                .expectStatus().isFound()
                .expectHeader().valueMatches("location", ".*/login.*");
    }

    @Test
    void showArticle() {
        WebTestClient.ResponseSpec responseSpec = webTestClient.get()
                .uri(setUpArticleUrl)
                .exchange()
                .expectStatus().isOk();

        assertThat(getBody(responseSpec)).contains(SAMPLE_TITLE, SAMPLE_COVER_URL, SAMPLE_CONTENTS);
    }

    @Test
    void showEditPageWhenUserMatch() {
        WebTestClient.ResponseSpec responseSpec = webTestClient.get()
                .uri(setUpArticleUrl + "/edit")
                .cookie("JSESSIONID", logInAsBaseUser(webTestClient))
                .exchange()
                .expectStatus().isOk();

        assertThat(getBody(responseSpec)).contains(SAMPLE_TITLE, SAMPLE_COVER_URL, SAMPLE_CONTENTS);
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
        webTestClient.put()
                .uri(setUpArticleUrl)
                .cookie("JSESSIONID", logInAsBaseUser(webTestClient))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("title", NEW_TITLE)
                        .with("coverUrl", NEW_COVER_URL)
                        .with("contents", NEW_CONTENTS))
                .exchange()
                .expectStatus().is3xxRedirection();

        WebTestClient.ResponseSpec responseSpec = webTestClient.get()
                .uri(setUpArticleUrl)
                .exchange()
                .expectStatus().isOk();

        assertThat(getBody(responseSpec)).contains(NEW_TITLE, NEW_COVER_URL, NEW_CONTENTS);
    }

    @Test
    void editArticleWhenUserMismatch() {
        webTestClient.put()
                .uri(setUpArticleUrl)
                .cookie("JSESSIONID", logInAsMismatchUser(webTestClient))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("title", NEW_TITLE)
                        .with("coverUrl", NEW_COVER_URL)
                        .with("contents", NEW_CONTENTS))
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
