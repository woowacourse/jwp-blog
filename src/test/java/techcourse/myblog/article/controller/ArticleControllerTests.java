package techcourse.myblog.article.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import java.net.URI;
import java.util.Objects;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ArticleControllerTests {
    private static final Long DEFAULT_ARTICLE_ID = 999L;

    @Autowired
    private WebTestClient webTestClient;

    private String jSessionId;
    private URI location;
    private String title;
    private String coverUrl;
    private String contents;

    @BeforeEach
    void setUp() {
        title = "title";
        coverUrl = "";
        contents = "contents";

        jSessionId = getJSessionId("john123@example.com", "p@ssW0rd");
        location = getArticleLocation(jSessionId);
    }

    private String getJSessionId(String email, String password) {
        EntityExchangeResult<byte[]> loginResult = webTestClient.post().uri("/users/login")
                .body(BodyInserters
                        .fromFormData("email", email)
                        .with("password", password))
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueMatches("Location", ".*/.*")
                .expectBody()
                .returnResult();

        return extractJSessionId(loginResult);
    }

    private String extractJSessionId(EntityExchangeResult<byte[]> loginResult) {
        String[] cookies = loginResult.getResponseHeaders().get("Set-Cookie").stream()
                .filter(it -> it.contains("JSESSIONID"))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("JSESSIONID가 없습니다."))
                .split(";");
        return Stream.of(cookies)
                .filter(it -> it.contains("JSESSIONID"))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("JSESSIONID가 없습니다."))
                .split("=")[1];
    }

    private URI getArticleLocation(String jSessionId) {
        return webTestClient.post().uri("/articles")
                .cookie("JSESSIONID", jSessionId)
                .body(BodyInserters
                        .fromFormData("title", title)
                        .with("coverUrl", coverUrl)
                        .with("contents", contents))
                .exchange()
                .expectStatus().isFound()
                .expectHeader().valueMatches("location", ".*/articles.*")
                .expectBody()
                .returnResult()
                .getResponseHeaders().getLocation();
    }

    @Test
    void 게시글_생성() {
        URI articleLocation = webTestClient.post().uri("/articles")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .cookie("JSESSIONID", jSessionId)
                .body(BodyInserters
                        .fromFormData("title", title)
                        .with("coverUrl", coverUrl)
                        .with("contents", contents))
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueMatches("location", ".*/articles/\\d*")
                .expectBody()
                .returnResult()
                .getRequestHeaders().getLocation();

        webTestClient.get().uri(articleLocation)
                .exchange()
                .expectBody()
                .consumeWith(response -> {
                    String body = new String(Objects.requireNonNull(response.getResponseBody()));
                    assertThat(body).contains(title);
                    assertThat(body).contains(coverUrl);
                    assertThat(body).contains(contents);
                });

        webTestClient.delete().uri(articleLocation)
                .cookie("JSESSIONID", jSessionId)
                .exchange();
    }

    @Test
    void 게시글_조회() {
        webTestClient.get().uri(location)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response -> {
                    String body = new String(Objects.requireNonNull(response.getResponseBody()));
                    assertThat(body).contains(title);
                    assertThat(body).contains(coverUrl);
                    assertThat(body).contains(contents);
                });
    }

    @Test
    void 게시글_작성자가_게시글_수정() {
        URI articleLocation = webTestClient.put().uri(location)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .cookie("JSESSIONID", jSessionId)
                .body(BodyInserters
                        .fromFormData("title", "newTitle")
                        .with("coverUrl", coverUrl)
                        .with("contents", "newContents"))
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueMatches("location", ".*/articles/\\d*")
                .expectBody()
                .returnResult()
                .getResponseHeaders().getLocation();

        webTestClient.get().uri(articleLocation)
                .exchange()
                .expectBody()
                .consumeWith(response -> {
                    String body = new String(Objects.requireNonNull(response.getResponseBody()));
                    assertThat(body).contains("newTitle");
                    assertThat(body).contains(coverUrl);
                    assertThat(body).contains("newContents");
                });
    }

    @Test
    void 게시글_작성자가_게시글_삭제() {
        URI articleLocation = webTestClient.post().uri("/articles")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .cookie("JSESSIONID", jSessionId)
                .body(BodyInserters
                        .fromFormData("title", title)
                        .with("coverUrl", coverUrl)
                        .with("contents", contents))
                .exchange()
                .expectBody()
                .returnResult()
                .getResponseHeaders().getLocation();

        webTestClient.delete().uri(articleLocation)
                .cookie("JSESSIONID", jSessionId)
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueMatches("location", ".*/");
    }

    @Test
    void 게시글_작성자가_게시글_수정_페이지_이동() {
        webTestClient.get().uri(location + "/edit")
                .cookie("JSESSIONID", jSessionId)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void 다른_사용자가_게시글_수정() {
        String outsiderSessionId = getJSessionId("paul123@example.com", "p@ssW0rd");

        webTestClient.put().uri(location)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .cookie("JSESSIONID", outsiderSessionId)
                .body(BodyInserters
                        .fromFormData("title", "newTitle")
                        .with("coverUrl", coverUrl)
                        .with("contents", "newContents"))
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueMatches("location", ".*/");
    }

    @Test
    void 다른_사용자가_게시글_삭제() {
        String outsiderSessionId = getJSessionId("paul123@example.com", "p@ssW0rd");

        webTestClient.delete().uri(location)
                .cookie("JSESSIONID", outsiderSessionId)
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueMatches("location", ".*/");
    }

    @Test
    void 다른_사용자가_게시글_수정_페이지_이동() {
        String outsiderSessionId = getJSessionId("paul123@example.com", "p@ssW0rd");

        webTestClient.get().uri("/articles/" + DEFAULT_ARTICLE_ID + "/edit")
                .cookie("JSESSIONID", outsiderSessionId)
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueMatches("location", ".*/articles/\\d*");
    }
}
