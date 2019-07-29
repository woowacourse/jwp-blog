package techcourse.myblog.web;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import techcourse.myblog.utils.Utils;

import java.net.URI;

import static io.restassured.RestAssured.delete;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ArticleControllerTests {
    private static final String SAMPLE_TITLE = "SAMPLE_TITLE";
    private static final String SAMPLE_COVER_URL = "SAMPLE_COVER_URL";
    private static final String SAMPLE_CONTENTS = "SAMPLE_CONTENTS";

    private String setUpArticleUrl;

    @LocalServerPort
    int srverPort;

    @Autowired
    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        String baseUrl = "http://localhost:" + srverPort;

        setUpArticleUrl = given()
                .param("title", SAMPLE_TITLE)
                .param("coverUrl", SAMPLE_COVER_URL)
                .param("contents", SAMPLE_CONTENTS)
                .cookie("JSESSIONID", Utils.logInAsSampleUser(webTestClient))
                .post(baseUrl + "/articles")
                .getHeader("Location");
    }

    @Test
    @DisplayName("Article들의 목록을 index페이지에 담아 되돌려준다.")
    void showArticles() {
        webTestClient.get().uri("/articles")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    @DisplayName("User가 login 되어있을 때 새 글을 쓰는 페이지를 되돌려준다.")
    void showCreatePageWhenUserLogIn() {
        webTestClient.get().uri("/articles/new")
                .cookie("JSESSIONID", Utils.logInAsSampleUser(webTestClient))
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    @DisplayName("User가 logout 되어있을 때 새 글 쓰는 페이지에 접근하면 login 페이지로 redirect한다.")
    void showCreatePageWhenUserLogOut() {
        webTestClient.get().uri("/articles/new")
                .exchange()
                .expectStatus().isFound();
    }

    @Test
    @DisplayName("새로운 Article을 생성하고 생성된 article을 보여준다.")
    void createArticle() {
        String newTitle = "New Title";
        String newCoverUrl = "New Cover Url";
        String newContents = "New Contents";

        WebTestClient.ResponseSpec createdArticle = webTestClient.post()
                .uri("/articles")
                .cookie("JSESSIONID", Utils.logInAsSampleUser(webTestClient))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("title", newTitle)
                        .with("coverUrl", newCoverUrl)
                        .with("contents", newContents))
                .exchange()
                .expectStatus().isFound();

        createdArticle.expectBody()
                .consumeWith(response -> {
                    URI location = response.getResponseHeaders().getLocation();
                    webTestClient.get()
                            .uri(location)
                            .cookie("JSESSIONID", Utils.logInAsSampleUser(webTestClient))
                            .exchange()
                            .expectBody()
                            .consumeWith(res -> {
                                String body = new String(res.getResponseBody());
                                assertThat(body).contains(newTitle);
                                assertThat(body).contains(newCoverUrl);
                                assertThat(body).contains(newContents);
                            });
                });
    }

    @Test
    @DisplayName("게시된 Article 하나를 보여준다.")
    void showArticle() {
        webTestClient.get()
                .uri(setUpArticleUrl)
                .cookie("JSESSIONID", Utils.logInAsSampleUser(webTestClient))
                .exchange()
                .expectBody()
                .consumeWith(res -> {
                    String body = new String(res.getResponseBody());
                    assertThat(body).contains(SAMPLE_TITLE);
                    assertThat(body).contains(SAMPLE_COVER_URL);
                    assertThat(body).contains(SAMPLE_CONTENTS);
                });
    }

    @Test
    @DisplayName("Article author가 article 수정 페이지에 접근하는 경우 수정 페이지를 되돌려준다.")
    void showEditPage() {
        webTestClient.get()
                .uri(setUpArticleUrl + "/edit")
                .cookie("JSESSIONID", Utils.logInAsSampleUser(webTestClient))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response -> {
                    String body = new String(response.getResponseBody());
                    assertThat(body).contains(SAMPLE_TITLE);
                    assertThat(body).contains(SAMPLE_COVER_URL);
                    assertThat(body).contains(SAMPLE_CONTENTS);
                });
    }

    @Test
    @DisplayName("Article author가 아닌 사람이 article 수정 페이지에 접근하는 경우 예외 페이지를 되돌려준다,")
    void showPermissionDeniedPageWhenNotAuthenticatedEditor() {
        webTestClient.get()
                .uri(setUpArticleUrl + "/edit")
                .exchange()
                .expectStatus().isFound();
    }

    @Test
    @DisplayName("Article을 수정한다.")
    void editArticle() {
        String newTitle = "test";
        String newCoverUrl = "newCorverUrl";
        String newContents = "newContents";

        webTestClient.put()
                .uri(setUpArticleUrl)
                .cookie("JSESSIONID", Utils.logInAsSampleUser(webTestClient))
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
                            .cookie("JSESSIONID", Utils.logInAsSampleUser(webTestClient))
                            .exchange()
                            .expectBody()
                            .consumeWith(res -> {
                                String body = new String(res.getResponseBody());
                                assertThat(body).contains(newTitle);
                                assertThat(body).contains(newCoverUrl);
                                assertThat(body).contains(newContents);
                            });
                });
    }

    @Test
    @DisplayName("Article을 지운다.")
    void deleteArticle() {
        webTestClient.delete()
                .uri(setUpArticleUrl)
                .cookie("JSESSIONID", Utils.logInAsSampleUser(webTestClient))
                .exchange()
                .expectStatus().isFound();

        webTestClient.get()
                .uri(setUpArticleUrl)
                .cookie("JSESSIONID", Utils.logInAsSampleUser(webTestClient))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response -> {
                    String body = new String(response.getResponseBody());
                    assertThat(body).contains("Wrong access");
                });
    }

    @AfterEach
    void tearDown() {
        delete(setUpArticleUrl);
    }
}
