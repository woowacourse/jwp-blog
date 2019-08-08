package techcourse.myblog.web.controlloer;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import techcourse.myblog.domain.article.Contents;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static techcourse.myblog.domain.article.ArticleTest.article;
import static techcourse.myblog.domain.article.ArticleTest.contents;
import static techcourse.myblog.domain.user.UserTest.user;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ArticleControllerTest extends AbstractControllerTest {
    @Test
    void 로그인_전_Article_생성_페이지_접근() {
        webTestClient.get().uri("/articles/new").exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueMatches("location", ".*/login.*");
    }

    @Test
    void 로그인_후_Article_생성_페이지_접근() {
        String jSessionId = extractJSessionId(login(user));
        webTestClient.get().uri("/articles/new")
                .cookie("JSESSIONID", jSessionId)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void 로그인_전_Article_생성() {
        getResponse(webTestClient.post()
                .uri("/articles/new"), contents, null)
                .expectStatus().is3xxRedirection()
                .expectHeader().valueMatches("location", ".*/login.*");
    }

    @Test
    void 로그인_후_Article_생성() {
        String jSessionId = extractJSessionId(login(user));

        WebTestClient.RequestBodySpec requestBodySpec = webTestClient.post().uri("/articles/new")
                .cookie("JSESSIONID", jSessionId);

        WebTestClient.ResponseSpec responseSpec = getResponse(requestBodySpec, contents, jSessionId)
                .expectStatus().isFound()
                .expectHeader().valueMatches("location", ".*/articles.*");

        checkBody(responseSpec, contents);
    }

    @Test
    void 없는_Article() {
        webTestClient.get().uri("/articles/10").exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueMatches("location", ".*/.*");
    }

    @Test
    void 로그인_전_수정_페이지_접근() {
        webTestClient.get().uri("articles/" + article.getArticleId() + "/edit")
                .exchange().expectStatus().is3xxRedirection();
    }

    @Test
    void 로그인_후_수정_페이지_접근() {
        String jSessionId = extractJSessionId(login(user));
        webTestClient.get().uri("articles/1/edit")
                .cookie("JSESSIONID", jSessionId)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void 로그인_전_Article_수정() {
        getResponse(webTestClient.put().uri("/articles/" + article.getArticleId()), contents, null)
                .expectStatus().is3xxRedirection();
    }

    @Test
    void 로그인_후_Article_수정() {
        Contents contents = new Contents("update title", "update coverUrl", "update contents");
        String jSessionId = extractJSessionId(login(user));

        WebTestClient.ResponseSpec responseSpec = getResponse(webTestClient.put().uri("/articles/1"), contents, jSessionId);

        responseSpec.expectBody()
                .consumeWith(response -> {
                    webTestClient.get().uri(Objects.requireNonNull(response.getResponseHeaders().get("Location")).get(0))
                            .exchange()
                            .expectStatus().isOk()
                            .expectBody()
                            .consumeWith(res -> {
                                String body = new String(Objects.requireNonNull(res.getResponseBody()));
                                assertThat(body.contains(contents.getTitle())).isTrue();
                                assertThat(body.contains(contents.getCoverUrl())).isTrue();
                                assertThat(body.contains(contents.getContents())).isTrue();
                            });
                });
    }

    @Test
    void 로그인_전_Article_삭제() {
        webTestClient.delete().uri("/articles/2")
                .exchange()
                .expectStatus()
                .is3xxRedirection()
                .expectHeader().valueMatches("location", ".*/login.*");
    }

    @Test
    void 로그인_후_Article_삭제() {
        String jSessionId = extractJSessionId(login(user));
        webTestClient.delete().uri("/articles/2")
                .cookie("JSESSIONID", jSessionId)
                .exchange()
                .expectStatus()
                .is3xxRedirection()
                .expectHeader().valueMatches("location", ".*/.*");
    }

    private WebTestClient.ResponseSpec getResponse(WebTestClient.RequestBodySpec requestBodySpec, Contents contents, String jSessionId) {
        return requestBodySpec
                .cookie("JSESSIONID", jSessionId)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("title", contents.getTitle())
                        .with("coverUrl", contents.getCoverUrl())
                        .with("contents", contents.getContents()))
                .exchange();
    }

    private void checkBody(WebTestClient.ResponseSpec responseSpec, Contents contents) {
        responseSpec.expectBody()
                .consumeWith(response -> {
                    webTestClient.get().uri(Objects.requireNonNull(response.getResponseHeaders().get("Location")).get(0))
                            .exchange()
                            .expectStatus().isOk()
                            .expectBody()
                            .consumeWith(res -> {
                                String body = new String(Objects.requireNonNull(res.getResponseBody()));
                                assertThat(body.contains(contents.getTitle())).isTrue();
                                assertThat(body.contains(contents.getCoverUrl())).isTrue();
                                assertThat(body.contains(contents.getContents())).isTrue();
                            });
                });
    }
}
