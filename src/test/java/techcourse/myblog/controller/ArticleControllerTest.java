package techcourse.myblog.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import techcourse.myblog.dto.ArticleRequest;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

public class ArticleControllerTest extends AbstractControllerTest {
    private static final String TITLE = "title";
    private static final String COVER_URL = "coverUrl";
    private static final String CONTENTS = "contents";

    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void 로그인_전에_글쓰기_페이지에_접근하면_로그인_페이지로_리다이렉트한다() {
        webTestClient.get().uri("/articles")
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueMatches("location", ".*/login.*");
    }

    @Test
    public void 로그인_후에_글쓰기_페이지에_접근한다() {
        String jSessionId = extractJSessionId(login(user1));
        webTestClient.get().uri("/articles")
                .cookie(JSESSIONID, jSessionId)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void 로그인_전에_글쓰기를_시도하면_로그인_페이지로_리다이렉트한다() {
        ArticleRequest articleDto = new ArticleRequest(TITLE, COVER_URL, CONTENTS);
        getResponse(webTestClient.post().uri("/articles"), articleDto, null)
                .expectStatus().is3xxRedirection()
                .expectHeader().valueMatches("location", ".*/login.*");
    }

    @Test
    public void 로그인_후에_글을_작성한다() {
        String jSessionId = extractJSessionId(login(user1));
        ArticleRequest articleDto = new ArticleRequest(TITLE, COVER_URL, CONTENTS);

        WebTestClient.RequestBodySpec requestBodySpec = webTestClient.post().uri("/articles")
                .cookie(JSESSIONID, jSessionId);

        WebTestClient.ResponseSpec responseSpec = getResponse(requestBodySpec, articleDto, jSessionId)
                .expectStatus().is3xxRedirection()
                .expectHeader().valueMatches("location", ".*/articles.*");

        checkBody(responseSpec, articleDto);
    }

    @Test
    public void 로그인_전에_글_수정을_시도하면_메인으로_리다이렉트한다() {
        webTestClient.get().uri("articles/1/edit")
                .exchange()
                .expectStatus().is3xxRedirection();
    }

    @Test
    public void 로그인_후에_글을_수정한다() {
        String jSessionId = extractJSessionId(login(user1));
        ArticleRequest articleDto = new ArticleRequest(TITLE, COVER_URL, "new contents!");

        WebTestClient.RequestBodySpec requestBodySpec = webTestClient.put().uri("/articles/1")
                .cookie(JSESSIONID, jSessionId);

        WebTestClient.ResponseSpec responseSpec = getResponse(requestBodySpec, articleDto, jSessionId)
                .expectStatus().is3xxRedirection()
                .expectHeader().valueMatches("location", ".*/articles.*");

        checkBody(responseSpec, articleDto);
    }

    @Test
    public void 로그인_전에_글_삭제를_시도하면_메인으로_리다이렉트한다() {
        webTestClient.delete().uri("articles/1")
                .exchange()
                .expectStatus().is3xxRedirection();
    }

    @Test
    public void 로그인_후에_글을_삭제한다() {
        String jSessionId = extractJSessionId(login(user1));
        webTestClient.delete().uri("/articles/2")
                .cookie(JSESSIONID, jSessionId)
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueMatches("location", ".*/.*");
    }

    private WebTestClient.ResponseSpec getResponse(WebTestClient.RequestBodySpec requestBodySpec, ArticleRequest articleDto, String jSessionId) {
        return requestBodySpec
                .cookie(JSESSIONID, jSessionId)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData(TITLE, articleDto.getTitle())
                        .with(COVER_URL, articleDto.getCoverUrl())
                        .with(CONTENTS, articleDto.getContents()))
                .exchange();
    }

    private void checkBody(WebTestClient.ResponseSpec responseSpec, ArticleRequest articleDto) {
        responseSpec.expectBody()
                .consumeWith(response -> {
                    webTestClient.get().uri(Objects.requireNonNull(response.getResponseHeaders().get("Location")).get(0))
                            .exchange()
                            .expectStatus().isOk()
                            .expectBody()
                            .consumeWith(res -> {
                                String body = new String(Objects.requireNonNull(res.getResponseBody()));
                                assertThat(body.contains(articleDto.getTitle())).isTrue();
                                assertThat(body.contains(articleDto.getCoverUrl())).isTrue();
                                assertThat(body.contains(articleDto.getContents())).isTrue();
                            });
                });
    }
}